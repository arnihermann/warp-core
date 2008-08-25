package com.wideplay.warp.widgets;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.util.ToStringBuilder;
import com.wideplay.warp.widgets.rendering.*;
import com.wideplay.warp.widgets.routing.PageBook;
import net.jcip.annotations.ThreadSafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@ThreadSafe @Singleton
class WidgetRegistry {
    public static final String TEXT_WIDGET = "__w:wRawText_Widget"; 

    private final Injector injector;
    private final Evaluator evaluator;
    private final PageBook pageBook;

    private final ConcurrentMap<String, WidgetWrapper> widgets = new ConcurrentHashMap<String, WidgetWrapper>();

    @Inject
    public WidgetRegistry(Evaluator evaluator, PageBook pageBook, Injector injector) {
        this.evaluator = evaluator;
        this.pageBook = pageBook;
        this.injector = injector;
    }

    public void add(String key, Class<? extends Renderable> widget) {
        widgets.put(key.toLowerCase().trim(), WidgetWrapper.forWidget(key, widget));
    }

    public boolean isSelfRendering(String widget) {
        WidgetWrapper wrapper = widgets.get(widget);

        if (null == wrapper)
            throw new NoSuchWidgetException("No widget found matching the name: @" + widget + " ; Did you forget to" +
                    " annotate your widget class with @Embed?");
        
        return wrapper.isSelfRendering();
    }

    public RepeatToken parseRepeat(String expression) {
        //parse and convert widget into metadata annotation
        final Map<String, String> bindMap = Parsing.toBindMap(expression);

        //noinspection OverlyComplexAnonymousInnerClass
        return new RepeatToken() {

            public String items() {
                return bindMap.get(RepeatToken.ITEMS);
            }

            public String var() {
                final String var = bindMap.get(RepeatToken.VAR);

                return null != var ? Parsing.stripQuotes(var) : null;
            }

            public String pageVar() {
                final String pageVar = bindMap.get(RepeatToken.PAGE_VAR);

                return null == pageVar ? RepeatToken.DEFAULT_PAGEVAR : pageVar;
            }
        };
    }


    public XmlWidget xmlWidget(WidgetChain childsChildren, String elementName, Map<String, String> attribs,
                                EvaluatorCompiler compiler) throws ExpressionCompileException {

        final XmlWidget widget = new XmlWidget(childsChildren, elementName, compiler, attribs);
        injector.injectMembers(widget);

        return widget;
    }

    public Renderable newWidget(String key, String expression, WidgetChain widgetChain, EvaluatorCompiler compiler)
            throws ExpressionCompileException {
        if (!widgets.containsKey(key))
            throw new NoSuchWidgetException("No such widget registered (did you add it correctly in module setup?): " + key);

        if (TEXT_WIDGET.equals(key))
            return new TextWidget(null, compiler);

        //otherwise construct via reflection (all widgets MUST have
        // a constructor with: widgetchain, expression, evaluator; in that order)
        final Renderable widget = widgets
                .get(key)
                .newWidget(widgetChain, expression, evaluator, pageBook);

        //add some injection (some widgets require it). It's a bit hacky, maybe we can reimplement some stuff later with @AssistedInject
        injector.injectMembers(widget);

        return widget;
    }

    private static class WidgetWrapper {
        private final Class<? extends Renderable> clazz;
        private final Constructor<? extends Renderable> constructor;
        private final String key;
        private final boolean selfRendering;
        private final WidgetKind kind;

        private WidgetWrapper(Class<? extends Renderable> clazz, Constructor<? extends Renderable> constructor,
                              WidgetKind kind, String key) {
            this.kind = kind;
            this.clazz = clazz;
            this.constructor = constructor;
            this.key = key;

            selfRendering = clazz.isAnnotationPresent(SelfRendering.class);
        }

        public Renderable newWidget(WidgetChain widgetChain, String expression, Evaluator evaluator, PageBook pageBook) {
            try {


            return WidgetKind.NORMAL.equals(kind) ?
                    constructor
                        .newInstance(widgetChain, expression, evaluator) :
                    constructor
                        .newInstance(toArguments(widgetChain), expression, evaluator, pageBook, key);

            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Malformed Widget (this should never happen): " + clazz);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException("Could not construct an instance of " + clazz, e);
            } catch (InstantiationException e) {
                throw new IllegalStateException("Could not construct an instance of : " + clazz, e);
            }
        }

        private static Map<String, ArgumentWidget> toArguments(WidgetChain widgetChain) {
            Set<ArgumentWidget> arguments = widgetChain.collect(ArgumentWidget.class);

            Map<String, ArgumentWidget> map = new HashMap<String, ArgumentWidget>();

            for (ArgumentWidget argument : arguments) {
                map.put(argument.getName(), argument);
            }

            return map;
        }

        public static WidgetWrapper forWidget(String key, Class<? extends Renderable> widgetClass) {
            WidgetKind kind = EmbedWidget.class.isAssignableFrom(widgetClass) ? WidgetKind.EMBED : WidgetKind.NORMAL;
            Constructor<? extends Renderable> constructor;

            try {
                switch (kind) {
                    case EMBED:
                        constructor = widgetClass
                                .getConstructor(Map.class, String.class, Evaluator.class, PageBook.class, String.class);
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

        @Override
        public String toString() {
            return new ToStringBuilder(WidgetWrapper.class)
                    .add("key", key)
                    .add("class", clazz)
                    .add("kind", kind)
                    .toString();
        }

        @SuppressWarnings({"InnerClassTooDeeplyNested"})
        private static enum WidgetKind { NORMAL, EMBED }
    }
}
