package com.wideplay.warp.widgets.routing;

import com.wideplay.warp.widgets.*;
import com.wideplay.warp.widgets.rendering.EmbedAs;
import com.google.inject.name.Named;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class PageBookImplTest {
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

    @Test
    public final void fireGetMethodOnPage() {
        RenderableWidget mock = new RenderableWidget() {
            public void render(Object bound, Respond respond) {

            }
        };

        final PageBook pageBook = new PageBookImpl(null);
        pageBook.at("/wiki", mock, MyPage.class);

        PageBook.Page page = pageBook.get("/wiki");
        final MyPage bound = new MyPage();
        page.doGet(bound, "/wiki");

        assert page.widget().equals(mock);
        assert bound.getted : "@Get method was not fired, on doGet()"; 
    }

    @Test
    public final void fireGetMethodWithArgsOnPage() {
        RenderableWidget mock = new RenderableWidget() {
            public void render(Object bound, Respond respond) {

            }
        };

        final PageBook pageBook = new PageBookImpl(null);
        pageBook.at("/wiki/:title", mock, MyPageWithTemplate.class);

        PageBook.Page page = pageBook.get("/wiki/IMAX");
        final MyPageWithTemplate bound = new MyPageWithTemplate();
        page.doGet(bound, "/wiki/IMAX");

        assert page.widget().equals(mock);
        assert "IMAX".equals(bound.title) : "@Get method was not fired, on doGet() with the right arg, instead: " + bound.title; 
    }

    @Test
    public final void firePostMethodWithArgsOnPage() {
        RenderableWidget mock = new RenderableWidget() {
            public void render(Object bound, Respond respond) {

            }
        };

        final PageBook pageBook = new PageBookImpl(null);
        pageBook.at("/wiki/:title/cat/:id", mock, MyPageWithTemplate.class);

        PageBook.Page page = pageBook.get("/wiki/IMAX_P/cat/12");
        final MyPageWithTemplate bound = new MyPageWithTemplate();
        page.doPost(bound, "/wiki/IMAX_P/cat/12");

        assert page.widget().equals(mock);
        assert "IMAX_P".equals(bound.post) && "12".equals(bound.id) 
                : "@Post method was not fired, on doPost() with the right arg, instead: " + bound.post;
    }

    @Test(expectedExceptions = InvalidEventHandlerException.class)
    public final void errorOnPostMethodWithUnnamedArgs() {
        RenderableWidget mock = new RenderableWidget() {
            public void render(Object bound, Respond respond) {

            }
        };

        final PageBook pageBook = new PageBookImpl(null);
        pageBook.at("/wiki/:title/cat/:id", mock, MyBrokenPageWithTemplate.class);

        PageBook.Page page = pageBook.get("/wiki/IMAX_P/cat/12");
        final MyBrokenPageWithTemplate bound = new MyBrokenPageWithTemplate();
        page.doPost(bound, "/wiki/IMAX_P/cat/12");

        assert page.widget().equals(mock);
    }

    @Test
    public final void firePostMethodOnPage() {
        RenderableWidget mock = new RenderableWidget() {
            public void render(Object bound, Respond respond) {

            }
        };

        final PageBook pageBook = new PageBookImpl(null);
        pageBook.at("/wiki", mock, MyPage.class);

        PageBook.Page page = pageBook.get("/wiki");
        final MyPage bound = new MyPage();
        page.doPost(bound, "/wiki");

        assert page.widget().equals(mock);
        assert bound.posted : "@Post method was not fired, on doPost()";
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
        final MyPage myPage = new MyPage();
        page.widget().render(myPage, respond);

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
        private boolean getted;
        private boolean posted;

        @Get
        public void get() {
            getted = true;
        }

        @Post
        public void post() {
            posted = true;
        }

    }

    @At("/wiki/:title/cat/:id")
    @EmbedAs("Hi")
    public static class MyPageWithTemplate {
        private String title;
        private boolean posted;
        private String post;
        private String id;

        @Get
        public void get(@Named("title") String title) {
            this.title = title;
        }

        @Post
        public void post(@Named("title") String title, @Named("id") String id) {
            this.post = title;
            this.id = id;
        }

    }
    @At("/wiki/:title/cat/:id")
    @EmbedAs("Hi")
    public static class MyBrokenPageWithTemplate {

        @Post
        public void post(@Named("title") String title, int x, @Named("id") String id) {
        }

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
