package nz.co.delacour.firefull.core.save;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Transaction;
import nz.co.delacour.firefull.core.HasId;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 10/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class SaveResultTransaction<T extends HasId> {

    private final Transaction transaction;

    private final T t;

    private final DocumentReference documentReference;

    public SaveResultTransaction(Transaction transaction, T t, DocumentReference documentReference) {
        this.transaction = transaction;
        this.t = t;
        this.documentReference = documentReference;
    }

    public Transaction async() {
        if (documentReference == null) {
            return this.transaction;
        }

        transaction.set(documentReference, this.t);

        return this.transaction;
    }

}
