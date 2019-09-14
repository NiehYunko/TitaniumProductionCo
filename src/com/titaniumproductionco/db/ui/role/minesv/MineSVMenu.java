package com.titaniumproductionco.db.ui.role.minesv;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.titaniumproductionco.db.ui.UIFrame;
import com.titaniumproductionco.db.ui.UIImage;
import com.titaniumproductionco.db.ui.component.UIMenu;

@SuppressWarnings("serial")
public class MineSVMenu extends UIMenu {

    public MineSVMenu(UIFrame ui) {
        super("[Mine Supervisor]", ui);
    }

    @Override
    protected void init() {

        JMenuItem viewLog = new JMenuItem("View Mining Logs");
        viewLog.addActionListener((e) -> {
            ui.openComponent(new MiningLogPanel(), "Mining Log", UIImage.FILE);
        });
        viewLog.setIcon(new ImageIcon(UIImage.FILE));
        JMenuItem viewOreShip = new JMenuItem("View Ore Shipping Logs");
        viewOreShip.addActionListener((e) -> {
            ui.openComponent(new OreShippingLogPanel(), "Ore Shipping Log", UIImage.FILE);
        });
        viewOreShip.setIcon(new ImageIcon(UIImage.FILE));
        JMenuItem addMiningLog = new JMenuItem("Add Mining Log");
        addMiningLog.addActionListener((e) -> {
            ui.openComponent(new MiningLogAddModPanel(true), "Add Mining Log", UIImage.ADD_FILE);
        });
        addMiningLog.setIcon(new ImageIcon(UIImage.ADD_FILE));
        JMenuItem addOreShip = new JMenuItem("Add Ore Shipping Log");
        addOreShip.addActionListener((e) -> {
            ui.openComponent(new OreShippingLogAddModPanel(true), "Add Ore Shipping Log", UIImage.ADD_FILE);
        });
        addOreShip.setIcon(new ImageIcon(UIImage.ADD_FILE));
        JMenuItem viewMine = new JMenuItem("Responsibility");
        viewMine.addActionListener((e) -> {
            ui.openComponent(new MinePanel(false), "Responsibility", UIImage.USER);
        });
        viewMine.setIcon(new ImageIcon(UIImage.USER));

        add(viewLog);
        add(addMiningLog);
        add(new JSeparator());
        add(viewOreShip);
        add(addOreShip);
        add(new JSeparator());
        add(viewMine);
    }

}
