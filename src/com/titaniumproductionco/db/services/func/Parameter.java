package com.titaniumproductionco.db.services.func;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

/**
 * Defines a parameter in sproc
 */
public class Parameter {
    public final int TYPE;
    public final String NAME;
    private boolean out;

    private Parameter(int t, String n) {
        TYPE = t;
        NAME = n;
    }

    public Parameter out() {
        out = true;
        return this;
    }

    final boolean isOut() {
        return out;
    }

    /**
     * Get the output from a call to stored procedure.<br>
     * If this parameter is not an output, nothing will happen
     * 
     * @param stmt
     * @param map
     * @throws SQLException
     */
    public void get(CallableStatement stmt, Map<String, Object> map) throws SQLException {
        if (out) {
            map.put(NAME, stmt.getObject(NAME));
        }
    }

    /**
     * Set the parameter of a call.<br>
     * If this parameter is an output, the second argument will not be used and the
     * output will be registered on the call
     * 
     * @param stmt
     * @param value
     * @throws SQLException
     */
    public void set(CallableStatement stmt, Object value) throws SQLException {
        if (out) {
            stmt.registerOutParameter(NAME, TYPE);
        } else {
            stmt.setObject(NAME, value, TYPE);
        }
    }

    public static Parameter decimal(String name) {
        return instance(Types.DECIMAL, name);
    }

    public static Parameter nvarchar(String name) {
        return instance(Types.NVARCHAR, name);
    }

    public static Parameter varchar(String name) {
        return instance(Types.VARCHAR, name);
    }

    public static Parameter date(String name) {
        return instance(Types.DATE, name);
    }

    public static Parameter integer(String name) {
        return instance(Types.INTEGER, name);
    }

    public static Parameter timestamp(String name) {
        return instance(Types.TIMESTAMP, name);
    }

    public static Parameter instance(int t, String n) {
        return new Parameter(t, n);
    }
}
