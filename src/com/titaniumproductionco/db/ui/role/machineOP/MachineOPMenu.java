package com.titaniumproductionco.db.ui.role.machineOP;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.titaniumproductionco.db.ui.UIFrame;
import com.titaniumproductionco.db.ui.UIImage;
import com.titaniumproductionco.db.ui.component.UIMenu;

@SuppressWarnings("serial")
public class MachineOPMenu extends UIMenu {

    public MachineOPMenu(UIFrame ui) {
        super("[Machine Operator]", ui);

    }

    @Override
    protected void init() {

        JMenuItem viewLog = new JMenuItem("View Process Execution Logs");
        viewLog.addActionListener((e) -> {
            ui.openComponent(new ProcessExecutionLogPanel(), "Process Execution Log", UIImage.FILE);
        });
        viewLog.setIcon(new ImageIcon(UIImage.FILE));

        JMenuItem addLog = new JMenuItem("Add New Log");
        addLog.addActionListener((e) -> {
            ui.openComponent(new ProcessExecAddModPanel(true), "Add Process Execution Log", UIImage.ADD_FILE);
        });
        addLog.setIcon(new ImageIcon(UIImage.ADD_FILE));

        JMenuItem machine = new JMenuItem("Responsibility");
        machine.addActionListener((e) -> {
            ui.openComponent(new MachinePanel(), "Responsibility", UIImage.USER);
        });
        machine.setIcon(new ImageIcon(UIImage.USER));

        add(viewLog);
        add(addLog);
        add(new JSeparator());
        add(machine);

    }

}
