package nz.co.delacour.firefall.core.delete;

import com.google.cloud.firestore.DocumentReference;
import lombok.Data;
import nz.co.delacour.firefall.core.Firefall;
import nz.co.delacour.firefall.core.HasId;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 8/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

@Data
public class Deleter {

    private final Firefall firefall;

    private final DocumentReference parent;

    public Deleter(Firefall firefall, DocumentReference parent) {
        this.firefall = firefall;
        this.parent = parent;
    }

    public <T extends HasId<T>> TypeDeleter<T> type(Class<T> entityClass) {
        return new TypeDeleter<>(this, entityClass, parent);
    }

}
