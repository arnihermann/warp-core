package com.wideplay.warp.widgets.routing;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import com.wideplay.warp.widgets.RenderableWidget;
import com.wideplay.warp.widgets.Respond;
import com.wideplay.warp.widgets.At;
import com.wideplay.warp.widgets.rendering.EmbedAs;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class PageBookTest {
    private static final String FIRST_PATH_ELEMENTS = "firstPathElements";
    private static final String URI_TEMPLATES_AND_MATCHES = "uriTemplatesAndMatches";
    private static final String NOT_URIS_AND_TEMPLATES = "noturisandTemplates";

    @Test
    public final void storeAndRetrievePageInstance() {
        final Respond respond = new MockRespond();

        RenderableWidget mock = new RenderableWidget() {
            public void render(Object bound, Respond respond) {

            }
        };

        final PageBook pageBook = new PageBookImpl(null);
        pageBook.at("/wiki", mock, MyPage.class);

        PageBook.Page page = pageBook.get("/wiki");
        page.widget().render(new Object(), respond);

        assert page.widget().equals(mock);
    }

    @DataProvider(name = URI_TEMPLATES_AND_MATCHES)
    public Object[][] getUriTemplatesAndMatches() {
        return new Object[][] {
            { "/wiki/:title", "/wiki/HelloPage"  },
            { "/wiki/:title", "/wiki/HelloPage%20"  },
            { "/wiki/:title/dude", "/wiki/HelloPage/dude"  },
            { "/:title/thing", "/wiki/thing"  },
            { "/:title", "/aposkdapoksd"  },
        };
    }

    @Test(dataProvider = URI_TEMPLATES_AND_MATCHES)
    public final void matchPageByUriTemplate(final String template, final String toMatch) {
        final Respond respond = new MockRespond();

        RenderableWidget mock = new RenderableWidget() {
            public void render(Object bound, Respond respond) {

            }
        };

        final PageBook pageBook = new PageBookImpl(null);
        pageBook.at(template, mock, MyPage.class);

        PageBook.Page page = pageBook.get(toMatch);
        page.widget().render(new Object(), respond);

        assert mock.equals(page.widget());
    }

    @DataProvider(name = NOT_URIS_AND_TEMPLATES)
    public Object[][] getNotUriTemplatesAndMatches() {
        return new Object[][] {
            { "/wiki/:title", "/tiki/HelloPage"  },
            { "/wiki/:title", "/wiki/HelloPage%20/didle"  },
            { "/wiki/:title/dude", "/wiki/HelloPage"  },
            { "/:title/thing", "/wiki/thing/thingaling"  },
            { "/:title", "/aposkdapoksd/12"  },
        };
    }

    @Test(dataProvider = NOT_URIS_AND_TEMPLATES)
    public final void notMatchPageByUriTemplate(final String template, final String toMatch) {
        RenderableWidget mock = new RenderableWidget() {
            public void render(Object bound, Respond respond) {

            }
        };

        final PageBook pageBook = new PageBookImpl(null);
        pageBook.at(template, mock, MyPage.class);

        //cant find
        assert null == pageBook.get(toMatch);

    }

    @At("/oas")
    @EmbedAs("Hi")
    public static class MyPage {

    }

    @DataProvider(name = FIRST_PATH_ELEMENTS)
    public Object[][] get() {
        return new Object[][] {
            { "/wiki/:title", "wiki" },
            { "/wiki/:title/:thing", "wiki" },
            { "/wiki/other/thing/dude", "wiki" },
            { "/wiki", "wiki" },
            { "/wiki/", "wiki" },
            { "/", "" },
        };
    }

    @Test(dataProvider = FIRST_PATH_ELEMENTS)
    public final void firstPathElement(final String uri, final String answer) {
        final String fPath = new PageBookImpl(null)
                .firstPathElement(uri);

        assert answer.equals(fPath) : "wrong path: " + fPath;
    }

    private static class MockRespond implements Respond {

        public void write(String text) {
        }

        public HtmlTagBuilder withHtml() {
            throw new AssertionError();
        }

        public void write(char c) {
        }

        public void chew() {

        }

        public void writeToHead(String text) {
            
        }
    }
}
