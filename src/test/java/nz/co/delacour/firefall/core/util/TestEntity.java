package nz.co.delacour.firefall.core.util;

import lombok.Data;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.annotations.Entity;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 13/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

@Data
@Entity
public class TestEntity extends HasId<TestEntity> {

    private String testString;

    public TestEntity() {
        super(TestEntity.class);
    }

}
