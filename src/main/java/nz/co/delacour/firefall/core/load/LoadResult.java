package nz.co.delacour.firefall.core.load;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.Transaction;
import com.google.common.base.Strings;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefallException;
import nz.co.delacour.firefall.core.exceptions.NotFoundException;
import nz.co.delacour.firefall.core.registrar.LifecycleMethod;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Function;

import static nz.co.delacour.firefall.core.FirefallService.getMetadata;


public class LoadResult<T extends HasId<T>> {

    private final Class<T> entityClass;
    private final ApiFuture<?> future;
    @Nullable
    private Function<T, T> afterLoad;
    private final Transaction transaction;
    private T entity;

    public LoadResult(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.future = ApiFutures.immediateFuture(null);
        this.transaction = null;
        this.entity = null;
    }

    public LoadResult(Class<T> entityClass, T entity) {
        this.entityClass = entityClass;
        this.future = ApiFutures.immediateFuture(null);
        this.transaction = null;
        this.entity = entity;
    }

    public LoadResult(Class<T> entityClass, Transaction transaction) {
        this.entityClass = entityClass;
        this.future = ApiFutures.immediateFuture(null);
        this.transaction = transaction;
    }

    public LoadResult(DocumentReference reference, Class<T> entityClass, Transaction transaction) {
        this(reference, entityClass, null, transaction);
    }

    public LoadResult(DocumentReference reference, Class<T> entityClass, @Nullable Function<T, T> afterLoad, Transaction transaction) {
        this.entityClass = entityClass;
        this.afterLoad = afterLoad;
        this.transaction = transaction;

        if (transaction != null) {
            this.future = transaction.get(reference);
        } else {
            this.future = reference.get();
        }
    }

    public LoadResult(com.google.cloud.firestore.Query query, Class<T> entityClass, Transaction transaction) {
      this(query, entityClass, null, transaction);
    }


    public LoadResult(com.google.cloud.firestore.Query query, Class<T> entityClass, @Nullable Function<T, T> afterLoad, Transaction transaction) {
        query = query.limit(1);
        this.entityClass = entityClass;
        this.afterLoad = afterLoad;
        this.transaction = transaction;
        if (transaction != null) {
            this.future = transaction.get(query);
        } else {
            this.future = query.get();
        }
    }

    @Nullable
    public T now() {

        if (entity != null) {
            executeOnLoad(entity);
            return entity;
        }

        String id;
        DocumentSnapshot documentSnapshot;

        try {
            Object result = this.future.get();
            if (result instanceof QuerySnapshot) {
                QuerySnapshot querySnapshot = (QuerySnapshot) result;
                var documents = querySnapshot.getDocuments();
                if (documents.isEmpty()) {
                    return null;
                }
                documentSnapshot = querySnapshot.getDocuments().get(0);
            } else if (result instanceof DocumentSnapshot) {
                documentSnapshot = (DocumentSnapshot) result;
            } else {
                return null;
            }

            id = documentSnapshot.getId();
            entity = documentSnapshot.toObject(this.entityClass);
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefallException(e);
        }

        if (entity == null || Strings.isNullOrEmpty(id)) {
            return null;
        }
        entity.setId(id);

        executeOnLoad(entity);

        if (afterLoad != null) {
            entity = afterLoad.apply(entity);
        }

        return entity;
    }

    public T safe() {
        T entity = now();
        if (entity == null) {
            throw new NotFoundException(String.format("%s not found", entityClass.getSimpleName()));
        }

        return entity;
    }

    public LoadResult<T> listener(Runnable runnable, Executor executor) {
        this.future.addListener(runnable, executor);
        return this;
    }

    private void executeOnLoad(T entity) {
        var metadata = getMetadata(entityClass);
        if (metadata == null) {
            return;
        }

        var onLoadMethods = metadata.getOnLoadMethods();
        if (onLoadMethods.isEmpty()) {
            return;
        }

        for (LifecycleMethod onLoadMethod : onLoadMethods) {
            onLoadMethod.execute(entity);
        }
    }

}
