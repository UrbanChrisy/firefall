package nz.co.delacour.firefull.core.load;

import com.google.cloud.firestore.*;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import nz.co.delacour.firefull.core.HasId;
import nz.co.delacour.firefull.core.exceptions.FirefullException;
import nz.co.delacour.firefull.core.util.TypeUtils;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

@Slf4j
public class Query<T extends HasId> {

    private final Loader loader;

    private final Class<T> entityClass;

    private final String kind;

    private CollectionReference collection;

    private com.google.cloud.firestore.Query query;

    public Query(Loader loader, Class<T> entityClass) {
        this.loader = loader;
        this.entityClass = entityClass;
        this.kind = TypeUtils.getKind(entityClass);

        var collectionQuery = this.loader.getFirefull().getFirefullFactory().getFirestore().collection(this.kind);
        this.collection = collectionQuery;
        this.query = collectionQuery;
    }

    public Query(Loader loader, Class<T> entityClass, CollectionReference collectionQuery) {
        this.loader = loader;
        this.entityClass = entityClass;
        this.kind = collectionQuery.getPath();
        this.collection = collectionQuery;
        this.query = collectionQuery;
    }

    public com.google.cloud.firestore.Query query() {
        return this.query;
    }

    public LoadResult<T> id(String id) {
        return new LoadResult<>(collection.document(id), entityClass);
    }

    public LoadResult<T> document(DocumentReference reference) {
        return new LoadResult<>(reference, entityClass);
    }

    public LoadResult<T> document() {
        return new LoadResult<>(this.collection.document(), entityClass);
    }

    public Query filter(FieldPath field, Object value) {
        return filter(field, "=", value);
    }

    public Query filter(FieldPath field, String operatorStr, Object value) {
        FilterOperator operator = this.translateFilterOperator(operatorStr);

        switch (operator) {
            case LESS_THAN:
                query = query.whereLessThan(field, value);
                break;
            case LESS_THAN_OR_EQUAL:
                query = query.whereLessThanOrEqualTo(field, value);
                break;
            case GREATER_THAN:
                query = query.whereGreaterThan(field, value);
                break;
            case GREATER_THAN_OR_EQUAL:
                query = query.whereGreaterThanOrEqualTo(field, value);
                break;
            case EQUAL:
                query = query.whereEqualTo(field, value);
                break;
            case ARRAY_CONTAINS:
                query = query.whereArrayContains(field, value);
                break;
        }

        return this;
    }

    public Query<T> filter(String condition, Object value) {
        String[] parts = condition.trim().split(" ");
        if (parts.length >= 1 && parts.length <= 2) {
            String field = parts[0].trim();

            if (Strings.isNullOrEmpty(field)) {
                return this;
            }

            FilterOperator operator = parts.length == 2 ? this.translateFilterOperator(parts[1]) : FilterOperator.EQUAL;

//            if (value.getClass().getSuperclass().isAssignableFrom(HasId.class)) {
//                field = String.format("%s.%s", field, "reference");
//            }

            switch (operator) {
                case LESS_THAN:
                    query = query.whereLessThan(field, value);
                    break;
                case LESS_THAN_OR_EQUAL:
                    query = query.whereLessThanOrEqualTo(field, value);
                    break;
                case GREATER_THAN:
                    query = query.whereGreaterThan(field, value);
                    break;
                case GREATER_THAN_OR_EQUAL:
                    query = query.whereGreaterThanOrEqualTo(field, value);
                    break;
                case EQUAL:
                    query = query.whereEqualTo(field, value);
                    break;
                case ARRAY_CONTAINS:
                    query = query.whereArrayContains(field, value);
                    break;
            }
        } else {
            throw new IllegalArgumentException("'" + condition + "' is not a legal filter condition");
        }

        return this;
    }

    public Query<T> order(String order) {
        this.query = this.query.orderBy(order);
        return this;
    }

    public Query<T> order(FieldPath order) {
        this.query = this.query.orderBy(order);
        return this;
    }

    public Query<T> limit(int limit) {
        this.query = this.query.limit(limit);
        return this;
    }

    public LoadResult<T> first() {
        return new LoadResult<T>(this.query.limit(1), entityClass);
    }

    public LoadResults<T> list() {
        return new LoadResults<>(this.query, entityClass);
    }

    private FilterOperator translateFilterOperator(String operator) {
        operator = operator.trim();
        switch (operator) {
            case "=":
            case "==":
                return FilterOperator.EQUAL;
            case ">":
                return FilterOperator.GREATER_THAN;
            case ">=":
                return FilterOperator.GREATER_THAN_OR_EQUAL;
            case "<":
                return FilterOperator.LESS_THAN;
            case "<=":
                return FilterOperator.LESS_THAN_OR_EQUAL;
            case "in":
                return FilterOperator.ARRAY_CONTAINS;
            default:
                throw new IllegalArgumentException("'" + operator + "' is not a legal filter operator");
        }
    }

    public Query<T> whereLessThan(String field, Object value) {
        query = query.whereLessThan(field, value);
        return this;
    }

    public Query<T> whereLessThan(FieldPath field, Object value) {
        query = query.whereLessThan(field, value);
        return this;
    }

    public Query<T> whereLessThanOrEqualTo(String field, Object value) {
        query = query.whereLessThanOrEqualTo(field, value);
        return this;
    }

    public Query<T> whereLessThanOrEqualTo(FieldPath field, Object value) {
        query = query.whereLessThanOrEqualTo(field, value);
        return this;
    }

    public Query<T> whereGreaterThan(String field, Object value) {
        query = query.whereGreaterThan(field, value);
        return this;
    }

    public Query<T> whereGreaterThan(FieldPath field, Object value) {
        query = query.whereGreaterThan(field, value);
        return this;
    }

    public Query<T> whereGreaterThanOrEqualTo(String field, Object value) {
        query = query.whereGreaterThanOrEqualTo(field, value);
        return this;
    }

    public Query<T> whereGreaterThanOrEqualTo(FieldPath field, Object value) {
        query = query.whereGreaterThanOrEqualTo(field, value);
        return this;
    }

    public Query<T> whereEqualTo(String field, Object value) {
        query = query.whereEqualTo(field, value);
        return this;
    }

    public Query<T> whereEqualTo(FieldPath field, Object value) {
        query = query.whereEqualTo(field, value);
        return this;
    }

    public Query<T> whereArrayContains(String field, Object value) {
        query = query.whereArrayContains(field, value);
        return this;
    }

    public Query<T> whereArrayContains(FieldPath field, Object value) {
        query = query.whereArrayContains(field, value);
        return this;
    }

    public Query<T> startAt(DocumentSnapshot documentSnapshot) {
        query = query.startAt(documentSnapshot);
        return this;
    }

    public Query<T> startAt(String cursor) {
        query = query.startAt(cursor);
        return this;
    }

    public Iterator<QueryDocumentSnapshot> iterator() {
        try {
            return query.get().get().iterator();
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }
    }

    public int count() {
        try {
            return query.get().get().size();
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }
    }
}