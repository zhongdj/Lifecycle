package net.madz.lifecycle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.meta.type.LifecycleMetaRegistry;
import net.madz.verification.VerificationException;

public class StaticWeaver {
	private final static Logger logger = Logger
			.getLogger("Lifecycle Framework");
	private ClassLoader classLoader;
	private LifecycleMetaRegistry registry = AbsStateMachineRegistry
			.getInstance();

	public static void main(String[] args) throws ClassNotFoundException,
			SecurityException, FileNotFoundException, IOException,
			VerificationException {

		if (1 != args.length) {
			throw new IllegalArgumentException(
					"Usage: net.madz.lifecycle.StaticWeaver ${classesFolder}");
		}
		LogManager.getLogManager().readConfiguration(
				StaticWeaver.class
						.getResourceAsStream("lifecycle_logging.properties"));
		final String targetClassesFolder = args[0];
		final File folder = new File(targetClassesFolder);
		final StaticWeaver weaver = new StaticWeaver();
		final String uriPrefix;
		if (!folder.getAbsolutePath().startsWith("/")) {
			uriPrefix = "file:/";
		} else {
			uriPrefix = "file:";
		}
		URL url = URI.create(uriPrefix + folder.getAbsolutePath() + "/")
				.toURL();
		final URL[] urls = new URL[] { url };
		weaver.classLoader = new URLClassLoader(urls,
				StaticWeaver.class.getClassLoader());

		weaver.processFolder("", folder);

	}

	private void processFolder(String packagePrefix, File folder)
			throws ClassNotFoundException, VerificationException {

		for (final File file : folder.listFiles()) {
			if (file.isDirectory()) {
				processFolder(packagePrefix + file.getName() + ".", file);
			} else if (file.getName().endsWith("class")) {
				weaveClass(
						packagePrefix
								+ file.getName().substring(0,
										file.getName().length() - 6), file);
			}
		}
	}

	private void weaveClass(String className, File file)
			throws ClassNotFoundException, VerificationException {
		logger.info("loading: " + className);
		Class<?> class1 = classLoader.loadClass(className);
		if (null != class1.getAnnotation(LifecycleMeta.class)) {
			registry.loadStateMachineObject(class1);
		}
	}
}
