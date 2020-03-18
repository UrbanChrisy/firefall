package nz.co.delacour.firefall.core;

import com.google.cloud.firestore.Firestore;
import com.google.common.base.Preconditions;
import nz.co.delacour.firefall.core.registrar.EntityMetadata;

import java.io.Closeable;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class FirefallService {

    private static FirefallFactory factory;

    public FirefallService() {
    }

    public static void init(Firestore firestore) {
        init(new FirefallFactory(firestore));
    }

    public static void init(FirefallFactory fact) {
        factory = fact;
    }

    public static FirefallFactory factory() {
        Preconditions.checkState(factory != null, "You must call FirefallFactory.init() before using Firefall");
        return factory;
    }

    public static Firefall fir() {
        return factory().fir();
    }

    public static <T extends HasId<T>> void register(Class<T> clazz) {
        factory().register(clazz);
    }

    public static <T extends HasId<T>> EntityMetadata<T> getMetadata(final String kind) {
        return factory().getMetadata(kind);
    }

    public static <T extends HasId<T>> EntityMetadata<T> getMetadata(final Class<T> entityClass) {
        return factory().getMetadata(entityClass);
    }

}
