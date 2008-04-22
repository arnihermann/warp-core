package com.wideplay.warp.widgets;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;

import com.google.inject.Singleton;
import com.wideplay.warp.widgets.rendering.SelfRendering;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@ThreadSafe @Singleton
class WidgetRegistry {
    public static final String TEXT_WIDGET = "__w:wRawText_Widget"; 

    private final Evaluator evaluator;
    private final ConcurrentMap<String, WidgetWrapper> widgets = new ConcurrentHashMap<String, WidgetWrapper>();

    public WidgetRegistry(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public void add(String key, Class<? extends RenderableWidget> widget) {
        widgets.put(key, WidgetWrapper.forWidget(widget));
    }

    public boolean isSelfRendering(String widget) {
        return widgets.get(widget).isSelfRendering();
    }

    public RenderableWidget newWidget(String key, String expression, WidgetChain widgetChain) {
        if (!widgets.containsKey(key))
            throw new NoSuchWidgetException("No such widget registered (did you add it correctly in module setup?): " + key);

        if (TEXT_WIDGET.equals(key))
            return new TextWidget(null, evaluator);

        //otherwise construct via reflection (all widgets MUST have
        // a constructor with: widgetchain, expression, evaluator; in that order)
        return widgets
                .get(key)
                .newWidget(widgetChain, expression, evaluator);
    }

    private static class WidgetWrapper {
        private final Class<? extends RenderableWidget> clazz;
        private final Constructor<? extends RenderableWidget> constructor;
        private final boolean selfRendering;

        private WidgetWrapper(Class<? extends RenderableWidget> clazz, Constructor<? extends RenderableWidget> constructor) {
            this.clazz = clazz;
            this.constructor = constructor;

            selfRendering = clazz.isAnnotationPresent(SelfRendering.class);
        }

        public RenderableWidget newWidget(WidgetChain widgetChain, String expression, Evaluator evaluator) {
            try {


            return constructor
                        .newInstance(widgetChain, expression, evaluator);

        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Malformed Widget (this should never happen): " + clazz);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("Could not construct an instance of " + clazz, e);
        } catch (InstantiationException e) {
            throw new IllegalStateException("Could not construct an instance of : " + clazz, e);
        }
        }

        public static WidgetWrapper forWidget(Class<? extends RenderableWidget> widgetClass) {
            //TODO validate & store variable ctors here as necessary
            Constructor<? extends RenderableWidget> constructor;

            try {
                constructor = widgetClass
                        .getConstructor(WidgetChain.class, String.class, Evaluator.class);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("Malformed Widget (this should never happen): " + widgetClass);
            }


            //ugh...
            if (!constructor.isAccessible())
                constructor.setAccessible(true);
            
            return new WidgetWrapper(widgetClass, constructor);
        }

        public boolean isSelfRendering() {
            return selfRendering;
        }
    }
}
