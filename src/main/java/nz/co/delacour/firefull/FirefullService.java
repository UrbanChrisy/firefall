package nz.co.delacour.firefull;

import com.google.cloud.firestore.Firestore;
import com.google.common.base.Preconditions;

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
        Preconditions.checkState(factory != null, "You must call nz.co.delacour.firefull.FirefullFactory.init() before using nz.co.delacour.firefull.Firefull");
        return factory;
    }

    public static Firefull fir() {
        return factory().fir();
    }
}
