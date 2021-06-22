package nz.co.delacour.firefall.core.entities;

import lombok.Data;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.annotations.Entity;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 17/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

@Data
@Entity
public class Basic extends HasId<Basic> {

    private String someString;

    public Basic() {
        super(Basic.class);
    }
    
}