package com.titaniumproductionco.db.services.func;

import static com.titaniumproductionco.db.services.func.Parameter.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.titaniumproductionco.db.services.DBService;

/**
 * Helper system for selecting from views
 *
 */
public enum View {
    MaterialList,
    OreList,

    UserList,

    MineAddressView,
    MineAndSupervisor,
    MiningLog,
    OreShippingLog,

    MachineList,
    MaterialForMachine,
    MachineAndOperator,
    ProcessExecutionLog,

    FactoryList,
    FactoryAndSupervisor,
    SupervisorMachineView,
    ProcessList,
    UnshippedOrder,

    OrderList,

    func_Analysis_Mine(
        date("FromDate"),
        date("ToDate"),
        decimal("Allowance")),

    func_Analysis_MachineEfficiency(
        date("FromDate"),
        date("ToDate"),
        decimal("Allowance")),

    func_Analysis_OreUsage(
        date("FromDate"),
        date("ToDate"),
        decimal("Allowance")),

    func_Analysis_Sale(
        date("FromDate"),
        date("ToDate"));

    private final Parameter[] PARAMS;

    View(Parameter... parameters) {
        PARAMS = parameters;
    }

    @Override
    public String toString() {
        if (PARAMS.length == 0)
            return super.name();
        StringBuilder build = new StringBuilder(super.name());
        build.append("(");
        for (int i = 0; i < PARAMS.length; i++) {
            build.append("?,");
        }
        build.setCharAt(build.length() - 1, ')');
        return build.toString();
    }

    /**
     * Select from this view the specified columns.
     * 
     * @param columns  The columns to select
     * @param callback The callback function on the result set returned by the
     *                 query. See IViewCallback
     * @return
     */
    public boolean select(String columns, IViewCallback callback, Object... parameters) {
        return select(callback, columns, "", null, null, null, parameters);
    }

    /**
     * Used for ConditionedView. This method should not be called individually to
     * avoid confusion
     */
    boolean select(IViewCallback callback, String columns, String where, List<Integer> typeList, List<Object> argList, String orderList, Object... parameters) {
        System.out.println("[VIEW] Viewing " + name());
        if (parameters.length != PARAMS.length)
            return false;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = DBService.getConnection().prepareStatement("SELECT DISTINCT " + columns + " FROM " + toString() + " " + where + " " + (orderList == null ? "" : orderList));
            int arg = 1;
            for (int i = 0; i < PARAMS.length; i++) {
                stmt.setObject(arg++, parameters[i], PARAMS[i].TYPE);
            }
            if (typeList != null && argList != null) {
                for (int i = 0; i < typeList.size(); i++) {
                    if (argList.get(i) != null)
                        stmt.setObject(arg++, argList.get(i), typeList.get(i));
                }
            }
            rs = stmt.executeQuery();
            if (rs != null && callback != null) {
                callback.onViewSelected(rs);
            }
            return rs != null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBService.closeResultSetQuietly(rs);
            DBService.closeStmtQuietly(stmt);
        }
        return false;
    }

}
