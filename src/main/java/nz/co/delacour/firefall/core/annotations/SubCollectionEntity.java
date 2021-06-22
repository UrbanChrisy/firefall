package nz.co.delacour.firefall.core.annotations;

import nz.co.delacour.firefall.core.HasId;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface SubCollectionEntity {

    String name() default "";

    Class<? extends HasId<?>> parent();

}

