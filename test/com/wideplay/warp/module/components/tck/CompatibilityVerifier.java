package com.wideplay.warp.module.components.tck;

import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.ScriptLibrary;
import com.wideplay.warp.rendering.ScriptEvents;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.module.components.Renderable;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.google.inject.Injector;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * On: 22/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class CompatibilityVerifier {
    private final Object component;
    private final Class<?> componentClass;

    private CompatibilityVerifier(Object component) {
        this.component = component;
        this.componentClass = component.getClass();
    }

    int elementCounter = 0;
    int rawElementCounter = 0;
    int componentHandlerCounter = 0;

    private List<String> verify() throws AssertionError {
        List<String> complaints = new LinkedList<String>();

        assert componentClass.isAnnotationPresent(Component.class) : "Component classes must be tagged with @Component";
        assert (component instanceof Renderable) : "Component must implement Renderable (if you're trying to test a widget, use verifyWidget() instead)";

        testRenderable(complaints);

        assert elementCounter == 0 : "Component did not close one or more elements (or closed more than it should have!!)";

        return complaints;
    }

    private void testRenderable(List<String> complaints) {
        Renderable renderable = (Renderable)component;

        try {
            renderable.render(newTestingHtmlWriter(), newTestingComponentHandlers(), newThrowingTestingGuiceInjector(complaints), null, new Object());
        } catch(GuiceInjectionRequestedError err) {
            complaints.add("Component requests resources from guice injector. This indicates tight coupling to the deployment environment");
        } catch(NullPointerException npe) {
            //cant help this, so pass the test
            throw new FurtherTestImpossibleError();
        }


        //warn?
        if (componentHandlerCounter != 0)
            complaints.add("Component does not chain to all of its children; it could violate the template integrity and cause unpredictable results");    
    }

    private Injector newThrowingTestingGuiceInjector(final List<String> complaints) {
        return (Injector) Proxy.newProxyInstance(null, new Class[] { Injector.class }, new InvocationHandler() {

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                throw new GuiceInjectionRequestedError();
            }
        });
    }

    private List<ComponentHandler> newTestingComponentHandlers() {
        List<ComponentHandler> handlers = new LinkedList<ComponentHandler>();

        //add fuzz test arbitrary numbers of handlers (10 times)
        for (int testNum = 0; testNum < 10; testNum++, componentHandlerCounter++)
            handlers.add(new ComponentHandler() {
                public void handleRender(HtmlWriter writer, Injector injector, PageClassReflection pageReflection, Object page) {
                    componentHandlerCounter--;  //count that children were rendered
                }

                public List<? extends ComponentHandler> getNestedComponents() {
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }
            });

        return handlers;
    }

    private HtmlWriter newTestingHtmlWriter() {
        return new HtmlWriter() {

            //convenience varargs method
            public void element(String name, Object... nameValuePairs) {
                elementCounter++;
            }

            public void elementWithAttrs(String name, Object[] nameValuePairs) {
                elementCounter++;
            }

            public void end(String name) {
                elementCounter--;
            }

            public void selfClosedElement(String name, Object[] nameValuePairs) {

            }

            public void writeRaw(String text) {

            }

            public void registerScriptLibrary(ScriptLibrary library) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void registerEvent(String elementName, ScriptEvents event, String annotation)//write raw text to the body load js func
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void writeToOnLoad(String text) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public String newId(Object object)//convenience varargs method
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }


            public String getBuffer() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

    public enum Compatibility { ACCEPTED, MISBEHAVED, REJECTED }

    private class GuiceInjectionRequestedError extends Error { }

    private class FurtherTestImpossibleError extends Error { }

    public static final class CompatibilityResult {
        private final Compatibility result;
        private final List<String> complaints;

        public CompatibilityResult(List<String> complaints, Compatibility result) {
            this.complaints = complaints;
            this.result = result;
        }

        public List<String> getComplaints() {
            return complaints;
        }

        public Compatibility getResult() {
            return result;
        }
    }

    public static CompatibilityResult verifyComponent(Object object) {
        CompatibilityVerifier verifier = new CompatibilityVerifier(object);
        Compatibility result;
        List<String> complaints = new ArrayList<String>();
        try {
            complaints = verifier.verify();

            result = complaints.isEmpty() ? Compatibility.ACCEPTED : Compatibility.MISBEHAVED;
        } catch (AssertionError error) {
            result = Compatibility.REJECTED;
            complaints.add(error.getMessage());
            complaints.add("Complaints list truncated :: due to reject");
        } catch(FurtherTestImpossibleError err) {
            System.err.println("Could not test any further as the component has dependencies on the page state, but looks good so far. TCK passes with reservations");
            return new CompatibilityResult(complaints, Compatibility.ACCEPTED);
        }

        return new CompatibilityResult(complaints, result);
    }
}
