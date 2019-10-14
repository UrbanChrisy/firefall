package nz.co.delacour.firefall.core.util;

import com.google.common.base.Strings;
import nz.co.delacour.firefall.core.annotations.Entity;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class TypeUtils {
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = new HashMap<>();

    static {
        PRIMITIVE_TO_WRAPPER.put(Boolean.TYPE, Boolean.class);
        PRIMITIVE_TO_WRAPPER.put(Byte.TYPE, Byte.class);
        PRIMITIVE_TO_WRAPPER.put(Short.TYPE, Short.class);
        PRIMITIVE_TO_WRAPPER.put(Integer.TYPE, Integer.class);
        PRIMITIVE_TO_WRAPPER.put(Long.TYPE, Long.class);
        PRIMITIVE_TO_WRAPPER.put(Float.TYPE, Float.class);
        PRIMITIVE_TO_WRAPPER.put(Double.TYPE, Double.class);
        PRIMITIVE_TO_WRAPPER.put(Character.TYPE, Character.class);
    }

    private TypeUtils() {
    }

    public static <T> Constructor<T> getNoArgConstructor(Class<T> clazz) {
        try {
            Constructor<T> ctor = clazz.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor;
        } catch (NoSuchMethodException var2) {
            if (!clazz.isMemberClass() && !clazz.isAnonymousClass() && !clazz.isLocalClass()) {
                throw new IllegalStateException(clazz.getName() + " must have a no-arg constructor", var2);
            } else {
                throw new IllegalStateException(clazz.getName() + " must be static and must have a no-arg constructor", var2);
            }
        }
    }

    public static MethodHandle getConstructor(Class<?> clazz, Class<?>... args) {
        try {
            Constructor<?> ctor = clazz.getDeclaredConstructor(args);
            ctor.setAccessible(true);
            return MethodHandles.lookup().unreflectConstructor(ctor);
        } catch (NoSuchMethodException var3) {
            throw new IllegalStateException(clazz.getName() + " has no constructor with args " + Arrays.toString(args), var3);
        } catch (IllegalAccessException var4) {
            throw new IllegalStateException("Problem getting constructor for " + clazz.getName() + " with args " + Arrays.toString(args), var4);
        }
    }

    public static <T> T invoke(MethodHandle methodHandle, Object... params) {
        try {
            return (T) methodHandle.invokeWithArguments(params);
        } catch (RuntimeException var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new RuntimeException(var4);
        }
    }

    public static <T> T newInstance(Constructor<T> ctor, Object... params) {
        try {
            return ctor.newInstance(params);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static Object field_get(Field field, Object obj) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException | IllegalArgumentException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static boolean isAssignableFrom(Class<?> to, Class<?> from) {
        Class<?> notPrimitiveTo = to.isPrimitive() ? (Class) PRIMITIVE_TO_WRAPPER.get(to) : to;
        Class<?> notPrimitiveFrom = from.isPrimitive() ? (Class) PRIMITIVE_TO_WRAPPER.get(from) : from;
        return notPrimitiveTo.isAssignableFrom(notPrimitiveFrom);
    }

    public static <A extends Annotation> A getAnnotation(Annotation[] annotations, Class<A> annotationType) {
        for (Annotation anno : annotations) {
            if (annotationType.isAssignableFrom(anno.getClass())) {
                return (A) anno;
            }
        }
        return null;
    }

    public static <A extends Annotation> A getDeclaredAnnotation(Class<?> onClass, Class<A> annotationType) {
        return getAnnotation(onClass.getDeclaredAnnotations(), annotationType);
    }

    public static <A extends Annotation> boolean isDeclaredAnnotationPresent(Class<?> onClass, Class<A> annotationType) {
        return getDeclaredAnnotation(onClass, annotationType) != null;
    }

    public static String getKind(Class<?> clazz) {
        String kind = getKindRecursive(clazz);
        return kind == null ? clazz.getSimpleName() : kind;
    }

    public static String getKindRecursive(Class<?> clazz) {
        if (clazz == Object.class) {
            return null;
        } else {
            String kind = getKindHere(clazz);
            return !Strings.isNullOrEmpty(kind) ? kind : getKindRecursive(clazz.getSuperclass());
        }
    }

    public static String getKindHere(Class<?> clazz) {
        Entity ourAnn = TypeUtils.getDeclaredAnnotation(clazz, Entity.class);
        if (ourAnn != null) {
            return ourAnn.name().length() != 0 ? ourAnn.name() : clazz.getSimpleName();
        } else {
            return null;
        }
    }
}
