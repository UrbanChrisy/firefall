package nz.co.delacour.firefull.core.save;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Transaction;
import com.google.cloud.firestore.WriteResult;
import com.google.common.base.Strings;
import nz.co.delacour.firefull.core.HasId;
import nz.co.delacour.firefull.core.exceptions.FirefullException;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 9/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class SaveResult<T extends HasId> {

    private final T t;

    private final CollectionReference collection;

    public SaveResult(T t, CollectionReference collection) {
        this.t = t;
        this.collection = collection;
    }

    @Nullable
    public T now() {
        DocumentReference ref = this.ref();
        if (ref == null) {
            return null;
        }

        try {
            ref.set(this.t).get();
            return this.t;
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }
    }

    @Nullable
    public ApiFuture<WriteResult> async() {
        DocumentReference ref = this.ref();
        if (ref == null) {
            return null;
        }

        return ref.set(this.t);
    }

    @Nullable
    public DocumentReference ref() {

        if (this.t == null) {
            return null;
        }

        DocumentReference reference;
        if (Strings.isNullOrEmpty(this.t.getId())) {
            reference = this.collection.document();
            this.t.setId(reference.getId());
        } else {
            reference = this.collection.document(this.t.getId());
        }
        return reference;
    }

    public SaveResultTransaction<T> transaction(Transaction transaction) {
        return new SaveResultTransaction<>(transaction, this.t, this.ref());
    }

}
