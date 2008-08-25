package com.wideplay.warp.widgets.debug.client;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public class PageTools {

    static Widget newPanel(final int i) {

        //setup dialog links
        Panel panel = new HorizontalPanel();

        //page hyperlink
        final Hyperlink pageLink = new Hyperlink("Page", History.getToken());
        pageLink.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                new PageInfoDialog(RootPanel.get("page-tools-" + i + "-page")).show();
            }
        });
        panel.add(pageLink);

        panel.add(separator());

        //page hyperlink
        final Hyperlink templateLink = new Hyperlink("Template", History.getToken());
        final RootPanel content = RootPanel.get("page-tools-" + i + "-template");
        content.setVisible(false);
        templateLink.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                final PageInfoDialog dialog = new PageInfoDialog(content);
                dialog.show();
            }
        });
        panel.add(templateLink);

        panel.add(separator());


        //page hyperlink
        final Hyperlink assetsLink = new Hyperlink("Assets", History.getToken());
        assetsLink.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                new PageInfoDialog(RootPanel.get("page-tools-" + i + "-page")).show();
            }
        });
        panel.add(assetsLink);


        return panel;
    }

    private static Widget separator() {
        return new HTML("&nbsp;|&nbsp;");
    }

    private static class PageInfoDialog extends DialogBox {

        public PageInfoDialog(Panel content) {
            //non-hiding, modal
            super(false, true);

            // Set the dialog box's caption.
            setText("Page Object");
            setSize("700", "500");

            //set content loaded from page
            Panel panel = new FlowPanel();
            panel.add(content);

            //ok button
            Button ok = new Button("OK");
            ok.addClickListener(new ClickListener() {
                public void onClick(Widget sender) {
                    PageInfoDialog.this.hide();
                }
            });
            panel.add(ok);
            
            setWidget(panel);

            center();
        }
    }
}
