package com.titaniumproductionco.db.ui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.beans.PropertyVetoException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;

public class InternalFrameFactory implements IInternalFrameFactory {
    @Override
    public JInternalFrame createInternalFrame(JComponent component, String title, Image icon, UIFrame ui) {
        JInternalFrame frame = new JInternalFrame();
        frame.setTitle(title);
        frame.setLayout(new BorderLayout());
        frame.add(component);
        frame.setMaximizable(true);
        frame.setIconifiable(true);
        frame.setClosable(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setFrameIcon(new ImageIcon(icon));

        try {
            frame.setSelected(true);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        frame.setPreferredSize(component.getPreferredSize());
        frame.pack();
        return frame;
    }

}
