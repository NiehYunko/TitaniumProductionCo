package com.titaniumproductionco.db.services.func;

import java.sql.SQLException;

@FunctionalInterface
public interface ITransaction {
    /**
     * A functional method used with transaction in database <br>
     * This method should call the stored procedures or select from views. When an
     * anomaly is detected, this method should call setRollback() on the transaction
     * tracker and return. When the transaction is completed successfully, this
     * method must call setCommit() on the transaction tracker
     * 
     * @param trans
     * @throws SQLException
     */
    public void call(TransactionTracker trans) throws SQLException;
}
