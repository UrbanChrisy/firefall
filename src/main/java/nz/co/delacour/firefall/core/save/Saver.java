package nz.co.delacour.firefall.core.save;


import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.Transaction;
import nz.co.delacour.firefall.core.EntityType;
import nz.co.delacour.firefall.core.HasId;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class Saver<T extends HasId<T>> {

    private final EntityType<T> entityType;
    private final Class<T> entityClass;
    private final CollectionReference collection;
    @Nullable
    private Transaction transaction;
    private Function<T, T> beforeSave;
    private Function<T, T> afterSave;

    public Saver(EntityType<T> entityType, Class<T> entityClass, CollectionReference collection, @Nullable Transaction transaction) {
        this.entityType = entityType;
        this.entityClass = entityClass;
        this.collection = collection;
        this.transaction = transaction;
    }

    public EntityType<T> getEntityType() {
        return entityType;
    }

    public SaveResult<T> entity(T t) {
        return new SaveResult<>(t, this.entityClass, this.collection, beforeSave, afterSave, this.transaction);
    }

    public SaveResult<T> entity(T t, SetOptions options) {
        return new SaveResult<>(t, this.entityClass, this.collection, beforeSave, afterSave, options, this.transaction);
    }

    public SaveResults<T> entities(List<T> t) {
        return new SaveResults<>(t, this.entityClass, this.collection, beforeSave, afterSave);
    }
    public SaveResults<T> entities(List<T> t, SetOptions options) {
        return new SaveResults<>(t, this.entityClass, this.collection, beforeSave, afterSave, options);
    }

    public Saver<T> beforeSave(Function<T, T> beforeSave) {
        this.beforeSave = beforeSave;
        return this;
    }

    public Saver<T> afterSave(Function<T, T> afterSave) {
        this.afterSave = afterSave;
        return this;
    }

    public Saver<T> transaction(Transaction transaction) {
        this.transaction = transaction;
        return this;
    }
}
