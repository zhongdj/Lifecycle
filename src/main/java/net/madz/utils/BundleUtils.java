package net.madz.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class BundleUtils {

    private BundleUtils() {}

    public static String getBundledMessage(Class<?> cls, Locale locale, String bundle, String errorCode, Object[] messageVars) {
        return ResourceBundle.getBundle(bundle, locale, cls.getClassLoader()).getString(errorCode);
    }

    public static String getBundledMessage(Class<?> cls, String bundle, String errorCode, Object... messageVars) {
        return MessageFormat.format(ResourceBundle.getBundle(bundle, Locale.CHINA, cls.getClassLoader()).getString(errorCode), messageVars);
    }

    public static String getBundledMessage(Class<?> cls, String bundle, String errorCode) {
        return getBundledMessage(cls, bundle, errorCode, new Object[0]);
    }
}
