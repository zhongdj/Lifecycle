package net.imadz.lifecycle.meta.builder.impl.helpers;

import java.lang.reflect.Method;

public class MethodWrapper {
	private int priority;
	private int inheritanceLevel;
	private int generalize;
	private Method method;

	public MethodWrapper(final Method method, final int inheritanceLevel) {
		this.method = method;
		this.inheritanceLevel = inheritanceLevel;
	}

	public int getPriority() {
		return priority;
	}

	public int getInheritanceLevel() {
		return inheritanceLevel;
	}

	public int getGeneralize() {
		return generalize;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setGeneralize(int generalize) {
		this.generalize = generalize;
	}

	public Method getMethod() {
		return method;
	}
}
