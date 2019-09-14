package com.titaniumproductionco.db.ui.role.sales;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.sql.Types;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.titaniumproductionco.db.services.DBService;
import com.titaniumproductionco.db.services.Role;
import com.titaniumproductionco.db.services.func.ConditionedView;
import com.titaniumproductionco.db.services.func.Proc;
import com.titaniumproductionco.db.services.func.View;
import com.titaniumproductionco.db.ui.UIImage;
import com.titaniumproductionco.db.ui.component.DateRangeSelector;
import com.titaniumproductionco.db.ui.component.DateSelector;
import com.titaniumproductionco.db.ui.component.IntegerField;
import com.titaniumproductionco.db.ui.component.RadioSelectionPanel;
import com.titaniumproductionco.db.ui.model.UITableModel;

@SuppressWarnings("serial")
public class SalesOrderPanel extends JPanel {

    private JTable table;
    private JPopupMenu popupMenu;
    private JPanel searchPanel;

    private RadioSelectionPanel searchSelectionPanel;
    private JComboBox<String> searchOrderTypeSelector;
    private IntegerField searchOrderIDField;

    private DateRangeSelector searchDateSelector;

    public SalesOrderPanel() {
        setPreferredSize(new Dimension(1000, 600));
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(table.getFontMetrics(table.getFont()).getHeight());
        JScrollPane scroll = new JScrollPane(table);

        FontMetrics fm = getFontMetrics(getFont());
        int height = fm.getHeight();

        searchPanel = new JPanel();
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Order"));
        searchPanel.setPreferredSize(new Dimension(700, 150));

        searchSelectionPanel = new RadioSelectionPanel();
        searchOrderTypeSelector = new JComboBox<>(new String[] { "All Orders", "UnShipped", "Unreceived" });
        searchOrderTypeSelector.setPreferredSize(new Dimension(200, height + 8));
        searchSelectionPanel.addSelectionComponent("By Type", searchOrderTypeSelector);
        searchOrderIDField = new IntegerField();
        searchOrderIDField.setPreferredSize(new Dimension(200, height + 8));
        searchSelectionPanel.addSelectionComponent("By OrderID", searchOrderIDField);
        searchPanel.add(searchSelectionPanel);

        searchDateSelector = new DateRangeSelector();
        searchDateSelector.setFromDate(DateSelector.daysAfterCurrentDate(-7));
        searchPanel.add(searchDateSelector);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(this::updateTableModel);
        searchPanel.add(searchButton);

        this.add(searchPanel, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);

        popupMenu = new JPopupMenu();
        Role r = DBService.getRole();
        if (r == Role.FACTORY_SUPERVISOR) {
            JMenuItem ship = new JMenuItem("Ship This Order");
            ship.addActionListener(this::handleShip);
            ship.setIcon(new ImageIcon(UIImage.UPDATE_FILE));

            JMenuItem unship = new JMenuItem("Unship This Order");
            unship.addActionListener(this::handleUnship);
            unship.setIcon(new ImageIcon(UIImage.REMOVE_FILE));

            popupMenu.add(ship);
            popupMenu.add(unship);
        } else if (r == Role.SALES_DEPART) {
            JMenuItem updateReceive = new JMenuItem("Update Receive Date...");
            updateReceive.addActionListener(this::handleReceive);
            updateReceive.setIcon(new ImageIcon(UIImage.UPDATE_FILE));

            JMenuItem updateOrder = new JMenuItem("Update Order...");
            updateOrder.addActionListener(this::handleUpdate);
            updateOrder.setIcon(new ImageIcon(UIImage.UPDATE_FILE));

            JMenuItem deleteLog = new JMenuItem("Delete");
            deleteLog.addActionListener(this::handleDeleteLog);
            deleteLog.setIcon(new ImageIcon(UIImage.REMOVE_FILE));

            popupMenu.add(updateReceive);
            popupMenu.add(updateOrder);
            popupMenu.add(deleteLog);
        }

        JMenuItem refresh = new JMenuItem("Refresh");
        refresh.addActionListener(this::updateTableModel);
        refresh.setIcon(new ImageIcon(UIImage.REFRESH));

        popupMenu.add(new JSeparator());
        popupMenu.add(refresh);
        scroll.setComponentPopupMenu(popupMenu);
        table.setComponentPopupMenu(popupMenu);

        updateTableModel(null);
    }

    public void updateTableModel(ActionEvent e) {
        // permission check for now
        if (!DBService.validateRoleWithError(this, Role.SALES_DEPART, Role.FACTORY_SUPERVISOR)) {
            table.setModel(new DefaultTableModel());
            return;
        }

        String supervisor = DBService.getUser();
        ConditionedView v = new ConditionedView(View.OrderList);
        if (DBService.getRole() == Role.FACTORY_SUPERVISOR) {
            v.where(true, "[Shipped On] IS NULL OR Supervisor=?", Types.NVARCHAR, supervisor);
        }
        Object selectedSearch = searchSelectionPanel.getEnabledComponent();
        if (selectedSearch == searchOrderTypeSelector) {
            switch ((String) searchOrderTypeSelector.getSelectedItem()) {
            case "All Order":
                break;
            case "UnShipped":
                v.where("[Shipped On] IS NULL");
                break;
            case "Unreceived":
                v.where("[Received On] IS NULL");
                break;
            }
        } else if (selectedSearch == searchOrderIDField) {
            if (searchOrderIDField.isInputValidPos()) {
                v.where(true, "[Order]=?", Types.INTEGER, searchOrderIDField.getParsedInt());
            } else {
                table.setModel(new DefaultTableModel());
                return;
            }
        }

        Date from = searchDateSelector.getFromDate();
        Date to = searchDateSelector.getToDate();
        v.where(from != null, "[Placed On]>=?", Types.DATE, from).where(to != null, "[Placed On]<=?", Types.DATE, to);

        boolean successful = v.select("[Order],[Placed On],[Ship To],[Quantity],[Shipped On],[Shipped From],[Received On]", (rs) -> {
            table.setModel(UITableModel.createTableModel(rs));
        });

        if (!successful) {
            table.setModel(new DefaultTableModel());
        }

    }

    private boolean handleDeleteLog(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "Please choose a row!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        StringBuilder build = new StringBuilder().append("<html>Are you sure to delete this Order?<br>");
        build.append("<br>Order ID: ").append(table.getModel().getValueAt(row, 0));
        build.append("<br>Shipment Address: ").append(table.getModel().getValueAt(row, 1));
        build.append("<br>Quantity: ").append(table.getModel().getValueAt(row, 2));
        build.append("<br>Ship Date: ").append(table.getModel().getValueAt(row, 3));
        build.append("<br>Receive Date: ").append(table.getModel().getValueAt(row, 3));
        build.append("<br></html>");
        int op = JOptionPane.showConfirmDialog(this, new JLabel(build.toString()), "Confirm", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            boolean successful = sendDeleteLogQuery(Integer.parseInt((String) table.getModel().getValueAt(row, 0)));
            updateTableModel(e);
            return successful;
        } else {
            return false;
        }
    }

    private boolean sendDeleteLogQuery(int OrderID) {
        if (!DBService.validateRoleWithError(this, Role.SALES_DEPART))
            return false;
        if (OrderID < 0) {
            JOptionPane.showMessageDialog(this, "Invalid Inputs", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return Proc.Sales_DeleteOrder.call(this, OrderID) != null;
    }

    private boolean handleReceive(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "Please choose a row!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        int orderID = Integer.parseInt((String) table.getModel().getValueAt(row, 0));
        JPanel panel = new JPanel();
        DateSelector receiveDateSelector = new DateSelector();
        panel.add(new JLabel("Choose Receive Date"));
        receiveDateSelector.setPreferredSize(new Dimension(200, 30));
        panel.add(receiveDateSelector);
        while (true) {
            int op = JOptionPane.showConfirmDialog(this, panel, "Received Order", JOptionPane.OK_CANCEL_OPTION);
            if (op == JOptionPane.OK_OPTION) {
                Date receiveDate = receiveDateSelector.getDate();
                boolean successful = Proc.Sales_UpdateShippedTo.call(this, orderID, receiveDate) != null;

                if (successful) {
                    updateTableModel(e);
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    private boolean handleUpdate(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "Please choose a row!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int orderID = Integer.parseInt((String) table.getValueAt(row, 0));
        Date oldDate = DateSelector.parseDate((String) table.getValueAt(row, 1));
        String oldAddress = (String) table.getValueAt(row, 2);
        int oldQuantity = Integer.parseInt((String) table.getValueAt(row, 3));

        SalesOrderAddModPanel panel = new SalesOrderAddModPanel(false);
        panel.setOld(orderID, oldDate, oldQuantity, oldAddress);
        while (true) {
            int op = JOptionPane.showConfirmDialog(this, panel, "Received Order", JOptionPane.OK_CANCEL_OPTION);
            if (op == JOptionPane.OK_OPTION) {
                boolean successful = panel.handleUpdate();

                if (successful) {
                    updateTableModel(e);
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    private void handleShip(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "Please choose a row!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!DBService.validateRoleWithError(this, Role.FACTORY_SUPERVISOR))
            return;

        JPanel panel = new JPanel();
        DateSelector shipDateSelector = new DateSelector();
        panel.add(new JLabel("Choose Ship Date"));
        shipDateSelector.setPreferredSize(new Dimension(200, 30));
        panel.add(shipDateSelector);
        while (true) {
            int op = JOptionPane.showConfirmDialog(this, panel, "Ship Order", JOptionPane.OK_CANCEL_OPTION);
            if (op == JOptionPane.OK_OPTION) {
                Date shipDate = shipDateSelector.getDate();
                int orderID = Integer.parseInt((String) table.getModel().getValueAt(row, 0));
                String supervisor = DBService.getUser();

                boolean successful = Proc.FactorySV_AddShippedTo.call(this, supervisor, orderID, shipDate) != null;
                if (successful) {
                    updateTableModel(e);
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void handleUnship(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "Please choose a row!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!DBService.validateRoleWithError(this, Role.FACTORY_SUPERVISOR))
            return;
        int orderID = Integer.parseInt((String) table.getModel().getValueAt(row, 0));
        String supervisor = DBService.getUser();

        Proc.FactorySV_DeleteShippedTo.call(this, supervisor, orderID);
        updateTableModel(e);
    }
}
