package com.wideplay.warp.widgets;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import com.wideplay.warp.widgets.routing.PageBook;
import static org.easymock.EasyMock.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class EmbedWidgetTest {
    private static final String PAGES_FOR_EMBEDDING = "pagesForEmbedding";
    private static final String PAGES_FOR_EMBEDDING_BROKEN = "pagesForEmbeddingBroken";
    private static final String PAGES_FOR_EMBEDDING_BROKEN_EXCEPTION = "pagesForEmbeddingBrokenThrowing";

    @DataProvider(name = PAGES_FOR_EMBEDDING)
    public Object[][] getPages() {
        return new Object[][] {
            { "MyEmbeddedPage", "aString", "message=pass" },
            { "YourPage", "anotherString", "message=pass" },
            { "YourPage2", "aaaa", "message='aaaa'" },
            { "YourPage3", "aaaa", "message=\"aaaa\"" },
            { "YourPage4", "bbbb", "message=\"aaaa\", message='bb' + 'bb'" },
            { "YourPage5", "cccc", "message=\"aaaa\", message='bb' + 'bb', message = getPass()" },
        };
    }

    @DataProvider(name = EmbedWidgetTest.PAGES_FOR_EMBEDDING_BROKEN)
    public Object[][] getPagesBroken() {
        return new Object[][] {
            { "YourPage", "anotherString", "  " },
            { "YourPage", "anotherString", "" },
            { "YourPage", "anotherString", ",," },
            { "YourPage2", "aaaa", "message='aa'" },
            { "YourPage2", "aaaa", "message='aa'," },
            { "YourPage3", "aaaa", "message=\"aaa\"" },
            { "YourPage4", "bbbbb", "message=\"aaaa\", message='bb' + 'bb'" },
            { "YourPage4", "bbbbb", "message=\"aaaa\", message='bb' + 'bb',," },
        };
    }

    @DataProvider(name = EmbedWidgetTest.PAGES_FOR_EMBEDDING_BROKEN_EXCEPTION)
    public Object[][] getPagesBrokenThrowing() {
        return new Object[][] {
            { "MyEmbeddedPage", "aString", "=pass" },
            { "YourPage", "anotherString", "message=" },
            { "YourPage", "anotherString", "message=pass=pass" },
        };
    }

    //the parent page object
    public static class MyParentPage {
        private String pass;

        MyParentPage(String pass) {
            this.pass = pass;
        }

        public String getPass() {
            return pass;
        }
    }

    public static class MyEmbeddedPage {
        private boolean set;
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
            assert null != message;
            assert !"".equals(message.trim());
            set = true;
        }

        public boolean isSet() {
            return set;
        }
    }

    @Test(dataProvider = PAGES_FOR_EMBEDDING)
    public final void pageEmbeddingSupressesNormalWidgetChain(String pageName, final String passOn, final String expression) {
        //forName expects pageName to be in all-lower case (it's an optimization)
        pageName = pageName.toLowerCase();

        final PageBook pageBook = createMock(PageBook.class);
        final PageBook.Page page = createMock(PageBook.Page.class);
        final Respond respond = new StringBuilderRespond();
        final RenderableWidget widget = createMock(RenderableWidget.class);

        expect(pageBook.forName(pageName))
                .andReturn(page);


        //mypage does?
        final MyEmbeddedPage myEmbeddedPage = new MyEmbeddedPage();
        expect(page.instantiate())
                .andReturn(myEmbeddedPage);

        expect(page.widget())
                .andReturn(widget);

        widget.render(myEmbeddedPage, respond);

        
        replay(pageBook, page, widget);

        final MvelEvaluator evaluator = new MvelEvaluator();
        final WidgetChain widgetChain = new WidgetChain();
        final WidgetChain targetWidgetChain = new WidgetChain();

        //noinspection unchecked
        targetWidgetChain.addWidget(new XmlWidget(new TerminalWidgetChain(), "p", evaluator, Collections.EMPTY_MAP));
        widgetChain.addWidget(new ShowIfWidget(targetWidgetChain, "true", evaluator));

        new EmbedWidget(expression, evaluator, pageBook, pageName)
                .render(new MyParentPage(passOn), respond);

        //assert bindings
        assert myEmbeddedPage.isSet() : "variable not passed on to embedded page";
        assert passOn.equals(myEmbeddedPage.getMessage()) : "variable not set on embedded page";

        //the render was ok
        final String resp = respond.toString();
        assert "".equals(resp) : "widget not embedded correctly : " + resp;
        
        verify(pageBook, page, widget);
    }


    @Test(dataProvider = PAGES_FOR_EMBEDDING)
    public final void pageEmbeddingChainsToEmbeddedWidget(String pageName, final String passOn, final String expression) {

        //forName expects pageName to be in all-lower case (it's an optimization)
        pageName = pageName.toLowerCase();

        final PageBook pageBook = createMock(PageBook.class);
        final PageBook.Page page = createMock(PageBook.Page.class);
        final Respond respond = new StringBuilderRespond();


        final MvelEvaluator evaluator = new MvelEvaluator();

        final WidgetChain widget = new WidgetChain();
        final WidgetChain targetWidgetChain = new WidgetChain();
        //noinspection unchecked
        targetWidgetChain.addWidget(new XmlWidget(new TerminalWidgetChain(), "p", evaluator, new LinkedHashMap<String, String>() {{
            put("class", "pretty");
            put("id", "a-p-tag");
        }}));
        widget.addWidget(new ShowIfWidget(targetWidgetChain, "true", evaluator));

        expect(pageBook.forName(pageName))
                .andReturn(page);


        //mypage does?
        final MyEmbeddedPage myEmbeddedPage = new MyEmbeddedPage();
        expect(page.instantiate())
                .andReturn(myEmbeddedPage);

        expect(page.widget())
                .andReturn(widget);

        replay(pageBook, page);



        new EmbedWidget(expression, evaluator, pageBook, pageName)
                .render(new MyParentPage(passOn), respond);

        //assert bindings
        assert myEmbeddedPage.isSet() : "variable not passed on to embedded page";
        assert passOn.equals(myEmbeddedPage.getMessage()) : "variable not set on embedded page";

        //the render was ok
        final String resp = respond.toString();
        assert "<p class=\"pretty\" id=\"a-p-tag\"/>".equals(resp) : "widget not embedded correctly : " + resp;

        verify(pageBook, page);
    }


    @Test(dataProvider = PAGES_FOR_EMBEDDING)
    public final void pageEmbeddingChainsToEmbeddedWidgetBehavior(String pageName, final String passOn, final String expression) {

        //forName expects pageName to be in all-lower case (it's an optimization)
        pageName = pageName.toLowerCase();

        final PageBook pageBook = createMock(PageBook.class);
        final PageBook.Page page = createMock(PageBook.Page.class);
        final Respond respond = new StringBuilderRespond();


        final MvelEvaluator evaluator = new MvelEvaluator();

        final WidgetChain widget = new WidgetChain();
        final WidgetChain targetWidgetChain = new WidgetChain();
        //noinspection unchecked
        targetWidgetChain.addWidget(new XmlWidget(new TerminalWidgetChain(), "p", evaluator, new LinkedHashMap<String, String>() {{
            put("class", "pretty");
            put("id", "a-p-tag");
        }}));
        widget.addWidget(new ShowIfWidget(targetWidgetChain, "false", evaluator));

        expect(pageBook.forName(pageName))
                .andReturn(page);


        //mypage does?
        final MyEmbeddedPage myEmbeddedPage = new MyEmbeddedPage();
        expect(page.instantiate())
                .andReturn(myEmbeddedPage);

        expect(page.widget())
                .andReturn(widget);

        replay(pageBook, page);



        new EmbedWidget(expression, evaluator, pageBook, pageName)
                .render(new MyParentPage(passOn), respond);

        //assert bindings
        assert myEmbeddedPage.isSet() : "variable not passed on to embedded page";
        assert passOn.equals(myEmbeddedPage.getMessage()) : "variable not set on embedded page";

        //the render was ok
        final String resp = respond.toString();
        assert "".equals(resp) : "widget not embedded correctly : " + resp;

        verify(pageBook, page);
    }



    @Test(dataProvider = PAGES_FOR_EMBEDDING)
    public final void pageEmbeddingAndBinding(String pageName, final String passOn, final String expression) {
        //forName expects pageName to be in all-lower case (it's an optimization)
        pageName = pageName.toLowerCase();

        final PageBook pageBook = createMock(PageBook.class);
        final PageBook.Page page = createMock(PageBook.Page.class);
        final Respond mockRespond = createMock(Respond.class);
        final RenderableWidget widget = createMock(RenderableWidget.class);

        expect(pageBook.forName(pageName))
                .andReturn(page);


        //mypage does?
        final MyEmbeddedPage myEmbeddedPage = new MyEmbeddedPage();
        expect(page.instantiate())
                .andReturn(myEmbeddedPage);

        expect(page.widget())
                .andReturn(widget);

        widget.render(myEmbeddedPage, mockRespond);


        replay(pageBook, page, mockRespond, widget);

        new EmbedWidget(expression, new MvelEvaluator(), pageBook, pageName)
                .render(new MyParentPage(passOn), mockRespond);


        assert myEmbeddedPage.isSet() : "variable not passed on to embedded page";
        assert passOn.equals(myEmbeddedPage.getMessage()) : "variable not set on embedded page";

        verify(pageBook, page, mockRespond, widget);
    }

    @Test(dataProvider = PAGES_FOR_EMBEDDING_BROKEN_EXCEPTION, expectedExceptions = IllegalArgumentException.class)
    public final void failedPageEmbeddingThrowing(String pageName, final String passOn, final String expression) {
        //forName expects pageName to be in all-lower case (it's an optimization)
        pageName = pageName.toLowerCase();

        final PageBook pageBook = createMock(PageBook.class);
        final PageBook.Page page = createMock(PageBook.Page.class);
        final Respond mockRespond = createMock(Respond.class);
        final RenderableWidget widget = createMock(RenderableWidget.class);

        expect(pageBook.forName(pageName))
                .andReturn(page);


        //mypage does?
        final MyEmbeddedPage myEmbeddedPage = new MyEmbeddedPage();
        expect(page.instantiate())
                .andReturn(myEmbeddedPage);

        expect(page.widget())
                .andReturn(widget);

        widget.render(myEmbeddedPage, mockRespond);


        replay(pageBook, page, mockRespond, widget);

        new EmbedWidget(expression, new MvelEvaluator(), pageBook, pageName)
                .render(new MyParentPage(passOn), mockRespond);


//        assert !passOn.equals(myEmbeddedPage.getMessage()) : "variable somehow set on embedded page";

        verify(pageBook, page, mockRespond, widget);
    }

    @Test(dataProvider = PAGES_FOR_EMBEDDING_BROKEN)
    public final void failedPageEmbedding(String pageName, final String passOn, final String expression) {
        //forName expects pageName to be in all-lower case (it's an optimization)
        pageName = pageName.toLowerCase();

        final PageBook pageBook = createMock(PageBook.class);
        final PageBook.Page page = createMock(PageBook.Page.class);
        final Respond mockRespond = createMock(Respond.class);
        final RenderableWidget widget = createMock(RenderableWidget.class);

        expect(pageBook.forName(pageName))
                .andReturn(page);


        //mypage does?
        final MyEmbeddedPage myEmbeddedPage = new MyEmbeddedPage();
        expect(page.instantiate())
                .andReturn(myEmbeddedPage);

        expect(page.widget())
                .andReturn(widget);

        widget.render(myEmbeddedPage, mockRespond);


        replay(pageBook, page, mockRespond, widget);

        new EmbedWidget(expression, new MvelEvaluator(), pageBook, pageName)
                .render(new MyParentPage(passOn), mockRespond);


        assert !passOn.equals(myEmbeddedPage.getMessage()) : "variable somehow set on embedded page";

        verify(pageBook, page, mockRespond, widget);
    }
}
