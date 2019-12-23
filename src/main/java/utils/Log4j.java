package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Author: leeping
 * @Date: 2019/8/16 17:01
 */
public class Log4j {
    public interface PrintCallback{
        void callback(Object message);
    }
    private static PrintCallback callback;

    public static void setCallback(PrintCallback callback) {
        Log4j.callback = callback;
    }

    private final static Logger logger = LogManager.getLogger();

    public static void debug(Object obj){
        logger.debug(obj);
        if (callback!=null) callback.callback(obj);
    }
    public static void info(Object obj){
        logger.info(obj);
        if (callback!=null) callback.callback(obj);
    }
    public static void error(Object obj){
        logger.error(obj);
        if (callback!=null) callback.callback(obj);
    }
    public static void error(String message, Throwable t){
        logger.error(message,t);
        if (callback!=null) callback.callback(t);
    }
    public static void trace(Object obj){
        logger.trace(obj);
        if (callback!=null) callback.callback(obj);
    }
    public static void warn(Object obj){
        logger.warn(obj);
        if (callback!=null) callback.callback(obj);
    }
    public static void fatal(Object obj){
        logger.fatal(obj);
        if (callback!=null) callback.callback(obj);
    }
}
