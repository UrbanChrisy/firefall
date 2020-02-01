package nz.co.delacour.firefall.core.annotations;

import nz.co.delacour.firefall.core.HasId;

import java.lang.annotation.*;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface SubCollectionEntity {

    String name() default "";

    Class<? extends HasId<?>> parent();

}

