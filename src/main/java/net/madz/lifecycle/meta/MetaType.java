package net.madz.lifecycle.meta;

import net.madz.common.Dumpable;
import net.madz.meta.MetaData;

public interface MetaType<T> extends Inheritable<T>, MetaData, Dumpable, MultiKeyed {}
