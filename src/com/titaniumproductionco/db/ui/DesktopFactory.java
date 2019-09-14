package com.titaniumproductionco.db.ui;

import java.awt.Color;

import javax.swing.JDesktopPane;

public class DesktopFactory implements IDesktopFactory {

    @Override
    public JDesktopPane createDesktopFor(UIFrame ui) {
        JDesktopPane desktop = new JDesktopPane();
        desktop.setBackground(new Color(52, 72, 105));

        return desktop;
    }

}
