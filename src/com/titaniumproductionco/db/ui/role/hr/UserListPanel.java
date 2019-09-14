package com.titaniumproductionco.db.ui.role.hr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.Types;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.titaniumproductionco.db.services.DBService;
import com.titaniumproductionco.db.services.Role;
import com.titaniumproductionco.db.services.func.ConditionedView;
import com.titaniumproductionco.db.services.func.View;
import com.titaniumproductionco.db.ui.UIImage;
import com.titaniumproductionco.db.ui.component.IntegerField;
import com.titaniumproductionco.db.ui.component.RadioSelectionPanel;
import com.titaniumproductionco.db.ui.model.UITableModel;

@SuppressWarnings("serial")
public class UserListPanel extends JPanel {

    private JTable table;
    private JPopupMenu popupMenu;

    private RadioSelectionPanel searchSelectionPanel;
    private JPanel roleSelectionPanel;
    private JCheckBox searchRoleAll;
    private JComboBox<String> searchByRole;
    private IntegerField searchByID;
    private JTextField searchByUsername;

    public UserListPanel() {
        setPreferredSize(new Dimension(850, 500));
        this.setLayout(new BorderLayout());
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(table.getFontMetrics(table.getFont()).getHeight());
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel();
        searchPanel.setPreferredSize(new Dimension(850, 150));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search User"));
        add(searchPanel, BorderLayout.NORTH);
        searchSelectionPanel = new RadioSelectionPanel();
        roleSelectionPanel = new JPanel() {
            @Override
            public void setEnabled(boolean enabled) {
                super.setEnabled(enabled);
                searchRoleAll.setEnabled(enabled);
                searchByRole.setEnabled(enabled ? (!searchRoleAll.isSelected()) : false);
            }
        };
        searchRoleAll = new JCheckBox("All");
        searchRoleAll.setSelected(true);
        searchRoleAll.addActionListener((e) -> {
            searchByRole.setEnabled(!searchRoleAll.isSelected());
        });
        searchByRole = new JComboBox<>(Role.roleNames());
        searchByRole.setSelectedIndex(0);
        searchByRole.setEnabled(false);
        roleSelectionPanel.add(searchRoleAll);
        roleSelectionPanel.add(searchByRole);
        searchSelectionPanel.addSelectionComponent("By Role", roleSelectionPanel);
        searchPanel.add(searchSelectionPanel);

        searchByID = new IntegerField();
        searchByID.setPreferredSize(new Dimension(150, getFontMetrics(getFont()).getHeight() + 8));
        searchSelectionPanel.addSelectionComponent("By ID", searchByID);

        searchByUsername = new JTextField();
        searchByUsername.setPreferredSize(new Dimension(150, getFontMetrics(getFont()).getHeight() + 8));
        searchSelectionPanel.addSelectionComponent("By Username", searchByUsername);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(this::updateTableModel);
        searchPanel.add(searchButton);

        popupMenu = new JPopupMenu();

        JMenuItem refresh = new JMenuItem("Refresh");
        refresh.addActionListener(this::updateTableModel);
        refresh.setIcon(new ImageIcon(UIImage.REFRESH));

        popupMenu.add(new JSeparator());
        popupMenu.add(refresh);
        scroll.setComponentPopupMenu(popupMenu);
        table.setComponentPopupMenu(popupMenu);

        updateTableModel(null);
    }

    public void updateTableModel(ActionEvent e) {
        // permission check for now
        if (!DBService.validateRoleWithError(this, Role.HUMAN_RESOURCE, Role.CEO)) {
            table.setModel(new DefaultTableModel());
            return;
        }
        ConditionedView v = new ConditionedView(View.UserList);
        Object searchOn = searchSelectionPanel.getEnabledComponent();
        if (searchOn == roleSelectionPanel) {
            if (!searchRoleAll.isSelected()) {
                String role = (String) searchByRole.getSelectedItem();
                v.where(role != null, "Role=?", Types.VARCHAR, role);
            }
        } else if (searchOn == searchByID) {
            if (searchByID.isInputValidPos()) {
                v.where(true, "ID=?", Types.INTEGER, searchByID.getParsedInt());
            } else {
                table.setModel(new DefaultTableModel());
                return;
            }
        } else if (searchOn == searchByUsername) {
            v.where(true, "Username LIKE ?", Types.VARCHAR, "%" + searchByUsername.getText() + "%");
        }

        boolean successful = v.select("*", (rs) -> {
            table.setModel(UITableModel.createTableModel(rs));
        });

        if (!successful) {
            table.setModel(new DefaultTableModel());
        }

    }

}
