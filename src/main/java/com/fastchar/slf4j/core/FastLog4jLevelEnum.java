package com.fastchar.slf4j.core;

import java.util.Arrays;

/**
 * @author 沈建（Janesen）
 * @date 2021/12/25 15:03
 */
public enum FastLog4jLevelEnum {
    All,
    Trace,
    Debug,
    Info,
    Warn,
    Error,
    Fatal,
    OFF
    ;


    public static FastLog4jLevelEnum getNextLevel(FastLog4jLevelEnum currLevel) {
        return getNextLevel(currLevel.name());
    }

    public static FastLog4jLevelEnum getNextLevel(String currLevel) {
        FastLog4jLevelEnum[] values = FastLog4jLevelEnum.values();
        for (int i = 0; i < values.length; i++) {
            if (i == values.length - 1) {
                return null;
            }
            if (values[i].name().equalsIgnoreCase(currLevel)) {
                return values[i + 1];
            }
        }
        return null;
    }

    public static FastLog4jLevelEnum[] getNextLevels(String currLevel) {
        FastLog4jLevelEnum level = getLevel(currLevel);
        if (level == null) {
            return new FastLog4jLevelEnum[0];
        }
        FastLog4jLevelEnum[] values = FastLog4jLevelEnum.values();
        return Arrays.asList(values).subList(level.ordinal(), values.length).toArray(new FastLog4jLevelEnum[]{});
    }


    public static FastLog4jLevelEnum getLevel(String level) {
        FastLog4jLevelEnum[] values = FastLog4jLevelEnum.values();
        for (FastLog4jLevelEnum value : values) {
            if (value.name().equalsIgnoreCase(level)) {
                return value;
            }
        }
        return null;
    }
}
