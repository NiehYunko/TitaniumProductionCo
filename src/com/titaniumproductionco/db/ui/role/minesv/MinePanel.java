package com.titaniumproductionco.db.ui.role.minesv;

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

/**
 * For viewing mines
 *
 */
@SuppressWarnings("serial")
public class MinePanel extends JPanel {
    private JTable table;
    private boolean complex;

    public MinePanel(boolean complex) {
        this.complex = complex;
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
        if (!DBService.validateRoleWithError(this, Role.MINE_SUPERVISOR, Role.HUMAN_RESOURCE, Role.CEO)) {
            table.setModel(new DefaultTableModel());
            return;
        }
        boolean successful = false;
        IViewCallback callback = (rs) -> {
            table.setModel(UITableModel.createTableModel(rs));
        };
        switch (DBService.getRole()) {
        case MINE_SUPERVISOR:
            String supervisor = DBService.getUser();
            successful = new ConditionedView(View.MineAndSupervisor).where(true, "Username=?", Types.NVARCHAR, supervisor).select("ID,Address,DiscoverDate,EstExhaustDate", callback);
            break;
        default:
            if (complex)
                successful = View.MineAddressView.select("*", callback);
            else
                successful = View.MineAddressView.select("ID,Address", callback);
        }

        if (!successful) {
            table.setModel(new DefaultTableModel());
        }

    }

}
