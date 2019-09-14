package com.titaniumproductionco.db.ui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import com.titaniumproductionco.db.services.DBService;
import com.titaniumproductionco.db.services.Role;

/**
 * Main Frame of the application
 *
 */
@SuppressWarnings("serial")
public class UIFrame extends JFrame implements WindowListener {
    private IDesktopFactory desktopFactory;
    private IInternalFrameFactory internalFrameFactory;
    private IMenuBarFactory menuBarFactory;

    private JDesktopPane desktop;

    public void init(Role r) {
        this.setJMenuBar(menuBarFactory.createMenuBarFor(r, this));
        desktop = desktopFactory.createDesktopFor(this);

        this.add(desktop);
        this.setPreferredSize(new Dimension(1200, 900));

        this.pack();
        this.setTitle("Titanium Production Co. - " + r.NAME);

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.addWindowListener(this);
        this.setIconImage(UIImage.PROGRAM_ICON);

        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    public void setDesktopFactory(IDesktopFactory factory) {
        if (desktopFactory != null)
            throw new IllegalStateException("Desktop Factory is already set!");
        desktopFactory = factory;
    }

    public void setInternalFrameFactory(IInternalFrameFactory factory) {
        if (internalFrameFactory != null)
            throw new IllegalStateException("Internal Frame Factory is already set!");
        internalFrameFactory = factory;
    }

    public void setMenuBarFactory(IMenuBarFactory factory) {
        if (menuBarFactory != null)
            throw new IllegalStateException("Desktop Factory is already set!");
        menuBarFactory = factory;
    }

    public void openComponent(JComponent component, String title, Image icon) {
        JInternalFrame f = internalFrameFactory.createInternalFrame(component, title, icon, this);
        desktop.add(f);
        f.moveToFront();
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        exitWithConfirmation();
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
    }

    public void exitWithConfirmation() {
        int op = JOptionPane.showConfirmDialog(this, "Are you sure to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            DBService.close();
            this.dispose();
        }
    }
}
