package nz.co.delacour.firefall.core.load;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.Transaction;
import com.google.common.collect.Lists;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefallException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;


public class LoadResults<T extends HasId<T>> {

    private final Query<T> query;

    private final Class<T> entityClass;

    private ApiFuture<QuerySnapshot> future;

    public LoadResults(Query<T> query, Class<T> entityClass, Transaction transaction) {
        this.query = query;
        this.entityClass = entityClass;
        if (transaction != null) {
            this.future = transaction.get(query.query());
        } else {
            this.future = query.query().get();
        }
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
