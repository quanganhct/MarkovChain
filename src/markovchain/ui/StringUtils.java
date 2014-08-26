package markovchain.ui;

/**
 *
 * @author Admin
 */
public class StringUtils {

    public static final String NEW_LINE = "\n";//System.getProperty("line.separator");

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.equals("");
    }

    public static boolean IsNullOrWhiteSpace(String value) {
        return isNullOrEmpty(value) || value.trim().length() == 0;
    }

    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return endsWith(str, suffix, true);
    }

    public static boolean endsWith(String str, String suffix, boolean ignoreCase) {
        if (str == null || suffix == null) {
            return (str == null && suffix == null);
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        int strOffset = str.length() - suffix.length();
        return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
    }
}
