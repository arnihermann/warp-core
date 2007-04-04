package com.wideplay.warp.internal.pages;

import com.wideplay.warp.rendering.PageHandler;
import com.wideplay.warp.rendering.HtmlWriter;

import javax.servlet.ServletContext;
import java.util.Map;
import java.util.MissingResourceException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;

/**
 * Created with IntelliJ IDEA.
 * On: Mar 24, 2007 10:38:12 PM
 *
 * @author Dhanji R. Prasanna
 */
public class PageBuilders {

    private PageBuilders() {
    }

    public static void buildAndStorePageHandlers(ServletContext context, Class<?> pageClass, String packageName, Map<String, PageHandler> pages) {
        new PageHandlerBuilder(context).build(pageClass, packageName, pages);

        //load resources
        JsSupportUtils.loadResources();
    }

    public static String loadResource(Class<?> forClass, String name) {
        try {
            return FileUtils.readFileToString(new File(forClass.getResource(name).toURI()), null);
        } catch (IOException e) {
            throw new MissingResourceException("Missing javascript resources required by Warp (are you copying .js files from src to build?)", forClass.getName(), name);
        } catch (URISyntaxException e) {
            throw new MissingResourceException("Missing javascript resources required by Warp (are you copying .js files from src to build?)", forClass.getName(), name);
        } catch (NullPointerException e) {
            throw new MissingResourceException("Missing javascript resources required by Warp (are you copying .js files from src to build?)", forClass.getName(), name);            
        }
    }
}
