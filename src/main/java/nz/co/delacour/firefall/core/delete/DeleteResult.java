package nz.co.delacour.firefall.core.delete;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Precondition;
import com.google.cloud.firestore.WriteResult;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefallException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 10/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class DeleteResult<T extends HasId<T>> {

    private final ApiFuture<WriteResult> future;

    public DeleteResult(DocumentReference reference, Precondition options) {
        if (reference == null) {
            this.future = ApiFutures.immediateFuture(null);
        } else {

            if (options == null) {
                options = Precondition.NONE;
            }

            this.future = reference.delete(options);
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
