package nz.co.delacour.firefull.load;

import nz.co.delacour.firefull.HasId;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public interface Result<T extends HasId> {
    T now();
}
