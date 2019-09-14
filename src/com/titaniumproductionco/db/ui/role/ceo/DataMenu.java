package com.titaniumproductionco.db.ui.role.ceo;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import com.titaniumproductionco.db.services.ImportService;
import com.titaniumproductionco.db.ui.UIFrame;
import com.titaniumproductionco.db.ui.UIImage;
import com.titaniumproductionco.db.ui.component.UIMenu;

@SuppressWarnings("serial")
public class DataMenu extends UIMenu {

    public DataMenu(UIFrame ui) {
        super("[Data]", ui);
    }

    @Override
    protected void init() {
        JMenuItem analyzeMine = new JMenuItem("Analyze Mine");
        analyzeMine.addActionListener((e) -> {
            ui.openComponent(new MineAnalysisPanel(), "Analyze Mines", UIImage.ANALYZE_FILE);
        });
        analyzeMine.setIcon(new ImageIcon(UIImage.ANALYZE_FILE));
        add(analyzeMine);
        JMenuItem analyzeOreUsage = new JMenuItem("Analyze Ore Usage");
        analyzeOreUsage.addActionListener((e) -> {
            ui.openComponent(new OreUsageAnalysisPanel(), "Analyze Ore Usage", UIImage.ANALYZE_FILE);
        });
        analyzeOreUsage.setIcon(new ImageIcon(UIImage.ANALYZE_FILE));
        add(analyzeOreUsage);
        JMenuItem analyzeMach = new JMenuItem("Analyze Machine");
        analyzeMach.addActionListener((e) -> {
            ui.openComponent(new MachineEffAnalysisPanel(), "Analyze Machine Efficiency", UIImage.ANALYZE_FILE);
        });
        analyzeMach.setIcon(new ImageIcon(UIImage.ANALYZE_FILE));
        add(analyzeMach);
        JMenuItem analyzeSale = new JMenuItem("Analyze Sale");
        analyzeSale.addActionListener((e) -> {
            ui.openComponent(new SaleAnalysisPanel(), "Sale", UIImage.ANALYZE_FILE);
        });
        analyzeSale.setIcon(new ImageIcon(UIImage.ANALYZE_FILE));
        add(analyzeSale);

        JMenuItem importItem = new JMenuItem("Import Data...");
        importItem.addActionListener((e) -> {
            File f = ImportService.chooseFile();
            if (f != null) {
                int op = JOptionPane.showConfirmDialog(null, "<html>Are you sure to import?<br>This will take a while.</html>", "Confirm Import Data", JOptionPane.OK_CANCEL_OPTION);
                if (op == JOptionPane.OK_OPTION) {

                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    frame.setSize(500, 200);
                    frame.add(new JLabel("<html>Importing Data....<br>This will take a few minutes.</html>"));
                    frame.setVisible(true);
                    frame.setTitle("Import");
                    frame.setLocationRelativeTo(null);
                    frame.setResizable(false);
                    SwingUtilities.invokeLater(() -> {
                        try {
                            ImportService.importData(f);
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error Reading File!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        frame.dispose();
                    });

                }
            }
        });
        importItem.setIcon(new ImageIcon(UIImage.ADD_FILE));
        add(new JSeparator());
        add(importItem);
    }

}
