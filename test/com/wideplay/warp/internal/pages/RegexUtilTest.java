package com.wideplay.warp.internal.pages;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: dhanji
 * Date: Dec 13, 2007
 * Time: 10:35:25 PM
 *
 * @author Dhanji R. Prasanna (dhanji gmail com)
 */
public class RegexUtilTest {
    private static final String HTMLS = "htmls";

    @DataProvider(name = HTMLS)
    Object[][] getHtmls() {
        return new Object[][] {
                { "  <script type=\"text/javascript\" src=\"/somethign.js\" ></script>   " },
                { "  <link type=\"text/css\" href=\"/script/somethign.js\" ></script>   " },
                { "  <script type=\"text/javascript\" src=\"script/somethign.js\" ></script>   " },
                { "  <script type=\"text/javascript\" src=\"http://something.com/js\" ></script>   " },
                { "  <script type=\"text/javascript\" href=\"script/somethign.js\" ></script>   " },
        };
    }

    @Test(dataProvider = HTMLS)
    public final void regexToReplaceUriContexts(String html) {


        //run mock script ***
        html = html.replaceAll("href=\"/", "href=\"/ctx/")
                .replaceAll("src=\"/", "src=\"/ctx/");


        //assert expectations
        System.out.println(html);
    }
}
