package nz.co.delacour.firefall.core;

import com.google.cloud.firestore.Firestore;
import nz.co.delacour.firefall.core.delete.Deleter;
import nz.co.delacour.firefall.core.load.Loader;
import nz.co.delacour.firefall.core.save.Saver;

import java.io.Closeable;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class Firefall implements Closeable {

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

    public Loader load() {
        return new Loader(this);
    }

    public Saver save() {
        return new Saver(this);
    }

    public Deleter delete() {
        return new Deleter(this);
    }

    @Override
    public void close() {
        factory().close(this);
    }

}
