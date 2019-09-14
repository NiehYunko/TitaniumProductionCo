package com.titaniumproductionco.db.ui.role.factsv;

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
import com.titaniumproductionco.db.ui.component.DoubleField;
import com.titaniumproductionco.db.ui.component.ViewComboBox;

@SuppressWarnings("serial")
public class FactoryMachineAddModPanel extends JPanel {
    private DateSelector dateSelector;
    private ViewComboBox processSelector;

    private DoubleField cyclesField;

    private int updateMachineID;

    public FactoryMachineAddModPanel(boolean add) {
        this.setPreferredSize(new Dimension(350, 270));
        this.setLayout(new GridLayout(7, 1));
        this.add(new JLabel("Start Use Date"));
        this.add(dateSelector = new DateSelector());
        this.add(new JLabel("Process"));
        this.add(processSelector = new ViewComboBox(View.ProcessList));
        this.add(new JLabel("Cycles Per Day"));
        this.add(cyclesField = new DoubleField());

        if (add) {
            JButton button = new JButton();
            button.setText("Add");
            button.addActionListener(this::handleAdd);
            this.add(button);
        }

    }

    public void setOld(int oMachineID, Date oDate, String oProcess, double oCycles) {
        updateMachineID = oMachineID;
        dateSelector.setDate(oDate);
        processSelector.setSelectedItem(oProcess);
        cyclesField.setText(String.valueOf(oCycles));
    }

    private boolean validateInputs() {
        if (processSelector.getSelected() == null || !cyclesField.isInputValidPos()) {
            JOptionPane.showMessageDialog(this, new JLabel("<html>Please make sure Process is selected.<br>Please make sure Cycles Per Day is valid.</html>"), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!DBService.validateRoleWithError(this, Role.FACTORY_SUPERVISOR))
            return false;
        return true;
    }

    private boolean handleAdd(ActionEvent e) {
        if (!validateInputs())
            return false;
        Date date = dateSelector.getDate();
        String supervisor = DBService.getUser();
        String process = processSelector.getSelected();
        double cycleperday = cyclesField.getParsedDouble();

        return Proc.FactorySV_AddMachine.call(this, date, supervisor, cycleperday, process) != null;
    }

    public boolean handleUpdate() {
        if (!validateInputs())
            return false;

        Date date = dateSelector.getDate();
        String supervisor = DBService.getUser();
        String process = processSelector.getSelected();
        double cycles = cyclesField.getParsedDouble();

        return Proc.FactorySV_UpdateMachine.call(this, supervisor, updateMachineID, cycles, date, process) != null;
    }
}
