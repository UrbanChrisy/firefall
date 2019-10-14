package nz.co.delacour.firefall.core;

import com.google.cloud.firestore.annotation.Exclude;


/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class HasId<T extends HasId<T>> {

    private final Class<T> entityClass;

    private String id;

    public HasId(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public Ref<T> ref() {
        return new Ref<T>(entityClass, id);
    }
}
