package nz.co.delacour.firefall.core.load;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.common.base.Strings;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.Ref;
import nz.co.delacour.firefall.core.util.TypeUtils;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 13/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class LoadType<T extends HasId<T>> extends Query<T> {

    private final CollectionReference collection;

    public LoadType(Loader loader, Class<T> entityClass, CollectionReference collection) {
        super(loader, entityClass, collection);
        this.collection = collection;
    }

    public Query<T> parent(Ref<?> ref) {
        var subCollectionQuery = TypeUtils.getSubCollection(loader.getFirefall().getFirestore(), entityClass);
        return new Query<>(loader, entityClass, subCollectionQuery);
    }

    public LoadResult<T> id(String id) {
        if (Strings.isNullOrEmpty(id)) {
            return new LoadResult<>(entityClass);
        }
        return new LoadResult<>(collection.document(id), entityClass);
    }

    public Loader getLoader() {
        return this.loader;
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
