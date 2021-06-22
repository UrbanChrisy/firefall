package nz.co.delacour.firefall.core.delete;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Precondition;
import com.google.cloud.firestore.WriteResult;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefallException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;


public class DeleteResults<T extends HasId<T>> {

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
            throw new FirefallException(e);
        }
    }

    public DeleteResults<T> listener(Runnable runnable, Executor executor) {
        this.future.addListener(runnable, executor);
        return this;
    }

}
