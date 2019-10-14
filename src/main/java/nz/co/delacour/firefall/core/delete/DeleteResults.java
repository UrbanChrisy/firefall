package nz.co.delacour.firefall.core.delete;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Precondition;
import com.google.cloud.firestore.Transaction;
import nz.co.delacour.firefall.core.exceptions.FirefullException;
import nz.co.delacour.firefall.core.HasId;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 10/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class DeleteResults<T extends HasId> {

    private final List<DocumentReference> references;

    private final CollectionReference collection;

    private Precondition options = Precondition.NONE;

    public DeleteResults(List<DocumentReference> references, CollectionReference collection) {
        this.references = references;
        this.collection = collection;
    }

    public DeleteResults<T> options(Precondition options) {
        this.options = options;
        return this;
    }

    public void now() {
        try {
            async().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }
    }

    public ApiFuture<Void> async() {
        return collection.getFirestore().runTransaction(transaction -> {
            for (DocumentReference reference : this.references) {
                transaction.delete(reference, options);
            }
            return null;
        });
    }

    public DeleteResultsTransaction<T> transaction(Transaction transaction) {
        return new DeleteResultsTransaction<>(transaction);
    }
}
