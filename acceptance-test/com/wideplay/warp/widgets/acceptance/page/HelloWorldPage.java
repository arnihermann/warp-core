package com.wideplay.warp.widgets.acceptance.page;

import com.wideplay.warp.widgets.acceptance.util.AcceptanceTest;
import com.wideplay.warp.widgets.example.HelloWorld;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class HelloWorldPage {
    static final String HELLO_WORLD_MSG = String.format("Message : %s", HelloWorld.MESSAGE);

    private WebDriver driver;

    public HelloWorldPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean hasHelloWorldMessage() {
        //TODO ugh! stupid xpath doesn't work =(
        return driver.getPageSource().contains(HELLO_WORLD_MSG);
    }

    public static HelloWorldPage open(WebDriver driver) {
        driver.get(AcceptanceTest.BASE_URL + "/hello");
        return PageFactory.initElements(driver, HelloWorldPage.class);
    }
}