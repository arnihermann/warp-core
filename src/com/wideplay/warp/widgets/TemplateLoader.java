package com.wideplay.warp.widgets;

import net.jcip.annotations.Immutable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.io.FileUtils;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Immutable
class TemplateLoader {
    public String load(Class<?> pageClass) {
        Show show = pageClass.getAnnotation(Show.class);
        String template;

        //annotation not present, resolve by name
        if (null == show) {
            template = resolve(pageClass);
        } else
            template = show.value();

        String text;
        try {
            final InputStream stream = pageClass.getResourceAsStream(template);

            //look on the webapp resource path if not in classpath
            if (null == stream) {
                throw new MissingTemplateException("Could not find template for: " + pageClass);
            }

            text = read(stream);
        } catch (IOException e) {
            throw new TemplateLoadingException("Could not load template for (i/o error): " + pageClass, e);
        }

        return text;
    }

    //resolves a location for this page class's template (assuming @Show is not present)
    private String resolve(Class<?> pageClass) {
        String name = String.format("%s.html", pageClass.getSimpleName());

        URL resource = pageClass.getResource(name);

        if (null == resource) {
            name = String.format("%s.xhtml", pageClass.getSimpleName());
            resource = pageClass.getResource(name);
        } else
            return name;

        if (null == resource) {
            name = String.format("%s.xml", pageClass.getSimpleName());
            resource = pageClass.getResource(name);
        } else
            return name;

        if (null == resource)
            throw new TemplateLoadingException(String.format("Could not find a suitable template for %s, did you remember to place " +
                    "an @Show? None of [%s.html, %s.xhtml or %s.xml] could be found in either " +
                    "package [%s] OR in the root of the resource dir.", pageClass.getName(), pageClass.getSimpleName(),
                    pageClass.getSimpleName(), pageClass.getSimpleName(), pageClass.getPackage().getName()));

        return name;
    }

    private static String read(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        StringBuilder builder = new StringBuilder();
        try {
            while (reader.ready()) {
                builder.append(reader.readLine());
            }
        } finally {
            stream.close();
        }

        return builder.toString();
    }
}
