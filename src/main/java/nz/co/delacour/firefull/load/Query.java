package nz.co.delacour.firefull.load;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.FieldPath;
import lombok.var;
import nz.co.delacour.firefull.HasId;
import nz.co.delacour.firefull.Ref;
import nz.co.delacour.firefull.exceptions.FirefullException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class Query<T extends HasId> {

    private final Loader loader;

    private final Class<T> entityClass;

    private final String kind;

    private CollectionReference collection;

    private com.google.cloud.firestore.Query query;

    public Query(Loader loader, Class<T> entityClass) {
        this.loader = loader;
        this.entityClass = entityClass;
        this.kind = Ref.getKind(entityClass);
        this.collection = loader.getFirefull().getFirefullFactory().getFirestore().collection(this.kind);
        this.query = loader.getFirefull().getFirefullFactory().getFirestore().collection(this.kind);
    }

    public LoadResult<T> id(String id) {
        try {
            return new LoadResult<>(collection.document(id).get().get(), entityClass);
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
            FilterOperator operator = parts.length == 2 ? this.translateFilterOperator(parts[1]) : FilterOperator.EQUAL;

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
        this.collection = (CollectionReference) this.collection.orderBy(order);
        return this;
    }

    public Query<T> order(FieldPath order) {
        this.collection = (CollectionReference) this.collection.orderBy(order);
        return this;
    }

    public LoadResult<T> first() {
        try {
            var documents = this.collection.limit(1).get().get().getDocuments();
            if (documents.size() == 0) {
                return new LoadResult<T>(null, entityClass);
            }
            return new LoadResult<T>(documents.get(0), entityClass);
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }
    }

    public List<LoadResult<T>> resultList() {
        try {
            return this.collection.get().get().getDocuments().stream().map((snapshot) -> new LoadResult<>(snapshot, entityClass)).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            throw new FirefullException(e);
        }
    }

    public List<T> list() {
        return resultList().stream().map(LoadResult::now).collect(Collectors.toList());
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
