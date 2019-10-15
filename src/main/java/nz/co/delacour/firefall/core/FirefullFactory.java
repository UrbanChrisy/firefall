package nz.co.delacour.firefall.core;

import com.google.cloud.firestore.Firestore;
import nz.co.delacour.firefall.core.registrar.Registrar;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class FirefullFactory {

    private final Firestore firestore;

    private final Registrar registrar;

    private final ThreadLocal<Deque<Firefull>> stacks = ThreadLocal.withInitial(ArrayDeque::new);

    public FirefullFactory(Firestore firestore) {
        this.firestore = firestore;
        this.registrar = new Registrar(this);
    }

    public Firestore getFirestore() {
        return firestore;
    }

    public Registrar getRegistrar() {
        return registrar;
    }

    public Firefull fir() {
        final Deque<Firefull> stack = stacks.get();
        if (stack.isEmpty())
            throw new IllegalStateException("You have not started a Firefull context. You are probably missing the " +
                    "FirefullFilter. If you are not running in the context of an http request, see the " +
                    "Firefull.run() method.");
        return stack.getLast();
    }

    public Firefull open() {
        final Firefull objectify = new Firefull(this);
        stacks.get().add(objectify);
        return objectify;
    }

    public void close(final Firefull ofy) {
        final Deque<Firefull> stack = stacks.get();
        if (stack.isEmpty())
            throw new IllegalStateException("You have already destroyed the Firefull context.");

        final Firefull popped = stack.removeLast();
        assert popped == ofy : "Mismatched Firefull instances; somehow the stack was corrupted";
    }

    public <T extends HasId> void register(final Class<T> clazz) {
        this.registrar.register(clazz);
    }
}
