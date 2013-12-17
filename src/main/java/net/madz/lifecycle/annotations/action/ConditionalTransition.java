package net.madz.lifecycle.annotations.action;

public interface ConditionalTransition<T> {

    Class<?> doConditionJudge(T t);
}
