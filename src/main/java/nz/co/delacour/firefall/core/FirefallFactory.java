package nz.co.delacour.firefall.core;

import com.google.cloud.firestore.Firestore;
import nz.co.delacour.firefall.core.registrar.EntityMetadata;
import nz.co.delacour.firefall.core.registrar.Registrar;


public class FirefallFactory {

    private final Firestore firestore;

    private final Registrar registrar;

    public FirefallFactory(Firestore firestore) {
        this.firestore = firestore;
        this.registrar = new Registrar(this);
    }

    public Firestore getFirestore() {
        return firestore;
    }

    public Registrar getRegistrar() {
        return registrar;
    }

    public Firefall fir() {
        return new Firefall(this, null);
    }

    public <T extends HasId<T>> void register(final Class<T> clazz) {
        this.registrar.register(clazz);
    }

    public <T extends HasId<T>> EntityMetadata<T> getMetadata(final String kind) {
        return this.registrar.getMetadata(kind);
    }

    public <T extends HasId<T>> EntityMetadata<T> getMetadata(final Class<T> entityClass) {
        return this.registrar.getMetadata(entityClass);
    }

}
