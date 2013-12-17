package net.madz.lifecycle;

public interface LifecycleLockStrategry {

    void lockRead(Object reactiveObject);

    void unlockRead(Object targetReactiveObject);

    void lockWrite(Object reactiveObject);

    void unlockWrite(Object targetReactiveObject);
}
