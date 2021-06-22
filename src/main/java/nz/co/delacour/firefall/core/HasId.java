package nz.co.delacour.firefall.core;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.Exclude;

import java.io.Serializable;


public abstract class HasId<T extends HasId<T>> implements Serializable {

    @Exclude
    private final Class<T> entityClass;

    private String id;

    public Timestamp createdAt = Timestamp.now();

    public Timestamp updatedAt = Timestamp.now();

    public HasId() {
        this.entityClass = null;
    }

    public HasId(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public Ref<T> ref() {
        return new Ref<>(this.entityClass, this.id);
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
