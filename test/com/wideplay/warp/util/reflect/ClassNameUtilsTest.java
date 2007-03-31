package com.wideplay.warp.util.reflect;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.wideplay.warp.util.TextTools;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class ClassNameUtilsTest {
    @DataProvider(name = "names")
    public Object[][] makeNames() {
        return new Object[][] {
                { "/Start" },
                { "/start." },
                { "/Start%" },
                { "/Start" },
                { "/Start()" },
                { "/Start@" },
                { "/Start~" },
                { "/Start;" },
                { "/Start,@" },
                { "/Sta-rt" },
                { "/Sta=rt" },
                { "/Sta-rt?" },
                { "/Sta-+++rt" },
                { "/Start&&*^#!" },
        };
    }

    @Test(dataProvider = "names")
    public void testNormalize(String name) {
        System.out.println("Normalized name: " + (name = ClassNameUtils.normalize(name)));

        assert TextTools.isValidURI(name) : "converted classname was not a valid URI: " + name;
    }
}
