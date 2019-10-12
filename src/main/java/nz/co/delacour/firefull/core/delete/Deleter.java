package nz.co.delacour.firefull.core.delete;

import lombok.Data;
import nz.co.delacour.firefull.core.Firefull;
import nz.co.delacour.firefull.core.HasId;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 8/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

@Data
public class Deleter {

    private final Firefull firefull;

    public Deleter(Firefull firefull) {
        this.firefull = firefull;
    }

    public <T extends HasId> TypeDeleter<T> type(Class<T> entityClass) {
        return new TypeDeleter<T>(this, entityClass);
    }

}
