package com.wideplay.warp.module;

import com.google.inject.ImplementedBy;
import com.wideplay.warp.rendering.PageHandler;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dhanji
 * Date: Dec 13, 2007
 * Time: 10:16:40 PM
 *
 * @author Dhanji R. Prasanna (dhanji gmail com)
 */
@ImplementedBy(PathDelimitingUriMatcher.class)
public interface UriMatcher {
    MatchTuple extractMatch(String uri, Map<String, Object> uris);

    public static final class MatchTuple {
        public String uriExtract;
        public PageHandler pageHandler;

        public MatchTuple(String uriExtract, PageHandler pageHandler) {
            this.uriExtract = uriExtract;
            this.pageHandler = pageHandler;
        }

        @Override
        public String toString() {
            return String.format("URI: %s; Handler: %s", uriExtract, pageHandler);
        }

        static PathDelimitingUriMatcher.MatchTuple tuple(String uriExtract, PageHandler pageHandler) {
            return new PathDelimitingUriMatcher.MatchTuple(uriExtract, pageHandler);
        }
    }
}
