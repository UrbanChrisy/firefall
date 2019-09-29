package nz.co.delacour.firefull.save;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.common.base.Strings;
import nz.co.delacour.firefull.HasId;
import nz.co.delacour.firefull.Ref;
import nz.co.delacour.firefull.exceptions.FirefullException;
import nz.co.delacour.firefull.load.LoadResult;

import java.util.concurrent.ExecutionException;

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
        this.kind = Ref.getKind(entityClass);
        this.collection = saver.getFirefull().getFirefullFactory().getFirestore().collection(this.kind);
    }

    public LoadResult<T> save(T t) {
        if (Strings.isNullOrEmpty(t.getId())) {
            return saveWithoutId(t);
        }
        return saveWithId(t);
    }

    public LoadResult<T> saveWithId(T t) {
        try {
            DocumentReference ref = this.collection.document(t.getId());
            ref.set(t).get();
            DocumentSnapshot snapshot = ref.get().get();
            return new LoadResult<T>(snapshot, entityClass);
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }
    }

    public LoadResult<T> saveWithoutId(T t) {
        try {
            DocumentSnapshot snapshot = this.collection.add(t).get().get().get();
            return new LoadResult<T>(snapshot, entityClass);
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }
    }
}
