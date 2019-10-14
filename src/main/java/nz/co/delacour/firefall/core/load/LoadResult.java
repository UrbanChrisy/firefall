package nz.co.delacour.firefall.core.load;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.common.base.Strings;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefullException;
import nz.co.delacour.firefall.core.exceptions.NotFoundException;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class LoadResult<T extends HasId> {

    private final Class<T> entityClass;

    private final ApiFuture<?> future;

    public LoadResult(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.future = ApiFutures.immediateFuture(null);
    }

    public LoadResult(DocumentReference reference, Class<T> entityClass) {
        this.entityClass = entityClass;
        this.future = reference.get();
    }

    public LoadResult(com.google.cloud.firestore.Query query, Class<T> entityClass) {
        this.entityClass = entityClass;
        this.future = query.get();
    }

    @Nullable
    public T now() {
        T entity;
        String id;
        DocumentSnapshot documentSnapshot;

        try {
            Object result = this.future.get();
            if (result instanceof QuerySnapshot) {
                QuerySnapshot querySnapshot = (QuerySnapshot) result;
                documentSnapshot = querySnapshot.getDocuments().get(0);
            } else if (result instanceof DocumentSnapshot) {
                documentSnapshot = (DocumentSnapshot) result;
            } else {
                return null;
            }

            id = documentSnapshot.getId();
            entity = documentSnapshot.toObject(this.entityClass);
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }

        if (entity == null || Strings.isNullOrEmpty(id)) {
            return null;
        }

        entity.setId(id);
        return entity;
    }

    public T safe() {
        T entity = now();
        if (entity == null) {
            throw new NotFoundException();
        }

        return entity;
    }
}
