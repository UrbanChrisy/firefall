package nz.co.delacour.firefall.core.test;

import com.google.common.collect.Lists;
import nz.co.delacour.firefall.core.SubCollection;
import nz.co.delacour.firefall.core.entities.Basic;
import nz.co.delacour.firefall.core.entities.BasicSubCollectionEntity;
import nz.co.delacour.firefall.core.util.TestBase;
import org.junit.jupiter.api.Test;

import static nz.co.delacour.firefall.core.FirefallService.factory;
import static nz.co.delacour.firefall.core.FirefallService.fir;
import static org.junit.Assert.*;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 13/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class BasicTest extends TestBase {

    @Test
    public void loadNullId() {
        factory().register(Basic.class);

        var savedEntity = fir().load().type(Basic.class).id(null).now();
        assertNull(savedEntity);
    }

    @Test
    public void saveEntity() {
        factory().register(Basic.class);

        Basic basic = new Basic();
        basic.setTestString("testString");

        var savedEntity = fir().save().type(Basic.class).entity(basic).now();
        assertNotNull(savedEntity);
        assertNotNull(savedEntity.getId());

        var entity = fir().load().type(Basic.class).id(savedEntity.getId()).now();
        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertEquals(entity.getId(), savedEntity.getId());
        assertEquals(entity.getTestString(), basic.getTestString());
    }

    @Test
    public void saveEntities() {
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
    }

    @Test
    public void save2xEntity() {
        factory().register(Basic.class);

        Basic basic = new Basic();
        basic.setTestString("testString");

        var savedEntity = fir().save().type(Basic.class).entity(basic).now();
        assertNotNull(savedEntity);
        assertNotNull(savedEntity.getId());

        var entity = fir().load().type(Basic.class).id(savedEntity.getId()).now();
        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertEquals(entity.getId(), savedEntity.getId());
        assertEquals(entity.getTestString(), basic.getTestString());

        entity.setTestString("testString2");

        var saved2xEntity = fir().save().type(Basic.class).entity(entity).now();
        assertNotNull(savedEntity);
        assertNotNull(savedEntity.getId());

        var entity2 = fir().load().type(Basic.class).id(savedEntity.getId()).now();
        assertNotNull(entity2);
        assertNotNull(entity2.getId());
        assertEquals(entity2.getId(), savedEntity.getId());
        assertEquals(entity2.getTestString(), saved2xEntity.getTestString());
    }

    @Test
    public void loadSubCollectionEntity() {
        factory().register(Basic.class);
        factory().register(BasicSubCollectionEntity.class);

        Basic basic = new Basic();
        basic.setTestString("testString");

        var savedEntity = fir().save().type(Basic.class).entity(basic).now();
        assertNotNull(savedEntity);
        assertNotNull(savedEntity.getId());



        BasicSubCollectionEntity basicSubCollectionEntity = new BasicSubCollectionEntity();
        basicSubCollectionEntity.setTestString("testString");

        var savedSubCollectionEntity1 = fir().parent(savedEntity.ref()).save().type(BasicSubCollectionEntity.class).entity(basicSubCollectionEntity).now();
        var savedSubCollectionEntity2 = fir().parent(savedEntity.ref()).save().type(BasicSubCollectionEntity.class).entity(basicSubCollectionEntity).now();
        var savedSubCollectionEntity3 = fir().parent(savedEntity.ref()).save().type(BasicSubCollectionEntity.class).entity(basicSubCollectionEntity).now();
        var savedSubCollectionEntity4 = fir().parent(savedEntity.ref()).save().type(BasicSubCollectionEntity.class).entity(basicSubCollectionEntity).now();
        assertNotNull(savedSubCollectionEntity1);
        assertNotNull(savedSubCollectionEntity1.getId());

    }



}
