package com.wideplay.warp.example;

import com.wideplay.warp.annotations.Managed;

/**
 * Created with IntelliJ IDEA.
 * On: 25/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class FormsDemo {
    @Managed private String sampleText = "default sample text";
    private String selected = "none";
    private String[] items = new String[] { "item1", "item2", "item3", "item4",};
    private boolean checkedValue1 = false;
    private boolean checkedValue2 = true;

    public boolean isCheckedValue1() {
        return checkedValue1;
    }

    public void setCheckedValue1(boolean checkedValue1) {
        this.checkedValue1 = checkedValue1;
    }

    public boolean isCheckedValue2() {
        return checkedValue2;
    }

    public void setCheckedValue2(boolean checkedValue2) {
        this.checkedValue2 = checkedValue2;
    }

    public String getSampleText() {
        return sampleText;
    }

    public void setSampleText(String sampleText) {
        this.sampleText = sampleText;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }


    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
