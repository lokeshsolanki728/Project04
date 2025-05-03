package com.rays.pro4.Model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;

import com.rays.pro4.Bean.DropdownListBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.JDBCDataSource;

import org.apache.log4j.Logger;

/**
 * The Class BaseModel.
 *
 * @author Lokesh SOlanki
 */
public abstract class BaseModel implements Serializable, DropdownListBean,Comparable<BaseModel> {

	private long id;
	private String createdBy;
	protected static Logger log = Logger.getLogger(BaseModel.class);
	private String modifiedBy;
	private Timestamp createdDatetime;
	private Timestamp modifiedDateTime;

	public abstract String getTableName();
	public abstract long nextPK() throws DatabaseException;

	public long nextPK() throws DatabaseException {
		log.debug("Model nextPK Started");
		long pk = 0;
		try (Connection conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM " + getTableName())) {
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				pk = rs.getLong(1);

			}
			
		} catch (SQLException e) {
			log.error("Database Exception in nextPK", e);
		}
		log.debug("Model nextPK End");
		return pk + 1;	
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
	@Override
    public int compareTo(BaseModel other) {
        return Long.compare(this.id, other.getId());
    }
	
	/**
	 * Generates and returns the next primary key.
	 *
	 * @return The next primary key.
	 * @throws DatabaseException If a database error occurs.
	 */
	/*
	        } 
	    } catch (SQLException e) {
	        getLog().error("Database Exception in nextPK", e);
	        throw new DatabaseException("Exception : Exception in getting PK"+ e.getMessage());
	    }
	    log.debug("Model nextPK End");
	    return pk + 1; // Increment PK and return
	}*/

	public abstract String getTableName();

	/**
	 * Updates the create information of a record.
	 *
	 * @throws ApplicationException If a database or application error occurs.
	 */
	public void updateCreatesInfo() throws ApplicationException {
	    log.debug("Model updateCreatesInfo Started..." + createdBy);
	    String sql = "UPDATE " + getTableName() + " SET CREATED_BY=?, CREATED_DATETIME=? WHERE ID=?";
	    log.debug(sql);
	    try (Connection conn = JDBCDataSource.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        conn.setAutoCommit(false);
	        pstmt.setString(1, createdBy);
	        pstmt.setTimestamp(2, DataUtility.getCurrentTimestamp());
	        pstmt.setLong(3, id);
	        pstmt.executeUpdate();
	        conn.commit();
	    } catch (SQLException e) {
	        log.error("Database Exception in updateCreatesInfo", e);
	        JDBCDataSource.trnRollback(conn);
	        throw new ApplicationException("Exception in updateCreatesInfo: " + e.getMessage());
	    }
	    log.debug("Model updateCreatesInfo End");
	}

	/**
	 * Updates the modified information of a record.
	 *
	 * @throws ApplicationException If a database or application error occurs.
	 */
	public void updateModifiedInfo() throws ApplicationException {
	    log.debug("Model updateModifiedInfo Started");
	    String sql = "UPDATE " + getTableName() + " SET MODIFIED_BY=?, MODIFIED_DATETIME=? WHERE ID=?";
	    try (Connection conn = JDBCDataSource.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        conn.setAutoCommit(false);
	        pstmt.setString(1, modifiedBy);
	        pstmt.setTimestamp(2, DataUtility.getCurrentTimestamp());
	        pstmt.setLong(3, id);
	        pstmt.executeUpdate();
	        conn.commit();
	    } catch (SQLException e) {
	        log.error("Database Exception in updateModifiedInfo", e);
	        JDBCDataSource.trnRollback(conn);
	        throw new ApplicationException("Exception in updateModifiedInfo: " + e.getMessage());
	    }
	    log.debug("Model updateModifiedInfo End");
	}

	/**
	 * Populates a BaseModel object with data from a ResultSet.
	 *
	 * @param <T>       The type of the BaseModel object.
	 * @param model     The BaseModel object to populate.
	 * @param pstmt     The PreparedStatement object.
	 * @param rs        The ResultSet containing the data.
	 * @return The populated BaseModel object.
	 * @throws SQLException If a database access error occurs.
	 */
	
}
