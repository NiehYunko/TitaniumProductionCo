package com.titaniumproductionco.db.services.func;

public class OrderBy {
    private String sql;

    private OrderBy(String col, boolean desc) {
        sql = "ORDER BY " + col + (desc ? " DESC" : " ASC");
    }

    /**
     * Order the column by ascending order
     * 
     * @param column
     * @return
     */
    public static OrderBy asc(String column) {
        return new OrderBy(column, false);
    }

    /**
     * Order the column by descending order
     * 
     * @param column
     * @return
     */
    public static OrderBy desc(String column) {
        return new OrderBy(column, true);
    }

    public String getSQL() {
        return sql;
    }
}
