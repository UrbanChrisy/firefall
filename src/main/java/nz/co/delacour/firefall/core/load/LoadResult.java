package nz.co.delacour.firefall.core.load;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.common.base.Strings;
import lombok.var;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefullException;
import nz.co.delacour.firefall.core.exceptions.NotFoundException;
import nz.co.delacour.firefall.core.registrar.LifecycleMethod;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static nz.co.delacour.firefall.core.FirefallService.getMetadata;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class LoadResult<T extends HasId> {


    private final Class<T> entityClass;

    private final ApiFuture<?> future;

    public LoadResult(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.future = ApiFutures.immediateFuture(null);
    }

    public LoadResult(DocumentReference reference, Class<T> entityClass) {
        this.entityClass = entityClass;
        this.future = reference.get();
    }

    public LoadResult(com.google.cloud.firestore.Query query, Class<T> entityClass) {
        this.entityClass = entityClass;
        this.future = query.limit(1).get();
    }

    @Nullable
    public T now() {
        T entity;
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
            throw new FirefullException(e);
        }

        if (entity == null || Strings.isNullOrEmpty(id)) {
            return null;
        }
        entity.setId(id);

        executeOnLoad(entity);

        return entity;
    }

    public T safe() {
        T entity = now();
        if (entity == null) {
            throw new NotFoundException();
        }

        return entity;
    }

    public LoadResult<T> listener(Runnable runnable, Executor executor) {
        this.future.addListener(runnable, executor);
        return this;
    }

    private void executeOnLoad(T entity) {
        var metadata = getMetadata(entityClass);
        var onLoadMethods = metadata.getOnLoadMethods();

        if (onLoadMethods.isEmpty()) {
            return;
        }

        for (LifecycleMethod onLoadMethod : onLoadMethods) {
            onLoadMethod.execute(entity);
        }

    }

}
