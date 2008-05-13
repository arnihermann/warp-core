package com.wideplay.warp.widgets;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import com.wideplay.warp.widgets.routing.PageBook;
import static org.easymock.EasyMock.*;

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
    public final void pageEmbedding(final String pageName, final String passOn, final String expression) {

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

        new EmbedWidget(new WidgetChain(), expression, new MvelEvaluator(), pageBook, pageName)
                .render(new MyParentPage(passOn), mockRespond);


        assert myEmbeddedPage.isSet() : "variable not passed on to embedded page";
        assert passOn.equals(myEmbeddedPage.getMessage()) : "variable not set on embedded page";
        
        verify(pageBook, page, mockRespond, widget);
    }

    @Test(dataProvider = PAGES_FOR_EMBEDDING_BROKEN_EXCEPTION, expectedExceptions = IllegalArgumentException.class)
    public final void failedPageEmbeddingThrowing(final String pageName, final String passOn, final String expression) {

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

        new EmbedWidget(new WidgetChain(), expression, new MvelEvaluator(), pageBook, pageName)
                .render(new MyParentPage(passOn), mockRespond);


//        assert !passOn.equals(myEmbeddedPage.getMessage()) : "variable somehow set on embedded page";

        verify(pageBook, page, mockRespond, widget);
    }

    @Test(dataProvider = PAGES_FOR_EMBEDDING_BROKEN)
    public final void failedPageEmbedding(final String pageName, final String passOn, final String expression) {

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

        new EmbedWidget(new WidgetChain(), expression, new MvelEvaluator(), pageBook, pageName)
                .render(new MyParentPage(passOn), mockRespond);


        assert !passOn.equals(myEmbeddedPage.getMessage()) : "variable somehow set on embedded page";

        verify(pageBook, page, mockRespond, widget);
    }
}
