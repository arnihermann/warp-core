package com.wideplay.warp.util;

import static com.google.inject.matcher.Matchers.any;
import com.wideplay.warp.widgets.Evaluator;
import com.wideplay.warp.widgets.SwitchKind;
import com.wideplay.warp.widgets.Widgets;
import com.wideplay.warp.widgets.binding.RequestBinder;
import com.wideplay.warp.widgets.rendering.EvaluatorCompiler;
import com.wideplay.warp.widgets.resources.ResourcesService;
import com.wideplay.warp.widgets.routing.PageBook;
import com.wideplay.warp.widgets.routing.RoutingDispatcher;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class PackagesTest {

    @Test
    public final void listClassesInAPackage() throws IOException, ClassNotFoundException {
        Set<Class<?>> inPackage = Classes.matching(any())
                                         .in(Widgets.class.getPackage());

        //make sure we are scanning the hierarchy and picking up expected classes
        assert inPackage.contains(Widgets.class);
        assert inPackage.contains(EvaluatorCompiler.class);
        assert inPackage.contains(RequestBinder.class);
        assert inPackage.contains(ResourcesService.class);
        assert inPackage.contains(Evaluator.class);
        assert inPackage.contains(SwitchKind.class);
        assert inPackage.contains(PageBook.class);
        assert inPackage.contains(RoutingDispatcher.class);
    }
}
