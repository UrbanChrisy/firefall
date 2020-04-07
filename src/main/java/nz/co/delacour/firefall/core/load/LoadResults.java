package nz.co.delacour.firefall.core.load;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.common.collect.Lists;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefallException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static nz.co.delacour.firefall.core.FirefallService.getMetadata;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 10/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class LoadResults<T extends HasId<T>> {

    private final Query<T> query;

    private final Class<T> entityClass;

    private ApiFuture<QuerySnapshot> future;

    public LoadResults(Query<T> query, Class<T> entityClass) {
        this.query = query;
        this.future = query.query().get();
        this.entityClass = entityClass;
    }

    public ListResult<T> now() {
        try {
            QuerySnapshot snapshot = future.get();

            var documents = snapshot.getDocuments();
            if (documents.isEmpty()) {
                return new ListResult<>(entityClass, Lists.newArrayList());
            }

            return new ListResult<>(entityClass, documents);
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefallException(e);
        }
    }

    public LoadResults<T> listener(Runnable runnable, Executor executor) {
        this.future.addListener(runnable, executor);
        return this;
    }
}
