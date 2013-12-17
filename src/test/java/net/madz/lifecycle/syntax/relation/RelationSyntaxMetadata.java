package net.madz.lifecycle.syntax.relation;

import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.annotations.CompositeState;
import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.relation.ErrorMessage;
import net.madz.lifecycle.annotations.relation.InboundWhile;
import net.madz.lifecycle.annotations.relation.Parent;
import net.madz.lifecycle.annotations.relation.RelateTo;
import net.madz.lifecycle.annotations.relation.RelationSet;
import net.madz.lifecycle.annotations.relation.ValidWhile;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.annotations.state.LifecycleOverride;
import net.madz.lifecycle.annotations.state.ShortCut;
import net.madz.lifecycle.syntax.BaseMetaDataTest;
import net.madz.lifecycle.syntax.relation.RelationSyntaxMetadata.POwningStateMachine.Transitions.OwningX;
import net.madz.lifecycle.syntax.relation.RelationSyntaxMetadata.POwningStateMachine.Transitions.OwningY;

public class RelationSyntaxMetadata extends BaseMetaDataTest {

    @StateMachine
    static interface InvalidRelationReferenceSM {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = InvalidRelationReferenceSM.Transitions.X.class, value = B.class)
            static interface A {}
            @End
            static interface B {}
        }
        @TransitionSet
        static interface Transitions {

            static interface X {}
        }
    }
    @StateMachine
    static interface RelatedSM {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = RelatedSM.Transitions.RX.class, value = RB.class)
            static interface RA {}
            @End
            static interface RB {}
        }
        @TransitionSet
        static interface Transitions {

            static interface RX {}
        }
    }
    @StateMachine
    static interface PStandalone {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = PStandalone.Transitions.PX.class, value = PB.class)
            @InboundWhile(on = { RelatedSM.States.RB.class }, relation = PStandalone.Relations.PR.class, otherwise = { @ErrorMessage(
                    bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
                    states = { RelatedSM.States.RA.class }) })
            @ValidWhile(on = { RelatedSM.States.RB.class }, relation = PStandalone.Relations.PR.class, otherwise = { @ErrorMessage(
                    bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
                    states = { RelatedSM.States.RA.class }) })
            static interface PA {}
            @End
            static interface PB {}
        }
        @TransitionSet
        static interface Transitions {

            static interface PX {}
        }
        @RelationSet
        static interface Relations {

            @RelateTo(RelatedSM.class)
            static interface PR {}
        }
    }
    @StateMachine
    static interface NStandalone {

        static String error = SyntaxErrors.RELATION_INBOUNDWHILE_RELATION_NOT_DEFINED_IN_RELATIONSET;

        @StateSet
        static interface States {

            @Initial
            @Function(transition = NStandalone.Transitions.NX.class, value = NB.class)
            @InboundWhile(on = { RelatedSM.States.RB.class }, relation = PStandalone.Relations.PR.class)
            @ValidWhile(on = { RelatedSM.States.RB.class }, relation = PStandalone.Relations.PR.class)
            static interface NA {}
            @End
            static interface NB {}
        }
        @TransitionSet
        static interface Transitions {

            static interface NX {}
        }
        @RelationSet
        static interface Relations {

            @RelateTo(RelatedSM.class)
            static interface NR {}
        }
    }
    @StateMachine
    static interface NStandalone2 {

        static String error = SyntaxErrors.RELATION_ON_ATTRIBUTE_OF_INBOUNDWHILE_NOT_MATCHING_RELATION;

        @StateSet
        static interface States {

            @Initial
            @Function(transition = NStandalone2.Transitions.NX.class, value = NStandalone2.States.NB.class)
            @InboundWhile(on = { InvalidRelationReferenceSM.States.B.class }, relation = NStandalone2.Relations.NR.class)
            static interface NA {}
            @End
            static interface NB {}
        }
        @TransitionSet
        static interface Transitions {

            static interface NX {}
        }
        @RelationSet
        static interface Relations {

            @RelateTo(RelatedSM.class)
            static interface NR {}
        }
    }
    @StateMachine
    static interface NStandalone3 {

        static String error = SyntaxErrors.RELATION_RELATED_TO_REFER_TO_NON_STATEMACHINE;

        @StateSet
        static interface States {

            @Initial
            @Function(transition = NStandalone3.Transitions.NX.class, value = NStandalone3.States.NB.class)
            @InboundWhile(on = { InvalidRelationReferenceSM.States.B.class }, relation = NStandalone2.Relations.NR.class)
            static interface NA {}
            @End
            static interface NB {}
        }
        @TransitionSet
        static interface Transitions {

            static interface NX {}
        }
        @RelationSet
        static interface Relations {

            @RelateTo(NStandalone3.Transitions.NX.class)
            static interface NR {}
        }
    }
    @StateMachine
    static interface NStandalone4 {

        static String error = SyntaxErrors.RELATIONSET_MULTIPLE;

        @StateSet
        static interface States {

            @Initial
            @Function(transition = NStandalone4.Transitions.NX.class, value = NStandalone4.States.NB.class)
            @InboundWhile(on = { InvalidRelationReferenceSM.States.B.class }, relation = NStandalone2.Relations.NR.class)
            static interface NA {}
            @End
            static interface NB {}
        }
        @TransitionSet
        static interface Transitions {

            static interface NX {}
        }
        @RelationSet
        static interface Relations {

            @RelateTo(NStandalone4.Transitions.NX.class)
            static interface NR {}
        }
        @RelationSet
        static interface Relations2 {

            @RelateTo(NStandalone4.Transitions.NX.class)
            static interface NR {}
        }
    }
    @StateMachine
    static interface Super {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = Super.Transitions.SX.class, value = SB.class)
            @InboundWhile(relation = Super.Relations.SR.class, on = { RelatedSM.States.RB.class }, otherwise = { @ErrorMessage(
                    bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
                    states = { RelatedSM.States.RA.class }) })
            @ValidWhile(relation = Super.Relations.SR.class, on = { RelatedSM.States.RB.class }, otherwise = { @ErrorMessage(
                    bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
                    states = { RelatedSM.States.RA.class }) })
            static interface SA {}
            @End
            static interface SB {}
        }
        @TransitionSet
        static interface Transitions {

            static interface SX {}
        }
        @RelationSet
        static interface Relations {

            @RelateTo(RelatedSM.class)
            static interface SR {}
        }
    }
    @StateMachine
    static interface PChild extends Super {

        @StateSet
        static interface States extends Super.States {

            @Function(transition = PChild.Transitions.PCX.class, value = CC.class)
            static interface CA extends Super.States.SA {}
            @Function(transition = PChild.Transitions.PCX.class, value = SB.class)
            @InboundWhile(relation = Super.Relations.SR.class, on = { RelatedSM.States.RB.class })
            static interface CC {}
        }
        @TransitionSet
        static interface Transitions extends Super.Transitions {

            static interface PCX {};
        }
    }
    @StateMachine
    static interface NChild extends Super {

        static String error = SyntaxErrors.RELATION_INBOUNDWHILE_RELATION_NOT_DEFINED_IN_RELATIONSET;

        @StateSet
        static interface States extends Super.States {

            @Function(transition = NChild.Transitions.NCX.class, value = NCC.class)
            static interface NCA extends Super.States.SA {}
            @Function(transition = NChild.Transitions.NCX.class, value = SB.class)
            @InboundWhile(relation = PStandalone.Relations.PR.class, on = { InvalidRelationReferenceSM.States.B.class })
            static interface NCC {}
        }
        @TransitionSet
        static interface Transitions extends Super.Transitions {

            static interface NCX {};
        }
    }
    @StateMachine
    static interface NChild2 extends Super {

        static String error = SyntaxErrors.RELATION_ON_ATTRIBUTE_OF_INBOUNDWHILE_NOT_MATCHING_RELATION;

        @StateSet
        static interface States extends Super.States {

            @Function(transition = NChild2.Transitions.NC2X.class, value = NC2C.class)
            static interface NCA extends Super.States.SA {}
            @Function(transition = NChild2.Transitions.NC2X.class, value = SB.class)
            @InboundWhile(relation = Super.Relations.SR.class, on = { InvalidRelationReferenceSM.States.B.class })
            static interface NC2C {}
        }
        @TransitionSet
        static interface Transitions extends Super.Transitions {

            static interface NC2X {};
        }
    }
    @StateMachine
    static interface NChild3 extends Super {

        static String error = SyntaxErrors.RELATION_VALIDWHILE_RELATION_NOT_DEFINED_IN_RELATIONSET;

        @StateSet
        static interface States extends Super.States {

            @Function(transition = NChild3.Transitions.NC3X.class, value = NC3C.class)
            static interface NC3A extends Super.States.SA {}
            @Function(transition = NChild3.Transitions.NC3X.class, value = SB.class)
            @ValidWhile(relation = PStandalone.Relations.PR.class, on = { InvalidRelationReferenceSM.States.B.class })
            static interface NC3C {}
        }
        @TransitionSet
        static interface Transitions extends Super.Transitions {

            static interface NC3X {};
        }
    }
    @StateMachine
    static interface NChild4 extends Super {

        static String error = SyntaxErrors.RELATION_ON_ATTRIBUTE_OF_VALIDWHILE_NOT_MACHING_RELATION;

        @StateSet
        static interface States extends Super.States {

            @Function(transition = NChild4.Transitions.NC4X.class, value = NC4C.class)
            static interface NC4A extends Super.States.SA {}
            @Function(transition = NChild4.Transitions.NC4X.class, value = SB.class)
            @ValidWhile(relation = Super.Relations.SR.class, on = { InvalidRelationReferenceSM.States.B.class })
            static interface NC4C {}
        }
        @TransitionSet
        static interface Transitions extends Super.Transitions {

            static interface NC4X {};
        }
    }
    @StateMachine
    static interface NStandalone5 {

        static String error = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID;

        @StateSet
        static interface States {

            @Initial
            @Function(transition = NStandalone5.Transitions.N5X.class, value = N5B.class)
            @InboundWhile(relation = NStandalone5.Relations.N5R.class, on = { RelatedSM.States.RB.class }, otherwise = { @ErrorMessage(
                    bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
                    states = { InvalidRelationReferenceSM.States.A.class }) })
            @ValidWhile(relation = NStandalone5.Relations.N5R.class, on = { RelatedSM.States.RB.class }, otherwise = { @ErrorMessage(
                    bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_VALIDWHILE_INVALID,
                    states = { InvalidRelationReferenceSM.States.A.class }) })
            static interface N5A {}
            @End
            static interface N5B {}
        }
        @TransitionSet
        static interface Transitions {

            static interface N5X {}
        }
        @RelationSet
        static interface Relations {

            @RelateTo(RelatedSM.class)
            static interface N5R {}
        }
    }
    @StateMachine
    static interface PStandaloneParent {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = PStandaloneParent.Transitions.PPX.class, value = PStandaloneParent.States.PPB.class)
            @InboundWhile(on = { RelatedSM.States.RB.class }, relation = PStandaloneParent.Relations.PPR.class, otherwise = { @ErrorMessage(
                    bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
                    states = { RelatedSM.States.RA.class }) })
            @ValidWhile(on = { RelatedSM.States.RB.class }, relation = PStandaloneParent.Relations.PPR.class, otherwise = { @ErrorMessage(
                    bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_VALIDWHILE_INVALID,
                    states = { RelatedSM.States.RA.class }) })
            static interface PPA {}
            @End
            static interface PPB {}
        }
        @TransitionSet
        static interface Transitions {

            static interface PPX {}
        }
        @RelationSet
        static interface Relations {

            @Parent
            @RelateTo(RelatedSM.class)
            static interface PPR {}
        }
    }
    @StateMachine
    static interface POwningStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = OwningX.class, value = OwningB.class)
            static interface OwningA {}
            @CompositeState
            @Function(transition = OwningY.class, value = OwningC.class)
            static interface OwningB {

                @StateSet
                static interface CStates {

                    @Initial
                    @Function(transition = OwningB.CTransitions.CompositeX.class, value = OwningB.CStates.CompositeB.class)
                    @InboundWhile(on = { RelatedSM.States.RB.class }, relation = OwningB.CRelations.PCS1R.class, otherwise = { @ErrorMessage(
                            bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
                            states = { RelatedSM.States.RA.class }) })
                    static interface CompositeA {}
                    @Function(transition = OwningB.CTransitions.CompositeX.class, value = OwningB.CStates.CompositeC.class)
                    @InboundWhile(on = { RelatedSM.States.RB.class }, relation = OwningB.CRelations.PCS1R.class, otherwise = { @ErrorMessage(
                            bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
                            states = { RelatedSM.States.RA.class }) })
                    static interface CompositeB {}
                    @End
                    @ShortCut(OwningC.class)
                    static interface CompositeC {}
                }
                @TransitionSet
                static interface CTransitions {

                    static interface CompositeX {}
                }
                @RelationSet
                static interface CRelations {

                    @Parent
                    @RelateTo(RelatedSM.class)
                    static interface PCS1R {}
                }
            }
            @End
            static interface OwningC {}
        }
        @TransitionSet
        static interface Transitions {

            static interface OwningX {}
            static interface OwningY {}
        }
    }
    @StateMachine
    static interface PParentRelationSuper {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = PParentRelationSuper.Transitions.PPX.class, value = PParentRelationSuper.States.PPB.class)
            @InboundWhile(on = { RelatedSM.States.RB.class }, relation = PParentRelationSuper.Relations.PPR.class, otherwise = { @ErrorMessage(
                    bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
                    states = { RelatedSM.States.RA.class }) })
            @ValidWhile(on = { RelatedSM.States.RB.class }, relation = PParentRelationSuper.Relations.PPR.class, otherwise = { @ErrorMessage(
                    bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
                    states = { RelatedSM.States.RA.class }) })
            static interface PPA {}
            @End
            static interface PPB {}
        }
        @TransitionSet
        static interface Transitions {

            static interface PPX {}
        }
        @RelationSet
        static interface Relations {

            @Parent
            @RelateTo(RelatedSM.class)
            static interface PPR {}
        }
    }
    @StateMachine
    static interface PParentRelationChild extends PParentRelationSuper {

        @RelationSet
        static interface Relations {

            @Parent
            @RelateTo(RelatedSM.class)
            @LifecycleOverride
            static interface PCR {}
        }
    }
    @StateMachine
    static interface NStandaloneParent {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = NStandaloneParent.Transitions.PPX.class, value = NStandaloneParent.States.PPB.class)
            @InboundWhile(on = { RelatedSM.States.RB.class }, relation = NStandaloneParent.Relations.PPR.class, otherwise = { @ErrorMessage(
                    bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
                    states = { RelatedSM.States.RA.class }) })
            @ValidWhile(on = { RelatedSM.States.RB.class }, relation = NStandaloneParent.Relations.PPR.class, otherwise = { @ErrorMessage(
                    bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
                    states = { RelatedSM.States.RA.class }) })
            static interface PPA {}
            @End
            static interface PPB {}
        }
        @TransitionSet
        static interface Transitions {

            static interface PPX {}
        }
        @RelationSet
        static interface Relations {

            @Parent
            @RelateTo(RelatedSM.class)
            static interface PPR {}
            @Parent
            @RelateTo(RelatedSM.class)
            static interface PPR2 {}
        }
    }
    @StateMachine
    static interface NParentRelationChild extends PParentRelationSuper {

        @RelationSet
        static interface Relations {

            @Parent
            @RelateTo(RelatedSM.class)
            // Forget to @Overrides
            static interface PCR {}
        }
    }
    @StateMachine
    static interface NOwningStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = NOwningStateMachine.Transitions.NOwningX.class, value = NOwningB.class)
            static interface NOwningA {}
            @CompositeState
            @Function(transition = NOwningStateMachine.Transitions.NOwningY.class, value = NOwningC.class)
            static interface NOwningB {

                @StateSet
                static interface NCStates {

                    @Initial
                    @Function(transition = NOwningB.CTransitions.NCompositeX.class, value = NOwningB.NCStates.NCompositeB.class)
                    @InboundWhile(on = { RelatedSM.States.RB.class }, relation = NOwningB.CRelations.NCR.class, otherwise = { @ErrorMessage(
                            bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
                            states = { RelatedSM.States.RA.class }) })
                    static interface NCompositeA {}
                    @Function(transition = NOwningB.CTransitions.NCompositeX.class, value = NOwningB.NCStates.NCompositeC.class)
                    @InboundWhile(on = { RelatedSM.States.RB.class }, relation = NOwningB.CRelations.NCR.class, otherwise = { @ErrorMessage(
                            bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
                            states = { RelatedSM.States.RA.class }) })
                    static interface NCompositeB {}
                    @End
                    @ShortCut(NOwningC.class)
                    static interface NCompositeC {}
                }
                @TransitionSet
                static interface CTransitions {

                    static interface NCompositeX {}
                }
                @RelationSet
                static interface CRelations {

                    @Parent
                    @LifecycleOverride
                    // It's illegal whether overrides or not
                    @RelateTo(RelatedSM.class)
                    static interface NCR {}
                }
            }
            @End
            static interface NOwningC {}
        }
        @TransitionSet
        static interface Transitions {

            static interface NOwningX {}
            static interface NOwningY {}
        }
        @RelationSet
        static interface Relations {

            @Parent
            @RelateTo(RelatedSM.class)
            static interface NR {}
        }
    }
    @StateMachine
    static interface N2OwningStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = N2OwningStateMachine.Transitions.N2OwningX.class, value = N2OwningB.class)
            static interface N2OwningA {}
            @CompositeState
            @Function(transition = N2OwningStateMachine.Transitions.N2OwningY.class, value = N2OwningC.class)
            static interface N2OwningB {

                @StateSet
                static interface N2CStates {

                    @Initial
                    @Function(transition = N2OwningB.CTransitions.N2CompositeX.class, value = N2OwningB.N2CStates.N2CompositeB.class)
                    @InboundWhile(on = { RelatedSM.States.RB.class }, relation = N2OwningB.CRelations.N2CR.class, otherwise = { @ErrorMessage(
                            bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
                            states = { RelatedSM.States.RA.class }) })
                    static interface N2CompositeA {}
                    @Function(transition = N2OwningB.CTransitions.N2CompositeX.class, value = N2OwningB.N2CStates.N2CompositeC.class)
                    @InboundWhile(on = { RelatedSM.States.RB.class }, relation = N2OwningB.CRelations.N2CR.class, otherwise = { @ErrorMessage(
                            bundle = SyntaxErrors.SYNTAX_ERROR_BUNDLE, code = SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
                            states = { RelatedSM.States.RA.class }) })
                    static interface N2CompositeB {}
                    @End
                    @ShortCut(N2OwningC.class)
                    static interface N2CompositeC {}
                }
                @TransitionSet
                static interface CTransitions {

                    static interface N2CompositeX {}
                }
                @RelationSet
                static interface CRelations {

                    @Parent
                    // @Overrides // It's illegal whether overrides or not
                    @RelateTo(RelatedSM.class)
                    static interface N2CR {}
                }
            }
            @End
            static interface N2OwningC {}
        }
        @TransitionSet
        static interface Transitions {

            static interface N2OwningX {}
            static interface N2OwningY {}
        }
        @RelationSet
        static interface Relations {

            @Parent
            @RelateTo(RelatedSM.class)
            static interface N2R {}
        }
    }
    @StateMachine
    static interface NoRelateTo {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = NoRelateTo.Transitions.Action.class, value = Finished.class)
            @ValidWhile(relation = NoRelateTo.Relations.Relative.class, on = RelatedSM.States.RA.class)
            static interface Created {}
            @End
            static interface Finished {}
        }
        @TransitionSet
        static interface Transitions {

            static interface Action {}
        }
        @RelationSet
        static interface Relations {

            static interface Relative {}
        }
    }
}
