package net.madz.lifecycle.meta.builder.impl.helpers;

import java.lang.reflect.Method;
import java.util.ArrayList;

import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.meta.type.TransitionMetadata;
import net.madz.util.MethodScanCallback;
import net.madz.util.StringUtil;
import net.madz.utils.Null;

public final class TransitionMethodScanner implements MethodScanCallback {

    private final ArrayList<Method> transitionMethodList = new ArrayList<Method>();
    private final TransitionMetadata transition;

    public TransitionMethodScanner(final TransitionMetadata transition) {
        this.transition = transition;
    }

    @Override
    public boolean onMethodFound(Method method) {
        final Transition transitionAnno = method.getAnnotation(Transition.class);
        if ( null == transitionAnno ) {
            return false;
        }
        final Class<?> transitionKey = transitionAnno.value();
        if ( matchedTransitionPrimaryKey(transitionKey, transition.getPrimaryKey()) ) {
            transitionMethodList.add(method);
        } else if ( matchedTransitionName(transitionKey, method.getName(), transition.getDottedPath().getName()) ) {
            transitionMethodList.add(method);
        }
        return false;
    }

    private boolean matchedTransitionName(final Class<?> transitionKey, String methodName, final String transitionName) {
        return isDefaultStyle(transitionKey) && StringUtil.toUppercaseFirstCharacter(methodName).equals(transitionName);
    }

    private boolean matchedTransitionPrimaryKey(final Class<?> transitionKey, Object primaryKey) {
        return !isDefaultStyle(transitionKey) && transitionKey.equals(primaryKey);
    }

    public Method[] getTransitionMethods() {
        return transitionMethodList.toArray(new Method[0]);
    }

    private boolean isDefaultStyle(final Class<?> transitionKey) {
        return Null.class == transitionKey;
    }
}