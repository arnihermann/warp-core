package com.wideplay.warp.util;

import org.testng.annotations.Test;
import static org.easymock.EasyMock.*;
import com.google.inject.Guice;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public class LoggingTest {

    @Test
    public final void loggingInterface() {
        final Memo mock = createMock(Memo.class);

        mock.debug("Deeebug", " ", "a thing");
        expectLastCall().once();

        mock.info("Hello!", " ", "there");
        expectLastCall().once();

        mock.error("Warning", " will robinson!");
        expectLastCall().once();


        Memo logger = new JdkLoggingMemoFactory().log(LoggingTest.class);
        assert null != logger;
    }

    @Test
    public final void consoleLoggingJunkIntegration() {
        final Memo memo = Guice.createInjector()
                .getInstance(MemoFactory.class)
                .log(LoggingTest.class);

        memo.info("Hello how are you doing?", " ", "dude");
        memo.debug("Hello how are you doing?", " ", "dude");
        memo.error("Hello how are you doing?", " ", "dude");
    }
}
