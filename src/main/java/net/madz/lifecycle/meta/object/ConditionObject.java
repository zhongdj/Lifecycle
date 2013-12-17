package net.madz.lifecycle.meta.object;

import java.lang.reflect.Method;

import net.madz.lifecycle.meta.MetaObject;
import net.madz.lifecycle.meta.MultiKeyed;
import net.madz.lifecycle.meta.type.ConditionMetadata;

public interface ConditionObject extends MetaObject<ConditionObject, ConditionMetadata>, MultiKeyed {

    Method conditionGetter();
}
