package com.titaniumproductionco.db.ui.role.factsv;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
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
import com.titaniumproductionco.db.ui.component.IntegerField;
import com.titaniumproductionco.db.ui.component.RadioSelectionPanel;
import com.titaniumproductionco.db.ui.component.ViewComboBox;
import com.titaniumproductionco.db.ui.model.UITableModel;

@SuppressWarnings("serial")
public class FactoryMachinePanel extends JPanel {
    private JTable table;
    private JPopupMenu popupMenu;

    private RadioSelectionPanel searchSelectionPanel;
    private JPanel searchProcessPanel;
    private ViewComboBox searchProcessSelector;
    private JCheckBox searchProcessAll;
    private IntegerField searchMachineIDField;

    private DateRangeSelector searchDateSelector;

    public FactoryMachinePanel() {
        setPreferredSize(new Dimension(800, 500));
        this.setLayout(new BorderLayout());
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(table.getFontMetrics(table.getFont()).getHeight());
        JScrollPane scroll = new JScrollPane(table);

        add(scroll, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Order"));
        searchPanel.setPreferredSize(new Dimension(700, 150));

        FontMetrics fm = getFontMetrics(getFont());
        int height = fm.getHeight();

        searchSelectionPanel = new RadioSelectionPanel();
        searchProcessSelector = new ViewComboBox(View.ProcessList);
        searchProcessAll = new JCheckBox("All Process");
        searchProcessAll.addActionListener((e) -> {
            searchProcessSelector.setEnabled(!searchProcessAll.isSelected());
        });
        searchProcessAll.setSelected(true);
        searchProcessSelector.setEnabled(false);
        searchProcessPanel = new JPanel() {
            @Override
            public void setEnabled(boolean enabled) {
                super.setEnabled(enabled);
                searchProcessAll.setEnabled(enabled);
                searchProcessSelector.setEnabled(enabled ? (!searchProcessAll.isSelected()) : false);
            }
        };
        searchProcessPanel.add(searchProcessAll);
        searchProcessPanel.add(searchProcessSelector);
        searchSelectionPanel.addSelectionComponent("By Process", searchProcessPanel);
        searchMachineIDField = new IntegerField();
        searchMachineIDField.setPreferredSize(new Dimension(200, height + 8));
        searchSelectionPanel.addSelectionComponent("By MachineID", searchMachineIDField);
        searchPanel.add(searchSelectionPanel);

        searchDateSelector = new DateRangeSelector();
        searchDateSelector.setFromDate(DateSelector.daysAfterCurrentDate(-30));
        JPanel searchDatePanel = new JPanel();
        searchDatePanel.add(new JLabel("Start Use Date"));
        searchDatePanel.add(searchDateSelector);
        searchPanel.add(searchDatePanel);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(this::updateTableModel);
        searchPanel.add(searchButton);

        this.add(searchPanel, BorderLayout.NORTH);

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
        if (!DBService.validateRoleWithError(this, Role.FACTORY_SUPERVISOR)) {
            table.setModel(new DefaultTableModel());
            return;
        }
        String operator = DBService.getUser();
        ConditionedView v = new ConditionedView(View.SupervisorMachineView).where(true, "Supervisor=?", Types.NVARCHAR, operator);
        Object selectedSearch = searchSelectionPanel.getEnabledComponent();
        if (selectedSearch == searchProcessPanel) {
            if (!searchProcessAll.isSelected()) {
                String process = (String) searchProcessSelector.getSelectedItem();
                v.where(process != null, "[Process]=?", Types.VARCHAR, process);
            }
        } else if (selectedSearch == searchMachineIDField) {
            if (searchMachineIDField.isInputValidPos()) {
                v.where(true, "[Machine ID]=?", Types.INTEGER, searchMachineIDField.getParsedInt());
            } else {
                table.setModel(new DefaultTableModel());
                return;
            }
        }

        Date from = searchDateSelector.getFromDate();
        Date to = searchDateSelector.getToDate();
        v.where(from != null, "[Start Use Date]>=?", Types.DATE, from).where(to != null, "[Start Use Date]<=?", Types.DATE, to);

        boolean successful = v.select("[Machine ID], Process, [Cycles Per Day],[Start Use Date]", (rs) -> {
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
        StringBuilder build = new StringBuilder().append("<html>Are you sure to delete this Machine?<br>");
        build.append("<br>Machine ID: ").append(table.getValueAt(row, 0));
        build.append("<br>Process: ").append(table.getValueAt(row, 1));
        build.append("<br>Cycles Per Day: ").append(table.getValueAt(row, 2));
        build.append("<br>Start Use Date: ").append(table.getValueAt(row, 3));
        build.append("<br></html>");
        int op = JOptionPane.showConfirmDialog(this, new JLabel(build.toString()), "Confirm", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            boolean successful = sendDeleteLogQuery(Integer.parseInt((String) table.getValueAt(row, 0)));
            updateTableModel(e);
            return successful;
        } else {
            return false;
        }
    }

    private boolean sendDeleteLogQuery(int MachineID) {
        if (!DBService.validateRoleWithError(this, Role.FACTORY_SUPERVISOR))
            return false;
        if (MachineID < 0) {
            JOptionPane.showMessageDialog(this, "Invalid Inputs", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return Proc.FactorySV_DeleteMachine.call(this, MachineID) != null;
    }

    private boolean handleUpdateLog(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "Please choose a row!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        FactoryMachineAddModPanel p = new FactoryMachineAddModPanel(false);
        p.setOld(Integer.parseInt((String) table.getValueAt(row, 0)), DateSelector.parseDate((String) table.getValueAt(row, 3)), (String) table.getValueAt(row, 1),
                Double.parseDouble((String) table.getValueAt(row, 2)));

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
