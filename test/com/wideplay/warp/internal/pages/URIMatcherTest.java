package com.wideplay.warp.internal.pages;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.wideplay.warp.example.ExampleModule;
import com.wideplay.warp.module.UriMatcher;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.PageHandler;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dhanji
 * Date: Aug 7, 2007
 * Time: 12:57:45 PM
 */
public class URIMatcherTest {
    private Map<String, Object> uris = new HashMap<String, Object>();

    @DataProvider(name = "uris")
    Object[][] getUris() {
        return new Object[][] {
                { "/person", null, true },
                { "/person/12", "12", true },
                { "/person/12/", "12", true },
                { "/movie/category/drama", "drama", true },
                { "/movie/category", null, true },
                { "/movie/category/", null, true },
                { "/movie/category/drama/AdamSandler", "drama/AdamSandler", true },
                { "/movie/category/drama/basd/asdasd", "drama/basd/asdasd", true },
                { "/movie", null, false }, //no match
                { "/movie/", null, false },  //no match
                { "/", null, false },  //no match
                { "/movie/category/drama?va=asdasd&asdk=asdk", "drama?va=asdasd&asdk=asdk", true },
        };
    }

    @Test
    public final void testsomething() throws UnsupportedEncodingException {
        System.out.println(URLEncoder.encode("%20", "UTF-8"));

        Guice.createInjector(Collections.unmodifiableList(Arrays.asList(new Module[] { new ExampleModule() } )));
    }

    @BeforeClass
    public final void before() {
        //register valid uris

        new UriMatchTreeBuilder().buildAndStore("/movie/category/{name}", new DummyPageHandler(), uris);
        new UriMatchTreeBuilder().buildAndStore("/person/{id}", new DummyPageHandler(), uris);


    }

    @Test
    public final void testUriMatchTreeBuilding() {
        Map uris = new HashMap();
        new UriMatchTreeBuilder().buildAndStore("/movie/category/{name}", new DummyPageHandler(), uris);
        new UriMatchTreeBuilder().buildAndStore("/person/{id}", new DummyPageHandler(), uris);

        System.out.println(uris);
    }

    @Test(dataProvider = "uris")
    public final void testDynamicUriMatching(String uri, String expected, boolean isMatch) {
        //first test if uri is in the literal uri map
        //...

        UriMatcher.MatchTuple extract = Guice.createInjector()
                                            .getInstance(UriMatcher.class)
                                            .extractMatch(uri, uris);

        if (null != expected)
            assert expected.equals(extract.uriExtract) : "Did not match expected : " + expected;
        else if (isMatch) {
            assert extract != null : "Did not match as expected (null returned!!) : " + expected;
            //noinspection StringEquality
            assert null == extract.uriExtract : "Did not match expected : " + expected;
        }

        if (!isMatch)
            assert extract == null : "Was not supposed to match : " + uri;

        System.out.println("Matched extract: " + extract);
    }

    static class DummyPageHandler implements PageHandler {

        public Object handleRequest(HttpServletRequest request, HttpServletResponse response, Injector injector, Object page, String uriPart) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public PageClassReflection getPageClassReflection() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public ComponentHandler getRootComponentHandler() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

}
