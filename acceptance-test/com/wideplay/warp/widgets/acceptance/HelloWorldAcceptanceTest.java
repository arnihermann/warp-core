package com.wideplay.warp.widgets.acceptance;

import com.wideplay.warp.widgets.acceptance.page.HelloWorldPage;
import com.wideplay.warp.widgets.acceptance.util.AcceptanceTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

/**
 * @author Tom Wilson (tom@tomwilson.name)
 */
@Test(suiteName = AcceptanceTest.SUITE)
public class HelloWorldAcceptanceTest {

    public void shouldRenderDynamicTextFromHelloWorld() {
        WebDriver driver = AcceptanceTest.createWebDriver();
        HelloWorldPage page = HelloWorldPage.open(driver);

        assert page.hasHelloWorldMessage() : "Did not generate dynamic text from el expression";
    }
}