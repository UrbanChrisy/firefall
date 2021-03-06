package nz.co.delacour.firefall.core.test;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.annotations.Entity;
import nz.co.delacour.firefall.core.entities.Basic;
import nz.co.delacour.firefall.core.util.TestBase;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nz.co.delacour.firefall.core.FirefallService.factory;
import static nz.co.delacour.firefall.core.FirefallService.fir;
import static org.junit.Assert.*;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 13/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

@Slf4j
public class QueryTest extends TestBase {

    @Data
    @Entity
    public static class QueryEntity extends HasId<QueryEntity> {

        private String someString;

        private int someInt;

        private Boolean someBoolean;

        public QueryEntity() {
            super(QueryEntity.class);
        }

    }

    @Test
    public void basicQuery() {
        factory().register(QueryEntity.class);

        QueryEntity queryEntity1 = new QueryEntity();
        queryEntity1.setSomeString("someString1");
        QueryEntity queryEntity2 = new QueryEntity();
        queryEntity2.setSomeString("someString2");

        var entities = fir().type(QueryEntity.class).save().entities(Lists.newArrayList(queryEntity1, queryEntity2)).now();
        assertNotNull(entities);

        var search = fir().type(QueryEntity.class).load().filter("someString", "someString1").list().now().items();

        assertNotNull(search);
        assertEquals(1, search.size());

        var item = search.get(0);
        assertNotNull(item);
        assertEquals(queryEntity1.getSomeString(), item.getSomeString());
    }

    @Test
    public void inQuery1() {
        factory().register(QueryEntity.class);

        QueryEntity queryEntity1 = new QueryEntity();
        queryEntity1.setSomeString("someString1");
        QueryEntity queryEntity2 = new QueryEntity();
        queryEntity2.setSomeString("someString2");

        var entities = fir().type(QueryEntity.class).save().entities(Lists.newArrayList(queryEntity1, queryEntity2)).now();
        assertNotNull(entities);

        var search = fir().type(QueryEntity.class).load().filter("someString in", Lists.newArrayList("someString1", "someString3")).list().now().items();

        assertNotNull(search);
        assertEquals(1, search.size());

        var item = search.get(0);
        assertNotNull(item);
        assertEquals(queryEntity1.getSomeString(), item.getSomeString());
    }

    @Test
    public void inQuery2() {
        factory().register(QueryEntity.class);

        QueryEntity queryEntity1 = new QueryEntity();
        queryEntity1.setSomeString("someString1");
        QueryEntity queryEntity2 = new QueryEntity();
        queryEntity2.setSomeString("someString2");
        QueryEntity queryEntity3 = new QueryEntity();
        queryEntity3.setSomeString("someString3");

        var entities = fir().type(QueryEntity.class).save().entities(Lists.newArrayList(queryEntity1, queryEntity2, queryEntity3)).now();
        assertNotNull(entities);

        var search = fir().type(QueryEntity.class).load().filter("someString in", Lists.newArrayList("someString1", "someString2")).list().now().items();

        assertNotNull(search);
        assertEquals(2, search.size());
    }

    @Test
    public void intQuery() {
        factory().register(QueryEntity.class);

        QueryEntity queryEntity1 = new QueryEntity();
        queryEntity1.setSomeInt(1);
        QueryEntity queryEntity2 = new QueryEntity();
        queryEntity2.setSomeInt(2);

        var entities = fir().type(QueryEntity.class).save().entities(Lists.newArrayList(queryEntity1, queryEntity2)).now();
        assertNotNull(entities);

        var search = fir().type(QueryEntity.class).load().filter("someInt", 1).list().now().items();

        assertNotNull(search);
        assertEquals(1, search.size());

        var item = search.get(0);
        assertNotNull(item);
        assertEquals(queryEntity1.getSomeInt(), item.getSomeInt());
    }

    @Test
    public void booleanQuery() {
        factory().register(QueryEntity.class);

        QueryEntity queryEntity1 = new QueryEntity();
        queryEntity1.setSomeBoolean(true);
        QueryEntity queryEntity2 = new QueryEntity();
        queryEntity2.setSomeBoolean(false);

        var entities = fir().type(QueryEntity.class).save().entities(Lists.newArrayList(queryEntity1, queryEntity2)).now();
        assertNotNull(entities);

        var search = fir().type(QueryEntity.class).load().filter("someBoolean", true).list().now().items();

        assertNotNull(search);
        assertEquals(1, search.size());

        var item = search.get(0);
        assertNotNull(item);
        assertEquals(queryEntity1.getSomeBoolean(), item.getSomeBoolean());
    }

    @Test
    public void greaterThanIntQuery() {
        factory().register(QueryEntity.class);

        QueryEntity queryEntity1 = new QueryEntity();
        queryEntity1.setSomeInt(1);
        QueryEntity queryEntity2 = new QueryEntity();
        queryEntity2.setSomeInt(2);
        QueryEntity queryEntity3 = new QueryEntity();
        queryEntity3.setSomeInt(3);

        var entities = fir().type(QueryEntity.class).save().entities(Lists.newArrayList(queryEntity1, queryEntity2, queryEntity3)).now();
        assertNotNull(entities);

        var search = fir().type(QueryEntity.class).load().filter("someInt >", 1).list().now().items();

        assertNotNull(search);
        assertEquals(2, search.size());

        var item1 = search.get(0);
        assertNotNull(item1);
        assertEquals(queryEntity2.getSomeInt(), item1.getSomeInt());

        var item2 = search.get(1);
        assertNotNull(item2);
        assertEquals(queryEntity3.getSomeInt(), item2.getSomeInt());
    }

    @Test
    public void greaterThanOrEqualIntQuery() {
        factory().register(QueryEntity.class);

        QueryEntity queryEntity1 = new QueryEntity();
        queryEntity1.setSomeInt(1);
        QueryEntity queryEntity2 = new QueryEntity();
        queryEntity2.setSomeInt(2);
        QueryEntity queryEntity3 = new QueryEntity();
        queryEntity3.setSomeInt(3);

        var entities = fir().type(QueryEntity.class).save().entities(Lists.newArrayList(queryEntity1, queryEntity2, queryEntity3)).now();
        assertNotNull(entities);

        var search = fir().type(QueryEntity.class).load().filter("someInt >=", 1).list().now().items();

        assertNotNull(search);
        assertEquals(3, search.size());

        var item1 = search.get(0);
        assertNotNull(item1);
        assertEquals(queryEntity1.getSomeInt(), item1.getSomeInt());

        var item2 = search.get(1);
        assertNotNull(item2);
        assertEquals(queryEntity2.getSomeInt(), item2.getSomeInt());

        var item3 = search.get(2);
        assertNotNull(item3);
        assertEquals(queryEntity3.getSomeInt(), item3.getSomeInt());
    }

    @Test
    public void lessThanIntQuery() {
        factory().register(QueryEntity.class);

        QueryEntity queryEntity1 = new QueryEntity();
        queryEntity1.setSomeInt(1);
        QueryEntity queryEntity2 = new QueryEntity();
        queryEntity2.setSomeInt(2);
        QueryEntity queryEntity3 = new QueryEntity();
        queryEntity3.setSomeInt(3);

        var entities = fir().type(QueryEntity.class).save().entities(Lists.newArrayList(queryEntity1, queryEntity2, queryEntity3)).now();
        assertNotNull(entities);
        assertEquals(3, entities.size());

        var search = fir().type(QueryEntity.class).load().filter("someInt <", 3).list().now().items();

        assertNotNull(search);
        assertEquals(2, search.size());

        var item1 = search.get(0);
        assertNotNull(item1);
        assertEquals(queryEntity1.getSomeInt(), item1.getSomeInt());

        var item2 = search.get(1);
        assertNotNull(item2);
        assertEquals(queryEntity2.getSomeInt(), item2.getSomeInt());
    }


    @Test
    public void lessThanOrEqualIntQuery() {
        factory().register(QueryEntity.class);

        QueryEntity queryEntity1 = new QueryEntity();
        queryEntity1.setSomeInt(1);
        QueryEntity queryEntity2 = new QueryEntity();
        queryEntity2.setSomeInt(2);
        QueryEntity queryEntity3 = new QueryEntity();
        queryEntity3.setSomeInt(3);

        var entities = fir().type(QueryEntity.class).save().entities(Lists.newArrayList(queryEntity1, queryEntity2, queryEntity3)).now();
        assertNotNull(entities);

        var search = fir().type(QueryEntity.class).load().filter("someInt <=", 3).list().now().items();

        assertNotNull(search);
        assertEquals(3, search.size());

        var item1 = search.get(0);
        assertNotNull(item1);
        assertEquals(queryEntity1.getSomeInt(), item1.getSomeInt());

        var item2 = search.get(1);
        assertNotNull(item2);
        assertEquals(queryEntity2.getSomeInt(), item2.getSomeInt());

        var item3 = search.get(2);
        assertNotNull(item3);
        assertEquals(queryEntity3.getSomeInt(), item3.getSomeInt());
    }

    @Test
    public void basicSubCollectionQuery() {
        factory().register(QueryEntity.class);
        factory().register(Basic.class);

        QueryEntity queryEntity1 = new QueryEntity();
        queryEntity1.setSomeString("someString1");

        var entity = fir().type(QueryEntity.class).save().entity(queryEntity1).now();
        assertNotNull(entity);
        assertNotNull(entity.getId());

        Basic basic1 = new Basic();
        basic1.setSomeString("someString2");

        Basic basic2 = new Basic();
        basic2.setSomeString("someString3");

        var entities = fir().type(Basic.class).parent(entity.ref()).save().entities(Lists.newArrayList(basic1, basic2)).now();
        assertNotNull(entities);
        assertEquals(2, entities.size());

        var search = fir().type(Basic.class).parent(entity.ref()).load().filter("someString", "someString2").list().now().items();
        assertNotNull(search);
        assertFalse(search.isEmpty());
        assertEquals( 1, search.size());

        assertNotNull(search.get(0).ref());
        assertNotNull(search.get(0).ref().getReference());

        fir().type(Basic.class).parent(entity.ref()).delete().id(search.get(0).getId()).now();

        var search2 = fir().type(Basic.class).parent(entity.ref()).load().filter("someString", "someString2").list().now().items();
        assertNotNull(search2);
        assertTrue(search2.isEmpty());

    }

}
