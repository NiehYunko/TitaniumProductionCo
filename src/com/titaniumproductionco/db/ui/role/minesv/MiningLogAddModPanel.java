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
public class MiningLogAddModPanel extends JPanel {
    private DateSelector dateSelector;
    private ViewComboBox materialSelector;

    private Date oldDate;
    private String oldMaterial;

    private IntegerField qtyField;

    public MiningLogAddModPanel(boolean add) {
        this.setPreferredSize(new Dimension(350, 270));
        this.setLayout(new GridLayout(7, 1));
        this.add(new JLabel("Date"));
        this.add(dateSelector = new DateSelector());
        this.add(new JLabel("Ore"));
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

    public void setOld(Date oDate, String oMaterial, int quantity) {
        oldDate = oDate;
        oldMaterial = oMaterial;
        dateSelector.setDate(oldDate);
        materialSelector.setSelectedItem(oldMaterial);
        qtyField.setText(String.valueOf(quantity));
    }

    private boolean validateInputs() {
        if (materialSelector.getSelected() == null || !qtyField.isInputValidPos()) {
            JOptionPane.showMessageDialog(this, new JLabel("<html>Please make sure material is selected.<br>Please make sure quantity is valid.</html>"), "Error", JOptionPane.ERROR_MESSAGE);
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
        String material = materialSelector.getSelected();
        int quantity = qtyField.getParsedInt();

        return Proc.MineSV_AddMiningLog.call(this, date, supervisor, quantity, material) != null;
    }

    public boolean handleUpdate() {
        if (!validateInputs())
            return false;

        Date date = dateSelector.getDate();
        String supervisor = DBService.getUser();
        String material = materialSelector.getSelected();
        int quantity = qtyField.getParsedInt();

        Object result = Proc.MineSV_UpdateMiningLog.call(this, date, oldDate, supervisor, quantity, material, oldMaterial);
        return result != null;
    }
}
