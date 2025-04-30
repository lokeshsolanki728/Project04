package com.rays.pro4.Util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;



import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.rays.pro4.Exception.ApplicationException;


/**
 * JDBC DataSource is a Data Connection Pool. It is a singleton class that
 * manages a pool of database connections.
 *
 * @author Lokesh Solanki
 */
public class JDBCDataSource {

    /**
     * JDBC Database connection pool ( DCP )
     */

    private static Logger log = Logger.getLogger(JDBCDataSource.class);
    private static JDBCDataSource datasource;

    private ComboPooledDataSource cpds = null;

    /**
     * Constructor of class
     */
    private JDBCDataSource() {
    }

    /**
     * Creates and returns instance of JDBCDataSource
     *
     * @return JDBCDataSource
     * @throws ApplicationException if there is problem
     */
    public static JDBCDataSource getInstance() throws ApplicationException {
        if (datasource == null) {
            try {
                log.info("JDBCDataSource.getInstance() method start");
                ResourceBundle rb = ResourceBundle.getBundle("com.rays.pro4.bundle.System");
                datasource = new JDBCDataSource();
                datasource.cpds = new ComboPooledDataSource();
                try {
                    log.info("JDBCDataSource.getInstance() try set driver");
                    datasource.cpds.setDriverClass(rb.getString("driver"));
                    log.info("JDBCDataSource.getInstance() finish set driver");
                } catch (ClassNotFoundException e) {
                    log.error("JDBCDataSource.getInstance() ClassNotFoundException", e);
                    throw new ApplicationException("JDBCDataSource.getInstance() ClassNotFoundException" + e.getMessage());
                }
                datasource.cpds.setJdbcUrl(rb.getString("url"));
                datasource.cpds.setUser(rb.getString("username"));
                datasource.cpds.setPassword(rb.getString("password"));
                datasource.cpds.setInitialPoolSize(Integer.parseInt(rb.getString("initialPoolSize")));
                datasource.cpds.setAcquireIncrement(Integer.parseInt(rb.getString("acquireIncrement")));
                datasource.cpds.setMaxPoolSize(Integer.parseInt(rb.getString("maxPoolSize")));
                datasource.cpds.setMaxIdleTime(DataUtility.getInt(rb.getString("timeout")));
                datasource.cpds.setMinPoolSize(Integer.parseInt(rb.getString("minPoolSize")));
                log.info("JDBCDataSource.getInstance() method end");
            } catch (Exception e) {
                log.error("Error in JDBCDataSource.getInstance()", e);
                throw new ApplicationException("Error in JDBCDataSource.getInstance(): " + e.getMessage());
            }
        }
        return datasource;
    }

    /**
     * Gets the connection from ComboPooledDataSource.
     *
     * @return Connection
     * @throws ApplicationException if there is problem
     */
    public static Connection getConnection() throws ApplicationException {
        log.debug("JDBCDataSource.getConnection() method start");
        Connection conn = null;
        try {
            conn = getInstance().cpds.getConnection();
            log.debug("JDBCDataSource.getConnection() method end");
        } catch (SQLException e) {
            log.error("Error in JDBCDataSource.getConnection() SQLException", e);
            throw new ApplicationException("Error in JDBCDataSource.getConnection(): " + e.getMessage());
        } catch (Exception e) {
            log.error("Error in JDBCDataSource.getConnection()", e);
            throw new ApplicationException("Error in JDBCDataSource.getConnection(): " + e.getMessage());
        }
        return conn;
    }

    /**
     * Closes a connection.
     *
     * @param connection the database connection to close
     * @throws ApplicationException if there is problem
     */
    public static void closeConnection(Connection connection) throws ApplicationException {
        log.debug("JDBCDataSource.closeConnection() method start");
        try {
            if (connection != null) {
                connection.close();
                log.debug("JDBCDataSource.closeConnection() connection close");
            }
            log.debug("JDBCDataSource.closeConnection() method end");
        } catch (SQLException e) {
            log.error("JDBCDataSource.closeConnection() SQLException", e);
            throw new ApplicationException("Error in JDBCDataSource.closeConnection(): " + e.getMessage());
        }
    }

    /**
     * Rollback the transaction
     *
     * @param connection connection
     * @throws ApplicationException
     */
    public static void trnRollback(Connection connection) throws ApplicationException {
        log.debug("JDBCDataSource.trnRollback() method start");
        if (connection != null) {
            try {
                connection.rollback();
                log.debug("JDBCDataSource.trnRollback() connection rollback");
            } catch (SQLException ex) {
                log.error("Error in JDBCDataSource.trnRollback()", ex);
                throw new ApplicationException("Error in JDBCDataSource.trnRollback(): " + ex.getMessage());
            }
        }
        log.debug("JDBCDataSource.trnRollback() method end");
    }

    /**
     * Shutdown of connection pool.
     */
    public void closeConnectionPool() {
        log.debug("JDBCDataSource.closeConnectionPool() method start");
        try {

            if (cpds != null) {
                cpds.close();
                log.debug("JDBCDataSource.closeConnectionPool() connection close");
            }
        } catch (Exception e) {
            log.error("Error in JDBCDataSource.closeConnectionPool()", e);
        }
        log.debug("JDBCDataSource.closeConnectionPool() method end");
    }

    /**
     * Test method for JDBC Connection pool.
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        JDBCDataSource j = new JDBCDataSource();
        Connection conn = null;
        try {
            for (int i = 0; i < 50; i++) {
                conn = j.getConnection();
                System.out.println(i + " connection : " + conn.toString());
                j.closeConnection(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
             if(conn!=null){
                 j.closeConnection(conn);
             }
        }
        j.closeConnectionPool();
    }

}
