package com.rays.pro4.Util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;



import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.rays.pro4.Exception.ApplicationException;


/**
 * JDBC DataSource is a Data Connection Pool.
 * 
 * @author Lokesh SOlanki
 *
 */
public class JDBCDataSource {

	 /**
     * JDBC Database connection pool ( DCP )
     *///
	
	
	private static Logger log = Logger.getLogger(JDBCDataSource.class);
    private static JDBCDataSource datasource;

    private JDBCDataSource() {
    }

    private ComboPooledDataSource cpds = null;

    /**
     * Create instance of Connection Pool
     *
     * @return
     */
    public static JDBCDataSource getInstance() throws Exception {
        if (datasource == null) {
        	
        	log.info("JDBCDataSource.getInstance() method start");
            ResourceBundle rb = ResourceBundle
                    .getBundle("com.rays.pro4.resources.System");

            datasource = new JDBCDataSource();
            datasource.cpds = new ComboPooledDataSource();
            try {
            	log.info("JDBCDataSource.getInstance() try set driver");
                datasource.cpds.setDriverClass(rb.getString("driver"));
            	log.info("JDBCDataSource.getInstance() finish set driver");
            } catch (ClassNotFoundException e) {
            	log.error("JDBCDataSource.getInstance() ClassNotFoundException", e);
                throw new Exception(e);
            }
            
            
            
            datasource.cpds.setJdbcUrl(rb.getString("url"));
            datasource.cpds.setUser(rb.getString("username"));
            datasource.cpds.setPassword(rb.getString("password"));            
            datasource.cpds.setInitialPoolSize(Integer.parseInt(rb
                    .getString("initialPoolSize")));
            datasource.cpds.setAcquireIncrement(new Integer((String) rb
                    .getString("acquireIncrement")));
            datasource.cpds.setMaxPoolSize(new Integer((String) rb
                    .getString("maxPoolSize")));
            datasource.cpds.setMaxIdleTime(DataUtility.getInt(rb
                    .getString("timeout")));
            datasource.cpds.setMinPoolSize(Integer.parseInt(rb
            		
                    .getString("minPoolSize")));

        }
        return datasource;
    }

    /**
     * Gets the connection from ComboPooledDataSource
     *
     * @return connection
     */
    public static Connection getConnection() throws Exception {
        return getInstance().cpds.getConnection();
    }

    /**
     * Closes a connection
     *
     * @param connection
     * @throws Exception
     */
    public static void closeConnection(Connection connection) throws Exception{
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
            	log.error("JDBCDataSource.closeConnection() SQLException", e);
                throw new Exception(e);
            }
        }
    }

    public static void trnRollback(Connection connection)
            throws ApplicationException {
    	
    	log.info("JDBCDataSource.trnRollback() method start");
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new ApplicationException(ex.toString());
            }
        }
    }
	
    
}
