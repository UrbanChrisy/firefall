package nz.co.delacour.firefall.core.delete;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Precondition;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.Ref;
import nz.co.delacour.firefall.core.util.TypeUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 8/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class TypeDeleter<T extends HasId<T>> {

    private final Deleter deleter;

    private final Class<T> entityClass;

    private final CollectionReference collection;

    public TypeDeleter(Deleter deleter, Class<T> entityClass) {
       this(deleter, entityClass, null);
    }

    public TypeDeleter(Deleter deleter, Class<T> entityClass, Ref<?> parent) {
        this.deleter = deleter;
        this.entityClass = entityClass;
        this.collection = TypeUtils.getCollection(deleter.getFirefall().factory().getFirestore(), entityClass);
    }

    public TypeDeleter<T> parent(Ref<?> ref) {
        return new TypeDeleter<>(this.deleter, this.entityClass, ref);
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
        return new DeleteResult<>(this, reference, Precondition.NONE);
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
        return new DeleteResult<>(this,reference, options);
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
