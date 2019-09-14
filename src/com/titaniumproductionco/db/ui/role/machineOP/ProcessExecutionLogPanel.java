package com.titaniumproductionco.db.ui.role.machineOP;

import java.awt.BorderLayout;
import java.awt.Color;
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
import com.titaniumproductionco.db.ui.component.IntegerField;

@SuppressWarnings("serial")
public class ProcessExecutionLogPanel extends JPanel {

    private JTable table;
    private JPopupMenu popupMenu;
    private JPanel searchPanel;

    private IntegerField searchProcessIDField;
    private JCheckBox searchProcessCheckBox;
    private DateRangeSelector searchDateSelector;

    public ProcessExecutionLogPanel() {
        setPreferredSize(new Dimension(1000, 600));
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);

        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(table.getFontMetrics(this.getFont()).getHeight() * 8);
        JScrollPane scroll = new JScrollPane(table);

        searchPanel = new JPanel();
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Execusion Log"));
        searchPanel.setPreferredSize(new Dimension(700, 100));
        searchProcessCheckBox = new JCheckBox("Search On Process ID");
        searchProcessCheckBox.setSelected(false);
        searchProcessCheckBox.addActionListener((e) -> {
            searchProcessIDField.setEnabled(searchProcessCheckBox.isSelected());
        });
        searchPanel.add(searchProcessCheckBox);
        searchProcessIDField = new IntegerField();
        searchProcessIDField.setPreferredSize(new Dimension(200, 25 + 8));
        searchProcessIDField.setEnabled(false);
        searchPanel.add(searchProcessIDField);

        searchDateSelector = new DateRangeSelector();
        searchDateSelector.setFromDate(DateSelector.daysAfterCurrentDate(-10));
        searchPanel.add(searchDateSelector);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(this::updateTableModel);
        searchPanel.add(searchButton);

        this.add(searchPanel, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);

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
        if (!DBService.validateRoleWithError(this, Role.MACHINE_OPERATOR)) {
            table.setModel(new DefaultTableModel());
            return;
        }
        String operator = DBService.getUser();

        ConditionedView v = new ConditionedView(View.ProcessExecutionLog);

        if (searchProcessCheckBox.isSelected()) {
            v.where(true, "[ProcExecID]=?", Types.INTEGER, searchProcessIDField.getParsedInt());
        }

        Date from = searchDateSelector.getFromDate();
        Date to = searchDateSelector.getToDate();
        v.where(from != null, "[TimeStamp]>=?", Types.DATE, from).where(to != null, "[TimeStamp]<=?", Types.DATE, to);

        boolean successful = v.where(true, "Operator=?", Types.NVARCHAR, operator).select("[ProcExecID],[TimeStamp],[ProcessName],[Material],[Moles],[Is Input]", (rs) -> {
            table.setModel(new ProcessExecutionLogTableModel(rs));
        });

        if (!successful) {
            table.setModel(new DefaultTableModel());
        }

    }

    private boolean handleDeleteLog(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
            System.out.println("ROW: " + row);
            JOptionPane.showMessageDialog(null, "Please choose a row!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        StringBuilder build = new StringBuilder().append("<html>Are you sure to delete this log?");
        build.append("<br>ProcExecID: ").append(table.getValueAt(row, 0));
        build.append("<br>TimeStamp: ").append(table.getValueAt(row, 1));
        build.append("<br>ProcessName: ").append(table.getValueAt(row, 2));
        build.append("<br>MaterialUsage:<br> ").append(table.getValueAt(row, 3));
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

    private boolean sendDeleteLogQuery(int processID) {
        if (!DBService.validateRoleWithError(this, Role.MACHINE_OPERATOR))
            return false;
        // String supervisor = DBService.getUser();
        if (processID < 0) {
            JOptionPane.showMessageDialog(this, "Invalid Inputs", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return Proc.MachineOp_DeleteProcessExecution.call(this, processID) != null;
    }

    private boolean handleUpdateLog(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "Please choose a row!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        ProcessExecAddModPanel p = new ProcessExecAddModPanel(false);
        p.setOld(Integer.parseInt((String) table.getValueAt(row, 0)));
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
