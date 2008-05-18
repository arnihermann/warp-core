package com.wideplay.warp.widgets.resources;

import com.wideplay.warp.widgets.Respond;
import com.google.inject.Singleton;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import net.jcip.annotations.ThreadSafe;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@ThreadSafe @Singleton
class ClasspathResourcesService implements ResourcesService {
    private final Map<String, Resource> resources = new ConcurrentHashMap<String, Resource>();

    public void add(Class<?> clazz, Export export) {
        resources.put(export.at(), new Resource(export, clazz));
    }

    public Respond serve(String uri) {
        final Resource resource = resources.get(uri);

        //nothing registered
        if (null == resource) {
            return null;
        }

        //load and render resource to responder
        return new StaticResourceRespond(resource);
    }

    private static class Resource {
        private final Export export;
        private final Class<?> clazz;

        private Resource(Export export, Class<?> clazz) {
            this.export = export;
            this.clazz = clazz;
        }


        public String toString() {
            return new StringBuilder()
                    .append("Resource {")
                    .append("export=")
                    .append(export)
                    .append(", class=")
                    .append(clazz).append('}')
                    
                    .toString();
        }
    }

    private static class StaticResourceRespond implements Respond {
        private final Resource resource;

        public StaticResourceRespond(Resource resource) {
            this.resource = resource;
        }

        @Override
        public String toString() {
            //load and render
            List list;
            try {
                final InputStream stream = resource.clazz.getResourceAsStream(resource.export.resource());

                if (null == stream)
                    throw new ResourceLoadingException("Couldn't find static resource (did you spell it right?) specified by: "
                            + resource);

                list = IOUtils.readLines(stream);
            } catch (IOException e) {
                throw new ResourceLoadingException("Error loading static resource specified by: " + resource, e);
            }

            StringBuilder buffer = new StringBuilder();
            for (Object o : list) {
                buffer.append((String)o);
            }

            return buffer.toString();
        }

        public void write(String text) {
            throw new UnsupportedOperationException("Static resource responders can't be written to");
        }

        public HtmlTagBuilder withHtml() {
            throw new UnsupportedOperationException("Static resource responders can't be written to");
        }

        public void write(char c) {
            throw new UnsupportedOperationException("Static resource responders can't be written to");
        }

        public void chew() {
            throw new UnsupportedOperationException("Static resource responders can't be written to");
        }

        public void writeToHead(String text) {
            throw new UnsupportedOperationException("Static resource responders can't be written to");
        }

        public void require(String requireString) {
            throw new UnsupportedOperationException("Static resource responders can't be written to");
        }

        public void redirect(String to) {
            throw new UnsupportedOperationException("Static resource responders can't be written to");
        }

        public String getRedirect() {
            return null;
        }
    }
}
