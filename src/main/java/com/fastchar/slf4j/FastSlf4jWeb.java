package com.fastchar.slf4j;

import com.fastchar.core.FastEngine;
import com.fastchar.interfaces.IFastWeb;
import com.fastchar.slf4j.core.FastExceptionProvider;
import com.fastchar.slf4j.provider.FastLog4jProvider;

public class FastSlf4jWeb implements IFastWeb {

    @Override
    public void onInit(FastEngine engine) throws Exception {
        engine.getOverrides().add(FastLog4jProvider.class);
        engine.getOverrides().add(FastExceptionProvider.class);
        engine.getObservable().addObserver(FastLog4jProvider.class);
    }

    @Override
    public void onFinish(FastEngine engine) throws Exception {
        engine.getConfig(FastLog4j2Config.class).initLog4jConfig();
    }
}
