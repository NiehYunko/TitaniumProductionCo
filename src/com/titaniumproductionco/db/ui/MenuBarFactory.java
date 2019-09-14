package com.titaniumproductionco.db.ui;

import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.titaniumproductionco.db.services.Role;
import com.titaniumproductionco.db.ui.component.UIMenu;
import com.titaniumproductionco.db.ui.role.ceo.DataMenu;
import com.titaniumproductionco.db.ui.role.factsv.FactorySVMenu;
import com.titaniumproductionco.db.ui.role.hr.AssetsMenu;
import com.titaniumproductionco.db.ui.role.hr.HRMenu;
import com.titaniumproductionco.db.ui.role.machineOP.MachineOPMenu;
import com.titaniumproductionco.db.ui.role.minesv.MineSVMenu;
import com.titaniumproductionco.db.ui.role.sales.SalesMenu;

public class MenuBarFactory implements IMenuBarFactory {

    @Override
    public JMenuBar createMenuBarFor(Role role, UIFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new UserMenu(frame));
        if (role == Role.HUMAN_RESOURCE) {
            menuBar.add(new HRMenu(frame));
            menuBar.add(new AssetsMenu(frame));
        } else if (role == Role.MINE_SUPERVISOR) {
            menuBar.add(new MineSVMenu(frame));
        } else if (role == Role.MACHINE_OPERATOR) {
            menuBar.add(new MachineOPMenu(frame));
        } else if (role == Role.FACTORY_SUPERVISOR) {
            menuBar.add(new FactorySVMenu(frame));
            menuBar.add(new SalesMenu(frame));
        } else if (role == Role.SALES_DEPART) {
            menuBar.add(new SalesMenu(frame));
        } else if (role == Role.CEO) {
            menuBar.add(new AssetsMenu(frame));
            menuBar.add(new HRMenu(frame));
            menuBar.add(new DataMenu(frame));
        }

        return menuBar;
    }

    @SuppressWarnings("serial")
    private static class UserMenu extends UIMenu {

        private UserMenu(UIFrame frame) {
            super("[User]", frame);
        }

        @Override
        protected void init() {
            JMenuItem exitItem = new JMenuItem("Exit");
            exitItem.addActionListener((e) -> {
                ui.exitWithConfirmation();
            });
            exitItem.setIcon(new ImageIcon(UIImage.EXIT));
            add(exitItem);
        }
    }

}
