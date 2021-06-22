package nz.co.delacour.firefall.core.load;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Transaction;
import com.google.common.base.Strings;
import nz.co.delacour.firefall.core.EntityType;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.Ref;

import javax.annotation.Nullable;
import java.util.function.Function;


public class Loader<T extends HasId<T>> extends Query<T> {

    public Loader(EntityType<T> entityType, Class<T> entityClass, CollectionReference collection, Transaction transaction) {
        super(entityType, entityClass, collection, collection, transaction);
    }

    public LoadResult<T> id(String id) {
        if (this.collection == null || Strings.isNullOrEmpty(id)) {
            return new LoadResult<>(entityClass, transaction);
        }
        return new LoadResult<>(this.collection.document(id), entityClass, afterLoad, transaction);
    }

    @Nullable
    public DocumentReference ref(String id) {
        if (collection == null) {
            return null;
        }
        return this.collection.document(id);
    }

    public LoadResult<T> ref(DocumentReference reference) {
        return new LoadResult<>(reference, entityClass, transaction);
    }

    public LoadResult<T> ref(Ref<T> ref) {
        return new LoadResult<>(ref.getReference(), entityClass, afterLoad, transaction);
    }

    public Loader<T> afterLoad(Function<T, T> afterLoad) {
        this.afterLoad = afterLoad;
        return this;
    }

    public Loader<T> transaction(Transaction transaction) {
        this.transaction = transaction;
        return this;
    }

}
