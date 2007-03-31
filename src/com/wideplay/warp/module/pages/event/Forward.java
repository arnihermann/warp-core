package com.wideplay.warp.module.pages.event;

/**
 * Created with IntelliJ IDEA.
 * On: 19/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class Forward {
    public static Forward.PageForwarding to(Object page) {
        return new Forward.PageForwarding(page);
    }

    public static final class PageForwarding implements Redirection {
        private Object page;

        private PageForwarding(Object page) {
            this.page = page;
        }

        public Object getPage() {
            return page;
        }
    }
}
