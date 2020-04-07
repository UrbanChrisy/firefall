package nz.co.delacour.firefall.core.test;

import lombok.Data;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.annotations.Entity;
import nz.co.delacour.firefall.core.annotations.OnLoad;
import nz.co.delacour.firefall.core.entities.Basic;
import nz.co.delacour.firefall.core.util.TestBase;
import org.junit.jupiter.api.Test;

import static nz.co.delacour.firefall.core.FirefallService.factory;
import static nz.co.delacour.firefall.core.FirefallService.fir;
import static org.junit.Assert.*;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 15/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class OnLoadTest extends TestBase {

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

        var savedEntity = fir().type(OnLoadEntity.class).save().entity(entity).now();
        assertNotNull(savedEntity);

        var loadedEntity = fir().type(OnLoadEntity.class).load().id(savedEntity.getId()).now();
        assertNotNull(loadedEntity);
        assertEquals(loadedEntity.getIncrementOnLoad(), 2);
    }

}
