package nz.co.delacour.firefall.core.delete;

import lombok.Data;
import nz.co.delacour.firefall.core.Firefall;
import nz.co.delacour.firefall.core.HasId;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 8/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

@Data
public class Deleter {

    private final Firefall firefall;

    public Deleter(Firefall firefall) {
        this.firefall = firefall;
    }

    public <T extends HasId> TypeDeleter<T> type(Class<T> entityClass) {
        return new TypeDeleter<T>(this, entityClass);
    }

}