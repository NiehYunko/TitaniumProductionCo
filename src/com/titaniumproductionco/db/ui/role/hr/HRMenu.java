package com.titaniumproductionco.db.ui.role.hr;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import com.titaniumproductionco.db.services.DBService;
import com.titaniumproductionco.db.services.Role;
import com.titaniumproductionco.db.ui.UIFrame;
import com.titaniumproductionco.db.ui.UIImage;
import com.titaniumproductionco.db.ui.component.UIMenu;

@SuppressWarnings("serial")
public class HRMenu extends UIMenu {

    public HRMenu(UIFrame ui) {
        super("[Human Resource]", ui);
    }

    @Override
    protected void init() {
        JMenuItem viewUser = new JMenuItem("View Users");
        viewUser.addActionListener((e) -> {
            ui.openComponent(new UserListPanel(), "Users", UIImage.FILE);
        });
        viewUser.setIcon(new ImageIcon(UIImage.FILE));
        add(viewUser);
        if (DBService.getRole() == Role.HUMAN_RESOURCE) {
            JMenuItem addUser = new JMenuItem("Add User");
            addUser.addActionListener((e) -> {
                ui.openComponent(new UserAddPanel(), "Add User", UIImage.ADD_FILE);
            });

            addUser.setIcon(new ImageIcon(UIImage.ADD_FILE));

            add(addUser);

        }
    }

}
