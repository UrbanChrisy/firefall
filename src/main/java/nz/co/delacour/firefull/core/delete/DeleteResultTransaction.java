package nz.co.delacour.firefull.core.delete;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Precondition;
import com.google.cloud.firestore.Transaction;
import nz.co.delacour.firefull.core.HasId;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 10/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class DeleteResultTransaction<T extends HasId> {

    private final Transaction transaction;

    private final DocumentReference reference;

    private final Precondition options;

    public DeleteResultTransaction(Transaction transaction, DocumentReference reference, Precondition options) {
        this.transaction = transaction;
        this.reference = reference;
        this.options = options;
    }

    public Transaction now() {

        if (this.reference == null) {
            return this.transaction;
        }

        return this.transaction.delete(this.reference, this.options);
    }

}
