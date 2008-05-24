package com.wideplay.warp.widgets;

import net.jcip.annotations.NotThreadSafe;

import javax.servlet.ServletContext;

import com.google.inject.Inject;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@NotThreadSafe
class ContextInitializer {
    private final PageWidgetBuilder builder;
    private final WidgetRegistry registry;

    @Inject
    public ContextInitializer(PageWidgetBuilder builder, WidgetRegistry registry) {
        this.builder = builder;
        this.registry = registry;
    }

    public void init(ServletContext servletContext) {

        //register core widgets
        registry.add("textfield", TextFieldWidget.class);
        registry.add("repeat", RepeatWidget.class);
        registry.add("showif", ShowIfWidget.class);
        registry.add("meta", HeaderWidget.class);
        registry.add("choose", ChooseWidget.class);
        registry.add("include", IncludeWidget.class);

        //scan and add pages/widgets
        builder.scan();

    }
}
