package nz.co.delacour.firefall.core.test;

import nz.co.delacour.firefall.core.entities.Basic;
import nz.co.delacour.firefall.core.entities.BasicSubCollectionEntity;
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
        basic.setSomeString("someString");

        var entity = fir().save().type(Basic.class).entity(basic).now();

        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertEquals(entity.getSomeString(), basic.getSomeString());

        fir().delete().type(Basic.class).id(entity.getId()).now();
        var loadEntity = fir().load().type(Basic.class).id(entity.getId()).now();
        assertNull(loadEntity);
    }

    @Test
    public void deleteEntityWithSubCollections() {
        factory().register(Basic.class);
        factory().register(BasicSubCollectionEntity.class);

        Basic basic = new Basic();
        basic.setSomeString("someString");

        var savedEntity = fir().save().type(Basic.class).entity(basic).now();
        assertNotNull(savedEntity);
        assertNotNull(savedEntity.getId());

        BasicSubCollectionEntity basicSubCollectionEntity1 = new BasicSubCollectionEntity();
        basicSubCollectionEntity1.setSomeString("someString");

        var savedSubCollectionEntity = fir().save().type(BasicSubCollectionEntity.class).parent(savedEntity.ref()).entity(basicSubCollectionEntity1).now();
        assertNotNull(savedSubCollectionEntity);
        assertNotNull(savedSubCollectionEntity.getId());

        fir().delete().type(Basic.class).entity(basic).now();

        var loadEntity = fir().load().type(Basic.class).id(basic.getId()).now();
        assertNull(loadEntity);

        var loadSubEntity = fir().load().type(BasicSubCollectionEntity.class).parent(savedEntity.ref()).id(savedSubCollectionEntity.getId()).now();
        assertNotNull(loadSubEntity);
        assertNotNull(loadSubEntity.getId());

    }
}
