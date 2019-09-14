package com.titaniumproductionco.db.ui.role.sales;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import com.titaniumproductionco.db.services.DBService;
import com.titaniumproductionco.db.services.Role;
import com.titaniumproductionco.db.ui.UIFrame;
import com.titaniumproductionco.db.ui.UIImage;
import com.titaniumproductionco.db.ui.component.UIMenu;

@SuppressWarnings("serial")
public class SalesMenu extends UIMenu {

    public SalesMenu(UIFrame ui) {
        super("[Order]", ui);
    }

    @Override
    protected void init() {
        JMenuItem viewOrder = new JMenuItem("View Orders");
        viewOrder.addActionListener((e) -> {
            ui.openComponent(new SalesOrderPanel(), "Orders", UIImage.FILE);
        });
        viewOrder.setIcon(new ImageIcon(UIImage.FILE));

        JMenuItem addOrder = new JMenuItem("Add Order");
        addOrder.addActionListener((e) -> {
            ui.openComponent(new SalesOrderAddModPanel(true), "Add Order", UIImage.ADD_FILE);
        });
        addOrder.setIcon(new ImageIcon(UIImage.ADD_FILE));

        if (DBService.getRole() != Role.SALES_DEPART)
            addOrder.setEnabled(false);

        add(viewOrder);
        add(addOrder);

    }

}
