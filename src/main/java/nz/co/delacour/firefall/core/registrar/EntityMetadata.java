package nz.co.delacour.firefall.core.registrar;

import com.google.common.collect.Lists;
import lombok.Data;
import nz.co.delacour.firefall.core.FirefullFactory;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.annotations.OnLoad;
import nz.co.delacour.firefall.core.annotations.OnSave;

import java.lang.reflect.Method;
import java.util.List;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 14/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

@Data
public class EntityMetadata<T extends HasId> {

    private final Class<T> entityClass;

    private final List<LifecycleMethod> onSaveMethods = Lists.newArrayList();

    private final List<LifecycleMethod> onLoadMethods = Lists.newArrayList();

    public EntityMetadata(FirefullFactory fact, Class<T> entityClass) {
        this.entityClass = entityClass;

        for (final Method method : entityClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(OnSave.class)) {
                this.onSaveMethods.add(new LifecycleMethod(method));
            }

            if (method.isAnnotationPresent(OnLoad.class)) {
                this.onLoadMethods.add(new LifecycleMethod(method));
            }

        }
    }
}
