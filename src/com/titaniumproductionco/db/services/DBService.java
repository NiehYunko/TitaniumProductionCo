package com.titaniumproductionco.db.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.titaniumproductionco.db.services.func.IErrorMessage;
import com.titaniumproductionco.db.services.func.ITransaction;
import com.titaniumproductionco.db.services.func.Proc;
import com.titaniumproductionco.db.services.func.TransactionTracker;

/**
 * Handles connection to the database
 *
 */
public class DBService {
    private static final String URL = "jdbc:sqlserver://golem.csse.rose-hulman.edu;databaseName=TitaniumProductionCo;user={username};password={{password}}";
    private static Connection connection = null;
    private static Role role = Role.UNKNOWN;
    private static String user;

    /**
     * Connect to the DB with the given credential
     * 
     * @param username Username used for connection
     * @param password Password used for connection
     * @return true if the connection is successfully established
     */
    public static boolean connect(String username, String password) {
        if (connection != null) {
            return true;
        }
        try {
            String connectionString = URL;
            connectionString = connectionString.replace("{username}", username).replace("{password}", password);
            connection = DriverManager.getConnection(connectionString);
            System.out.println("[CON] Connected");
            return true;
        } catch (SQLException exception) {
            connection = null;
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Close the current connection
     */
    public static void close() {
        try {
            connection.close();
            System.out.println("[CON] Closed");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        connection = null;
    }

    /**
     * Get the current connection
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Attempt to get the role of the given user and reconnect to the database with
     * the account for the role.
     * 
     * @param username
     */
    public static void setRole(String username) {
        role = Role.UNKNOWN;
        Map<String, Object> result = Proc.Login_Role.call(null, username);
        role = Role.toRoleSafe((String) result.get("Role"));
        if (role != Role.UNKNOWN) {
            close();
            connect(role.USERNAME, role.PASSWORD);
            user = username;
        }
        System.out.println("[CON] Role=" + role);
        if (role == Role.UNKNOWN) {
            close();
            JOptionPane.showMessageDialog(null, "Login Error", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * Helper method for closing a statement
     * 
     * @param stmt
     */
    public static void closeStmtQuietly(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Helper method for closing a result set
     * 
     * @param rs
     */
    public static void closeResultSetQuietly(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Role getRole() {
        return role;
    }

    public static String getUser() {
        return user;
    }

    /**
     * Validate if the current role matches one of the roles supplied
     * 
     * @param roles the roles that are valid
     * @return true if the current role matches one of the supplied
     */
    public static boolean validateRole(Role... roles) {
        if (getUser() == null)
            return false;
        for (Role r : roles) {
            if (role == r)
                return true;
        }
        return false;
    }

    /**
     * Validate the roles and pop an error message if the role is not valid
     * 
     * @param parent the JComponent to show the error message on
     * @param roles
     * @return
     */
    public static boolean validateRoleWithError(JComponent parent, Role... roles) {
        if (!validateRole(roles)) {
            JOptionPane.showMessageDialog(parent, "Access Error", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean useTransaction(ITransaction proc) {
        return useTransaction(null, proc);
    }

    public static boolean useTransaction(IErrorMessage error, ITransaction proc) {
        return useTransaction(null, error, proc);
    }

    public static boolean useTransaction(String successfulMessage, IErrorMessage error, ITransaction proc) {
        return useTransaction(null, successfulMessage, error, proc);
    }

    /**
     * Execute a transaction. See ITransaction for details<br>
     * 
     * The parent component, successful message and error message here will override
     * the ones defined in the stored procedure. The error message handler here
     * should handle all error messages returned by any stored procedure involved in
     * this transaction
     * 
     * <br>
     * 
     * @param parent            the JComponent to display any message
     * @param successfulMessage The message to display when the transaction commits
     * @param error             The error message handler
     * @param proc              The functions to wrap inside the transaction
     * @return true if the transaction commits
     */
    public static boolean useTransaction(JComponent parent, String successfulMessage, IErrorMessage error, ITransaction proc) {
        System.out.println("[TRANS] Transaction Begin");
        TransactionTracker tran = null;
        int returnValue = -25565;
        try {
            beginTransaction();
            tran = new TransactionTracker();
            proc.call(tran);
            if (tran.needsRollback()) {
                rollbackTransaction();
                System.out.println("[TRANS] Transaction Rollback");
            } else if (tran.needsCommit()) {
                commitTransaction();
                System.out.println("[TRANS] Transaction Commited");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            if (tran != null) {
                try {
                    rollbackTransaction();
                    System.out.println("[TRANS] Transaction Rollback");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            try {
                getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (tran != null) {
            returnValue = tran.lastReturnValue;
            if (returnValue == 0 && successfulMessage != null) {
                JOptionPane.showMessageDialog(parent, successfulMessage);
            } else if (returnValue > 0 && error != null) {
                String errorMes = error.getErrorMessage(returnValue);
                JOptionPane.showMessageDialog(parent, errorMes == null ? "Unknown Error" : errorMes, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        System.out.println("[TRANS] Transaction End with " + returnValue);
        return tran != null && tran.needsCommit();
    }

    private static void beginTransaction() throws SQLException {
        getConnection().setAutoCommit(false);
    }

    private static void commitTransaction() throws SQLException {
        getConnection().commit();
    }

    private static void rollbackTransaction() throws SQLException {
        getConnection().rollback();
    }

}
