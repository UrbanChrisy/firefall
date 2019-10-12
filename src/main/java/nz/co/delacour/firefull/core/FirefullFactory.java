package nz.co.delacour.firefull.core;

import com.google.cloud.firestore.Firestore;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class FirefullFactory {

    private final Firestore firestore;

    public FirefullFactory(Firestore firestore) {
        this.firestore = firestore;
    }

    public Firestore getFirestore() {
        return firestore;
    }

    public Firefull fir() {
        return new Firefull(this);
    }

}
