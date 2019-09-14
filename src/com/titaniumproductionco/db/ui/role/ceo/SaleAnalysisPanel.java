package com.titaniumproductionco.db.ui.role.ceo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import com.titaniumproductionco.db.ui.model.UITableModel;

@SuppressWarnings("serial")
public class SaleAnalysisPanel extends JPanel {
    private JTable table;
    private DateRangeSelector analyzeDates;

    public SaleAnalysisPanel() {
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

        JButton analyzeButton = new JButton("Get Sales");
        analyzeButton.addActionListener(this::updateTableModel);
        datePanel.add(analyzeButton);

        add(datePanel, BorderLayout.NORTH);

    }

    public boolean validateInputs() {
        return DBService.validateRoleWithError(this, Role.CEO);
    }

    public void updateTableModel(ActionEvent e) {
        if (!validateInputs()) {
            table.setModel(new DefaultTableModel());
            return;
        }

        boolean successful = false;

        Date from = analyzeDates.getFromDate();
        Date to = analyzeDates.getToDate();
        successful = View.func_Analysis_Sale.select("*", (rs) -> {
            table.setModel(UITableModel.createTableModel(rs));
        }, from, to);

        if (!successful) {
            table.setModel(new DefaultTableModel());

        }
    }
}
