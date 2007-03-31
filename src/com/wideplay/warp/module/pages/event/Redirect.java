package com.wideplay.warp.module.pages.event;

/**
 * Created with IntelliJ IDEA.
 * On: 19/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class Redirect {
    public static URLRedirection to(String url) {
        return new URLRedirection(url);
    }

    public static PageRedirection toPage(Object page) {
        return new PageRedirection(page);
    }

    public static final class URLRedirection implements Redirection {
        private String url;

        private URLRedirection(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    public static final class PageRedirection implements Redirection {
        private Object page;

        private PageRedirection(Object page) {
            this.page = page;
        }

        public Object getPage() {
            return page;
        }
    }
}
