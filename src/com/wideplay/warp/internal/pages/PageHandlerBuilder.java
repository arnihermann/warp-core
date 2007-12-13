package com.wideplay.warp.internal.pages;

import com.wideplay.warp.annotations.Template;
import com.wideplay.warp.annotations.URIMapping;
import com.wideplay.warp.internal.UriMatchTreeBuilder;
import com.wideplay.warp.internal.componentry.ComponentBuilders;
import com.wideplay.warp.module.ComponentRegistry;
import com.wideplay.warp.module.WarpConfigurationException;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.PageHandler;
import com.wideplay.warp.util.TextTools;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.XPP3Reader;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
class PageHandlerBuilder {
    private final ServletContext context;
    private final ComponentRegistry registry;

    private final Log log = LogFactory.getLog(PageHandlerBuilder.class);

    public PageHandlerBuilder(ServletContext context, ComponentRegistry registry) {
        this.context = context;
        this.registry = registry;
    }

    public void build(Class<?> pageClass, String packageName, Map<String, PageHandler> pages, Map<String, Object> pagesByTemplate) {
        //resolve template name from class name
        String template = pageClass.getName().substring(packageName.length() + 1);   //remove leading "."

        //use declared template name
        boolean isSearchForTemplate = true;
        if (pageClass.isAnnotationPresent(Template.class)) {
            template = pageClass.getAnnotation(Template.class).name();
            isSearchForTemplate = false;
        }

        String documentText = loadTemplateText(template, pageClass, isSearchForTemplate);

        //no template was found for the class, so skip it
        if (null == documentText)
            return;

        Document document;
        try {
            XPP3Reader reader = new XPP3Reader();
//            SAXReader reader = new SAXReader();
            document = reader.read(new StringReader(documentText));
        } catch (DocumentException e) {
            throw new WarpConfigurationException("could not parse xhtml template for: " + template + " because of " + e.getMessage(), e);
        } catch (IOException e) {
            throw new WarpConfigurationException("could not parse xhtml template for: " + template + " because of " + e.getMessage(), e);
        } catch (XmlPullParserException e) {
            throw new WarpConfigurationException("could not parse xhtml template for: " + template + " because of " + e.getMessage(), e);
        }

        //check for the explicit presence of a URI mapping or use the conventional name
        String[] uris = discoverUriMappings(pageClass, template);

        //store different instances of the pagehandler for the given URIs
        for (String uri : uris) {
            //check if this is a simple URI first
            if (isStaticUri(uri))
                pages.put(uri, new PageHandlerImpl(uri, new PageClassReflectionBuilder(pageClass).build(), buildComponentHandler(document)));
            else
                new UriMatchTreeBuilder().buildAndStore(uri,
                        new PageHandlerImpl(uri, new PageClassReflectionBuilder(pageClass).build(), buildComponentHandler(document)), 
                        pagesByTemplate);
        }
    }

    //a URI is not allowed to have both { } -- unless it is dynamic, i.e. a template
    private boolean isStaticUri(String uri) {
        return (!uri.contains("{")) && (!uri.contains("}"));
    }

    private String[] discoverUriMappings(Class<?> pageClass, String template) {
        String[] uris;
        if (pageClass.isAnnotationPresent(URIMapping.class)) {
            uris = pageClass.getAnnotation(URIMapping.class).value();

            //validate uris
            for (String uri : uris) {
                //validate template & static uris
                if (!TextTools.isValidURI(uri) && !TextTools.isValidTemplateUri(uri))
                    throw new WarpConfigurationException(pageClass.getName() + " specified an invalid URI mapping: " + uri);
            }
        }
        else
            uris = new String[] { String.format("/%s", template) };
        
        return uris;
    }


    private ComponentHandler buildComponentHandler(Document document) {
        return ComponentBuilders.buildComponentHandler(registry, document);
    }


    


    //smartly loads a template from the context filesystem
    private String loadTemplateText(String template, Class<?> pageClass, boolean isSearchForTemplate) {
        String documentText = null;


        //if template is specified literally, no need to search for it
        if (!isSearchForTemplate) {
            try {
                documentText = FileUtils.readFileToString(new File(context.getRealPath(template)), null);
            } catch (IOException e) {
                throw new WarpConfigurationException("could not find/read template for: " + template, e);
            }

            return documentText;
        }



        //otherwise, try to search & load either an .xhtml or .html file as a matching template
        try {
            documentText = FileUtils.readFileToString(new File(context.getRealPath(template + ".html")), null);
        } catch (FileNotFoundException fnfe) {
            //try the .html now
            try {
                documentText = FileUtils.readFileToString(new File(context.getRealPath(template + ".xhtml")), null);
            } catch (IOException e) {
                log.info(String.format("Class found that did not have a matching template (not an error or warning, just letting u know) : %s", pageClass.getName()));
            }

        } catch (IOException e) {
            throw new WarpConfigurationException("could not find/read template for: " + template, e);
        }

        return documentText;
    }
}
