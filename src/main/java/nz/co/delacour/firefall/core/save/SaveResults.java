package nz.co.delacour.firefall.core.save;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Transaction;
import com.google.cloud.firestore.WriteResult;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.var;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefullException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 9/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class SaveResults<T extends HasId> {

    private final List<T> items;

    private final Class<T> entityClass;

    private final CollectionReference collection;

    private final Map<DocumentReference, T> map;

    public SaveResults(List<T> items, Class<T> entityClass, CollectionReference collection) {
        this.items = items;
        this.entityClass = entityClass;
        this.collection = collection;

        Map<DocumentReference, T> map = Maps.newHashMap();
        for (T t : this.items) {
            DocumentReference reference;
            if (Strings.isNullOrEmpty(t.getId())) {
                reference = this.collection.document();
                t.setId(reference.getId());
            } else {
                reference = this.collection.document(t.getId());
            }

            map.put(reference, t);
        }

        this.map = map;
    }

    public List<T> now() {
        try {

            var batch = this.collection.getFirestore().batch();
            for (Map.Entry<DocumentReference, T> entry : this.map.entrySet()) {
                batch.set(entry.getKey(), entry.getValue());
            }
            batch.commit().get();

            return Lists.newArrayList(this.map.values());
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }
    }

    public ApiFuture<List<WriteResult>> async() {
        var batch = this.collection.getFirestore().batch();
        for (Map.Entry<DocumentReference, T> entry : this.map.entrySet()) {
            batch.set(entry.getKey(), entry.getValue());
        }
        return batch.commit();
    }

    public List<DocumentReference> refs() {
        return Lists.newArrayList(this.map.keySet());
    }

    public SaveResultsTransaction<T> transaction(Transaction transaction) {
        return new SaveResultsTransaction<>(transaction, this.map);
    }
}
