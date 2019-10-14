package nz.co.delacour.firefall.core;

import com.google.cloud.firestore.Firestore;
import com.google.common.base.Preconditions;

import java.io.Closeable;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class FirefullService {

    private static FirefullFactory factory;

    public FirefullService() {
    }

    public static void init(Firestore firestore) {
        init(new FirefullFactory(firestore));
    }

    public static void init(FirefullFactory fact) {
        factory = fact;
    }

    public static FirefullFactory factory() {
        Preconditions.checkState(factory != null, "You must call FirefullFactory.init() before using Firefull");
        return factory;
    }

    public static Firefull fir() {
        return factory().fir();
    }

    public static Closeable begin() {
        return factory().open();
    }

}
