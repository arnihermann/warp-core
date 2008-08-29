package com.wideplay.warp.widgets;

import com.google.inject.matcher.Matchers;
import com.wideplay.warp.widgets.core.CaseWidget;
import com.wideplay.warp.widgets.core.Repeat;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class PackagesTest {

////    @Test
//    public final void listClassesInAPackage() throws IOException, ClassNotFoundException {
//        Set<Class<?>> inPackage = Classes.matching(Matchers.any())
//                                         .in(Widgets.class.getPackage());
//
//        //make sure we are scanning the hierarchy and picking up expected classes
//        assert inPackage.contains(Widgets.class);
//        assert inPackage.contains(EvaluatorCompiler.class);
//        assert inPackage.contains(RequestBinder.class);
//        assert inPackage.contains(ResourcesService.class);
//        assert inPackage.contains(Evaluator.class);
//        assert inPackage.contains(PageBook.class);
//        assert inPackage.contains(RoutingDispatcher.class);
//    }
//

    @Test
    public final void listClassesInJarPackage() {
        final Set<Class<?>> set = Classes.matching(Matchers.any())
                .in(CaseWidget.class.getPackage());

        assert set.contains(CaseWidget.class);
        assert set.contains(Repeat.class);
//        URLClassLoader
    }
}
