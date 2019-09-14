package com.titaniumproductionco.db.ui.role.hr;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.titaniumproductionco.db.services.DBService;
import com.titaniumproductionco.db.services.Role;
import com.titaniumproductionco.db.ui.UIFrame;
import com.titaniumproductionco.db.ui.UIImage;
import com.titaniumproductionco.db.ui.component.UIMenu;
import com.titaniumproductionco.db.ui.role.factsv.FactoryPanel;
import com.titaniumproductionco.db.ui.role.machineOP.MachinePanel;
import com.titaniumproductionco.db.ui.role.minesv.MinePanel;

@SuppressWarnings("serial")
public class AssetsMenu extends UIMenu {

    public AssetsMenu(UIFrame ui) {
        super("[Assets]", ui);
    }

    @Override
    protected void init() {
        JMenuItem mine = new JMenuItem("View Mines");
        mine.addActionListener((e) -> {
            ui.openComponent(new MinePanel(false), "Mines", UIImage.FILE);
        });
        mine.setIcon(new ImageIcon(UIImage.FILE));
        JMenuItem mineComplex = new JMenuItem("View Mines (Detailed)");
        mineComplex.addActionListener((e) -> {
            ui.openComponent(new MinePanel(true), "Mines (Detailed)", UIImage.FILE);
        });
        mineComplex.setIcon(new ImageIcon(UIImage.FILE));
        JMenuItem factory = new JMenuItem("View Factories");
        factory.addActionListener((e) -> {
            ui.openComponent(new FactoryPanel(), "Factories", UIImage.FILE);
        });
        factory.setIcon(new ImageIcon(UIImage.FILE));
        JMenuItem machine = new JMenuItem("View Machines");
        machine.addActionListener((e) -> {
            ui.openComponent(new MachinePanel(), "Machines", UIImage.FILE);
        });
        machine.setIcon(new ImageIcon(UIImage.FILE));

        Role r = DBService.getRole();
        if (r != Role.CEO) {
            mineComplex.setEnabled(false);
        }

        add(mine);
        add(mineComplex);
        add(new JSeparator());
        add(factory);
        add(machine);

    }

}
