package com.fastchar.slf4j.core;

import java.util.LinkedHashMap;
import java.util.Map;

public class FastLog4jLogger {


    private String name;

    private boolean additivity;
    private final Map<FastLog4jLevelEnum, FastLog4jRollingFile> rollingFiles = new LinkedHashMap<>();


    public String getName() {
        return name;
    }

    /**
     * 需要分割的名匹配，一般是类的完整名，例如：com.fastchar.core.FastActionLogger
     *
     * @param name 字符串
     * @return 当前对象
     */
    public FastLog4jLogger setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 添加日志级别分割文件
     *
     * @param rollingFiles 配置信息
     * @return 当前对象
     */
    public FastLog4jLogger addRollingFile(FastLog4jRollingFile... rollingFiles) {
        for (FastLog4jRollingFile rollingFile : rollingFiles) {
            this.rollingFiles.put(rollingFile.getLevel(), rollingFile);
        }
        return this;
    }


    public Map<FastLog4jLevelEnum, FastLog4jRollingFile> getRollingFiles() {
        return rollingFiles;
    }

    public boolean isAdditivity() {
        return additivity;
    }

    public FastLog4jLogger setAdditivity(boolean additivity) {
        this.additivity = additivity;
        return this;
    }

    public FastLog4jLevelEnum getLevel() {
        FastLog4jLevelEnum minLevel = FastLog4jLevelEnum.OFF;
        for (Map.Entry<FastLog4jLevelEnum, FastLog4jRollingFile> entry : this.rollingFiles.entrySet()) {
            if (entry.getKey().ordinal() < minLevel.ordinal()) {
                minLevel = entry.getKey();
            }
        }
        return minLevel;
    }


}
