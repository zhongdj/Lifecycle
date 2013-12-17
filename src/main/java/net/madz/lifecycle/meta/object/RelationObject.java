package net.madz.lifecycle.meta.object;

import net.madz.lifecycle.meta.MetaObject;
import net.madz.lifecycle.meta.MultiKeyed;
import net.madz.lifecycle.meta.type.RelationMetadata;
import net.madz.util.Readable;

public interface RelationObject extends MetaObject<RelationObject, RelationMetadata>, MultiKeyed {

    Readable<?> getEvaluator();

}
