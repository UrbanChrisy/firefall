package nz.co.delacour.firefall.core.test;

import lombok.Data;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.annotations.Entity;
import nz.co.delacour.firefall.core.annotations.OnSave;
import nz.co.delacour.firefall.core.util.TestBase;
import org.junit.jupiter.api.Test;

import static nz.co.delacour.firefall.core.FirefallService.factory;
import static nz.co.delacour.firefall.core.FirefallService.fir;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 15/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class OnSaveTest extends TestBase {

    @Data
    @Entity
    public static class OnSaveEntity extends HasId<OnSaveEntity> {

        private int incrementOnSave = 0;

        public OnSaveEntity() {
            super(OnSaveEntity.class);
        }

        @OnSave
        public void onSave() {
            this.incrementOnSave += 1;
        }

    }

    @Test
    public void basicOnLoad() {
        factory().register(OnSaveEntity.class);

        OnSaveEntity entity = new OnSaveEntity();
        entity.setIncrementOnSave(1);

        var savedEntity = fir().save().type(OnSaveEntity.class).entity(entity).now();
        assertNotNull(savedEntity);

        assertNotNull(savedEntity);
        assertEquals(savedEntity.getIncrementOnSave(), 2);
    }

}
