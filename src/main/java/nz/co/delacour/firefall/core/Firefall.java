package nz.co.delacour.firefall.core;

import com.google.cloud.firestore.DocumentReference;
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

    private final DocumentReference parent;

    public Firefall(FirefallFactory firefallFactory, DocumentReference parent) {
        this.firefallFactory = firefallFactory;
        this.parent = parent;
    }

    public <T extends HasId<T>> Firefall parent(Ref<T> ref) {
        return new Firefall(this.firefallFactory, ref.getReference());
    }

    public FirefallFactory factory() {
        return firefallFactory;
    }

    public Loader load() {
        return new Loader(this, parent);
    }

    public Saver save() {
        return new Saver(this, parent);
    }

    public Deleter delete() {
        return new Deleter(this, parent);
    }

    @Override
    public void close() {
        factory().close(this);
    }

}
