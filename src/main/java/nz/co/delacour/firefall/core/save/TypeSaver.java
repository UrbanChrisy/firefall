package nz.co.delacour.firefall.core.save;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.util.TypeUtils;

import java.util.List;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class TypeSaver<T extends HasId<T>> {

    private final Class<T> entityClass;

    private CollectionReference collection;

    public TypeSaver(Saver saver, Class<T> entityClass, DocumentReference parent) {
        this.entityClass = entityClass;
        this.collection = TypeUtils.getCollection(saver.getFirefall().factory().getFirestore(), entityClass, parent);
    }

    public SaveResult<T> entity(T t) {
        return new SaveResult<>(t, this.entityClass, this.collection);
    }

    public SaveResults<T> entities(List<T> items) {
        return new SaveResults<>(items, entityClass, this.collection);
    }

    public CollectionReference getCollection() {
        return collection;
    }
}
