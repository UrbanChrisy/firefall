package nz.co.delacour.firefull;

import com.google.cloud.firestore.Firestore;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class FirefullFactory {

    private final Firestore firestore;

    private final ThreadLocal<Deque<Firefull>> stacks;

    public FirefullFactory(Firestore firestore) {
        this.firestore = firestore;

        this.stacks = ThreadLocal.withInitial(ArrayDeque::new);
    }

    public Firestore getFirestore() {
        return firestore;
    }

    public Firefull fir() {
        return new Firefull(this);
//        Deque<nz.co.delacour.firefull.Firefull> stack = this.stacks.get();
//        if (stack.isEmpty()) {
//            throw new IllegalStateException("You have not started an Objectify context. You are probably missing the ObjectifyFilter. If you are not running in the context of an http request, see the ObjectifyService.run() method.");
//        } else {
//            return stack.getLast();
//        }
    }

}
