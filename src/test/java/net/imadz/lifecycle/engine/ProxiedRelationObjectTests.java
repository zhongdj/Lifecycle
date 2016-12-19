package net.imadz.lifecycle.engine;

import net.imadz.lifecycle.LifecycleException;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Barry on 19/12/2016.
 */
public class ProxiedRelationObjectTests extends ProxiedRelationObjectTestMetadata {

    @BeforeClass
    public static void setup() throws Throwable {
        registerMetaFromClass(ProxiedRelationObjectTests.class);
    }


    @Test(expected = LifecycleException.class)
    public void should_fail_valid_while_check_when_proxied_relation_object_in_wrong_state() {
        //Given
        final TheRelationImpl theRelation = new TheRelationImpl();
        final TheRelationalObject relationalObject = new TheRelationalObject(theRelation);

        try {
            relationalObject.plan();
            throw new IllegalStateException("should throw exception here");
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, theRelation, relationalObject, TheRelationFSM.States.Completed.class);
        }

        theRelation.doComplete();
        {
            assertEquals(TheRelationFSM.States.Completed.class.getSimpleName(), theRelation.getState());
        }

        relationalObject.plan();
        {
            assertEquals(TheRelationalFSM.States.Planned.class.getSimpleName(), relationalObject.getState());
        }
    }

    @Test
    public void should_pass_valid_while_check_when_proxied_relation_object_in_right_state() {
        //Given
        final TheRelationImpl theRelation = new TheRelationImpl();
        final TheRelationalObject relationalObject = new TheRelationalObject(theRelation);

        theRelation.doComplete();
        {
            assertEquals(TheRelationFSM.States.Completed.class.getSimpleName(), theRelation.getState());
        }

        relationalObject.plan();
        {
            assertEquals(TheRelationalFSM.States.Planned.class.getSimpleName(), relationalObject.getState());
        }
    }
}
