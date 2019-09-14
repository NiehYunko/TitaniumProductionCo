package com.titaniumproductionco.db.ui;

import java.awt.Image;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;

public interface IInternalFrameFactory {
    JInternalFrame createInternalFrame(JComponent component, String title, Image icon, UIFrame ui);
}
