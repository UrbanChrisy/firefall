package nz.co.delacour.firefall.core.load;

import nz.co.delacour.firefall.core.Firefall;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.util.TypeUtils;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class Loader {

    private final Firefall firefall;

    public Loader(Firefall firefall) {
        this.firefall = firefall;
    }

    public Firefall getFirefall() {
        return firefall;
    }

    public <T extends HasId<T>> LoadType<T> type(Class<T> entityClass) {
        var collection = TypeUtils.getCollection(getFirefall().factory().getFirestore(), entityClass);
        return new LoadType<>(this, entityClass, collection);
    }

}
