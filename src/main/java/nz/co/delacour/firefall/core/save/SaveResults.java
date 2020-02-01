package nz.co.delacour.firefall.core.save;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefallException;
import nz.co.delacour.firefall.core.registrar.LifecycleMethod;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static nz.co.delacour.firefall.core.FirefallService.getMetadata;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 9/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class SaveResults<T extends HasId<T>> {

    private final Class<T> entityClass;

    private final Map<DocumentReference, T> map;

    private final ApiFuture<List<WriteResult>> future;

    public SaveResults(List<T> items, Class<T> entityClass, CollectionReference collection) {
        this.entityClass = entityClass;

        Map<DocumentReference, T> map = Maps.newHashMap();
        for (T t : items) {
            DocumentReference reference;
            if (Strings.isNullOrEmpty(t.getId())) {
                reference = collection.document();
                t.setId(reference.getId());
            } else {
                reference = collection.document(t.getId());
            }

            map.put(reference, t);
        }

        this.map = map;

        var batch = collection.getFirestore().batch();
        for (Map.Entry<DocumentReference, T> entry : this.map.entrySet()) {
            batch.set(entry.getKey(), entry.getValue());
        }
        this.future = batch.commit();
    }

    public List<T> now() {
        try {
            this.future.get();
            List<T> entities = Lists.newArrayList(this.map.values());

            var onSaveMethods = getOnSaveMethods();
            for (T entity : entities) {
                for (LifecycleMethod onSaveMethod : onSaveMethods) {
                    onSaveMethod.execute(entity);
                }
            }

            return entities;
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefallException(e);
        }
    }

    public SaveResults<T> listener(Runnable runnable, Executor executor) {
        this.future.addListener(runnable, executor);
        return this;
    }

    public List<DocumentReference> refs() {
        return Lists.newArrayList(this.map.keySet());
    }

    private List<LifecycleMethod> getOnSaveMethods() {
        var metadata = getMetadata(entityClass);
        if (metadata == null) {
            return Lists.newArrayList();
        }
        return metadata.getOnSaveMethods();
    }

}
