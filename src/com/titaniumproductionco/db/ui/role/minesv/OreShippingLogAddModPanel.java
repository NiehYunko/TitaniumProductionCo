package com.titaniumproductionco.db.ui.role.minesv;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.titaniumproductionco.db.services.DBService;
import com.titaniumproductionco.db.services.Role;
import com.titaniumproductionco.db.services.func.Proc;
import com.titaniumproductionco.db.services.func.View;
import com.titaniumproductionco.db.ui.component.DateSelector;
import com.titaniumproductionco.db.ui.component.IntegerField;
import com.titaniumproductionco.db.ui.component.ViewComboBox;

@SuppressWarnings("serial")
public class OreShippingLogAddModPanel extends JPanel {
    private DateSelector dateSelector;
    private ViewComboBox factorySelector;
    private ViewComboBox materialSelector;

    private Date oldDate;
    private String oldFactory;
    private String oldMaterial;

    private IntegerField qtyField;

    public OreShippingLogAddModPanel(boolean add) {
        this.setPreferredSize(new Dimension(350, 300));
        this.setLayout(new GridLayout(9, 1));
        this.add(new JLabel("Date"));
        this.add(dateSelector = new DateSelector());
        this.add(new JLabel("Factory"));
        this.add(factorySelector = new ViewComboBox(View.FactoryList, "Address"));
        this.add(new JLabel("Ore Material"));
        this.add(materialSelector = new ViewComboBox(View.OreList));
        this.add(new JLabel("Quantity"));
        this.add(qtyField = new IntegerField());

        if (add) {
            JButton button = new JButton();
            button.setText("Add");
            button.addActionListener(this::handleAdd);
            this.add(button);
        }

    }

    public void setOld(Date oDate, String oFactory, String oMaterial, int quantity) {
        oldDate = oDate;
        oldFactory = oFactory;
        oldMaterial = oMaterial;
        dateSelector.setDate(oldDate);
        factorySelector.setSelectedItem(oldFactory);
        materialSelector.setSelectedItem(oldMaterial);
        qtyField.setText(String.valueOf(quantity));
    }

    private boolean validateInputs() {
        if (factorySelector.getSelected() == null || materialSelector.getSelected() == null || !qtyField.isInputValidPos()) {
            JOptionPane.showMessageDialog(this, new JLabel("<html>Please make sure factory is selected.<br>Please make sure material is selected.<br>Please make sure quantity is valid.</html>"),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!DBService.validateRoleWithError(this, Role.MINE_SUPERVISOR))
            return false;
        return true;
    }

    private boolean handleAdd(ActionEvent e) {
        if (!validateInputs())
            return false;
        Date date = dateSelector.getDate();
        String supervisor = DBService.getUser();
        String factory = factorySelector.getSelected();
        String material = materialSelector.getSelected();
        int quantity = qtyField.getParsedInt();

        return Proc.MineSV_AddShipOresLog.call(this, date, supervisor, quantity, material, factory) != null;
    }

    public boolean handleUpdate() {
        if (!validateInputs())
            return false;

        Date date = dateSelector.getDate();
        String supervisor = DBService.getUser();
        String factory = factorySelector.getSelected();
        String material = materialSelector.getSelected();
        int quantity = qtyField.getParsedInt();

        return Proc.MineSV_UpdateShipOresLog.call(this, date, oldDate, supervisor, quantity, material, oldMaterial, factory, oldFactory) != null;
    }
}
