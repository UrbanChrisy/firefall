package nz.co.delacour.firefall.core.delete;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Precondition;
import com.google.cloud.firestore.Transaction;
import lombok.Data;
import nz.co.delacour.firefall.core.EntityType;
import nz.co.delacour.firefall.core.HasId;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class Deleter<T extends HasId<T>> {

    private final EntityType<T> entityType;
    private final Class<T> entityClass;
    private final CollectionReference collection;
    @Nullable
    private final Transaction transaction;

    public Deleter(EntityType<T> entityType, Class<T> entityClass, CollectionReference collection, @Nullable Transaction transaction) {
        this.entityType = entityType;
        this.entityClass = entityClass;
        this.collection = collection;
        this.transaction = transaction;
    }

    public EntityType<T> getEntityType() {
        return entityType;
    }

    public DeleteResult<T> id(String id) {
        var reference = this.collection.document(id);
        return this.document(reference);
    }

    public DeleteResult<T> entity(T entity) {
        var reference = this.collection.document(entity.getId());
        return this.document(reference);
    }

    public DeleteResult<T> document(DocumentReference reference) {
        return new DeleteResult<>(reference, Precondition.NONE, transaction);
    }

    public DeleteResult<T> id(String id, Precondition options) {
        var reference = this.collection.document(id);
        return this.document(reference, options);
    }

    public DeleteResult<T> entity(T entity, Precondition options) {
        var reference = this.collection.document(entity.getId());
        return this.document(reference, options);
    }

    public DeleteResult<T> document(DocumentReference reference, Precondition options) {
        return new DeleteResult<>(reference, options, transaction);
    }

    public DeleteResults<T> entities(List<T> entities) {
        return this.entities(entities, Precondition.NONE);
    }

    public DeleteResults<T> entities(List<T> entities, Precondition options) {
        var ids = entities.stream().map(HasId::getId).filter(Objects::nonNull).collect(Collectors.toList());
        return this.ids(ids, options);
    }

    public DeleteResults<T> ids(List<String> ids) {
        return this.ids(ids, Precondition.NONE);
    }

    public DeleteResults<T> ids(List<String> ids, Precondition options) {
        var references = ids.stream().map(this.collection::document).collect(Collectors.toList());
        return this.documents(references, options);
    }

    public DeleteResults<T> documents(List<DocumentReference> references) {
        return this.documents(references, Precondition.NONE);
    }

    public DeleteResults<T> documents(List<DocumentReference> references, Precondition options) {
        return new DeleteResults<>(references, this.collection, options);
    }
}
