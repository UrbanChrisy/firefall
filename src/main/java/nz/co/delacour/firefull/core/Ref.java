package nz.co.delacour.firefull.core;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nz.co.delacour.firefull.core.load.LoadResult;

import static nz.co.delacour.firefull.core.FirefullService.fir;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

@Data
@Slf4j
public class Ref<T extends HasId<T>> {

    private DocumentReference reference;

    public Ref() {
    }

    public Ref(Class<T> entityClass, String id) {
        if (Strings.isNullOrEmpty(id)) {
            return;
        }

        this.reference = fir().load().type(entityClass).id(id).ref();
    }

    public Ref(DocumentReference reference) {
        if (reference == null) {
            return;
        }

        this.reference = reference;
    }

    @Exclude
    public String getId() {
        return this.reference.getId();
    }

    @Exclude
    public T get(Class<T> entityClass) {
        return load(entityClass).now();
    }

    @Exclude
    public LoadResult<T> load(Class<T> entityClass) {
        return new LoadResult<T>(this.reference, entityClass);
    }

    public static <T extends HasId<T>> Ref<T> create(DocumentReference reference) {
        return new Ref<T>(reference);
    }

    public static <T extends HasId<T>> Ref<T> create(Class<T> entityClass, String id) {
        return new Ref<T>(entityClass, id);
    }

    public boolean equals(Ref<T> ref) {
        if (ref == null) {
            return false;
        }

        if (this == ref) {
            return true;
        }

        return ref.getId().equals(this.getId());
    }
}
