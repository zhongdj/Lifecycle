package net.madz.lifecycle.annotations.relation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import net.madz.utils.Null;

@Target({ ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Relation {

    Class<?> value() default Null.class;

    public static class Utils {

        public static boolean isRelationMethod(Method method) {
            return method.getName().startsWith("get") && method.getTypeParameters().length <= 0 && null != method.getAnnotation(Relation.class);
        }
    }
}
