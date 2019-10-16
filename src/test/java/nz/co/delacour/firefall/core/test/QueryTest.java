package nz.co.delacour.firefall.core.test;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.annotations.Entity;
import nz.co.delacour.firefall.core.util.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nz.co.delacour.firefall.core.FirefullService.factory;
import static nz.co.delacour.firefall.core.FirefullService.fir;
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

        var entities = fir().save().type(QueryEntity.class).entities(Lists.newArrayList(queryEntity1, queryEntity2)).now();
        assertNotNull(entities);

        var search = fir().load().type(QueryEntity.class).filter("someString", "someString1").list().now();

        assertNotNull(search);
        assertEquals(1, search.size());

        var item = search.get(0);
        assertNotNull(item);
        assertEquals(queryEntity1.getSomeString(), item.getSomeString());
    }

    @Test
    public void intQuery() {
        factory().register(QueryEntity.class);

        QueryEntity queryEntity1 = new QueryEntity();
        queryEntity1.setSomeInt(1);
        QueryEntity queryEntity2 = new QueryEntity();
        queryEntity2.setSomeInt(2);

        var entities = fir().save().type(QueryEntity.class).entities(Lists.newArrayList(queryEntity1, queryEntity2)).now();
        assertNotNull(entities);

        var search = fir().load().type(QueryEntity.class).filter("someInt", 1).list().now();

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

        var entities = fir().save().type(QueryEntity.class).entities(Lists.newArrayList(queryEntity1, queryEntity2)).now();
        assertNotNull(entities);

        var search = fir().load().type(QueryEntity.class).filter("someBoolean", true).list().now();

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

        var entities = fir().save().type(QueryEntity.class).entities(Lists.newArrayList(queryEntity1, queryEntity2, queryEntity3)).now();
        assertNotNull(entities);

        var search = fir().load().type(QueryEntity.class).filter("someInt >", 1).list().now();

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

        var entities = fir().save().type(QueryEntity.class).entities(Lists.newArrayList(queryEntity1, queryEntity2, queryEntity3)).now();
        assertNotNull(entities);

        var search = fir().load().type(QueryEntity.class).filter("someInt >=", 1).list().now();

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

        var entities = fir().save().type(QueryEntity.class).entities(Lists.newArrayList(queryEntity1, queryEntity2, queryEntity3)).now();
        assertNotNull(entities);

        var search = fir().load().type(QueryEntity.class).filter("someInt <", 3).list().now();

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

        var entities = fir().save().type(QueryEntity.class).entities(Lists.newArrayList(queryEntity1, queryEntity2, queryEntity3)).now();
        assertNotNull(entities);

        var search = fir().load().type(QueryEntity.class).filter("someInt <=", 3).list().now();

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


}
