package nz.co.delacour.firefall.core.util;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.common.base.Strings;
import lombok.Data;
import nz.co.delacour.firefall.core.HasId;


public class EntityMapper {

    public static <T extends HasId<T>> T map(Class<T> entityClass, DocumentSnapshot documentSnapshot) {
        if (documentSnapshot == null) {
            return null;
        }
        String id = documentSnapshot.getId();
        T t = documentSnapshot.toObject(entityClass);

        if (t == null) {
            return null;
        }

        t.setId(id);
        return t;
    }

    public static <T extends HasId<T>> MappedEntity<T, DocumentReference> map(T t, CollectionReference collection) {
        if (t == null) {
            return null;
        }

        DocumentReference reference;
        if (Strings.isNullOrEmpty(t.getId())) {
            reference = collection.document();
            t.setId(reference.getId());
        } else {
            reference = collection.document(t.getId());
        }

        return new MappedEntity<>(t, reference);
    }

    @Data
    public static class MappedEntity<T extends HasId<T>, U> {
        private T entity;
        private U type;

        MappedEntity(T entity, U type) {
            this.entity = entity;
            this.type = type;
        }
    }
}
