package com.fastchar.slf4j.core;

import com.fastchar.utils.FastStringUtils;

public class FastLog4jConsole {

    private final String name;

    private FastLog4jLevelEnum level = FastLog4jLevelEnum.All;

    //参考文档：https://logging.apache.org/log4j/2.x/manual/layouts.html#PatternLayout
    private String pattern = "%n[%d{yyyy-MM-dd HH:mm:ss}] %c%n[%highlight{%level}] %highlight{%msg}%n";

    public FastLog4jConsole() {
        this.name = FastStringUtils.buildOnlyCode("Console");
    }

    public FastLog4jLevelEnum getLevel() {
        return level;
    }

    /**
     * 设置日志需要显示的级别
     * @param level 级别 默认：debug
     * @return 当前对象
     */
    public FastLog4jConsole setLevel(FastLog4jLevelEnum level) {
        this.level = level;
        return this;
    }

    public String getPattern() {
        return pattern;
    }


    /**
     * 设置日志打印的格式 <a href="https://logging.apache.org/log4j/2.x/manual/layouts.html#PatternLayout">参考文档</a>
     * @param pattern 格式 默认：[%d{yyyy-MM-dd HH:mm:ss}] %c%n[%highlight{%level}] %highlight{%msg}%n%n
     * @return 当前对象
     */
    public FastLog4jConsole setPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public String getName() {
        return name;
    }

}
