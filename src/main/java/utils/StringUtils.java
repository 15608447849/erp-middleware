package utils;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class StringUtils {
    private static final String INTEGER_REGEX = "0|-?([1-9]{1}[0-9]*)";
    private final static String EMAIL_REGEXP =
            "^[\\.a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    //字符串不为空
    public static boolean isEmpty(String str){
        return str == null || str.trim().length() == 0 ;
    }

    //判断一组字符串都不为空
    public static boolean isEmpty(String... arr){
        for (String str : arr){
            if (isEmpty(str)) return true;
        }
        return false;
    }

    public static int getStrLength(String str) {
        return str == null ? -1 : str.length();
    }

    public static String trim(String text) {
        if(text == null || "".equals(text)) {
            return text;
        }
        return text.trim();
    }

    /*
     * 判定字符串是否为整数。
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        return !isEmpty(str) && Pattern.matches(INTEGER_REGEX, str);
    }

    public static boolean isBiggerZero(String str) {
        return isInteger(str) && Long.parseLong(str) > 0;
    }

    public static boolean isDateFormatter(String str) {
        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(str);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static boolean isDateTimeFormatter(String str) {
        try {
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static boolean isEmail(String str) {
        return !isEmpty(str) && Pattern.matches(EMAIL_REGEXP, str);
    }

    /** 获取指定字符在字符串中的个数 */
    public static int targetStrCount(String str, String tag) {
        int index = 0;
        int count = 0;
        while ((index = str.indexOf(tag)) != -1 ) {
            str = str.substring(index + tag.length());
            count++;
        }
        return count;
    }

    //错误输出
    public static String printExceptInfo(Throwable ex){
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        return writer.toString();
    }

    public static boolean checkStrLength(String str, int maxLen) {
        if (maxLen < 0 || str == null) {
            return false;
        }
        return str.length() <= maxLen;
    }

}
