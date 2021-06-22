package nz.co.delacour.firefall.core.registrar;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class LifecycleMethod {

    private final Method method;

    public LifecycleMethod(Method method) {
        this.method = method;
        this.method.setAccessible(true);

        if (method.getParameterTypes().length > 0) {
            throw new IllegalArgumentException("Lifecycle methods cannot have parameters:  " + method);
        }
    }

    public void execute(Object pojo) {
        try {
            this.method.invoke(pojo);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            } else {
                throw new RuntimeException(e.getCause());
            }
        }
    }
}
