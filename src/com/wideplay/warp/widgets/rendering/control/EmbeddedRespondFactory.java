package com.wideplay.warp.widgets.rendering.control;

import com.google.inject.Inject;
import com.wideplay.warp.widgets.Respond;
import net.jcip.annotations.Immutable;

import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@Immutable
class EmbeddedRespondFactory {
    private final Respond respond;

    @Inject
    public EmbeddedRespondFactory(Respond respond) {
        this.respond = respond;
    }

    public EmbeddedRespond get(Map<String, ArgumentWidget> arguments) {
        return new EmbeddedRespond(arguments, respond);
    }
}
