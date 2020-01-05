package nz.co.delacour.firefall.core.load;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.common.collect.Lists;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefallException;
import nz.co.delacour.firefall.core.registrar.LifecycleMethod;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static nz.co.delacour.firefall.core.FirefallService.getMetadata;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 10/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class LoadResults<T extends HasId<T>> {

    private final Class<T> entityClass;

    private ApiFuture<QuerySnapshot> future;

    public LoadResults(Query query, Class<T> entityClass) {
        this.future = query.get();
        this.entityClass = entityClass;
    }

    public List<T> now() {
        try {
            QuerySnapshot snapshot = future.get();

            var documents = snapshot.getDocuments();
            if (documents.isEmpty()) {
                return Lists.newArrayList();
            }

            List<T> entities = Lists.newArrayList();
            for (QueryDocumentSnapshot document : snapshot.getDocuments()) {
                T t = document.toObject(entityClass);
                t.setId(document.getId());
                entities.add(t);
            }

            var onLoadMethods = getOnLoadMethods();
            for (T entity : entities) {
                for (LifecycleMethod onLoadMethod : onLoadMethods) {
                    onLoadMethod.execute(entity);
                }
            }

            return entities;
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefallException(e);
        }
    }

    public LoadResults<T> listener(Runnable runnable, Executor executor) {
        this.future.addListener(runnable, executor);
        return this;
    }

    private List<LifecycleMethod> getOnLoadMethods() {
        var metadata = getMetadata(entityClass);
        if (metadata == null) {
            return Lists.newArrayList();
        }
        return metadata.getOnLoadMethods();
    }
}
