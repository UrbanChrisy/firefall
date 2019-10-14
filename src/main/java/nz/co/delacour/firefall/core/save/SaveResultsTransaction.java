package nz.co.delacour.firefall.core.save;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Transaction;
import nz.co.delacour.firefall.core.HasId;

import java.util.Map;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 10/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class SaveResultsTransaction<T extends HasId> {

    private final Transaction transaction;

    private final Map<DocumentReference, T> map;

    public SaveResultsTransaction(Transaction transaction, Map<DocumentReference, T> map) {
        this.transaction = transaction;
        this.map = map;
    }

    public Transaction async() {
        if (this.map == null) {
            return this.transaction;
        }

        for (Map.Entry<DocumentReference, T> entry : this.map.entrySet()) {
            this.transaction.set(entry.getKey(), entry.getValue()).getAll();
        }

        return this.transaction;
    }

}
