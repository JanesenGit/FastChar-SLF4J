package com.fastchar.slf4j;

import com.fastchar.core.FastActionLogger;
import com.fastchar.core.FastChar;
import com.fastchar.database.FastDB;
import com.fastchar.interfaces.IFastConfig;
import com.fastchar.slf4j.core.*;
import com.fastchar.utils.FastStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 沈建（Janesen）
 * @date 2021/12/25 14:58
 */
public class FastLog4j2Config implements IFastConfig {


    private boolean enable = true;
    private String logDirectory = "${sys:catalina.home}/logs";

    private boolean initLog4jConfig = false;
    private final Map<String, FastLog4jLogger> loggers = new LinkedHashMap<>();

    public String getLogDirectory() {
        return logDirectory;
    }

    public FastLog4j2Config setLogDirectory(String logDirectory) {
        this.logDirectory = logDirectory;
        return this;
    }


    public boolean isEnable() {
        return enable;
    }

    public FastLog4j2Config setEnable(boolean enable) {
        this.enable = enable;
        return this;
    }

    public boolean isInitLog4jConfig() {
        return initLog4jConfig;
    }

    /**
     * 开启FastChar相关的所有日志文件
     *
     * @return 当前对象
     */
    public FastLog4j2Config addLoggerByFastChar(boolean printOnConsole) {
        String fileName = "core";
        return addLogger(new FastLog4jLogger()
                .setName("com.fastchar")
                .addRollingFile(
                        new FastLog4jRollingFile(FastLog4jLevelEnum.Debug, printOnConsole, fileName),
                        new FastLog4jRollingFile(FastLog4jLevelEnum.Info, printOnConsole, fileName),
                        new FastLog4jRollingFile(FastLog4jLevelEnum.Warn, printOnConsole, fileName),
                        new FastLog4jRollingFile(FastLog4jLevelEnum.Error, printOnConsole, fileName)));
    }


    /**
     * 开启FastAction请求日志文件的分割，生成的日志文件名为：fastchar-action-debug.log
     *
     * @return 当前对象
     */
    public FastLog4j2Config addLoggerByFastAction(boolean printOnConsole) {
        return addLogger(new FastLog4jLogger().setName(FastActionLogger.class.getName())
                .addRollingFile(new FastLog4jRollingFile()
                        .setFileName("action")
                        .setConsole(printOnConsole)
                        .setLevel(FastLog4jLevelEnum.Debug)));
    }

    /**
     * 开启FastDB日志文件的分割，生成的日志文件名为：fastchar-sql-debug.log
     *
     * @return 当前对象
     */
    public FastLog4j2Config addLoggerByFastDB(boolean printOnConsole) {
        return addLogger(new FastLog4jLogger().setName(FastDB.class.getName())
                .addRollingFile(new FastLog4jRollingFile(FastLog4jLevelEnum.Debug, printOnConsole)
                        .setFileName("sql")));
    }

    /**
     * 添加默认的日志配置
     *
     * @param levels 需要显示的日志级别
     * @return 当前对象
     */
    public FastLog4j2Config addLogger(FastLog4jLevelEnum... levels) {
        return this.addLogger(true, levels);
    }

    /**
     * 添加默认的日志配置
     *
     * @param console 是否需要控制台打印
     * @param levels  需要显示的日志级别
     * @return 当前对象
     */
    public FastLog4j2Config addLogger(boolean console, FastLog4jLevelEnum... levels) {
        FastLog4jLogger logger = new FastLog4jLogger()
                .setName("root");
        for (FastLog4jLevelEnum level : levels) {
            logger.addRollingFile(new FastLog4jRollingFile(level, console));
        }

        return this.addLogger(logger);
    }




    /**
     * 添加日志配置
     *
     * @param loggers 日志的配置信息
     * @return 当前对象
     */
    public FastLog4j2Config addLogger(FastLog4jLogger... loggers) {
        for (FastLog4jLogger logger : loggers) {
            if (FastStringUtils.isEmpty(logger.getName())) {
                logger.setName("root");
            }
            this.loggers.put(logger.getName(), logger);
        }
        return this;
    }


    public Map<String, FastLog4jLogger> getLoggers() {
        return loggers;
    }


    /**
     * 初始化log4j的配置
     */
    synchronized void initLog4jConfig() {
        if (initLog4jConfig) {
            return;
        }
        try {
            if (isEnable()) {
                String configXml = new FastLog4j2XmlBuilder(this).createConfigXml();

                ConfigurationSource configurationSource = new ConfigurationSource(new ByteArrayInputStream(configXml.getBytes(StandardCharsets.UTF_8)));
                LoggerContext context = (LoggerContext) LogManager.getContext(false);
                Configuration configuration = ConfigurationFactory.getInstance().getConfiguration(context, configurationSource);
                context.setConfiguration(configuration);

                if (FastChar.getConstant().isDebug()) {
                    FastChar.getLogger().info(this.getClass(), "已启用SLF4j日志插件！");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            initLog4jConfig = true;
        }
    }

}
