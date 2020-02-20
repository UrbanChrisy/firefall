package nz.co.delacour.firefall.core.test;

import com.google.common.collect.Lists;
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
        basic.setSomeString("someString");

        var savedEntity = fir().save().type(Basic.class).entity(basic).now();
        assertNotNull(savedEntity);
        assertNotNull(savedEntity.getId());

        var entity = fir().load().type(Basic.class).id(savedEntity.getId()).now();
        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertEquals(entity.getId(), savedEntity.getId());
        assertEquals(entity.getSomeString(), basic.getSomeString());
    }

    @Test
    public void saveEntities() {
        factory().register(Basic.class);

        Basic basic1 = new Basic();
        basic1.setSomeString("someString1");
        Basic basic2 = new Basic();
        basic2.setSomeString("someString2");

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
        basic.setSomeString("someString");

        var savedEntity = fir().save().type(Basic.class).entity(basic).now();
        assertNotNull(savedEntity);
        assertNotNull(savedEntity.getId());

        var entity = fir().load().type(Basic.class).id(savedEntity.getId()).now();
        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertEquals(entity.getId(), savedEntity.getId());
        assertEquals(entity.getSomeString(), basic.getSomeString());

        entity.setSomeString("someString2");

        var saved2xEntity = fir().save().type(Basic.class).entity(entity).now();
        assertNotNull(savedEntity);
        assertNotNull(savedEntity.getId());

        var entity2 = fir().load().type(Basic.class).id(savedEntity.getId()).now();
        assertNotNull(entity2);
        assertNotNull(entity2.getId());
        assertEquals(entity2.getId(), savedEntity.getId());
        assertEquals(entity2.getSomeString(), saved2xEntity.getSomeString());

    }

//    @Test
//    public void saveSubCollectionEntity() {
//        factory().register(Basic.class);
//        factory().register(BasicSubCollectionEntity.class);
//
//        Basic basic = new Basic();
//        basic.setSomeString("someString");
//
//        var savedEntity = fir().save().type(Basic.class).entity(basic).now();
//        assertNotNull(savedEntity);
//        assertNotNull(savedEntity.getId());
//
//        BasicSubCollectionEntity basicSubCollectionEntity = new BasicSubCollectionEntity();
//        basicSubCollectionEntity.setSomeString("someString");
//
//
//        var savedSubCollectionEntity1 = fir().save().type(BasicSubCollectionEntity.class).parent(savedEntity.ref()).entity(basicSubCollectionEntity).now();
//        assertNotNull(savedSubCollectionEntity1);
//        assertNotNull(savedSubCollectionEntity1.getId());
//
//    }
//
//    @Test
//    public void loadSubCollectionEntity() {
//        factory().register(Basic.class);
//        factory().register(BasicSubCollectionEntity.class);
//
//        Basic basic = new Basic();
//        basic.setSomeString("someString");
//
//        var savedEntity = fir().save().type(Basic.class).entity(basic).now();
//        assertNotNull(savedEntity);
//        assertNotNull(savedEntity.getId());
//
//        BasicSubCollectionEntity basicSubCollectionEntity1 = new BasicSubCollectionEntity();
//        basicSubCollectionEntity1.setSomeString("someString");
//
//        var savedSubCollectionEntity =  fir().save().type(BasicSubCollectionEntity.class).parent(savedEntity.ref()).entity(basicSubCollectionEntity1).now();
//        assertNotNull(savedSubCollectionEntity);
//        assertNotNull(savedSubCollectionEntity.getId());
//
//        var loadSubEntity = fir().load().type(BasicSubCollectionEntity.class).parent(savedEntity.ref()).id(savedSubCollectionEntity.getId()).now();
//        assertNotNull(loadSubEntity);
//        assertNotNull(loadSubEntity.getId());
//        assertEquals(savedSubCollectionEntity.getId(), loadSubEntity.getId());
//
//    }

}
