package com.wideplay.warp.internal.pages;

import com.google.inject.Injector;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.RenderingContext;
import com.wideplay.warp.util.beans.BeanUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 30/08/2007
 *
 * A simple tuple to carry the rendering context to component instances.
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
class RenderingContextImpl implements RenderingContext {
    private final Injector injector;
    private final HtmlWriter writer;
    private final PageClassReflection reflection;
    private final Object page;
    private final Map<String, Object> contextVars;

    public RenderingContextImpl(HtmlWriter writer, Injector injector, PageClassReflection reflection, Object page) {
        this.injector = injector;
        this.page = page;
        this.reflection = reflection;
        this.writer = writer;
        this.contextVars = new ContextMapAdapter(page);
    }

    public Map<String, Object> getContextVars() {
        return contextVars;
    }

     public Injector getInjector() {
        return injector;
    }

    public Object getPage() {
        return page;
    }

    public PageClassReflection getReflection() {
        return reflection;
    }

    public HtmlWriter getWriter() {
        return writer;
    }

    private static class ContextMapAdapter extends HashMap<String, Object> {
        private final Object contextObject;

        public ContextMapAdapter(Object contextObject) {
            this.contextObject = contextObject;
        }

        @Override
        public Object get(Object key) {
            Object value = super.get(key);

            return (null == value) ? BeanUtils.getProperty((String) key, contextObject) : value;
        }

        @Override
        public boolean containsKey(Object key) {
            return true;
        }
    }
}
