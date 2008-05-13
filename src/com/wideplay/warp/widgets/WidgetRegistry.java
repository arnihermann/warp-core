package com.wideplay.warp.widgets;

import com.google.inject.Singleton;
import com.wideplay.warp.widgets.rendering.SelfRendering;
import com.wideplay.warp.widgets.routing.PageBook;
import net.jcip.annotations.ThreadSafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@ThreadSafe @Singleton
class WidgetRegistry {
    public static final String TEXT_WIDGET = "__w:wRawText_Widget"; 

    private final Evaluator evaluator;
    private final PageBook pageBook;
    private final ConcurrentMap<String, WidgetWrapper> widgets = new ConcurrentHashMap<String, WidgetWrapper>();

    public WidgetRegistry(Evaluator evaluator, PageBook pageBook) {
        this.evaluator = evaluator;
        this.pageBook = pageBook;
    }

    public void add(String key, Class<? extends RenderableWidget> widget) {
        widgets.put(key.toLowerCase().trim(), WidgetWrapper.forWidget(key, widget));
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
                .newWidget(widgetChain, expression, evaluator, pageBook);
    }

    private static class WidgetWrapper {
        private final Class<? extends RenderableWidget> clazz;
        private final Constructor<? extends RenderableWidget> constructor;
        private final String key;
        private final boolean selfRendering;
        private final WidgetKind kind;

        private WidgetWrapper(Class<? extends RenderableWidget> clazz, Constructor<? extends RenderableWidget> constructor,
                              WidgetKind kind, String key) {
            this.kind = kind;
            this.clazz = clazz;
            this.constructor = constructor;
            this.key = key;

            selfRendering = clazz.isAnnotationPresent(SelfRendering.class);
        }

        public RenderableWidget newWidget(WidgetChain widgetChain, String expression, Evaluator evaluator, PageBook pageBook) {
            try {


            return WidgetKind.NORMAL.equals(kind) ?
                    constructor
                        .newInstance(widgetChain, expression, evaluator) :
                    constructor
                        .newInstance(expression, evaluator, pageBook, key);

        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Malformed Widget (this should never happen): " + clazz);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("Could not construct an instance of " + clazz, e);
        } catch (InstantiationException e) {
            throw new IllegalStateException("Could not construct an instance of : " + clazz, e);
        }
        }

        public static WidgetWrapper forWidget(String key, Class<? extends RenderableWidget> widgetClass) {
            WidgetKind kind = EmbedWidget.class.isAssignableFrom(widgetClass) ? WidgetKind.EMBED : WidgetKind.NORMAL;
            Constructor<? extends RenderableWidget> constructor;

            try {
                switch (kind) {
                    case EMBED:
                        constructor = widgetClass
                                .getConstructor(String.class, Evaluator.class, PageBook.class, String.class);
                        break;
                    default:    //NORMAL
                        constructor = widgetClass
                                .getConstructor(WidgetChain.class, String.class, Evaluator.class);
                }
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("Malformed Widget (this should never happen): " + widgetClass);
            }


            //ugh...
            if (!constructor.isAccessible())
                constructor.setAccessible(true);
            
            return new WidgetWrapper(widgetClass, constructor, kind, key);
        }

        public boolean isSelfRendering() {
            return selfRendering;
        }

        @SuppressWarnings({"InnerClassTooDeeplyNested"})
        private static enum WidgetKind { NORMAL, EMBED }
    }
}
