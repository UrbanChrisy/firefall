package nz.co.delacour.firefall.core.load;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.common.base.Strings;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.util.TypeUtils;
import nz.co.delacour.firefall.core.Ref;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 13/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class LoadType<T extends HasId>  {

    private final Loader loader;

    private final Class<T> entityClass;

    private final String kind;

    private final CollectionReference collection;

    public LoadType(Loader loader, Class<T> entityClass) {
        this.loader = loader;
        this.entityClass = entityClass;
        this.kind = TypeUtils.getKind(entityClass);
        this.collection = this.loader.getFirefull().factory().getFirestore().collection(this.kind);
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
