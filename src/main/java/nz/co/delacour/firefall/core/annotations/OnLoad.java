package nz.co.delacour.firefall.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 14/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OnLoad {
}
