package nz.co.delacour.firefall.core.load;

import nz.co.delacour.firefall.core.Firefull;
import nz.co.delacour.firefall.core.HasId;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class Loader {

    private final Firefull firefull;

    public Loader(Firefull firefull) {
        this.firefull = firefull;
    }

    public Firefull getFirefull() {
        return firefull;
    }

    public <T extends HasId> LoadType<T> type(Class<T> entityClass) {
        return new LoadType<>(this, entityClass);
    }

}
