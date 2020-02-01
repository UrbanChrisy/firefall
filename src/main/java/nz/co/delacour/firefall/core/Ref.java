package nz.co.delacour.firefall.core;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.common.base.Strings;
import lombok.Data;
import nz.co.delacour.firefall.core.load.LoadResult;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static nz.co.delacour.firefall.core.FirefallService.fir;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

@Data
public class Ref<T extends HasId<T>> {

    private DocumentReference reference;

    public Ref() {
    }

    public Ref(Class<T> entityClass, String id) {
        if (Strings.isNullOrEmpty(id)) {
            return;
        }

        this.reference = fir().load().type(entityClass).ref(id);
    }

    @Exclude
    public String getId() {

        if (this.reference == null) {
            return null;
        }

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

    public static <T extends HasId<T>> Ref<T> create(Class<T> entityClass, String id) {
        return new Ref<T>(entityClass, id);
    }

    @Override
    public String toString() {
        return "Ref<?>(" + this.getId() + ")";
    }

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

}
