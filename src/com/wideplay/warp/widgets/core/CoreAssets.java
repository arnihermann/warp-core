package com.wideplay.warp.widgets.core;

import com.wideplay.warp.widgets.resources.Assets;
import com.wideplay.warp.widgets.resources.Export;

/**
 * Dummy class used to export assets.
 *
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@Assets({
    @Export(at="/assets/ll.gif", resource = "ll.gif"),
    @Export(at="/assets/lr.gif", resource = "lr.gif"),
    @Export(at="/assets/ul.gif", resource = "ul.gif"),
    @Export(at="/assets/ur.gif", resource = "ur.gif")
})
class CoreAssets {
}
