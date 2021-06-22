package nz.co.delacour.firefall.core;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.common.base.Strings;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static nz.co.delacour.firefall.core.FirefallService.fir;


@Data
public class Ref<T extends HasId<T>> {

    private DocumentReference reference;

    public Ref() {
    }

    public Ref(Class<T> entityClass, String id) {
        if (Strings.isNullOrEmpty(id)) {
            return;
        }

        this.reference = fir().type(entityClass).load().ref(id);
    }

    @Exclude
    public String getId() {

        if (this.reference == null) {
            return null;
        }

        return this.reference.getId();
    }

    public static <T extends HasId<T>> Ref<T> create(Class<T> entityClass, String id) {
        return new Ref<T>(entityClass, id);
    }

    @Override
    public String toString() {
        return this.getId();
    }

    @Exclude
    public String toUrlSafe() {

        var id = this.getId();
        if (Strings.isNullOrEmpty(id)) {
            return null;
        }

        try {
            return URLEncoder.encode(id, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException var2) {
            throw new IllegalStateException("Unexpected encoding exception", var2);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ref<T> ref = (Ref<T>) o;
        return ref.getId().equals(this.getId());
    }
}
