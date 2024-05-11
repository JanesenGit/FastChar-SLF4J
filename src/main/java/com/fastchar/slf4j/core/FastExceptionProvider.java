package com.fastchar.slf4j.core;

import com.fastchar.core.FastChar;
import com.fastchar.interfaces.IFastException;

public class FastExceptionProvider implements IFastException {

    @Override
    public boolean onPrintException(Throwable throwable) {
        if (!FastChar.getConstant().isWebStarted()) {
            return false;
        }
        if (FastChar.getConstant().isWebStopped()) {
            return false;
        }
        FastChar.getLogger().error(this.getClass(), throwable);
        return false;
    }

}
