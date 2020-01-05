package nz.co.delacour.firefall.core;

import com.google.cloud.firestore.Firestore;
import nz.co.delacour.firefall.core.registrar.EntityMetadata;
import nz.co.delacour.firefall.core.registrar.Registrar;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class FirefallFactory {

    private final Firestore firestore;

    private final Registrar registrar;

    private final ThreadLocal<Deque<Firefall>> stacks = ThreadLocal.withInitial(ArrayDeque::new);

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
        final Deque<Firefall> stack = stacks.get();
        if (stack.isEmpty()) {
            throw new IllegalStateException("You have not started a Firefall context. You are probably missing the " +
                    "FirefallFilter. If you are not running in the context of an http request, see the " +
                    "Firefall.run() method.");
        }
        return stack.getLast();
    }

    public Firefall open() {
        final Firefall objectify = new Firefall(this);
        stacks.get().add(objectify);
        return objectify;
    }

    public void close(final Firefall ofy) {
        final Deque<Firefall> stack = stacks.get();
        if (stack.isEmpty())
            throw new IllegalStateException("You have already destroyed the Firefall context.");

        final Firefall popped = stack.removeLast();
        assert popped == ofy : "Mismatched Firefall instances; somehow the stack was corrupted";
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
