package nz.co.delacour.firefall.core.save;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import lombok.var;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefullException;
import nz.co.delacour.firefall.core.util.EntityMapper;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 9/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class SaveResult<T extends HasId> {

    private final T entity;

    private final Class<T> entityClass;

    private final DocumentReference reference;

    private final ApiFuture<WriteResult> future;

    public SaveResult(T entity, Class<T> entityClass, CollectionReference collection) {
        this.entityClass = entityClass;

        if (entity == null) {
            this.entity = null;
            this.reference = null;
            this.future = ApiFutures.immediateFuture(null);
        } else {
            var mappedEntity = EntityMapper.map(entity, collection);
            this.entity = mappedEntity.getEntity();
            this.reference = mappedEntity.getType();

            this.future = reference.set(this.entity);
        }
    }

    @Nullable
    public WriteResult snapshot() {
        try {
            return this.future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }
    }

    @Nullable
    public T now() {
        var snapshot = this.snapshot();
        if (snapshot == null) {
            return null;
        }

        return this.entity;
    }
}
