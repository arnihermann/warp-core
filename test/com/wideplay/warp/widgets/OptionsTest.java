package com.wideplay.warp.widgets;

import org.testng.annotations.Test;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public class OptionsTest {

    @Test
    public final void setOptionsUsingBuilder() {
        final Options options = Widgets.options()
                .contextualizeUris()
                .elevateWarnings()
                .ignoreComments()
                .noDebugPage()
                .trimTemplateText()

                .build();


        assert options.isIgnoreComments();
        assert options.isContextualizeUris();
        assert options.isElevateWarnings();
        assert options.isNoDebugPage();
        assert options.isTrimTemplateText();

    }

    @Test
    public final void setDefaultOptions() {
        final Options options = Widgets.options()
                .build();


        assert !options.isIgnoreComments();
        assert !options.isContextualizeUris();
        assert !options.isElevateWarnings();
        assert !options.isNoDebugPage();
        assert !options.isTrimTemplateText();

    }
}
