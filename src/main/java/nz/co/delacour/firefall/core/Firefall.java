package nz.co.delacour.firefall.core;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Transaction;

import javax.annotation.Nullable;

public class Firefall {

    private final FirefallFactory firefallFactory;
    @Nullable
    private final Transaction transaction;

    public Firefall(FirefallFactory firefallFactory) {
        this.firefallFactory = firefallFactory;
        this.transaction = null;
    }

    public Firefall(FirefallFactory firefallFactory, @Nullable Transaction transaction) {
        this.firefallFactory = firefallFactory;
        this.transaction = transaction;
    }

    public FirefallFactory factory() {
        return firefallFactory;
    }

    public Firestore getFirestore() {
        return factory().getFirestore();
    }

    public <T extends HasId<T>> EntityType<T> type(Class<T> clazz) {
        return new EntityType<>(this, clazz, transaction);
    }

    public Firefall transaction(Transaction transaction) {
        return new Firefall(this.firefallFactory, transaction);
    }
}
