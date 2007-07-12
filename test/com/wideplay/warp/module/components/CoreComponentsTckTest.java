package com.wideplay.warp.module.components;

import com.wideplay.warp.core.Frame;
import com.wideplay.warp.core.Link;
import com.wideplay.warp.core.RawText;
import com.wideplay.warp.module.components.tck.CompatibilityVerifier;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * On: 22/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class CoreComponentsTckTest {
    @DataProvider(name = "core")
    public Object[][] components() {
        return new Object[][] {
                { new Frame() },
                { new RawText() },
                { new Link(null) },
        };
    }

    @Test(dataProvider = "core")
    public final void testComponent(Object component) {
        CompatibilityVerifier.CompatibilityResult results = CompatibilityVerifier.verifyComponent(component);
        for (String warning : results.getComplaints())
            System.err.println("TCK Message: " + warning);

        assert CompatibilityVerifier.Compatibility.REJECTED != results.getResult()
                : "Core component was rejected by the Warp Technology Compatibility Kit (omg what did you do?!)";
    }
}
