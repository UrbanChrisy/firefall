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
public @interface Entity {

    String name() default "";

    Class<? extends HasId<?>> parent() default DEFAULT.class;


    final class DEFAULT extends HasId<DEFAULT> {
        public DEFAULT() {
            super(DEFAULT.class);
        }
    }

}

