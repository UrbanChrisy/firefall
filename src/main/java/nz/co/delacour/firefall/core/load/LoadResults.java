package nz.co.delacour.firefall.core.load;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.common.collect.Lists;
import lombok.var;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefullException;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 10/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class LoadResults<T extends HasId> {

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

            List<T> items = Lists.newArrayList();
            for (QueryDocumentSnapshot document : snapshot.getDocuments()) {
                T t = document.toObject(entityClass);
                t.setId(document.getId());
                items.add(t);
            }

            return items;
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }
    }

}
