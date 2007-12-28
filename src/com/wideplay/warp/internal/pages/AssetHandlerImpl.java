package com.wideplay.warp.internal.pages;

import com.wideplay.warp.rendering.PageHandler;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.PageRenderException;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.util.reflect.FieldDescriptor;
import com.wideplay.warp.annotations.Asset;
import com.google.inject.Injector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: dhanji
 * Date: Dec 28, 2007
 * Time: 3:33:49 PM
 *
 * Serves static resources via the warp-filter pipeline (i.e. css, js, etc. packaged in Jars).
 * Anything bound with @Asset(..) typically is served by this class.
 *
 * @author Dhanji R. Prasanna (dhanji gmail com)
 */
class AssetHandlerImpl implements PageHandler {
    private final byte[] content;
    private final Asset asset;

    public AssetHandlerImpl(byte[] content, Asset asset) {
        this.content = content;
        this.asset = asset;
    }

    public Object handleRequest(HttpServletRequest request,
                                HttpServletResponse response,
                                Injector injector,
                                Object page,
                                String uriPart) {

        try {
            response.getOutputStream().write(content);
        } catch (IOException e) {
             throw new PageRenderException("Error obtaining the response writer while rendering asset: "
                     + asset.resource() + " at uri " + asset.uri());
        }

        return null; //do nothing after serving this resource (i.e. stay on this page)
    }

    public PageClassReflection getPageClassReflection() {
        //noinspection OverlyComplexAnonymousInnerClass,AnonymousInnerClassWithTooManyMethods
        return new PageClassReflection() {
            public Object getPropertyValue(Object bean, String name) {
                throw new UnsupportedOperationException("Static assets cannot be rendered dynamically");
            }

            public void setPropertyValue(Object bean, String name, Object value) {
                throw new UnsupportedOperationException("Static assets cannot be rendered dynamically");
            }

            public Class<?> getPageClass()//fires all event handlers (default action)
            {
                return Object.class;    //this is never used
            }

            public Object fireEvent(Object bean, String event, Object topic) {
                throw new UnsupportedOperationException("Static assets cannot be rendered dynamically");
            }

            public Set<FieldDescriptor> getManagedFields() {
                throw new UnsupportedOperationException("Static assets cannot be rendered dynamically");                
            }
        };
    }

    public ComponentHandler getRootComponentHandler() {
        throw new UnsupportedOperationException("Static assets cannot be rendered dynamically");
    }
}
