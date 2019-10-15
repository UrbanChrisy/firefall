package nz.co.delacour.firefall.core.test;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.var;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.annotations.Entity;
import nz.co.delacour.firefall.core.util.TestBase;
import org.junit.Test;

import static nz.co.delacour.firefall.core.FirefullService.factory;
import static nz.co.delacour.firefall.core.FirefullService.fir;
import static org.junit.Assert.*;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 13/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class BasicTests extends TestBase {

    @Data
    @Entity
    public static class Basic extends HasId<Basic> {

        private String testString;

        public Basic() {
            super(Basic.class);
        }

    }

    @Test
    public void loadNullId() {
        factory().register(Basic.class);

        var savedEntity = fir().load().type(Basic.class).id(null).now();
        assertNull(savedEntity);
    }

    @Test
    public void saveEntityWithoutId() {
        factory().register(Basic.class);

        Basic basic = new Basic();
        basic.setTestString("testString");

        var entity = fir().save().type(Basic.class).entity(basic).now();

        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertEquals(entity.getTestString(), basic.getTestString());

        fir().delete().type(Basic.class).entity(entity).now();
    }

    @Test
    public void saveEntitiesWithoutId() {
        factory().register(Basic.class);

        Basic basic1 = new Basic();
        basic1.setTestString("testString1");
        Basic basic2 = new Basic();
        basic2.setTestString("testString2");

        var entities = fir().save().type(Basic.class).entities(Lists.newArrayList(basic1, basic2)).now();

        assertNotNull(entities);
        assertFalse(entities.isEmpty());

        assertNotNull(basic1.getId());
        assertNotNull(basic2.getId());

        fir().delete().type(Basic.class).entities(entities).now();
    }

}
