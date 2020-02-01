package nz.co.delacour.firefall.core.load;

import com.google.cloud.firestore.*;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefallException;
import nz.co.delacour.firefall.core.util.TypeUtils;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

@Slf4j
public class Query<T extends HasId<T>> {

     final Loader loader;

     final Class<T> entityClass;

     CollectionReference collection;

     com.google.cloud.firestore.Query query;

    public Query(Loader loader, Class<T> entityClass, DocumentReference parent) {
        this.loader = loader;
        this.entityClass = entityClass;
        this.collection = TypeUtils.getCollection(loader.getFirefall().factory().getFirestore(), entityClass, parent);
        this.query = this.collection;
    }

    public Query(Loader loader, Class<T> entityClass, CollectionReference collectionQuery) {
        this.loader = loader;
        this.entityClass = entityClass;
        this.collection = collectionQuery;
        this.query = collectionQuery;
    }

    public CollectionReference getCollection() {
        return collection;
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

    public Query<T> order(String order, com.google.cloud.firestore.Query.Direction direction) {
        this.query = this.query.orderBy(order, direction);
        return this;
    }

    public Query<T> order(FieldPath order, com.google.cloud.firestore.Query.Direction direction) {
        this.query = this.query.orderBy(order, direction);
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
            throw new FirefallException(e);
        }
    }

    public int count() {
        try {
            return this.query.get().get().size();
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefallException(e);
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