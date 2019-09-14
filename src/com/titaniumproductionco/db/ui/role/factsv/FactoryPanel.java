package com.titaniumproductionco.db.ui.role.factsv;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Types;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.titaniumproductionco.db.services.DBService;
import com.titaniumproductionco.db.services.Role;
import com.titaniumproductionco.db.services.func.ConditionedView;
import com.titaniumproductionco.db.services.func.IViewCallback;
import com.titaniumproductionco.db.services.func.View;
import com.titaniumproductionco.db.ui.model.UITableModel;

@SuppressWarnings("serial")
public class FactoryPanel extends JPanel {
    private JTable table;

    public FactoryPanel() {
        setPreferredSize(new Dimension(500, 300));
        this.setLayout(new BorderLayout());
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(table.getFontMetrics(table.getFont()).getHeight());

        JScrollPane scroll = new JScrollPane(table);

        add(scroll);

        updateTableModel();
    }

    public void updateTableModel() {
        // permission check for now
        if (!DBService.validateRoleWithError(this, Role.FACTORY_SUPERVISOR, Role.HUMAN_RESOURCE, Role.CEO)) {
            table.setModel(new DefaultTableModel());
            return;
        }
        boolean successful = false;
        IViewCallback callback = (rs) -> {
            table.setModel(UITableModel.createTableModel(rs));
        };
        switch (DBService.getRole()) {
        case FACTORY_SUPERVISOR:
            String supervisor = DBService.getUser();
            successful = new ConditionedView(View.FactoryAndSupervisor).where(true, "Supervisor=?", Types.NVARCHAR, supervisor).select("*", callback);
            break;
        default:
            successful = View.FactoryList.select("ID,Address", callback);
        }

        if (!successful) {
            table.setModel(new DefaultTableModel());
        }

    }

}
