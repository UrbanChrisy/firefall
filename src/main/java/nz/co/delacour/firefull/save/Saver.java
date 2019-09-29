package nz.co.delacour.firefull.save;

import nz.co.delacour.firefull.Firefull;
import nz.co.delacour.firefull.HasId;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class Saver {

    private final Firefull firefull;

    public Saver(Firefull firefull) {
        this.firefull = firefull;
    }

    public Firefull getFirefull() {
        return firefull;
    }

    public <T extends HasId> TypeSaver<T> type(Class<T> entityClass) {
        return new TypeSaver<>(this, entityClass);
    }
}
