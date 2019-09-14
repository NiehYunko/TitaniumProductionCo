package com.titaniumproductionco.db.ui.component;

import javax.swing.JMenu;

import com.titaniumproductionco.db.ui.UIFrame;

@SuppressWarnings("serial")
public abstract class UIMenu extends JMenu {
    protected UIFrame ui;

    public UIMenu(String title, UIFrame ui) {
        super(title);
        this.ui = ui;
        init();
    }

    protected abstract void init();
}
