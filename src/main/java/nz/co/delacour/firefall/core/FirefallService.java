package nz.co.delacour.firefall.core;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import nz.co.delacour.firefall.core.registrar.EntityMetadata;


public class FirefallService {

    private static FirefallFactory factory;

    public FirefallService() {
    }

    public static FirefallFactory init(Firestore firestore) {
        return init(new FirefallFactory(firestore));
    }

    public static FirefallFactory init(FirefallFactory fact) {
        FirefallService.factory = fact;
        return fact;
    }

    public static FirefallFactory factory() {

        if (factory == null) {
            Firestore firestore = FirestoreClient.getFirestore();
            init(firestore);
        }

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
