package nz.co.delacour.firefull.core.delete;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Precondition;
import com.google.cloud.firestore.Transaction;
import com.google.cloud.firestore.WriteResult;
import nz.co.delacour.firefull.core.HasId;
import nz.co.delacour.firefull.core.exceptions.FirefullException;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 10/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class DeleteResult<T extends HasId> {

    private final DocumentReference reference;

    private Precondition options = Precondition.NONE;

    public DeleteResult(DocumentReference reference) {
        this.reference = reference;
    }

    public DeleteResult<T> options(Precondition options) {
        this.options = options;
        return this;
    }

    public void now() {

        if (reference == null) {
            return;
        }

        try {
            this.reference.delete(this.options).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }
    }

    @Nullable
    public ApiFuture<WriteResult> async() {

        if (this.reference == null) {
            return null;
        }

        return this.reference.delete(this.options);
    }

    public DeleteResultTransaction<T> transaction(Transaction transaction) {
        return new DeleteResultTransaction<>(transaction, this.reference, this.options);
    }


}
