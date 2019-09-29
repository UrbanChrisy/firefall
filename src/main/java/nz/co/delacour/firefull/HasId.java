package nz.co.delacour.firefull;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.common.base.Strings;
import lombok.var;


/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class HasId<T> {

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
    public DocumentReference ref() {

        if (Strings.isNullOrEmpty(this.getId())) {
            return null;
        }

        var kind = Ref.getKind(this.entityClass);
//        return getFirestore().collection(kind).document(this.getId());
        return null;
    }
}
