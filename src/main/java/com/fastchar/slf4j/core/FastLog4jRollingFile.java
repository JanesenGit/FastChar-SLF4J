package com.fastchar.slf4j.core;

import com.fastchar.utils.FastStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 沈建（Janesen）
 * @date 2021/12/25 16:57
 */
public class FastLog4jRollingFile {

    public FastLog4jRollingFile() {
    }

    public FastLog4jRollingFile(FastLog4jLevelEnum level, boolean console) {
        this.level = level;
        this.console = console;
    }

    public FastLog4jRollingFile(FastLog4jLevelEnum level, boolean console, String fileName) {
        this.level = level;
        this.console = console;
        this.fileName = fileName;
    }

    private String fileName;
    private int fileMaxSize = 10;
    private FastLog4jLevelEnum level = FastLog4jLevelEnum.Debug;
    private String pattern = "%n[%d{yyyy-MM-dd HH:mm:ss}] %c%n[%level] %msg%n";
    private boolean additivity;

    private volatile FastLog4jConsole consoleConfig;
    private boolean console;
    private String consolePattern = "%n[%d{yyyy-MM-dd HH:mm:ss}] %c%n[%highlight{%level}] %highlight{%msg}%n";

    public String getFileName() {
        return fileName;
    }

    /**
     * 日志保存的文件名，注意：不包含文件后缀名
     * @param fileName 字符串
     * @return 当前对象
     */
    public FastLog4jRollingFile setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public int getFileMaxSize() {
        return fileMaxSize;
    }

    /**
     * 日志最大保存大小，当超过配置的大小值后，将进行分割生成压缩文件
     * @param fileMaxSize 文件大小，单位MB ，默认：10
     * @return 当前对象
     */
    public FastLog4jRollingFile setFileMaxSize(int fileMaxSize) {
        this.fileMaxSize = fileMaxSize;
        return this;
    }

    public FastLog4jLevelEnum getLevel() {
        return level;
    }

    /**
     * 设置日志需要显示的级别
     * @param level 级别 默认：debug
     * @return 当前对象
     */
    public FastLog4jRollingFile setLevel(FastLog4jLevelEnum level) {
        this.level = level;
        return this;
    }

    public String getPattern() {
        return pattern;
    }

    /**
     * 设置日志打印的格式 <a href="https://logging.apache.org/log4j/2.x/manual/layouts.html#PatternLayout">参考文档</a>
     * @param pattern 格式 默认：[%d{yyyy-MM-dd HH:mm:ss}] %c%n[%level] %msg%n%n
     * @return 当前对象
     */
    public FastLog4jRollingFile setPattern(String pattern) {
        if (FastStringUtils.isNotEmpty(pattern)) {
            this.pattern = pattern;
        }
        return this;
    }

    public boolean isAdditivity() {
        return additivity;
    }

    public FastLog4jRollingFile setAdditivity(boolean additivity) {
        this.additivity = additivity;
        return this;
    }

    public boolean isConsole() {
        return console;
    }

    public FastLog4jRollingFile setConsole(boolean console) {
        this.console = console;
        return this;
    }

    public String getConsolePattern() {
        return consolePattern;
    }

    public FastLog4jRollingFile setConsolePattern(String consolePattern) {
        this.consolePattern = consolePattern;
        return this;
    }

    public FastLog4jConsole getConsole() {
        if (consoleConfig == null) {
            synchronized (this) {
                if (consoleConfig == null) {
                    consoleConfig = new FastLog4jConsole().setLevel(this.level).setPattern(this.consolePattern);
                }
            }
        }
        return consoleConfig;
    }


    public String getRollingFileName() {
        List<String> items = new ArrayList<>();
        items.add("RollingFile");
        if (FastStringUtils.isNotEmpty(this.fileName)) {
            items.add(this.fileName.toUpperCase());
        }
        items.add(this.level.name().toUpperCase());
        return FastStringUtils.join(items, "-");
    }

    public String getLogFileName() {
        List<String> items = new ArrayList<>();
        items.add("fastchar");
        if (FastStringUtils.isNotEmpty(this.fileName)) {
            items.add(this.fileName.toLowerCase());
        }
        items.add(this.level.name().toLowerCase());
        return FastStringUtils.join(items, "-");
    }

}
