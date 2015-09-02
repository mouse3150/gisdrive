package cn.com.esrichina.gisdrive.util;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
/**
 * 本地化字符串处理程序
 * @author Chenhao
 *
 */
public final class StringManager {
	private final int BUNDLE_NUM = 8;

    /**
     * The ResourceBundle for this StringManager.
     */

    private ResourceBundle bundle;

    private HashMap<Locale, ResourceBundle> bundles = new HashMap<Locale, ResourceBundle>(BUNDLE_NUM);
    
    private String bundleName = null;


    /**
     * @param packageName Name of package to create StringManager for.
     */

    private StringManager(String packageName) {
        this.bundleName = packageName + ".LocalStrings";
        bundle = ResourceBundle.getBundle(bundleName);
        bundles.put(Locale.getDefault(), bundle);
    }


    /**
     * Get a string from the underlying resource bundle.
     *
     * @param key
     */
    public String getString(String key) {
        return getString(key, Locale.getDefault());
    }
   
    public String getString(String key, Locale locale) {
        return MessageFormat.format(getStringInternal(key, locale),
                (Object[]) null);
    }

    protected String getStringInternal(String key) {
        return getStringInternal(key, Locale.getDefault());
    }

    protected String getStringInternal(String key, Locale locale) {
        if (key == null) {
        	return "";
        }

        String str = null;

        if (bundle == null) {
            return key;
        }

        ResourceBundle bundle = bundles.get(locale);
        if (bundle == null) {
            synchronized (bundles) {
                if (bundles.get(locale) == null) {
                    bundle = ResourceBundle.getBundle(this.bundleName, locale);
                    bundles.put(locale, bundle);
                }
            }
        }

        try {
            str = bundle.getString(key);
        } catch (MissingResourceException mre) {
        	str = key;
        }

        return str;
    }

    /**
     * Get a string from the underlying resource bundle and format it
     * with the given object arguments. This argument can of course be
     * a String object.
     *
     * @param key
     * @param args
     */
    public String getString(String key, Object... args) {
    	StringBuffer sb = new StringBuffer();
    	sb.append(getString(key,  Locale.getDefault(), args));
        return sb.toString();
    }

    public String getString(String key, Locale locale, Object... args) {
        String iString = null;
        String value = getStringInternal(key, locale);

        try {
            Object[] nonNullArgs = (Object[]) args;
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null) {
                    if (nonNullArgs == args) {
                    	nonNullArgs = (Object[]) args.clone();
                    }
                    nonNullArgs[i] = "null";
                }
            }

            iString = MessageFormat.format(value, nonNullArgs);
        } catch (IllegalArgumentException iae) {
            StringBuffer buf = new StringBuffer();
            buf.append(value);
            for (int i = 0; i < args.length; i++) {
                buf.append(" arg[" + i + "]=" + args[i]);
            }
            iString = buf.toString();
        }
        return iString;
    }

    /**
     * Returns the locale of the resource bundle for the given request locale.
     */
    public Locale getResourceBundleLocale(Locale requestLocale) {
        ResourceBundle bundle = bundles.get(requestLocale);
        if (bundle == null) {
            synchronized (bundles) {
                if (bundles.get(requestLocale) == null) {
                    bundle = ResourceBundle.getBundle(this.bundleName,
                                                      requestLocale);
                    bundles.put(requestLocale, bundle);
                }
            }
        }
        return bundle.getLocale();
    }

    private static Hashtable<String, StringManager> managers = new Hashtable<String, StringManager>();

    /**
     * Get the StringManager for a particular package. If a manager for
     * a package already exists, it will be reused, else a new
     * StringManager will be created and returned.
     *
     * @param packageName Name of package to create StringManager for.
     */
	public static synchronized StringManager getManager(String packageName) {
        StringManager mgr = managers.get(packageName);
        if (mgr == null) {
            mgr = new StringManager(packageName);
            managers.put(packageName, mgr);
        }
        return mgr;
    }
}
