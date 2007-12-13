package com.wideplay.warp.components.core;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.annotations.Page;
import com.wideplay.warp.components.AttributesInjectable;
import com.wideplay.warp.internal.pages.PageBuilders;
import com.wideplay.warp.module.WarpModuleAssembly;
import com.wideplay.warp.module.componentry.PropertyDescriptor;
import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.module.ioc.IocContextManager;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.PageHandler;
import com.wideplay.warp.rendering.RenderingContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 26/03/2007
 *
 * Viewport is a container component that projects the object bound to "embed" as
 * the current page object to all of its nested components. This object MUST be a top-level page (i.e.
 * must have a matching template and be available to the module).
 *
 * If ajax mode is enabled, Any events fired by nested components are trapped and sent
 * via XHR to the page represented by "embed." The rendered response is then re-inserted
 * as the current content of this viewport.
 *
 * The property expression which is placed in "embed", should be injected into the current containing
 * page via @Inject @Page (the normal way). If the ajax target page sends a redirect request, it is
 * propagated to the containing page as a normal redirect. Thus, forwarding is the only viable option
 * for redirecting WITHIN a viewport.
 *
 * A page ("fragment") wrapped inside a viewport is typically stripped of &lt;body&gt; tags. The page
 * object itself is disambiguated using the containing page's identity as a discriminator (via a request parameter).
 * In this way, a single page can be embedded in multiple viewports concurrently and maintain different
 * states (across different XHRs). This is a bit more RESTful (appearance of statlessness). 
 *
 * Thus, viewport is a slick, transparent way to convert any page into an ajax page-fragment. You can easily
 * see how such fragments, with a bit of behavior abstraction, can be turned into reusable "ajax widgets."
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@Component
public class Viewport implements Renderable, AttributesInjectable {
    private String id;
    private Object embed; //embedded page
    private String embedClass; //optionally embed by page class
    private boolean async = false;

    private Map<String, Object> attribs;    //embedded attributes

    public static final String EMBED_CLASS_PROPERTY = "embedClass";

    private final WarpModuleAssembly assembly;

    @Inject
    public Viewport(WarpModuleAssembly assembly) {
        this.assembly = assembly;
    }

    public void render(RenderingContext context, List<? extends ComponentHandler> nestedComponents) {
        HtmlWriter writer = context.getWriter();

        //obtain the embedded page object (either directly injected or get via page class from guice)
        Object embedded = embed;
        if (null != embedClass)
            embedded = context.getInjector().getInstance(Key.get(assembly.getPageClassByName(embedClass), Page.class));

        //get its component object tree (i.e. the PageHandler which renders it)
        String uri = assembly.resolvePageURI(embedded);
        PageHandler embeddedPageHandler = assembly.getPage(uri);

        //strip the frame (or whatever is wrapping) component
        List<? extends ComponentHandler> embeddedContent = ComponentSupport.obtainFrameNestedContent(embeddedPageHandler.getRootComponentHandler().getNestedComponents());

        //inject the embedded page (configure it) prior to render--with properties from the current page (as necessary)
        if (null != attribs)
            IocContextManager.injectProperties(extractAttributePropertyList(), embedded, context.getContextVars());

        //if asynchronous, we should proxy the html writer to trap and watch input bindings separately from the rest of the page
        if (async) {
            renderAsyncViewport(writer, embeddedContent, context, embedded, uri);
        } else
            //render the embedded content as my children, rather than my own children (which are discarded), using the embedded object as page
            ComponentSupport.renderMultiple(PageBuilders.newRenderingContext(writer, context.getInjector(), context.getReflection(), embedded),
                    embeddedContent);
    }



    private void renderAsyncViewport(HtmlWriter writer, List<? extends ComponentHandler> embeddedContent, RenderingContext context, Object embedded, String uri) {
        //grab the id from the property map
        String id = makeId(writer);

        //grab the tag name and write it as is
        String tagName = attribs.get(RawText.WARP_RAW_TEXT_PROP_TAG).toString();
        writer.element(tagName, "id", id);

        //use a static proxy pattern to intercept calls to register input bindings
        AsyncViewportHtmlWriter asyncWriter = new AsyncViewportHtmlWriter(id, writer);

        //wrap this in a new context
        RenderingContext proxyContext = PageBuilders.newRenderingContext(asyncWriter, context.getInjector(), context.getReflection(), embedded);

        //render viewport contents
        ComponentSupport.renderMultiple(proxyContext, embeddedContent);

        //get "watched" bindings and write them into a viewport property (using js)
        writer.writeToOnLoad("document.getElementById(\"");
        writer.writeToOnLoad(id);
        writer.writeToOnLoad("\").bindings=new Array(\"");
        for (int i = 0; i < asyncWriter.getBindings().size(); i++) {
            String binding = asyncWriter.getBindings().get(i);
            writer.writeToOnLoad(binding);

            //write commans except for last one
            if (i < asyncWriter.getBindings().size() - 1)
                writer.writeToOnLoad("\", ");
        }
        writer.writeToOnLoad("\");");

        //write target URI for asynchronous callback
        writer.writeToOnLoad("document.getElementById(\"");
        writer.writeToOnLoad(id);
        writer.writeToOnLoad("\").targetUri=\"");
        writer.writeToOnLoad(uri);
        writer.writeToOnLoad("\";");

        writer.end(tagName);
    }

    private String makeId(HtmlWriter writer) {
        if (null == this.id)
            return writer.newId(this);

        return this.id;
    }


    @SuppressWarnings("unchecked")
    private Collection<PropertyDescriptor> extractAttributePropertyList() {
        return ((Map<String, PropertyDescriptor>) attribs.get(RawText.WARP_RAW_TEXT_ATTR_MAP)).values();
    }


    public void setEmbed(Object embed) {
        this.embed = embed;
    }

    public void setEmbedClass(String embedClass) {
        this.embedClass = embedClass;
    }

    public void setAttributeNameValuePairs(Map<String, Object> attribs) {
        this.attribs = attribs;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public Map<String, Object> getAttributeNameValuePairs() {
        return attribs;
    }
}
