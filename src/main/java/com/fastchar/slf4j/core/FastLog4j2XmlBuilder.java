package com.fastchar.slf4j.core;

import com.fastchar.slf4j.FastLog4j2Config;
import com.fastchar.utils.FastStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FastLog4j2XmlBuilder {

    private final FastLog4j2Config config;

    public FastLog4j2XmlBuilder(FastLog4j2Config config) {
        this.config = config;
    }


    public String createConfigXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<configuration status=\"OFF\">" +
                "    <Properties>" +
                "        <Property name=\"LOG_DIRECTORY\">" + config.getLogDirectory() + "</Property>" +
                "    </Properties>" +
                "    <appenders>" +
                buildAppenderXml() +
                "    </appenders>" +
                "    <loggers>" +
                buildLoggersXml() +
                "    </loggers>" +
                "</configuration>";
    }

    private String buildAppenderXml() {
        List<String> appenderList = new ArrayList<>();
        Map<String, FastLog4jLogger> loggerMap = config.getLoggers();

        for (Map.Entry<String, FastLog4jLogger> entry : loggerMap.entrySet()) {
            FastLog4jLogger log4jLogger = entry.getValue();
            Map<FastLog4jLevelEnum, FastLog4jRollingFile> rollingFiles = log4jLogger.getRollingFiles();
            for (Map.Entry<FastLog4jLevelEnum, FastLog4jRollingFile> fileEntry : rollingFiles.entrySet()) {

                String configThresholdFilter = "  <ThresholdFilter level=\"" + fileEntry.getKey().name() + "\" />";

                FastLog4jLevelEnum nextLevel = FastLog4jLevelEnum.getNextLevel(fileEntry.getKey());
                if (nextLevel != null) {
                    configThresholdFilter += "<ThresholdFilter level=\"" + nextLevel.name().toUpperCase() + "\" onMatch=\"DENY\" onMismatch=\"NEUTRAL\"/>";
                }

                String rollinFileXml = "<RollingFile name=\"" + fileEntry.getValue().getRollingFileName() + "\" fileName=\"${LOG_DIRECTORY}/" + fileEntry.getValue().getLogFileName() + ".log\"" +
                        "             filePattern=\"${LOG_DIRECTORY}/$${date:yyyy-MM}/" + fileEntry.getValue().getLogFileName() + "-%d{yyyy-MM-dd}-%i.log.gz\" createOnDemand=\"true\" >" +
                        "    <Filters>" +
                        configThresholdFilter +
                        "    </Filters>" +
                        "    <PatternLayout pattern=\"" + fileEntry.getValue().getPattern() + "\"/>" +
                        "    <Policies>" +
                        "        <SizeBasedTriggeringPolicy size=\"" + fileEntry.getValue().getFileMaxSize() + " MB\"/>" +
                        "        <TimeBasedTriggeringPolicy interval=\"1\" modulate=\"true\" />" +
                        "    </Policies>" +
                        "</RollingFile>";
                appenderList.add(rollinFileXml);

                if (fileEntry.getValue().getConsole() != null) {
                    FastLog4jConsole console = fileEntry.getValue().getConsole();
                    String consoleAppender = "<Console name=\"" + console.getName() + "\" target=\"SYSTEM_OUT\">" +
                            "    <Filters>" +
                            configThresholdFilter +
                            "    </Filters>" +
                            "   <PatternLayout pattern=\"" + console.getPattern() + "\"/>" +
                            "</Console>";
                    appenderList.add(consoleAppender);
                }
            }


        }
        return FastStringUtils.join(appenderList, "");
    }




    private String buildLoggersXml() {
        List<String> loggers = new ArrayList<>();

        Map<String, FastLog4jLogger> loggerMap = config.getLoggers();
        for (Map.Entry<String, FastLog4jLogger> entry : loggerMap.entrySet()) {
            FastLog4jLogger log4jLogger = entry.getValue();

            List<String> appenderRefs = new ArrayList<>();
            for (Map.Entry<FastLog4jLevelEnum, FastLog4jRollingFile> rollingFileEntry : log4jLogger.getRollingFiles().entrySet()) {
                FastLog4jRollingFile rollingFile = rollingFileEntry.getValue();
                if (rollingFile.getConsole() != null) {
                    appenderRefs.add("<appender-ref ref=\"" + rollingFile.getConsole().getName() + "\"/>");
                }
                appenderRefs.add("<appender-ref ref=\"" + rollingFile.getRollingFileName() + "\"/>");
            }

            loggers.add("<logger name=\"" + log4jLogger.getName() + "\" level=\"" + log4jLogger.getLevel().name() + "\" additivity=\"" + log4jLogger.isAdditivity() + "\">" + FastStringUtils.join(appenderRefs, "") + "</logger>");
        }

        return FastStringUtils.join(loggers, "");
    }


}
