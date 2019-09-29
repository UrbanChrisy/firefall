package nz.co.delacour.firefull;

import nz.co.delacour.firefull.annotations.Entity;
import com.google.common.base.Strings;
import nz.co.delacour.firefull.util.TypeUtils;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 29/09/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class Ref {

    public static String getKind(Class<?> clazz) {
        String kind = getKindRecursive(clazz);
        return kind == null ? clazz.getSimpleName() : kind;
    }

    private static String getKindRecursive(Class<?> clazz) {
        if (clazz == Object.class) {
            return null;
        } else {
            String kind = getKindHere(clazz);
            return !Strings.isNullOrEmpty(kind) ? kind : getKindRecursive(clazz.getSuperclass());
        }
    }

    private static String getKindHere(Class<?> clazz) {
        Entity ourAnn = (Entity) TypeUtils.getDeclaredAnnotation(clazz, Entity.class);
        if (ourAnn != null) {
            return ourAnn.name().length() != 0 ? ourAnn.name() : clazz.getSimpleName();
        } else {
            return null;
        }
    }
}
