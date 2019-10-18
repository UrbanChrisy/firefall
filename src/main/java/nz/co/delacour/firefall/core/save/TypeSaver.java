package nz.co.delacour.firefall.core.save;

import com.google.cloud.firestore.CollectionReference;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.util.TypeUtils;
import nz.co.delacour.firefall.core.load.LoadResult;

import java.util.List;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class TypeSaver<T extends HasId> {

    private final Saver saver;

    private final Class<T> entityClass;

    private final String kind;

    private CollectionReference collection;

    public TypeSaver(Saver saver, Class<T> entityClass) {
        this.saver = saver;
        this.entityClass = entityClass;
        this.kind = TypeUtils.getKind(entityClass);
        this.collection = saver.getFirefall().factory().getFirestore().collection(this.kind);
    }

    public LoadResult<T> document() {
        return new LoadResult<>(this.collection.document(), this.entityClass);
    }

    public SaveResult<T> entity(T t) {
        return new SaveResult<>(t, this.entityClass, this.collection);
    }

    public SaveResults<T> entities(List<T> items) {
        return new SaveResults<>(items, entityClass, this.collection);
    }


}
