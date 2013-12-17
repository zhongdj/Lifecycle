package net.madz.bcel.intercept;

public interface UnlockableStack {

    Unlockable popUnlockable();

    void pushUnlockable(Unlockable unlockable);

    boolean isEmpty();
}
