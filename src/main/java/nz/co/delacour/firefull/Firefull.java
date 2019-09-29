package nz.co.delacour.firefull;

import nz.co.delacour.firefull.load.Loader;
import nz.co.delacour.firefull.save.Saver;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class Firefull {

    private final FirefullFactory firefullFactory;

    public Firefull(FirefullFactory firefullFactory) {
        this.firefullFactory = firefullFactory;
    }

    public FirefullFactory getFirefullFactory() {
        return firefullFactory;
    }

    public Loader load() {
        return new Loader(this);
    }

    public Saver save() {
        return new Saver(this);
    }


}
