package nz.co.delacour.firefall.core.save;


import com.google.cloud.firestore.DocumentReference;
import nz.co.delacour.firefall.core.Firefall;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.registrar.LifecycleMethod;

import static nz.co.delacour.firefall.core.FirefallService.getMetadata;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class Saver {

    private final Firefall firefall;

    public Saver(Firefall firefall) {
        this.firefall = firefall;
    }

    public Firefall getFirefall() {
        return firefall;
    }

    public <T extends HasId<T>> TypeSaver<T> type(Class<T> entityClass) {
        return new TypeSaver<>(this, entityClass);
    }
}
