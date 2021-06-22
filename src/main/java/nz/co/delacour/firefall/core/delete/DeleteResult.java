package nz.co.delacour.firefall.core.delete;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Precondition;
import com.google.cloud.firestore.Transaction;
import com.google.cloud.firestore.WriteResult;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefallException;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;


public class DeleteResult<T extends HasId<T>> {

    private final ApiFuture<WriteResult> future;
    @Nullable
    private final Transaction transaction;

    public DeleteResult(DocumentReference reference, Precondition options, @Nullable Transaction transaction) {
        if (reference == null) {
            this.future = ApiFutures.immediateFuture(null);
            this.transaction = transaction;
        } else {

            if (options == null) {
                options = Precondition.NONE;
            }

            if (transaction != null) {
                this.future = ApiFutures.immediateFuture(null);
                this.transaction = transaction.delete(reference, options);
            } else {
                this.future = reference.delete(options);
                this.transaction = null;
            }
        }
    }

    public void now() {
        try {
            this.future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefallException(e);
        }
    }

    public DeleteResult<T> listener(Runnable runnable, Executor executor) {
        this.future.addListener(runnable, executor);
        return this;
    }

}
