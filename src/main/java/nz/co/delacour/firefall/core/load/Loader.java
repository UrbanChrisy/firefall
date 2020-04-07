package nz.co.delacour.firefall.core.load;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.common.base.Strings;
import nz.co.delacour.firefall.core.EntityType;
import nz.co.delacour.firefall.core.Firefall;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.Ref;
import nz.co.delacour.firefall.core.util.TypeUtils;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class Loader<T extends HasId<T>> extends Query<T> {

    public Loader(EntityType<T> entityType, Class<T> entityClass, CollectionReference collection) {
        super(entityType, entityClass, collection, collection);
    }

    public LoadResult<T> id(String id) {
        if (Strings.isNullOrEmpty(id)) {
            return new LoadResult<>(entityClass);
        }
        return new LoadResult<>(collection.document(id), entityClass);
    }

    public DocumentReference ref(String id) {
        return this.collection.document(id);
    }

    public LoadResult<T> ref(DocumentReference reference) {
        return new LoadResult<>(reference, entityClass);
    }

    public LoadResult<T> ref(Ref<T> ref) {
        return new LoadResult<>(ref.getReference(), entityClass);
    }

}
