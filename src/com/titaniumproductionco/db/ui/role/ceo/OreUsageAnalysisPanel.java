package com.titaniumproductionco.db.ui.role.ceo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.titaniumproductionco.db.services.DBService;
import com.titaniumproductionco.db.services.Role;
import com.titaniumproductionco.db.services.func.View;
import com.titaniumproductionco.db.ui.component.DateRangeSelector;
import com.titaniumproductionco.db.ui.component.DateSelector;
import com.titaniumproductionco.db.ui.component.DoubleField;
import com.titaniumproductionco.db.ui.model.UITableModel;

@SuppressWarnings("serial")
public class OreUsageAnalysisPanel extends JPanel {
    private JTable table;
    private DateRangeSelector analyzeDates;
    private DoubleField errorField;

    public OreUsageAnalysisPanel() {
        setPreferredSize(new Dimension(800, 500));
        this.setLayout(new BorderLayout());
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(table.getFontMetrics(table.getFont()).getHeight());
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JPanel datePanel = new JPanel();
        datePanel.setPreferredSize(new Dimension(500, 100));
        datePanel.setBorder(BorderFactory.createTitledBorder("Choose Date"));

        analyzeDates = new DateRangeSelector();
        analyzeDates.setFromDate(DateSelector.daysAfterCurrentDate(-7));
        datePanel.add(analyzeDates);
        datePanel.add(new JLabel("% Allowance"));
        errorField = new DoubleField();
        errorField.setPreferredSize(new Dimension(100, table.getFontMetrics(table.getFont()).getHeight() + 8));
        errorField.setText("0.05");
        datePanel.add(errorField);

        JButton analyzeButton = new JButton("Analyze");
        analyzeButton.addActionListener(this::updateTableModel);
        datePanel.add(analyzeButton);

        add(datePanel, BorderLayout.NORTH);

    }

    public boolean validateInputs() {
        if (!DBService.validateRoleWithError(this, Role.CEO)) {
            return false;
        }
        if (!errorField.isInputValidNonNeg()) {
            JOptionPane.showMessageDialog(this, "Invalid Allowance!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void updateTableModel(ActionEvent e) {
        // permission check for now
        if (!validateInputs()) {
            table.setModel(new DefaultTableModel());
            return;
        }

        boolean successful = false;

        double allowance = errorField.getParsedDouble();
        Date from = analyzeDates.getFromDate();
        Date to = analyzeDates.getToDate();
        successful = View.func_Analysis_OreUsage.select("*", (rs) -> {
            table.setModel(UITableModel.createTableModel(rs));
        }, from, to, allowance / 100);

        if (!successful) {
            table.setModel(new DefaultTableModel());

        }
    }
}
