package nz.co.delacour.firefall.core.save;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.*;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefallException;
import nz.co.delacour.firefall.core.registrar.LifecycleMethod;
import nz.co.delacour.firefall.core.util.EntityMapper;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Function;

import static nz.co.delacour.firefall.core.FirefallService.getMetadata;


public class SaveResult<T extends HasId<T>> {

    private T entity;
    private final Class<T> entityClass;
    private final SetOptions setOptions;
    private final DocumentReference reference;
    private final ApiFuture<WriteResult> future;
    @Nullable
    private final Transaction transaction;
    private final Function<T, T> beforeSave;
    private final Function<T, T> afterSave;

    public SaveResult(T entity, Class<T> entityClass, CollectionReference collection, Function<T, T> beforeSave, Function<T, T> afterSave) {
        this(entity, entityClass, collection, beforeSave, afterSave, null, null);
    }

    public SaveResult(T entity, Class<T> entityClass, CollectionReference collection, Function<T, T> beforeSave, Function<T, T> afterSave, SetOptions setOptions) {
        this(entity, entityClass, collection, beforeSave, afterSave, setOptions, null);
    }

    public SaveResult(T entity, Class<T> entityClass, CollectionReference collection, Function<T, T> beforeSave, Function<T, T> afterSave, @Nullable Transaction transaction) {
        this(entity, entityClass, collection, beforeSave, afterSave, SetOptions.merge(), transaction);
    }

    public SaveResult(T entity, Class<T> entityClass, CollectionReference collection, Function<T, T> beforeSave, Function<T, T> afterSave, SetOptions setOptions, @Nullable Transaction transaction) {
        this.entityClass = entityClass;
        this.setOptions = setOptions;

        this.beforeSave = beforeSave;
        this.afterSave = afterSave;

        if (entity == null) {
            this.entity = null;
            this.reference = null;
            this.future = ApiFutures.immediateFuture(null);
            this.transaction = transaction;
        } else {

            if (this.beforeSave != null) {
                entity = this.beforeSave.apply(entity);
            }

            var mappedEntity = EntityMapper.map(entity, collection);
            this.entity = mappedEntity.getEntity();
            this.reference = mappedEntity.getType();

            executeOnSave(this.entity);

            if (transaction != null) {
                this.future = ApiFutures.immediateFuture(null);
                this.transaction = transaction.set(this.reference, this.entity, setOptions);
            } else {
                this.future = reference.set(this.entity, setOptions);
                this.transaction = null;
            }
        }
    }

    public DocumentReference getReference() {
        return reference;
    }

    @Nullable
    public WriteResult snapshot() {
        if (this.future == null) {
            return null;
        }

        try {
            var result = this.future.get();
            if (this.afterSave != null) {
                this.entity = this.afterSave.apply(this.entity);
            }
            return result;
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefallException(e);
        }
    }

    public T now() {
        this.snapshot();
        return this.entity;
    }

    public SaveResult<T> listener(Runnable runnable, Executor executor) {
        this.future.addListener(runnable, executor);
        return this;
    }

    private void executeOnSave(T entity) {
        var metadata = getMetadata(entityClass);
        if (metadata == null) {
            return;
        }

        var onSaveMethods = metadata.getOnSaveMethods();
        if (onSaveMethods.isEmpty()) {
            return;
        }

        for (LifecycleMethod onSaveMethod : onSaveMethods) {
            onSaveMethod.execute(entity);
        }

    }

}
