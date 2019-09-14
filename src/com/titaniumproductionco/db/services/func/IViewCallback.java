package com.titaniumproductionco.db.services.func;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface IViewCallback {
    /**
     * A view callback function used by view helper system.<br>
     * This functional method will only be executed if the select query is
     * successful.<br>
     * The result set will be closed by the system. You do not have to close the
     * result set.
     * 
     * @param result
     * @throws SQLException
     */
    void onViewSelected(ResultSet result) throws SQLException;
}
