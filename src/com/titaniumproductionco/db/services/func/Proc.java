package com.titaniumproductionco.db.services.func;

import static com.titaniumproductionco.db.services.func.ErrorMes.ERROR_MESSAGE;
import static com.titaniumproductionco.db.services.func.Parameter.*;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.titaniumproductionco.db.services.DBService;

/**
 * Helper to handle calls from stored procedure
 *
 */
public enum Proc {
    Login_Check(
        nvarchar("Username"),
        varchar("PasswordHash").out(),
        varchar("PasswordSalt").out()),
    Login_New(
        ERROR_MESSAGE,
        nvarchar("Username"),
        varchar("PasswordHash"),
        varchar("PasswordSalt"),
        varchar("Role"),
        integer("ArgumentID")),
    Login_Role(
        nvarchar("Username"),
        varchar("Role").out()),

    Login_Remove(
        nvarchar("Username")),

    MineSV_AddMiningLog(
        "Mining Log Added!",
        ERROR_MESSAGE,
        date("Date"),
        nvarchar("SupervisorUsername"),
        integer("Quantity"),
        nvarchar("MaterialDescription")),

    MineSV_UpdateMiningLog(
        "Mining Log Updated!",
        ERROR_MESSAGE,
        date("NewDate"),
        date("OldDate"),
        nvarchar("SupervisorUsername"),
        integer("Quantity"),
        nvarchar("NewMaterialDescription"),
        nvarchar("OldMaterialDescription")),

    MineSV_DeleteMiningLog(
        "Mining Log Deleted!",
        ERROR_MESSAGE,
        date("Date"),
        nvarchar("SupervisorUsername"),
        nvarchar("MaterialDescription")),

    MineSV_AddShipOresLog(
        "Ore Shipping Log Added!",
        ERROR_MESSAGE,
        date("Date"),
        nvarchar("SupervisorUsername"),
        integer("Quantity"),
        nvarchar("MaterialDescription"),
        nvarchar("FactoryAddr")),

    MineSV_UpdateShipOresLog(
        "Ore Shipping Log Updated!",
        ERROR_MESSAGE,
        date("NewDate"),
        date("OldDate"),
        nvarchar("SupervisorUsername"),
        integer("Quantity"),
        nvarchar("NewMaterialDescription"),
        nvarchar("OldMaterialDescription"),
        nvarchar("NewFactoryAddr"),
        nvarchar("OldFactoryAddr")),

    MineSV_DeleteShipOresLog(
        "Ore Shipping Log Deleted!",
        ERROR_MESSAGE,
        date("Date"),
        nvarchar("SupervisorUsername"),
        nvarchar("MaterialDescription"),
        nvarchar("FactoryAddr")),

    MachineOp_AddProcessLog(
        nvarchar("MachineOpUsername"),
        integer("ProcExecID").out()),

    MachineOp_AddUsedIn(
        integer("ProcessExecID"),
        varchar("Materialname"),
        integer("Quantity")),

    MachineOp_DeleteProcessExecution(
        "Process Execution Deleted Successfully",
        ERROR_MESSAGE,
        integer("ProcExecID")),

    MachineOp_UpdateUsedIn(
        "Process Execution Updated Successfully.",
        ERROR_MESSAGE,
        integer("ProcessExecID"),
        varchar("MaterialName"),
        integer("Quantity")),

    MachineOp_CheckProcessLog(
        integer("ProcessExecID")),

    FactorySV_DeleteMachine(
        "Machine Deleted Successfully.",
        ERROR_MESSAGE,
        integer("MachineID")),

    FactorySV_AddMachine(
        "Machine Added Successfully.",
        ERROR_MESSAGE,
        date("Date"),
        nvarchar("SVUsername"),
        decimal("Cycles"),
        nvarchar("ProcessName")),
    FactorySV_UpdateMachine(
        "Machine Updated Successfully.",
        ERROR_MESSAGE,
        nvarchar("SVUsername"),
        integer("MachineID"),
        decimal("Cycles"),
        date("Date"),
        nvarchar("ProcessName")),

    FactorySV_AddShippedTo(
        "Order Shipped Succesfully.",
        ERROR_MESSAGE,
        nvarchar("Supervisor"),
        integer("OrderID"),
        date("Date")),

    FactorySV_DeleteShippedTo(
        "Order Unshipped!",
        ERROR_MESSAGE,
        nvarchar("Username"),
        integer("OrderID")),

    Sales_DeleteOrder(
        "Order Deleted Successfully.",
        ERROR_MESSAGE,
        integer("OrderID")),

    Sales_AddOrder(
        "Order Added Successfully.",
        ERROR_MESSAGE,
        date("DatePlaced"),
        nvarchar("ShipAddr"),
        integer("Quantity")),

    Sales_UpdateOrder(
        "Order Updated Successfully.",
        ERROR_MESSAGE,
        integer("OrderID"),
        date("Date"),
        nvarchar("ShipmentAddr"),
        integer("Quantity")),

    Sales_UpdateShippedTo(
        "Received Date Set Successfully!",
        ERROR_MESSAGE,
        integer("OrderID"),
        date("ReceiveDate")),

    DB_Import_Mine(
        integer("ID"),
        nvarchar("City"),
        nvarchar("State"),
        nvarchar("Country"),
        date("DiscoverDate"),
        date("EstExhaustDate")),

    DB_Import_Mines(
        date("Date"),
        integer("MineID"),
        integer("MaterialID"),
        integer("Quantity")),

    DB_Import_Factory(
        integer("ID"),
        nvarchar("Address")),

    DB_Import_FactorySupervisor(
        integer("SupervisorID"),
        integer("FactoryID")),

    DB_Import_Login(
        integer("ID"),
        nvarchar("Username"),
        varchar("PasswordSalt"),
        varchar("PasswordHash"),
        varchar("Role")),

    DB_Import_Machine(
        integer("ID"),
        decimal("CyclesPerDay"),
        date("StartUseDate"),
        integer("ProcessID"),
        integer("FactoryID")),

    DB_Import_MachineOperator(
        integer("OperatorID"),
        integer("MachineID")),

    DB_Import_MineSupervisor(
        integer("SupervisorID"),
        integer("MineID")),

    DB_Import_Order(
        integer("ID"),
        date("DatePlaced"),
        nvarchar("ShipmentAddr"),
        integer("Quantity")),

    DB_Import_ProcessExecution(
        integer("ID"),
        integer("UsedMachineID"),
        timestamp("TimeStamp")),

    DB_Import_ShipOresTo(
        date("Date"),
        integer("MineID"),
        integer("FactoryID"),
        integer("Quantity"),
        integer("MaterialID")),

    DB_Import_ShippedTo(
        integer("FactoryID"),
        integer("OrderID"),
        date("ShipDate"),
        date("ReceiveDate")),

    DB_Import_UsedIn(
        integer("MaterialID"),
        integer("ProcessExecID"),
        integer("Quantity"))

    ;

    private final Parameter[] PARAMS;
    private int inArgs;
    private IErrorMessage errorHandler;
    private String successHandler;

    Proc(Parameter... parameters) {
        this(null, null, parameters);
    }

    Proc(IErrorMessage error, Parameter... parameters) {
        this(null, error, parameters);
    }

    /**
     * Define a stored procedure.<br>
     * 
     * The parameters include both input and output parameters. They do not have to
     * be in the same order as defined in the database. However, a call to this
     * stored procedure through Proc.call() must supply arguments in the same order
     * as it is defined here.<br>
     * 
     * Although it is not enforced, you should put output parameters in the end. If
     * an output parameter is put between two input parameters, you MUST SUPPLY A
     * NULL VALUE to the parameter when calling.
     * 
     * @param success    The message to display when the stored procedure returns 0
     * @param error      The handler to get error messages when a non-zero value is
     *                   returned. The error message will automatically be displayed
     * @param parameters parameters of the stored procedure. See Parameter class
     */
    Proc(String success, IErrorMessage error, Parameter... parameters) {
        PARAMS = parameters;
        inArgs = 0;
        for (Parameter p : PARAMS) {
            if (!p.isOut())
                inArgs++;
        }
        successHandler = success;
        errorHandler = error;
    }

    /**
     * Call the stored procedure with the given parameters<br>
     * 
     * The parameters MUST BE IN THE SAME ORDER as they are defined in the enum
     * constructor.<br>
     * 
     * Output parameters in the end can be ignored. However, output parameters
     * between two input parameters must be included as a <code>null</code>.
     * Therefore it is recommended that output parameters to be put in the end in
     * the declaration<br>
     * 
     * The returned map does not include the error code. Error handling should be
     * solely done by IErrorMessage defined
     * 
     * @param args
     * @return The output parameters as a map
     */
    public Map<String, Object> call(Object... args) {
        return call(null, args);
    }

    /**
     * Call the stored procedure. See call(Object...)
     * 
     * @param parent The parent component to display the success/error message on
     * @param args
     * @return The output parameters as a map
     */
    public Map<String, Object> call(JComponent parent, Object... args) {
        try {
            return call(null, parent, args);
        } catch (SQLException e) {
        }
        return null;
    }

    /**
     * Core method for calling stored procedures<br>
     * 
     * See call(Object...) for basic description<br>
     * 
     * When a transaction is involved, the system will set the tracker's return
     * value automatically and will suppress all messages to ensure the transaction
     * is not paused halfway<br>
     * 
     * When using transaction, the error messages and success messages defined will
     * be overriden by the ones supplied to the call to transaction. See
     * DBService.useTransaction()<br>
     * 
     * @param tran   The transaction tracker used for this stored procedure. A
     *               transaction should be initiated with
     *               DBService.useTransaction(). See ITransaction for detail.
     * @param parent The component to display messages on.
     * @param args   The arguments
     * @return A map of output parameters
     * @throws SQLException
     */
    public Map<String, Object> call(TransactionTracker tran, JComponent parent, Object... args) throws SQLException {
        if (!ProcOutput.suppressed)
            System.out.println("[PROC] Calling PROC " + name());
        if (args.length != inArgs)
            throw new IllegalArgumentException("Wrong number of arguments!");
        StringBuilder build = new StringBuilder("{?=CALL ");
        build.append(name()).append("(");
        for (int i = 0; i < PARAMS.length; i++) {
            if (i != 0)
                build.append(',');
            build.append('?');
        }
        build.append(")}");
        CallableStatement stmt = null;
        int returnValue = -25565;
        try {
            stmt = DBService.getConnection().prepareCall(build.toString());
            for (int i = 0; i < PARAMS.length; i++) {
                PARAMS[i].set(stmt, i < args.length ? args[i] : null);
            }
            stmt.registerOutParameter(1, Types.INTEGER);

            stmt.execute();

            returnValue = stmt.getInt(1);
            if (tran != null)
                tran.lastReturnValue = returnValue;
            if (returnValue == 0) {
                if (successHandler != null && tran == null) {
                    JOptionPane.showMessageDialog(parent, successHandler, "Message", JOptionPane.INFORMATION_MESSAGE);
                }
                HashMap<String, Object> map = new HashMap<>();
                for (int i = 0; i < PARAMS.length; i++) {
                    PARAMS[i].get(stmt, map);
                }
                return map;
            } else {
                if (tran == null) {
                    if (errorHandler != null) {
                        String err = errorHandler.getErrorMessage(returnValue);
                        if (err == null)
                            err = "Unknown Error";
                        JOptionPane.showMessageDialog(parent, err, "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(parent, "Unknown Error", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (tran != null) {
                tran.lastReturnValue = returnValue;
                tran.setRollback();
                throw e;
            }
        } finally {
            DBService.closeStmtQuietly(stmt);
            if (!ProcOutput.suppressed)
                System.out.println("[PROC] " + name() + " returned " + returnValue);
        }
        if (tran == null)
            JOptionPane.showMessageDialog(parent, "Unknown Error", "Error", JOptionPane.ERROR_MESSAGE);
        return null;
    }
}
