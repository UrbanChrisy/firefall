package nz.co.delacour.firefall.core;

import com.google.cloud.firestore.Firestore;
import nz.co.delacour.firefall.core.delete.Deleter;
import nz.co.delacour.firefall.core.load.Loader;
import nz.co.delacour.firefall.core.load.Query;
import nz.co.delacour.firefall.core.save.Saver;
import nz.co.delacour.firefall.core.util.TypeUtils;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class Firefall {

    private final FirefallFactory firefallFactory;

    public Firefall(FirefallFactory firefallFactory) {
        this.firefallFactory = firefallFactory;
    }

    public FirefallFactory factory() {
        return firefallFactory;
    }

    public Firestore getFirestore() {
        return factory().getFirestore();
    }

    public <T extends HasId<T>> EntityType<T> type(Class<T> clazz) {
        return new EntityType<>(this, clazz);
    }

}
