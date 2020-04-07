package nz.co.delacour.firefall.core;

import com.google.cloud.firestore.CollectionReference;
import nz.co.delacour.firefall.core.delete.Deleter;
import nz.co.delacour.firefall.core.load.Loader;
import nz.co.delacour.firefall.core.load.Query;
import nz.co.delacour.firefall.core.save.Saver;
import nz.co.delacour.firefall.core.util.TypeUtils;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 8/04/20.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class EntityType<T extends HasId<T>> {

    private final Firefall firefall;

    private final Class<T> entityClass;

    private final CollectionReference collection;

    public EntityType(Firefall firefall, Class<T> entityClass) {
        this.firefall = firefall;
        this.entityClass = entityClass;
        this.collection = TypeUtils.getCollection(getFirefall().getFirestore(), entityClass);
    }

    public EntityType(Firefall firefall, Ref<?> parent, Class<T> entityClass) {
        this.firefall = firefall;
        this.entityClass = entityClass;
        this.collection = TypeUtils.getSubCollection(parent, entityClass);
    }

    public Firefall getFirefall() {
        return firefall;
    }

    public Loader<T> load() {
        return new Loader<>(this, entityClass, collection);
    }

    public Saver<T> save() {
        return new Saver<T>(this, entityClass, collection);
    }

    public Deleter<T> delete() {
        return new Deleter<>(this, entityClass, collection);
    }

    public EntityType<T> parent(Ref<?> ref) {
        return new EntityType<>(this.firefall, ref, entityClass);
    }

    public Query<T> global() {
        var collection = TypeUtils.getCollectionGroup(getFirefall().getFirestore(), entityClass);
        return new Query<>(this, entityClass, collection);
    }
}
