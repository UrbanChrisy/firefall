package nz.co.delacour.firefall.core.test;

import lombok.var;
import nz.co.delacour.firefall.core.entities.Basic;
import nz.co.delacour.firefall.core.util.TestBase;
import org.junit.jupiter.api.Test;

import static nz.co.delacour.firefall.core.FirefallService.factory;
import static nz.co.delacour.firefall.core.FirefallService.fir;
import static org.junit.Assert.*;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 17/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class DeleteTest extends TestBase {

    @Test
    public void deleteEntity() {
        factory().register(Basic.class);

        Basic basic = new Basic();
        basic.setTestString("testString");

        var entity = fir().save().type(Basic.class).entity(basic).now();

        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertEquals(entity.getTestString(), basic.getTestString());

        fir().delete().type(Basic.class).id(entity.getId()).now();
        var loadEntity = fir().load().type(Basic.class).id(entity.getId()).now();
        assertNull(loadEntity);
    }

}
