package com.wideplay.warp.widgets;

/**
 * Part of the EDSL for configuring warp-widgets
 */
class Options implements Widgets.OptionsBuilder {
    private boolean contextualizeUris;
    private boolean trimTemplateText;
    private boolean ignoreComments;
    private boolean noDebugPage;
    private boolean elevateWarnings;

    public boolean isContextualizeUris() {
        return contextualizeUris;
    }

    public boolean isTrimTemplateText() {
        return trimTemplateText;
    }

    public boolean isIgnoreComments() {
        return ignoreComments;
    }

    public boolean isNoDebugPage() {
        return noDebugPage;
    }

    public boolean isElevateWarnings() {
        return elevateWarnings;
    }

    public Widgets.OptionsBuilder contextualizeUris() {
        this.contextualizeUris = true;
        return this;
    }

    public Widgets.OptionsBuilder trimTemplateText() {
        this.trimTemplateText = true;
        return this;
    }

    public Widgets.OptionsBuilder elevateWarnings() {
        this.elevateWarnings = true;
        return this;
    }

    public Widgets.OptionsBuilder ignoreComments() {
        this.ignoreComments = true;
        return this;
    }

    public Widgets.OptionsBuilder noDebugPage() {
        this.noDebugPage = true;
        return this;
    }

    public Options build() {
        return this;
    }
}
