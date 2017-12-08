/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2013-2020 Madz. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License"). You
 * may not use this file except in compliance with the License. You can
 * obtain a copy of the License at
 * https://raw.github.com/zhongdj/Lifecycle/master/License.txt
 * . See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license." If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above. However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package net.imadz.lifecycle;

import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.meta.type.LifecycleMetaRegistry;
import net.imadz.verification.VerificationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class StaticWeaver {

  private final static Logger logger = Logger.getLogger("Lifecycle Framework");
  private ClassLoader classLoader;
  private LifecycleMetaRegistry registry = AbsStateMachineRegistry.getInstance();

  public static void main(String[] args) throws ClassNotFoundException, SecurityException, FileNotFoundException, IOException, VerificationException {
    if (1 != args.length) {
      throw new IllegalArgumentException("Usage: net.imadz.lifecycle.StaticWeaver ${classesFolder}");
    }
    LogManager.getLogManager().readConfiguration(StaticWeaver.class.getResourceAsStream("lifecycle_logging.properties"));
    final String targetClassesFolder = args[0];
    final File folder = new File(targetClassesFolder);
    final StaticWeaver weaver = new StaticWeaver();
    final URL url = folder.toURI().toURL();
    final URL[] urls = new URL[] {url};
    weaver.classLoader = new URLClassLoader(urls, StaticWeaver.class.getClassLoader());
    weaver.processFolder("", folder);
  }

  private void processFolder(String packagePrefix, File folder) throws ClassNotFoundException, VerificationException {
    for (final File file : folder.listFiles()) {
      if (file.isDirectory()) {
        processFolder(packagePrefix + file.getName() + ".", file);
      } else if (file.getName().endsWith("class")) {
        weaveClass(packagePrefix + file.getName().substring(0, file.getName().length() - 6), file);
      }
    }
  }

  private void weaveClass(String className, File file) throws ClassNotFoundException, VerificationException {
    logger.info("loading: " + className);
    Class<?> class1 = classLoader.loadClass(className);
    if (null != class1.getAnnotation(LifecycleMeta.class)) {
      registry.loadStateMachineObject(class1);
    }
  }
}
