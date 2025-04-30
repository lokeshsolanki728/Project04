package com.rays.pro4.Util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Read the property values from application properties file using Resource
 * Bundle.
 *
 * @author Lokesh SOlanki
 */
public class PropertyReader {

    /**
     * Resource Bundle for system properties.
     */
    private static ResourceBundle rb = ResourceBundle.getBundle("com.rays.pro4.bundle.System");

    /**
     * Gets the value of a key from the property file.
     *
     * @param key The key whose value is to be retrieved.
     * @return The value associated with the key, or the key itself if not found.
     */
    public static String getValue(String key) {

        String val = null;

        try {
            val = rb.getString(key);
        } catch (MissingResourceException e) {
            val = key;
        }

        return val;

    }

    /**
     * Gets the value of a key from the property file and replaces the placeholder {0} with the provided parameter.
     *
     * @param key   The key whose value is to be retrieved.
     * @param param The parameter to replace the placeholder {0}.
     * @return The formatted value.
     */
    public static String getValue(String key, String param) {
        String msg = getValue(key);
        msg = msg.replace("{0}", param);
        return msg;
    }


    /**
     * Gets the value of a key from the property file and replaces the placeholders {0}, {1}, ... with the provided parameters.
     *
     * @param key    The key whose value is to be retrieved.
     * @param params An array of parameters to replace the placeholders {0}, {1}, ...
     * @return The formatted value.
     */
    public static String getValue(String key, String[] params) {
        String msg = getValue(key);
        for (int i = 0; i < params.length; i++) {
            msg = msg.replace("{" + i + "}", params[i]);
        }
        return msg;
    }

    /**
     * Test method
	 *
	 *
	 *
	 * @param key
	 * @param params
	 * @return
	 */
    public static void main(String[] args) {
        String[] params = {"Roll No"};
        System.out.println(PropertyReader.getValue("error.require", params));
    }

}
