package com.wideplay.warp.widgets;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.easymock.EasyMock.*;
import com.wideplay.warp.widgets.routing.PageBook;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class WidgetRegistryTest {
    private static final String WIDGETS_AND_KEYS = "widgetsAndKeys";

    @DataProvider(name = WIDGETS_AND_KEYS)
    public Object[][] get() {
        return new Object[][] {
            { "twidg", TextFieldWidget.class },
            { "teasdasxt", RepeatWidget.class },
            { "sastext", ShowIfWidget.class },
        };
    }

    @Test(dataProvider = WIDGETS_AND_KEYS)
    public final void storeRetrieveWidgets(final String key, final Class<RenderableWidget> expected) {
        final WidgetRegistry registry = new WidgetRegistry(new MvelEvaluator(), createNiceMock(PageBook.class));
        registry.add(key, expected);

        RenderableWidget widget = registry.newWidget(key, "some.expression", new WidgetChain());

        assert expected.isInstance(widget) : "Wrong widget returned";
    }
}
