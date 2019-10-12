package nz.co.delacour.firefull.core.load;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.common.collect.Lists;
import lombok.var;
import nz.co.delacour.firefull.core.HasId;
import nz.co.delacour.firefull.core.exceptions.FirefullException;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 10/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class LoadResults<T extends HasId> {

    private final Query query;

    private final Class<T> entityClass;

    private ApiFuture<QuerySnapshot> snapshotFuture;

    public LoadResults(Query query, Class<T> entityClass) {
        this.query = query;
        this.entityClass = entityClass;
    }

    public List<T> now() {
        try {
            QuerySnapshot snapshot;
            if (snapshotFuture != null) {
                snapshot = snapshotFuture.get();
            } else {
                snapshot = this.query.get().get();
            }

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

    public LoadResults<T> load() {
        this.snapshotFuture = this.query.get();
        return this;
    }

    public ApiFuture<QuerySnapshot> async() {
        return this.query.get();
    }

}
