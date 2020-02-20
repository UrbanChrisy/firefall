package nz.co.delacour.firefall.core.save;

import com.google.cloud.firestore.CollectionReference;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.Ref;
import nz.co.delacour.firefall.core.util.TypeUtils;

import java.util.List;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class TypeSaver<T extends HasId<T>> {

    private final Saver saver;

    private final Class<T> entityClass;

    private CollectionReference collection;

    public TypeSaver(Saver saver, Class<T> entityClass) {
        this(saver, entityClass, null);
    }

    public TypeSaver(Saver saver, Class<T> entityClass, Ref<?> parent) {
        this.saver = saver;
        this.entityClass = entityClass;
        this.collection = TypeUtils.getCollection(saver.getFirefall().factory().getFirestore(), entityClass);
    }

    public TypeSaver<T> parent(Ref<?> ref) {
        return new TypeSaver<>(this.saver, this.entityClass, ref);
    }

    public SaveResult<T> entity(T t) {
        return new SaveResult<>(t, this.entityClass, this.collection);
    }

    public SaveResults<T> entities(List<T> items) {
        return new SaveResults<>(items, entityClass, this.collection);
    }

}
