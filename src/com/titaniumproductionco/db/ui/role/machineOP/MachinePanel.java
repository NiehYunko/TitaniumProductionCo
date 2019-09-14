package com.titaniumproductionco.db.ui.role.machineOP;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.sql.Types;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import com.titaniumproductionco.db.ui.component.IntegerField;
import com.titaniumproductionco.db.ui.component.RadioSelectionPanel;
import com.titaniumproductionco.db.ui.component.ViewComboBox;
import com.titaniumproductionco.db.ui.model.UITableModel;

@SuppressWarnings("serial")
public class MachinePanel extends JPanel {
    private JTable table;
    private JPanel searchPanel;
    private RadioSelectionPanel searchSelectionPanel;
    private IntegerField searchMachineIDField;
    private ViewComboBox factoryAddrSelector;
    private JCheckBox searchFactoryAll;
    private JPanel factoryProcessPanel;
    private ViewComboBox searchProcessSelector;
    private JCheckBox searchProcessAll;

    public MachinePanel() {
        setPreferredSize(new Dimension(1400, 600));
        this.setLayout(new BorderLayout());
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(table.getFontMetrics(table.getFont()).getHeight());
        JScrollPane scroll = new JScrollPane(table);

        if (DBService.getRole() != Role.MACHINE_OPERATOR) {
            FontMetrics fm = getFontMetrics(getFont());
            int height = fm.getHeight();

            searchPanel = new JPanel();
            searchPanel.setBorder(BorderFactory.createTitledBorder("Search Machine"));
            searchPanel.setPreferredSize(new Dimension(1400, 150));

            factoryProcessPanel = new JPanel() {
                @Override
                public void setEnabled(boolean enabled) {
                    super.setEnabled(enabled);
                    searchProcessAll.setEnabled(enabled);
                    searchProcessSelector.setEnabled(enabled ? (!searchProcessAll.isSelected()) : false);
                    searchFactoryAll.setEnabled(enabled);
                    factoryAddrSelector.setEnabled(enabled ? (!searchFactoryAll.isSelected()) : false);
                }
            };

            searchSelectionPanel = new RadioSelectionPanel();

            searchProcessSelector = new ViewComboBox(View.ProcessList);
            searchProcessSelector.setPreferredSize(new Dimension(200, height + 8));
            searchProcessAll = new JCheckBox("All Processes");
            searchProcessAll.addActionListener((e) -> {
                searchProcessSelector.setEnabled(!searchProcessAll.isSelected());
            });
            searchProcessAll.setSelected(true);
            searchProcessSelector.setEnabled(false);
            factoryAddrSelector = new ViewComboBox(View.FactoryList, "Address");
            searchFactoryAll = new JCheckBox("All Factories");
            searchFactoryAll.addActionListener((e) -> {
                factoryAddrSelector.setEnabled(!searchFactoryAll.isSelected());
            });
            searchFactoryAll.setSelected(true);
            factoryAddrSelector.setEnabled(false);
            factoryProcessPanel.add(searchProcessAll);
            factoryProcessPanel.add(searchProcessSelector);
            factoryProcessPanel.add(searchFactoryAll);
            factoryProcessPanel.add(factoryAddrSelector);

            searchSelectionPanel.addSelectionComponent("By Process/FactoryID", factoryProcessPanel);

            searchMachineIDField = new IntegerField();
            searchMachineIDField.setPreferredSize(new Dimension(200, height + 8));
            searchSelectionPanel.addSelectionComponent("By MachineID", searchMachineIDField);
            searchPanel.add(searchSelectionPanel);

            JButton searchButton = new JButton("Search");
            searchButton.addActionListener(this::updateTableModel);
            searchPanel.add(searchButton);

            this.add(searchPanel, BorderLayout.NORTH);
        }

        add(scroll, BorderLayout.CENTER);

        updateTableModel(null);
    }

    public void updateTableModel(ActionEvent e) {
        // permission check for now
        if (!DBService.validateRoleWithError(this, Role.MACHINE_OPERATOR, Role.HUMAN_RESOURCE, Role.CEO)) {
            table.setModel(new DefaultTableModel());
            return;
        }
        boolean successful = false;
        IViewCallback callback = (rs) -> {
            table.setModel(UITableModel.createTableModel(rs));
        };

        if (DBService.getRole() != Role.MACHINE_OPERATOR) {
            ConditionedView v = new ConditionedView(View.MachineList);
            Object selectedSearch = searchSelectionPanel.getEnabledComponent();
            if (selectedSearch == factoryProcessPanel) {
                if (!searchProcessAll.isSelected()) {
                    String process = (String) searchProcessSelector.getSelectedItem();
                    v.where(process != null, "[Process]=?", Types.VARCHAR, process);
                }
                if (!searchFactoryAll.isSelected()) {
                    String factory = (String) factoryAddrSelector.getSelectedItem();
                    v.where(factory != null, "[Factory]=?", Types.VARCHAR, factory);
                }
            } else if (selectedSearch == searchMachineIDField) {
                if (searchMachineIDField.isInputValidPos()) {
                    v.where(true, "[Machine]=?", Types.INTEGER, searchMachineIDField.getParsedInt());
                } else {
                    table.setModel(new DefaultTableModel());
                    return;
                }
            }
            successful = v.select("Machine,Process,Factory", callback);
        } else {

            successful = new ConditionedView(View.MachineAndOperator).where(true, "Operator=?", Types.NVARCHAR, DBService.getUser()).select("*", callback);

        }

        if (!successful) {
            table.setModel(new DefaultTableModel());
        }

    }

}
