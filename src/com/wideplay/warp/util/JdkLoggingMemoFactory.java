package com.wideplay.warp.util;

import com.google.inject.Singleton;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Arrays;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@Singleton
class JdkLoggingMemoFactory implements MemoFactory {

    public Memo log(final Class<?> aClass) {
        //noinspection OverlyComplexAnonymousInnerClass
        return new Memo() {
            private final Logger log = Logger.getLogger(aClass.toString());

            public void debug(String... debug) {
                if (!log.isLoggable(Level.FINE))
                    return;

                //quick check for common case
                if (debug.length == 1) {
                    log.fine(debug[0]);
                    return;
                }

                StringBuilder builder = new StringBuilder();
                for (String s : debug) {
                    builder.append(s);
                }

                log.fine(builder.toString());
            }

            public void info(String... info) {
                StringBuilder builder = new StringBuilder();
                for (String s : info) {
                    builder.append(s);
                }

                log.info(builder.toString());
            }

            public void error(String... warn) {
                StringBuilder builder = new StringBuilder();
                for (String s : warn) {
                    builder.append(s);
                }

                log.warning(builder.toString());
            }
        };
    }
}
