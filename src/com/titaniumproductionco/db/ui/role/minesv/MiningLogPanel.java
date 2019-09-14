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

/**
 * For viewing mining logs
 *
 */
@SuppressWarnings("serial")
public class MiningLogPanel extends JPanel {
    private JTable table;
    private JPopupMenu popupMenu;
    private JCheckBox searchByOre;
    private ViewComboBox oreSelector;
    private JCheckBox searchByQuantity;
    private IntegerRangeField quantityRangeField;
    private DateRangeSelector searchDateSelector;

    public MiningLogPanel() {
        setPreferredSize(new Dimension(750, 500));
        this.setLayout(new BorderLayout());
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(table.getFontMetrics(table.getFont()).getHeight());
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel();
        searchPanel.setPreferredSize(new Dimension(750, 150));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Mining Log"));
        searchByOre = new JCheckBox("By Ore");
        searchByOre.addActionListener((e) -> {
            oreSelector.setEnabled(searchByOre.isSelected());
        });
        oreSelector = new ViewComboBox(View.OreList);
        oreSelector.setSelectedIndex(0);
        oreSelector.setEnabled(false);
        searchPanel.add(searchByOre);
        searchPanel.add(oreSelector);

        searchByQuantity = new JCheckBox("By Quantity");
        quantityRangeField = new IntegerRangeField();
        searchByQuantity.addActionListener((e) -> {
            quantityRangeField.setEnabled(searchByQuantity.isSelected());
        });
        quantityRangeField.setEnabled(false);
        searchPanel.add(searchByQuantity);
        searchPanel.add(quantityRangeField);

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
        if (!DBService.validateRoleWithError(this, Role.MINE_SUPERVISOR)) {
            table.setModel(new DefaultTableModel());
            return;
        }
        String supervisor = DBService.getUser();
        Date beginDate = searchDateSelector.getFromDate();
        Date endDate = searchDateSelector.getToDate();
        ConditionedView v = new ConditionedView(View.MiningLog).where(true, "Supervisor=?", Types.NVARCHAR, supervisor).where(beginDate != null, "Date >= ?", Types.DATE, beginDate)
                .where(endDate != null, "Date <= ?", Types.DATE, endDate);
        if (searchByOre.isSelected()) {
            String ore = (String) oreSelector.getSelectedItem();
            v.where(ore != null, "Description=?", Types.VARCHAR, ore);
        }
        if (searchByQuantity.isSelected()) {
            v.where(quantityRangeField.isLowerBoundSet(), "Quantity>=?", Types.INTEGER, quantityRangeField.getLowerBound());
            v.where(quantityRangeField.isUpperBoundSet(), "Quantity<=?", Types.INTEGER, quantityRangeField.getUpperBound());
        }

        boolean successful = v.select("[Date],[Address],[Description],[Quantity]", (rs) -> {
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
        StringBuilder build = new StringBuilder().append("<html>Are you sure to delete this log?<br>Date: ");
        build.append(table.getModel().getValueAt(row, 0));
        build.append("<br>Mine: ").append(table.getValueAt(row, 1));
        build.append("<br>Ore: ").append(table.getValueAt(row, 2));
        build.append("<br>Quantity: ").append(table.getValueAt(row, 3));
        build.append("<br></html>");
        int op = JOptionPane.showConfirmDialog(this, new JLabel(build.toString()), "Confirm", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            boolean successful = sendDeleteLogQuery(DateSelector.parseDate((String) table.getValueAt(row, 0)), (String) table.getModel().getValueAt(row, 2));
            updateTableModel(e);
            return successful;
        } else {
            return false;
        }
    }

    private boolean sendDeleteLogQuery(Date date, String material) {
        if (!DBService.validateRoleWithError(this, Role.MINE_SUPERVISOR))
            return false;
        String supervisor = DBService.getUser();
        if (material == null || date == null || supervisor == null) {
            JOptionPane.showMessageDialog(this, "Invalid Inputs", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return Proc.MineSV_DeleteMiningLog.call(this, date, supervisor, material) != null;
    }

    private boolean handleUpdateLog(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "Please choose a row!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        MiningLogAddModPanel p = new MiningLogAddModPanel(false);
        p.setOld(DateSelector.parseDate((String) table.getValueAt(row, 0)), (String) table.getValueAt(row, 2), Integer.parseInt((String) table.getValueAt(row, 3)));
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
