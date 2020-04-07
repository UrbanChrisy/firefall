package nz.co.delacour.firefall.core.save;


import com.google.cloud.firestore.CollectionReference;
import nz.co.delacour.firefall.core.EntityType;
import nz.co.delacour.firefall.core.Firefall;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.Ref;
import nz.co.delacour.firefall.core.util.TypeUtils;

import java.util.List;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class Saver<T extends HasId<T>> {

    private final EntityType<T> entityType;

    private final Class<T> entityClass;

    private final CollectionReference collection;

    public Saver(EntityType<T> entityType, Class<T> entityClass, CollectionReference collection) {
        this.entityType = entityType;
        this.entityClass = entityClass;
        this.collection = collection;
    }

    public EntityType<T> getEntityType() {
        return entityType;
    }

    public SaveResult<T> entity(T t) {
        return new SaveResult<>(t, this.entityClass, this.collection);
    }

    public SaveResults<T> entities(List<T> items) {
        return new SaveResults<>(items, entityClass, this.collection);
    }
}
