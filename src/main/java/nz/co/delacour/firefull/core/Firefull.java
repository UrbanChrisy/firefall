package nz.co.delacour.firefull.core;

import lombok.var;
import nz.co.delacour.firefull.core.delete.Deleter;
import nz.co.delacour.firefull.core.exceptions.FirefullException;
import nz.co.delacour.firefull.core.load.LoadResult;
import nz.co.delacour.firefull.core.load.Loader;
import nz.co.delacour.firefull.core.save.Saver;

import java.util.concurrent.ExecutionException;

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

    public Deleter delete() {
        return new Deleter(this);
    }

    public <T extends HasId> LoadResult<T> path(String path, Class<T> entityClass) {
        var ref = getFirefullFactory().getFirestore().document(path);
        return new LoadResult<>(ref, entityClass);
    }

}
