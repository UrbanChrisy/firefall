package nz.co.delacour.firefall.core;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Transaction;
import nz.co.delacour.firefall.core.delete.Deleter;
import nz.co.delacour.firefall.core.load.Loader;
import nz.co.delacour.firefall.core.load.Query;
import nz.co.delacour.firefall.core.save.Saver;
import nz.co.delacour.firefall.core.util.TypeUtils;

import javax.annotation.Nullable;

public class EntityType<T extends HasId<T>> {

    private final Firefall firefall;
    private final Class<T> entityClass;
    private final CollectionReference collection;

    @Nullable
    private final Transaction transaction;

    public EntityType(Firefall firefall, Class<T> entityClass, @Nullable Transaction transaction) {
        this.firefall = firefall;
        this.entityClass = entityClass;
        this.collection = TypeUtils.getCollection(getFirefall().getFirestore(), entityClass);
        this.transaction = transaction;
    }

    public EntityType(Firefall firefall, Ref<?> parent, Class<T> entityClass, @Nullable Transaction transaction) {
        this.firefall = firefall;
        this.entityClass = entityClass;
        this.collection = TypeUtils.getSubCollection(parent, entityClass);
        this.transaction = transaction;
    }

    public Firefall getFirefall() {
        return firefall;
    }

    public CollectionReference getCollection() {
        return collection;
    }

    public Loader<T> load() {
        return new Loader<>(this, entityClass, collection, transaction);
    }

    public Saver<T> save() {
        return new Saver<>(this, entityClass, collection, transaction);
    }

    public Deleter<T> delete() {
        return new Deleter<>(this, entityClass, collection, transaction);
    }

    public EntityType<T> parent(Ref<?> ref) {
        return new EntityType<>(this.firefall, ref, entityClass, transaction);
    }

    public Query<T> global() {
        var collection = TypeUtils.getCollectionGroup(getFirefall().getFirestore(), entityClass);
        return new Query<>(this, entityClass, collection, transaction);
    }
}
