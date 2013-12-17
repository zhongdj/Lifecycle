package net.madz.meta;

import net.madz.common.DottedPath;
import net.madz.verification.VerificationFailureSet;

public interface MetaData {

    DottedPath getDottedPath();

    MetaData getParent();

    // For Inheritance
    KeySet getKeySet();

    Object getPrimaryKey();

    // String getNamespace();
    void verifyMetaData(VerificationFailureSet verificationSet);
}
