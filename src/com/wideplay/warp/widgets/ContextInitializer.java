package com.wideplay.warp.widgets;

import net.jcip.annotations.NotThreadSafe;

import javax.servlet.ServletContext;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@NotThreadSafe
class ContextInitializer {
    private final PageWidgetBuilder builder;
    private final WidgetRegistry registry;

    ContextInitializer(PageWidgetBuilder builder, WidgetRegistry registry) {
        this.builder = builder;
        this.registry = registry;
    }

    public void init(ServletContext servletContext) {
//        builder.scan();

        //register core widgets
        registry.add("textfield", TextFieldWidget.class);
        registry.add("repeat", RepeatWidget.class);
        registry.add("showif", ShowIfWidget.class);
        registry.add("meta", HeaderWidget.class);
        registry.add("choose", ChooseWidget.class);
    }
}
