package net.madz.lifecycle.meta.object;

import java.lang.reflect.Method;

import net.madz.lifecycle.meta.MetaObject;
import net.madz.lifecycle.meta.MultiKeyed;
import net.madz.lifecycle.meta.type.TransitionMetadata;

public interface TransitionObject extends MetaObject<TransitionObject, TransitionMetadata>, MultiKeyed {

    Method getTransitionMethod();
}
