package com.titaniumproductionco.db.services.func;

import java.util.ArrayList;

/**
 * Used for adding a where clause when selecting from a view
 *
 */
public class ConditionedView {
    private ArrayList<OrderBy> orderList = null;
    private StringBuilder whereBuilder = null;
    private ArrayList<Integer> typeList = null;
    private ArrayList<Object> argList = null;
    private View view;

    public ConditionedView(View v) {
        view = v;
    }

    /**
     * Supply a where condition
     * 
     * @param validate  If the expression supplied evaluates to false, the where
     *                  will not be added
     * @param condition the SQL expression to add to the where statement in the
     *                  query. Use ? for parameter
     * @param type      The type of the parameter
     * @param obj       The value for the parameter
     * @return this object for chaining methods
     */
    public ConditionedView where(boolean validate, String condition, int type, Object obj) {
        if (validate) {
            if (whereBuilder == null)
                whereBuilder = new StringBuilder("WHERE (" + condition + ")");
            else
                whereBuilder.append(" AND ").append("(" + condition + ")");
            if (typeList == null) {
                typeList = new ArrayList<>();
                argList = new ArrayList<>();
            }
            typeList.add(type);
            argList.add(obj);
        }
        return this;
    }

    /**
     * Specify a where condition
     * 
     * @param condition
     * @return
     */
    public ConditionedView where(String condition) {
        return where(true, condition, 0, null);
    }

    /**
     * Setup an order by clause. See OrderBy
     * 
     * @param order
     * @return
     */
    public ConditionedView orderBy(OrderBy order) {
        if (orderList == null) {
            orderList = new ArrayList<>();
        }
        orderList.add(order);
        return this;
    }

    /**
     * Select the columns with the where clause attached.
     * 
     * @param columns
     * @param callback See View.select() and IViewCallback
     * @return true if the select is successful
     */
    public boolean select(String columns, IViewCallback callback) {
        String orderString = null;
        if (orderList != null) {
            StringBuilder order = new StringBuilder();
            for (OrderBy o : orderList) {
                order.append(o.getSQL()).append(" ");
            }
            orderString = order.toString();
        }
        return view.select(callback, columns, whereBuilder == null ? "" : whereBuilder.toString(), typeList, argList, orderString);
    }
}
