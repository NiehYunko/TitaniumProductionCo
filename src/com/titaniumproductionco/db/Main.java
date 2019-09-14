package com.titaniumproductionco.db;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

import com.titaniumproductionco.db.services.DBService;
import com.titaniumproductionco.db.ui.LoginFrame;

/**
 * Driver of the application
 *
 */
public class Main {
    public static void main(String[] args) {
        try {

            if (DBService.connect("TiProductionUser", "titANIUMisawe5ome1")) {
                SwingUtilities.invokeLater(() -> {

                    FontUIResource font = new FontUIResource(new Font("Dialog", Font.PLAIN, 16));

                    Enumeration<?> keys = UIManager.getDefaults().keys();
                    while (keys.hasMoreElements()) {
                        Object key = keys.nextElement();
                        Object value = UIManager.get(key);
                        if (value instanceof FontUIResource)
                            UIManager.put(key, font);
                    }

                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                        e.printStackTrace();
                        System.err.println("Unable to set Look and Feel");
                    }

                    LoginFrame f = new LoginFrame();
                    f.display();
                });
            } else {
                JOptionPane.showMessageDialog(null, "Error connecting to the database");

            }
        } catch (Throwable e) {

            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            // System.exit(1);
        }

    }

}
