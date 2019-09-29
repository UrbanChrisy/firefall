package nz.co.delacour.firefull.load;

import com.google.cloud.firestore.DocumentSnapshot;
import nz.co.delacour.firefull.HasId;

import javax.annotation.Nullable;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class LoadResult<T extends HasId> implements Result<T> {

    private final String id;

    private final DocumentSnapshot snapshot;

    private final Class<T> entityClass;

    public LoadResult(@Nullable DocumentSnapshot snapshot, Class<T> entityClass) {
        this.snapshot = snapshot;
        this.entityClass = entityClass;

        if (snapshot != null) {
            this.id = snapshot.getId();
        } else {
            this.id = null;
        }
    }

    @Override
    public T now() {
        if (snapshot == null) {
            return null;
        }

        T t = snapshot.toObject(entityClass);
        if (t != null) {
            t.setId(this.id);
        }

        return t;
    }
}
