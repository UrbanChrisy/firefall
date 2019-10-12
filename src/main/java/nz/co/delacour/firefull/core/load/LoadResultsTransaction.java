package nz.co.delacour.firefull.core.load;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.Transaction;
import com.google.common.base.Strings;
import lombok.var;
import nz.co.delacour.firefull.core.HasId;
import nz.co.delacour.firefull.core.exceptions.FirefullException;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class LoadResultsTransaction<T extends HasId> {

    private final com.google.cloud.firestore.Query query;

    private final DocumentReference reference;

    private final Transaction transaction;

    private final Class<T> entityClass;

    public LoadResultsTransaction(Transaction transaction, DocumentReference reference, com.google.cloud.firestore.Query query, Class<T> entityClass) {
        this.query = query;
        this.reference = reference;
        this.transaction = transaction;
        this.entityClass = entityClass;
    }

    @Nullable
    public T now() {
        T t;
        String id;

        try {
            if (this.query != null) {
                QuerySnapshot querySnapshot = this.transaction.get(this.query).get();

                if (querySnapshot.isEmpty()) {
                    return null;
                }

                var queryDocumentSnapshot = querySnapshot.getDocuments().get(0);
                id = queryDocumentSnapshot.getId();
                t = queryDocumentSnapshot.toObject(entityClass);

            } else if (this.reference != null) {

                DocumentSnapshot documentSnapshot = this.transaction.get(this.reference).get();
                id = documentSnapshot.getId();
                t = documentSnapshot.toObject(entityClass);

            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }

        if (t == null || Strings.isNullOrEmpty(id)) {
            return null;
        }

        t.setId(id);
        return t;
    }
}
