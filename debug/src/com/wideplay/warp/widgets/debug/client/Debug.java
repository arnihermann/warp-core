package com.wideplay.warp.widgets.debug.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public class Debug implements EntryPoint {

    public void onModuleLoad() {
        
        //loop thru all the page-tools and create popup dialogs for them
        boolean morePageTools = true;
        for(int i = 1; morePageTools; i++) {
            final RootPanel panel = RootPanel.get("page-tools-" + i);
            if (null != panel)
                panel.add(PageTools.newPanel(i));
            else
                morePageTools = false;
        }

        RootPanel.get("top-bar").add(TopBar.newPanel());
    }


}
