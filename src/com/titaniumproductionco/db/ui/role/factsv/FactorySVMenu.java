package com.titaniumproductionco.db.ui.role.factsv;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.titaniumproductionco.db.ui.UIFrame;
import com.titaniumproductionco.db.ui.UIImage;
import com.titaniumproductionco.db.ui.component.UIMenu;

@SuppressWarnings("serial")
public class FactorySVMenu extends UIMenu {

    public FactorySVMenu(UIFrame ui) {
        super("[Factory Supervisor]", ui);
    }

    @Override
    protected void init() {
        JMenuItem viewLog = new JMenuItem("View Machines");
        viewLog.addActionListener((e) -> {
            ui.openComponent(new FactoryMachinePanel(), "Machines", UIImage.FILE);
        });
        viewLog.setIcon(new ImageIcon(UIImage.FILE));

        JMenuItem addLog = new JMenuItem("Add New Machine");
        addLog.addActionListener((e) -> {
            ui.openComponent(new FactoryMachineAddModPanel(true), "Add Machine", UIImage.ADD_FILE);
        });
        addLog.setIcon(new ImageIcon(UIImage.ADD_FILE));

        JMenuItem machine = new JMenuItem("Responsibility");
        machine.addActionListener((e) -> {
            ui.openComponent(new FactoryPanel(), "Responsibility", UIImage.USER);
        });
        machine.setIcon(new ImageIcon(UIImage.USER));

        add(viewLog);
        add(addLog);
        add(new JSeparator());
        add(machine);
    }

}
