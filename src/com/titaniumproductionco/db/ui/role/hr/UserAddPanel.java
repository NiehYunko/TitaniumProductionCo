package com.titaniumproductionco.db.ui.role.hr;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.ListDataListener;

import com.titaniumproductionco.db.services.LoginService;
import com.titaniumproductionco.db.services.Role;
import com.titaniumproductionco.db.services.func.ConditionedView;
import com.titaniumproductionco.db.services.func.OrderBy;
import com.titaniumproductionco.db.services.func.View;
import com.titaniumproductionco.db.ui.component.ViewComboBox;

@SuppressWarnings("serial")
public class UserAddPanel extends JPanel {
    private JComboBox<Role> roleBox;
    private JButton addButton;
    private JTextField userField;
    private JPasswordField passwordField;
    private JLabel argumentLabel;
    private JComponent argumentSelector;

    public UserAddPanel() {

        roleBox = new JComboBox<>(new RoleListModel());

        addButton = new JButton("Add User");

        userField = new JTextField();
        passwordField = new JPasswordField();

        this.add(new JLabel("Username:"));
        this.add(userField);

        this.setPreferredSize(new Dimension(250, 250));
        this.setLayout(new GridLayout(9, 1));
        this.add(new JLabel("Password:"));
        this.add(passwordField);
        this.add(new JLabel("Role"));

        this.add(roleBox);

        roleBox.addItemListener((e) -> {
            remove(7);
            Role r = (Role) roleBox.getSelectedItem();
            switch (r) {
            case MINE_SUPERVISOR:
                argumentLabel.setText("Select Mine");
                argumentSelector = new ViewComboBox(View.MineAddressView, "ID");
                break;
            case MACHINE_OPERATOR:
                argumentLabel.setText("Select Machine");
                argumentSelector = new ViewComboBox(View.MachineList, "Machine");
                break;
            case FACTORY_SUPERVISOR:
                argumentLabel.setText("Select Factory");
                argumentSelector = new ViewComboBox(new ConditionedView(View.FactoryList).orderBy(OrderBy.asc("ID")), "ID");
                break;
            default:
                argumentLabel.setText("");
                argumentSelector = new JLabel("");
            }
            add(argumentSelector, 7);
            doLayout();
        });

        this.add(argumentLabel = new JLabel(""));
        this.add(argumentSelector = new JLabel(""));

        this.add(addButton);
        addButton.addActionListener((e) -> {
            Role r = (Role) roleBox.getSelectedItem();

            int argID = 0;
            if (r.needsArgID()) {
                argID = Integer.parseInt((String) ((JComboBox<?>) argumentSelector).getSelectedItem());
            }
            boolean successful = LoginService.register(userField.getText(), passwordField.getPassword(), r, argID);
            if (successful) {
                JOptionPane.showMessageDialog(null, "User Added!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            userField.setText("");
            passwordField.setText("");

        });

    }

    private class RoleListModel implements ComboBoxModel<Role> {
        Role selectedItem;

        private RoleListModel() {

        }

        @Override
        public void addListDataListener(ListDataListener arg0) {

        }

        @Override
        public Role getElementAt(int i) {
            return Role.values()[i];
        }

        @Override
        public int getSize() {
            return Role.values().length;
        }

        @Override
        public void removeListDataListener(ListDataListener arg0) {

        }

        @Override
        public Role getSelectedItem() {
            return selectedItem;
        }

        @Override
        public void setSelectedItem(Object arg0) {
            selectedItem = (Role) arg0;
        }

    }

}
