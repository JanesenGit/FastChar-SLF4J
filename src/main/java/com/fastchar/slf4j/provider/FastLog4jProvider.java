package com.fastchar.slf4j.provider;

import com.fastchar.annotation.AFastObserver;
import com.fastchar.core.FastChar;
import com.fastchar.interfaces.IFastLogger;
import com.fastchar.slf4j.FastLog4j2Config;
import com.fastchar.utils.FastStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 沈建（Janesen）
 * @date 2021/12/25 14:53
 */
@AFastObserver
public class FastLog4jProvider implements IFastLogger {

    private boolean logMsg(String method, Class<?> targetClass, String message, Throwable throwable) {
        FastLog4j2Config config = FastChar.getConfig(FastLog4j2Config.class);
        if (!config.isInitLog4jConfig()) {
            return false;
        }

        String loggerName = "FastCharLogger";
        if (targetClass != null) {
            loggerName = targetClass.getName();
        }

        if (FastStringUtils.isEmpty(message)) {
            message = "Exception";
        }

        Logger logger = LoggerFactory.getLogger(loggerName);
        if (method.equalsIgnoreCase("debug")) {
            logger.debug(message, throwable);
        } else if (method.equalsIgnoreCase("info")) {
            logger.info(message, throwable);
        } else if (method.equalsIgnoreCase("warn")) {
            logger.warn(message, throwable);
        } else if (method.equalsIgnoreCase("error")) {
            logger.error(message, throwable);
        }else{
            return false;
        }
        return true;
    }

    @Override
    public boolean debug(Class<?> targetClass, String message, Throwable throwable) {
        return logMsg("debug", targetClass, message, throwable);
    }

    @Override
    public boolean info(Class<?> targetClass, String message, Throwable throwable) {
        return logMsg("info", targetClass, message, throwable);
    }

    @Override
    public boolean error(Class<?> targetClass, String message, Throwable throwable) {
        return logMsg("error", targetClass, message, throwable);
    }

    @Override
    public boolean warn(Class<?> targetClass, String message, Throwable throwable) {
        return logMsg("warn", targetClass, message, throwable);
    }
}
