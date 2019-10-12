package nz.co.delacour.firefull.core.save;

import com.google.cloud.firestore.CollectionReference;
import nz.co.delacour.firefull.core.HasId;
import nz.co.delacour.firefull.core.load.LoadResult;
import nz.co.delacour.firefull.core.util.TypeUtils;

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
        this.collection = saver.getFirefull().getFirefullFactory().getFirestore().collection(this.kind);
    }

    public LoadResult<T> document() {
        return new LoadResult<>(this.collection.document(), entityClass);
    }

    public SaveResult<T> save(T t) {
        return new SaveResult<>(t, this.collection);
    }

    public SaveResults<T> save(List<T> items) {
        return new SaveResults<>(items, this.collection);
    }


}
