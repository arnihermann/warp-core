package com.wideplay.warp.widgets.acceptance.page;

import com.wideplay.warp.widgets.acceptance.util.AcceptanceTest;
import com.wideplay.warp.widgets.example.DynamicJs;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class DynamicJsPage {

    private WebDriver driver;

    public DynamicJsPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean hasDynamicText() {
        return driver.getPageSource().contains(DynamicJs.A_MESSAGE);
    }

    public boolean hasNoMeta() {
        return !driver.getPageSource().contains("@Meta");
    }

    public static DynamicJsPage open(WebDriver driver) {
        driver.get(AcceptanceTest.BASE_URL + "/dynamic.js");
        return PageFactory.initElements(driver, DynamicJsPage.class);
    }
}