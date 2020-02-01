package nz.co.delacour.firefall.core.load;

import com.google.cloud.firestore.DocumentReference;
import nz.co.delacour.firefall.core.Firefall;
import nz.co.delacour.firefall.core.HasId;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class Loader {

    private final Firefall firefall;

    private final DocumentReference parent;

    public Loader(Firefall firefall, DocumentReference parent) {
        this.firefall = firefall;
        this.parent = parent;
    }

    public Firefall getFirefall() {
        return firefall;
    }

    public <T extends HasId<T>> LoadType<T> type(Class<T> entityClass) {
        return new LoadType<>(this, entityClass, parent);
    }

}
