package com.titaniumproductionco.db.ui.role.minesv;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.sql.Types;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.titaniumproductionco.db.services.DBService;
import com.titaniumproductionco.db.services.Role;
import com.titaniumproductionco.db.services.func.ConditionedView;
import com.titaniumproductionco.db.services.func.Proc;
import com.titaniumproductionco.db.services.func.View;
import com.titaniumproductionco.db.ui.UIImage;
import com.titaniumproductionco.db.ui.component.DateRangeSelector;
import com.titaniumproductionco.db.ui.component.DateSelector;
import com.titaniumproductionco.db.ui.component.IntegerRangeField;
import com.titaniumproductionco.db.ui.component.ViewComboBox;
import com.titaniumproductionco.db.ui.model.UITableModel;

@SuppressWarnings("serial")
public class OreShippingLogPanel extends JPanel {
    private JTable table;
    private JPopupMenu popupMenu;

    private JCheckBox searchByOre;
    private ViewComboBox oreSelector;
    private JCheckBox searchByQuantity;
    private IntegerRangeField quantityRangeField;
    private JCheckBox searchByFactory;
    private ViewComboBox factorySelector;
    private DateRangeSelector searchDateSelector;

    public OreShippingLogPanel() {
        setPreferredSize(new Dimension(750, 500));
        this.setLayout(new BorderLayout());
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(table.getFontMetrics(table.getFont()).getHeight());
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel();
        searchPanel.setPreferredSize(new Dimension(750, 180));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Ore Shipping Log"));
        searchByOre = new JCheckBox("By Ore");
        searchByOre.addActionListener((e) -> {
            oreSelector.setEnabled(searchByOre.isSelected());
        });
        oreSelector = new ViewComboBox(View.OreList);
        oreSelector.setSelectedIndex(0);
        oreSelector.setEnabled(false);
        JPanel searchOrePanel = new JPanel();
        searchOrePanel.add(searchByOre);
        searchOrePanel.add(oreSelector);
        searchPanel.add(searchOrePanel);

        searchByFactory = new JCheckBox("By Factory");
        searchByFactory.addActionListener((e) -> {
            factorySelector.setEnabled(searchByFactory.isSelected());
        });
        factorySelector = new ViewComboBox(View.FactoryList, "Address");
        factorySelector.setSelectedIndex(0);
        factorySelector.setEnabled(false);
        JPanel searchFactoryPanel = new JPanel();
        searchFactoryPanel.add(searchByFactory);
        searchFactoryPanel.add(factorySelector);
        searchPanel.add(searchFactoryPanel);

        searchByQuantity = new JCheckBox("By Quantity");
        quantityRangeField = new IntegerRangeField();
        searchByQuantity.addActionListener((e) -> {
            quantityRangeField.setEnabled(searchByQuantity.isSelected());
        });
        quantityRangeField.setEnabled(false);
        JPanel searchQuantityPanel = new JPanel();
        searchQuantityPanel.add(searchByQuantity);
        searchQuantityPanel.add(quantityRangeField);
        searchPanel.add(searchQuantityPanel);

        searchDateSelector = new DateRangeSelector();
        searchDateSelector.setFromDate(DateSelector.daysAfterCurrentDate(-7));
        searchPanel.add(searchDateSelector);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(this::updateTableModel);
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        popupMenu = new JPopupMenu();
        JMenuItem updateLog = new JMenuItem("Update...");
        updateLog.addActionListener(this::handleUpdateLog);
        updateLog.setIcon(new ImageIcon(UIImage.UPDATE_FILE));

        JMenuItem deleteLog = new JMenuItem("Delete");
        deleteLog.addActionListener(this::handleDeleteLog);
        deleteLog.setIcon(new ImageIcon(UIImage.REMOVE_FILE));

        JMenuItem refresh = new JMenuItem("Refresh");
        refresh.addActionListener(this::updateTableModel);
        refresh.setIcon(new ImageIcon(UIImage.REFRESH));

        popupMenu.add(updateLog);
        popupMenu.add(deleteLog);
        popupMenu.add(new JSeparator());
        popupMenu.add(refresh);
        scroll.setComponentPopupMenu(popupMenu);
        table.setComponentPopupMenu(popupMenu);

        updateTableModel(null);
    }

    public void updateTableModel(ActionEvent e) {
        // permission check for now
        if (DBService.getRole() != Role.MINE_SUPERVISOR) {
            table.setModel(new DefaultTableModel());
            return;
        }

        String supervisor = DBService.getUser();
        Date beginDate = searchDateSelector.getFromDate();
        Date endDate = searchDateSelector.getToDate();
        ConditionedView v = new ConditionedView(View.OreShippingLog);
        if (searchByFactory.isSelected()) {
            String factory = (String) factorySelector.getSelectedItem();
            v.where(factory != null, "Factory=?", Types.NVARCHAR, factory);
        }

        if (searchByOre.isSelected()) {
            String ore = (String) oreSelector.getSelectedItem();
            v.where(ore != null, "Material=?", Types.VARCHAR, ore);
        }
        if (searchByQuantity.isSelected()) {
            v.where(quantityRangeField.isLowerBoundSet(), "Quantity>=?", Types.INTEGER, quantityRangeField.getLowerBound());
            v.where(quantityRangeField.isUpperBoundSet(), "Quantity<=?", Types.INTEGER, quantityRangeField.getUpperBound());
        }
        boolean successful = v.where(true, "Supervisor=?", Types.NVARCHAR, supervisor).where(beginDate != null, "Date >= ?", Types.DATE, beginDate)
                .where(endDate != null, "Date <= ?", Types.DATE, endDate).select("[Date] ,[Mine] ,[Factory] ,[Material],[Quantity]", (rs) -> {
                    table.setModel(UITableModel.createTableModel(rs));
                });

        if (!successful) {
            table.setModel(new DefaultTableModel());
        }
    }

    private boolean handleDeleteLog(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "Please choose a row!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String factory = (String) table.getValueAt(row, 2);
        String material = (String) table.getValueAt(row, 3);
        String quantity = (String) table.getValueAt(row, 4);
        StringBuilder build = new StringBuilder().append("<html>Are you sure to delete this log?<br>Date: ");
        build.append(table.getValueAt(row, 0));
        build.append("<br>Mine: ").append(table.getValueAt(row, 1));
        build.append("<br>Factory: ").append(factory);
        build.append("<br>Ore: ").append(material);
        build.append("<br>Quantity: ").append(quantity);
        build.append("<br></html>");
        int op = JOptionPane.showConfirmDialog(this, new JLabel(build.toString()), "Confirm", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            boolean successful = sendDeleteLogQuery(DateSelector.parseDate((String) table.getValueAt(row, 0)), material, factory);
            updateTableModel(e);
            return successful;
        } else {
            return false;
        }
    }

    private boolean sendDeleteLogQuery(Date date, String material, String factory) {
        if (!DBService.validateRoleWithError(this, Role.MINE_SUPERVISOR))
            return false;
        String supervisor = DBService.getUser();
        if (material == null || date == null || supervisor == null || factory == null) {
            JOptionPane.showMessageDialog(this, "Invalid Inputs", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return Proc.MineSV_DeleteShipOresLog.call(date, supervisor, material, factory) != null;

    }

    private boolean handleUpdateLog(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "Please choose a row!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        OreShippingLogAddModPanel p = new OreShippingLogAddModPanel(false);
        String factory = (String) table.getValueAt(row, 2);
        String material = (String) table.getValueAt(row, 3);
        String quantity = (String) table.getValueAt(row, 4);
        p.setOld(DateSelector.parseDate((String) table.getValueAt(row, 0)), factory, material, Integer.parseInt(quantity));

        while (true) {
            int op = JOptionPane.showConfirmDialog(this, p, "Update", JOptionPane.OK_CANCEL_OPTION);
            if (op == JOptionPane.OK_OPTION) {
                boolean successful = p.handleUpdate();
                if (successful) {
                    updateTableModel(e);
                    return true;
                }
            } else {
                return false;
            }
        }
    }
}
