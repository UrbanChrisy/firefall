package nz.co.delacour.firefall.core;

import lombok.var;
import nz.co.delacour.firefall.core.delete.Deleter;
import nz.co.delacour.firefall.core.load.LoadResult;
import nz.co.delacour.firefall.core.load.Loader;
import nz.co.delacour.firefall.core.save.Saver;

import java.io.Closeable;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class Firefull implements Closeable {

    private final FirefullFactory firefullFactory;

    public Firefull(FirefullFactory firefullFactory) {
        this.firefullFactory = firefullFactory;
    }

    public FirefullFactory factory() {
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
        var ref = factory().getFirestore().document(path);
        return new LoadResult<>(ref, entityClass);
    }

    @Override
    public void close() {

        factory().close(this);

    }
}
