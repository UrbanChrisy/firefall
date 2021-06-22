package nz.co.delacour.firefall.core.load;

import com.google.cloud.firestore.*;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import nz.co.delacour.firefall.core.EntityType;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.exceptions.FirefallException;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;


@Slf4j
public class Query<T extends HasId<T>> {

    final EntityType<T> entityType;
    final Class<T> entityClass;
    com.google.cloud.firestore.Query query;
    Function<T, T> afterLoad;

    @Nullable
    final CollectionReference collection;
    Transaction transaction;

    public Query(EntityType<T> entityType,
                 Class<T> entityClass,
                 com.google.cloud.firestore.Query query,
                 @Nullable CollectionReference collection,
                 Transaction transaction) {
        this.entityType = entityType;
        this.entityClass = entityClass;
        this.query = query;
        this.collection = collection;
        this.transaction = transaction;
    }

    public Query(EntityType<T> entityType,
                 Class<T> entityClass,
                 com.google.cloud.firestore.Query query,
                 Transaction transaction) {
        this.entityType = entityType;
        this.entityClass = entityClass;
        this.query = query;
        this.collection = null;
        this.transaction = transaction;
    }

    public EntityType<T> getEntityType() {
        return entityType;
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

    public Query<T> limit(Integer limit) {
        if (limit == null) {
            return this;
        }

        this.query = this.query.limit(limit);
        return this;
    }

    public Query<T> offset(Integer offset) {
        if (offset == null) {
            return this;
        }

        this.query = this.query.offset(offset);
        return this;
    }

    public LoadResult<T> first() {
        return new LoadResult<>(this.query.limit(1), entityClass, transaction);
    }

    public LoadResults<T> list() {
        return new LoadResults<>(this, entityClass, transaction);
    }

    public Query<T> startAt(DocumentSnapshot documentSnapshot) {
        this.query = this.query.startAt(documentSnapshot);
        return this;
    }

    public Query<T> startAt(String cursor) {

        if (this.collection == null || Strings.isNullOrEmpty(cursor)) {
            return this;
        }

        try {
            var document = this.collection.document(cursor);
            return startAt(document.get().get());
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefallException(e);
        }
    }

    public Query<T> startAfter(DocumentSnapshot documentSnapshot) {
        this.query = this.query.startAfter(documentSnapshot);
        return this;
    }

    public Query<T> startAfter(String cursor) {

        if (this.collection == null || Strings.isNullOrEmpty(cursor)) {
            return this;
        }

        try {
            var document = this.collection.document(cursor);
            return startAfter(document.get().get());
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefallException(e);
        }
    }

    public Query<T> endAt(DocumentSnapshot documentSnapshot) {
        this.query = this.query.endAt(documentSnapshot);
        return this;
    }

    public Query<T> endAt(String cursor) {

        if (this.collection == null || Strings.isNullOrEmpty(cursor)) {
            return this;
        }

        try {
            var document = this.collection.document(cursor);
            return endAt(document.get().get());
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefallException(e);
        }
    }

    public Query<T> endBefore(DocumentSnapshot documentSnapshot) {
        this.query = this.query.endBefore(documentSnapshot);
        return this;
    }

    public Query<T> endBefore(String cursor) {

        if (this.collection == null || Strings.isNullOrEmpty(cursor)) {
            return this;
        }

        try {
            var document = this.collection.document(cursor);
            return endBefore(document.get().get());
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefallException(e);
        }
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
                case IN:
                    List<?> inList = (List<?>) value;
                    this.query = this.query.whereIn(field, inList);
                    break;
                case ARRAY_CONTAINS_ANY:
                    List<?> anyList = (List<?>) value;
                    this.query = this.query.whereArrayContainsAny(field, anyList);
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
                return FilterOperator.IN;
            case "contains any":
                return FilterOperator.ARRAY_CONTAINS_ANY;
            case "contains":
                return FilterOperator.ARRAY_CONTAINS;
            default:
                throw new IllegalArgumentException("'" + operator + "' is not a legal filter operator");
        }
    }

    public Query<T> afterLoad(Function<T, T> afterLoad) {
        this.afterLoad = afterLoad;
        return this;
    }

    public Query<T> transaction(Transaction transaction) {
        this.transaction = transaction;
        return this;
    }
}