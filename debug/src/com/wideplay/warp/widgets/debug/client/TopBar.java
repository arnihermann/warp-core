package com.wideplay.warp.widgets.debug.client;

import com.google.gwt.user.client.ui.*;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public class TopBar {
    public static Widget newPanel() {
        final TabBar bar = new TabBar();

        //start out hiding all tabs
        hideAll();


        bar.addTab(roundedButton("Errors"));
        bar.addTab(roundedButton("Warnings"));
        bar.addTab(roundedButton("Request"));
        bar.addTab(roundedButton("Response"));

        bar.selectTab(0);
        selectTab(0);

        //noinspection OverlyComplexAnonymousInnerClass
        bar.addTabListener(new TabListener() {
            public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
                return true;
            }

            public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
                hideAll();
                selectTab(tabIndex);
            }
        });

        return bar;
    }

    private static void selectTab(int i) {
        Panel selected;
        switch (i) {
            default:
            case 0:
                selected = RootPanel.get("errors");
                break;
            case 1:
                selected = RootPanel.get("warnings");
                break;
            case 2:
                selected = RootPanel.get("warnings");
                break;
            case 3:
                selected = RootPanel.get("warnings");
                break;
        }

        selected.setVisible(true);
    }

    private static Panel roundedButton(String name) {
        Panel button = new FlowPanel();
        button.setStyleName("tab-background");

        Panel roundedUl = new FlowPanel();
        roundedUl.setStyleName("rounded_ul");
        button.add(roundedUl);

        Panel roundedUr = new FlowPanel();
        roundedUr.setStyleName("rounded_ur");
        button.add(roundedUr);

        Panel tabButton = new FlowPanel();
        tabButton.setStyleName("tab-button");
        tabButton.add(new Label(name));

        button.add(tabButton);

        return button;
    }

    private static void hideAll() {
        RootPanel.get("errors").setVisible(false);
        RootPanel.get("warnings").setVisible(false);
        RootPanel.get("errors").setVisible(false);
        RootPanel.get("errors").setVisible(false);
    }
}
