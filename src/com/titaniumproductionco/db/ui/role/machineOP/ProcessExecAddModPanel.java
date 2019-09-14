package com.titaniumproductionco.db.ui.role.machineOP;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.titaniumproductionco.db.services.DBService;
import com.titaniumproductionco.db.services.Role;
import com.titaniumproductionco.db.services.func.ConditionedView;
import com.titaniumproductionco.db.services.func.ErrorMes;
import com.titaniumproductionco.db.services.func.Proc;
import com.titaniumproductionco.db.services.func.View;
import com.titaniumproductionco.db.ui.component.IntegerField;

@SuppressWarnings("serial")
public class ProcessExecAddModPanel extends JPanel {
    private int oldProcExecID;
    private Map<String, IntegerField> qtyFields;

    public ProcessExecAddModPanel(boolean add) {
        this.qtyFields = new HashMap<>();
        this.setPreferredSize(new Dimension(500, 270));
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JPanel input = new JPanel();
        input.setLayout(new GridLayout(3, 2));
        Border border = BorderFactory.createTitledBorder("Input");
        input.setBorder(border);
        JPanel output = new JPanel();
        output.setLayout(new GridLayout(2, 2));
        Border outputborder = BorderFactory.createTitledBorder("Output");
        output.setBorder(outputborder);
        new ConditionedView(View.MaterialForMachine).where(true, "Operator=?", Types.NVARCHAR, DBService.getUser()).select("[Material],[IsInput]", (rs) -> {
            while (rs.next()) {
                String mat = rs.getString("Material");
                IntegerField qtyField = new IntegerField();
                if (rs.getBoolean("IsInput")) {
                    input.add(new JLabel(mat));
                    input.add(qtyField);
                } else {
                    output.add(new JLabel(rs.getString("Material")));
                    output.add(qtyField);
                }
                qtyFields.put(mat, qtyField);
            }
        });

        this.add(input);
        this.add(output);

        if (add) {
            JButton button = new JButton("Add");
            button.addActionListener(this::handleAdd);
            this.add(button);
        }

    }

    public void setOld(int execID) {
        oldProcExecID = execID;
        new ConditionedView(View.ProcessExecutionLog).where(true, "ProcExecID=?", Types.INTEGER, oldProcExecID).select("Material,Moles", (rs) -> {
            while (rs.next()) {
                String mat = rs.getString("Material");
                IntegerField field = qtyFields.get(mat);
                if (field != null)
                    field.setText(String.valueOf(rs.getInt("Moles")));
            }
        });

    }

    private boolean validateInputs() {
        if (!DBService.validateRoleWithError(this, Role.MACHINE_OPERATOR))
            return false;
        for (IntegerField field : qtyFields.values()) {
            if (!field.isInputValidPos()) {
                JOptionPane.showMessageDialog(this, "One or more quantity is invalid.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private Map<String, Integer> parseUserInputs() {
        HashMap<String, Integer> map = new HashMap<>();
        for (Entry<String, IntegerField> e : qtyFields.entrySet()) {
            map.put(e.getKey(), e.getValue().getParsedInt());
        }
        return map;
    }

    private void handleAdd(ActionEvent event) {
        if (!validateInputs())
            return;

        DBService.useTransaction(this, "Execution Log Added!", ErrorMes.ERROR_MESSAGE, (tran) -> {
            String operator = DBService.getUser();
            Map<String, Object> map = Proc.MachineOp_AddProcessLog.call(tran, this, operator);
            if (map == null || !map.containsKey("ProcExecID")) {
                tran.setRollback();
                return;
            }
            int procID = (int) map.get("ProcExecID");
            for (Entry<String, Integer> e : parseUserInputs().entrySet()) {
                String material = e.getKey();
                int quantity = e.getValue();
                if (Proc.MachineOp_AddUsedIn.call(tran, this, procID, material, quantity) == null) {
                    tran.setRollback();
                    return;
                }
            }
           if (Proc.MachineOp_CheckProcessLog.call(tran,this,procID) == null) {
        	   tran.setRollback();
        	   return;
           }
            tran.setCommit();
        });
    }

    public boolean handleUpdate() {
        if (!validateInputs())
            return false;
        return DBService.useTransaction(this, "Execution Log Updated!", ErrorMes.ERROR_MESSAGE, (tran) -> {
            int procID = oldProcExecID;
            for (Entry<String, Integer> e : parseUserInputs().entrySet()) {
                String material = e.getKey();
                int quantity = e.getValue();
                if (Proc.MachineOp_UpdateUsedIn.call(tran, this, procID, material, quantity) == null) {
                    tran.setRollback();
                    return;
                }
            }
            if (Proc.MachineOp_CheckProcessLog.call(tran,this,procID) == null) {
         	   tran.setRollback();
         	   return;
            }
            tran.setCommit();
        });
    }

}
