package nz.co.delacour.firefall.core.test;

import lombok.Data;
import lombok.var;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.annotations.Entity;
import nz.co.delacour.firefall.core.annotations.OnLoad;
import nz.co.delacour.firefall.core.util.TestBase;
import org.junit.Test;

import static nz.co.delacour.firefall.core.FirefullService.factory;
import static nz.co.delacour.firefall.core.FirefullService.fir;
import static org.junit.Assert.*;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 15/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class OnLoadTests extends TestBase {

    @Data
    @Entity
    public static class OnLoadEntity extends HasId<OnLoadEntity> {

        private int incrementOnLoad = 0;

        public OnLoadEntity() {
            super(OnLoadEntity.class);
        }

        @OnLoad
        public void onLoad() {
            this.incrementOnLoad += 1;
        }

    }

    @Test
    public void basicOnLoad() {
        factory().register(OnLoadEntity.class);

        OnLoadEntity entity = new OnLoadEntity();
        entity.setIncrementOnLoad(1);

        var savedEntity = fir().save().type(OnLoadEntity.class).entity(entity).now();
        assertNotNull(savedEntity);
        assertEquals(savedEntity.getIncrementOnLoad(), 2);

        fir().delete().type(OnLoadEntity.class).entity(entity).now();
    }

}
