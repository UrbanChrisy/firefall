package nz.co.delacour.firefall.core.delete;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import lombok.var;
import nz.co.delacour.firefall.core.util.TypeUtils;
import nz.co.delacour.firefall.core.HasId;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 8/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class TypeDeleter<T extends HasId> {

    private final Deleter deleter;

    private final Class<T> entityClass;

    private final String kind;

    private CollectionReference collection;

    public TypeDeleter(Deleter deleter, Class<T> entityClass) {
        this.deleter = deleter;
        this.entityClass = entityClass;
        this.kind = TypeUtils.getKind(entityClass);
        this.collection = deleter.getFirefull().factory().getFirestore().collection(this.kind);
    }

    public DeleteResult id(String id) {
        var reference = this.collection.document(id);
        return this.document(reference);
    }

    public DeleteResult entity(T entity) {
        var reference = this.collection.document(entity.getId());
        return this.document(reference);
    }

    public DeleteResult document(DocumentReference reference) {
        return new DeleteResult(reference);
    }

    public DeleteResults ids(List<String> entities) {
        var references = entities.stream().map((id) -> this.collection.document(id)).collect(Collectors.toList());
        return this.documents(references);
    }

    public DeleteResults entities(List<T> entities) {
        var references = entities.stream().map(HasId::getId).filter(Objects::nonNull).map(this.collection::document).collect(Collectors.toList());
        return this.documents(references);
    }

    public DeleteResults documents(List<DocumentReference> references) {
        return new DeleteResults(references, this.collection);
    }
}
