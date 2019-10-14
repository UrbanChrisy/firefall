package nz.co.delacour.firefall.core.load;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldPath;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefullException;
import nz.co.delacour.firefall.core.util.TypeUtils;

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

        var collectionQuery = this.loader.getFirefull().factory().getFirestore().collection(this.kind);
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

    public Query<T> query(com.google.cloud.firestore.Query query) {
        this.query = query;
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
        return new LoadResult<>(this.query.limit(1), entityClass);
    }

    public LoadResults<T> list() {
        return new LoadResults<>(this.query, entityClass);
    }

    public Query<T> startAt(DocumentSnapshot documentSnapshot) {
        this.query = this.query.startAt(documentSnapshot);
        return this;
    }

    public Query<T> startAt(String cursor) {
        this.query = this.query.startAt(cursor);
        return this;
    }

    public Iterator<QueryDocumentSnapshot> iterator() {
        try {
            return this.query.get().get().iterator();
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }
    }

    public int count() {
        try {
            return this.query.get().get().size();
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }
    }

    public Query filter(FieldPath field, Object value) {
        return filter(field, "=", value);
    }

    public Query filter(FieldPath field, String operatorStr, Object value) {
        FilterOperator operator = this.translateFilterOperator(operatorStr);

        switch (operator) {
            case LESS_THAN:
                this.query = this.query.whereLessThan(field, value);
                break;
            case LESS_THAN_OR_EQUAL:
                this.query = this.query.whereLessThanOrEqualTo(field, value);
                break;
            case GREATER_THAN:
                this.query = this.query.whereGreaterThan(field, value);
                break;
            case GREATER_THAN_OR_EQUAL:
                this.query = this.query.whereGreaterThanOrEqualTo(field, value);
                break;
            case EQUAL:
                this.query = this.query.whereEqualTo(field, value);
                break;
            case ARRAY_CONTAINS:
                this.query = this.query.whereArrayContains(field, value);
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

            switch (operator) {
                case LESS_THAN:
                    this.query = this.query.whereLessThan(field, value);
                    break;
                case LESS_THAN_OR_EQUAL:
                    this.query = this.query.whereLessThanOrEqualTo(field, value);
                    break;
                case GREATER_THAN:
                    this.query = this.query.whereGreaterThan(field, value);
                    break;
                case GREATER_THAN_OR_EQUAL:
                    this.query = this.query.whereGreaterThanOrEqualTo(field, value);
                    break;
                case EQUAL:
                    this.query = this.query.whereEqualTo(field, value);
                    break;
                case ARRAY_CONTAINS:
                    this.query = this.query.whereArrayContains(field, value);
                    break;
            }
        } else {
            throw new IllegalArgumentException("'" + condition + "' is not a legal filter condition");
        }

        return this;
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

}