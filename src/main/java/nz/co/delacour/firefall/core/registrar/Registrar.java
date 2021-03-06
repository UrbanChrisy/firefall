package nz.co.delacour.firefall.core.registrar;

import com.google.common.collect.Maps;
import nz.co.delacour.firefall.core.FirefallFactory;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.annotations.Entity;
import nz.co.delacour.firefall.core.util.TypeUtils;

import java.util.Map;


public class Registrar {

    private final FirefallFactory fact;

    protected Map<String, EntityMetadata<?>> byKind = Maps.newHashMap();

    public Registrar(FirefallFactory fact) {
        this.fact = fact;
    }

    public <T extends HasId<T>> void register(Class<T> clazz) {
        if (!TypeUtils.isDeclaredAnnotationPresent(clazz, Entity.class)) {
            throw new IllegalArgumentException(clazz + " must be annotated with @Entity");
        }

        String kind = TypeUtils.getKind(clazz);
        if (this.byKind.containsKey(kind)) {
            return;
        }

        EntityMetadata<T> entityMetadata = new EntityMetadata<>(this.fact, clazz);
        this.byKind.put(kind, entityMetadata);

    }

    @SuppressWarnings("unchecked")
    public <T extends HasId<T>> EntityMetadata<T> getMetadata(String kind) {
        return (EntityMetadata<T>) this.byKind.get(kind);
    }

    public <T extends HasId<T>> EntityMetadata<T> getMetadata(Class<T> clazz) {
        return getMetadata(TypeUtils.getKind(clazz));
    }
}
