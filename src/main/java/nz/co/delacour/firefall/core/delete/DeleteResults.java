package nz.co.delacour.firefall.core.delete;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Precondition;
import com.google.cloud.firestore.WriteResult;
import lombok.var;
import nz.co.delacour.firefall.core.exceptions.FirefullException;
import nz.co.delacour.firefall.core.HasId;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 10/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class DeleteResults<T extends HasId> {

    private final ApiFuture<List<WriteResult>> future;

    public DeleteResults(List<DocumentReference> references, CollectionReference collection, Precondition options) {

        if (options == null) {
            options = Precondition.NONE;
        }

        var batch = collection.getFirestore().batch();
        for (DocumentReference reference : references) {
            batch.delete(reference, options);
        }
        this.future = batch.commit();
    }

    public void now() {
        try {
            this.future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }
    }

    public DeleteResults<T> listener(Runnable runnable, Executor executor) {
        this.future.addListener(runnable, executor);
        return this;
    }

}
