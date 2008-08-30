package com.wideplay.warp.widgets.acceptance;

import com.wideplay.warp.widgets.acceptance.page.EmbedPage;
import com.wideplay.warp.widgets.acceptance.util.AcceptanceTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Test(suiteName = AcceptanceTest.SUITE)
public class EmbedAcceptanceTest {

    public void shouldRenderHelloWorldEmbeddedWithRequires() {
        WebDriver driver = AcceptanceTest.createWebDriver();
        EmbedPage page = EmbedPage.open(driver);

        assert page.hasCssLink() : "Did not contain require widget (css link) from embedded page";
        assert page.hasHelloWorldMessage() : "Did not contain hellow world message";
    }
}