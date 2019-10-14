package nz.co.delacour.firefall.core.test;

import com.google.common.collect.Lists;
import lombok.var;
import nz.co.delacour.firefall.core.util.TestBase;
import nz.co.delacour.firefall.core.util.TestEntity;
import org.junit.Test;

import static nz.co.delacour.firefall.core.FirefullService.fir;
import static org.junit.Assert.*;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 13/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class BasicTests extends TestBase {

    @Test
    public void loadNullId() {
        var user = fir().load().type(TestEntity.class).id(null).now();
        assertNull(user);
    }

    @Test
    public void saveEntityWithoutId() {
        TestEntity testEntity = new TestEntity();
        testEntity.setTestString("testEntity");

        var entity = fir().save().type(TestEntity.class).save(testEntity).now();

        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertEquals(entity.getTestString(), testEntity.getTestString());

        fir().delete().type(TestEntity.class).entity(entity).now();
    }

    @Test
    public void saveEntitiesWithoutId() {
        TestEntity testEntity1 = new TestEntity();
        testEntity1.setTestString("testEntity1");
        TestEntity testEntity2 = new TestEntity();
        testEntity2.setTestString("testEntity2");

        var entities = fir().save().type(TestEntity.class).save(Lists.newArrayList(testEntity1, testEntity2)).now();

        assertNotNull(entities);
        assertFalse(entities.isEmpty());

        assertNotNull(testEntity1.getId());
        assertNotNull(testEntity2.getId());

        fir().delete().type(TestEntity.class).entities(entities).now();
    }

}
