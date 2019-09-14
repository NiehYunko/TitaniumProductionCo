package com.titaniumproductionco.db.services.func;

/**
 * Tracks the status of a transaction
 *
 */
public class TransactionTracker {
    private byte flag;
    /**
     * The return value of the last stored procedure called with this transaction
     */
    public int lastReturnValue = -1;

    /**
     * Test if the transaction is set to commit.
     * 
     * @return true if the transaction is set to commit by a call to setCommit().
     *         Returns true even after commit
     */
    public boolean needsCommit() {
        return flag == 2;
    }

    /**
     * Test if the transaction is set to rollback
     * 
     * @return true if the transaction is set to rollback by a call to
     *         setRollback(). Returns true even after rollback
     */
    public boolean needsRollback() {
        return flag == 1;
    }

    /**
     * Test if the transaction is unfinished
     * 
     * @return true if the transaction is neither set to commit or rollback
     */
    public boolean isUnfinished() {
        return flag == 0;
    }

    /**
     * Set the transaction to commit. When the call to ITransaction.call() returns,
     * the transaction will be committed
     */
    public void setCommit() {
        flag = 2;
    }

    /**
     * Set the transaction to rollback. When the call to ITransaction.call()
     * returns, the transaction will be rolled back
     */
    public void setRollback() {
        flag = 1;
    }
}
