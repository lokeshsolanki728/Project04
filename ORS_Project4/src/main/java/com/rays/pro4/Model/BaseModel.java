package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.logging.Level;

import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;

import com.rays.pro4.Util.JDBCDataSource;

import org.apache.log4j.Logger;

/**
 * The Class BaseModel.
 *
 * @author Lokesh SOlanki
 */
public abstract class BaseModel implements java.io.Serializable {

	private long id;
	private String createdBy;
	protected static Logger log = Logger.getLogger(BaseModel.class);
	private String modifiedBy;
    private Timestamp createdDatetime;
	private Timestamp modifiedDateTime;

	public abstract String getTableName();
	public abstract long nextPK() throws DatabaseException;
	public long getId() {
		return id;
	}
    public void setId(long id) {
		this.id = id;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Timestamp getCreatedDatetime() {
		return createdDatetime;
	}
	public void setCreatedDatetime(Timestamp createdDatetime) {
		this.createdDatetime = createdDatetime;
	}
	public Timestamp getModifiedDateTime() {
		return modifiedDateTime;
	}
	public void setModifiedDateTime(Timestamp modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}
	
	/**
	 * Generates and returns the next primary key.
	 */

	/**
	 *  Method to update the information like createdBy, modifiedBy and createdDatetime, modifiedDatetime.
	 * @throws ApplicationException
	 * @throws DatabaseException
	 */
    public void updateInfo() throws ApplicationException, DatabaseException {
        log.debug("Model updateInfo Started");
        Connection conn = null;
        String sql = "UPDATE " + getTableName() + " SET ";
        if(createdBy !=null){
            sql += "CREATED_BY=?, CREATED_DATETIME=?,";
        }
        if(modifiedBy!=null){
            sql += " MODIFIED_BY=?, MODIFIED_DATETIME=? ";
        }
        sql += " WHERE ID=?";
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int index = 1;
                if(createdBy!=null){
                    pstmt.setString(index++, createdBy);
                    pstmt.setTimestamp(index++, createdDatetime);
                }
                if(modifiedBy!=null){
                    pstmt.setString(index++, modifiedBy);
                    pstmt.setTimestamp(index++, modifiedDateTime);
                }
                pstmt.setLong(index, id);
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            log.error("Database Exception in updateInfo", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception in updateInfo: " + e.getMessage());
        }
        log.debug("Model updateInfo End");
    }
}
