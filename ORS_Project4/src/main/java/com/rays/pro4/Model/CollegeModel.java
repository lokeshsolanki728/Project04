package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.CollegeBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * JDBC Implementation of CollegeModel.
 * 
 * @author Lokesh SOlanki
 *
 */
public class CollegeModel {

	private static Logger log = Logger.getLogger(CollegeModel.class);
	private ResultSet rs = null;

	/**
	 * Generates the next primary key for the College table.
	 *
	 * @return The next primary key.
	 * @throws DatabaseException If a database error occurs.
	 */
	public synchronized Integer nextPK() throws DatabaseException {
		log.debug("Model nextPK Started");
		int pk = 0;
		try (Connection conn = JDBCDataSource.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_COLLEGE");
			 ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				pk = rs.getInt(1);
			}
		} catch (SQLException e) {
			log.error("Database Exception in nextPK", e);
			throw new DatabaseException("Exception: Unable to get PK - " + e.getMessage());
		}
		log.debug("Model nextPK End");
		return pk + 1;
	}

	/**
	 * Adds a new college to the database.
	 *
	 * @param bean The CollegeBean object containing college data.
	 * @return The primary key of the newly added college.
	 * @throws ApplicationException    If an application error occurs.
	 * @throws DuplicateRecordException If a college with the same name already
	 *                                  exists.
	 */
	public long add(CollegeBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");
		long pk = 0;
		// Check for duplicate college name
		CollegeBean duplicateCollegeName = findByName(bean.getName());
		if (duplicateCollegeName != null) {
			throw new DuplicateRecordException("College Name already exists");
		}
		try (Connection conn = JDBCDataSource.getConnection()) {
			conn.setAutoCommit(false); // Start transaction
			pk = nextPK();
			try (PreparedStatement pstmt = conn.prepareStatement(
					"INSERT INTO ST_COLLEGE (ID,NAME,ADDRESS,STATE,CITY,PHONE_NO,CREATED_BY,MODIFIED_BY,CREATED_DATETIME,MODIFIED_DATETIME) VALUES(?,?,?,?,?,?,?,?,?,?)")) {
				pstmt.setLong(1, pk);
				pstmt.setString(2, bean.getName());
				pstmt.setString(3, bean.getAddress());
				pstmt.setString(4, bean.getState());
				pstmt.setString(5, bean.getCity());
				pstmt.setString(6, bean.getPhoneNo());
				pstmt.setString(7, bean.getCreatedBy());
				pstmt.setString(8, bean.getModifiedBy());
				pstmt.setTimestamp(9, bean.getCreatedDatetime());
				pstmt.setTimestamp(10, bean.getModifiedDatetime());
				pstmt.executeUpdate();
			}
			conn.commit(); // Commit transaction
		} catch (SQLException e) {
			log.error("Database Exception in add", e);
			try (Connection conn = JDBCDataSource.getConnection()) {
				conn.rollback();
			} catch (SQLException ex) {
				throw new ApplicationException("Exception : Error in rollback - " + ex.getMessage());
			}
			throw new ApplicationException("Exception: Exception in adding college - " + e.getMessage());
		}
		log.debug("Model add End");
		return pk;
	}

    public void delete(long id) throws ApplicationException {
        delete(findByPK(id));
    }
	/**
	 * Deletes a college from the database.
	 *
	 * @param bean The CollegeBean object to be deleted.
	 * @throws ApplicationException If a database error occurs.
	 */
	public void delete(CollegeBean bean) throws ApplicationException {
		log.debug("Model delete Started");
		try (Connection conn = JDBCDataSource.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_COLLEGE WHERE ID=?")) {
			conn.setAutoCommit(false);// Begin transaction
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit();// End transaction
		} catch (SQLException e) {
			log.error("Database Exception in delete", e);
			try (Connection conn = JDBCDataSource.getConnection()) {
				conn.rollback();
			} catch (SQLException ex) {
				throw new ApplicationException("Exception : Error in rollback - " + ex.getMessage());
			}
			throw new ApplicationException("Exception: Exception in deleting College - " + e.getMessage());
		}
		log.debug("Modal delete End");
	}
    
	public CollegeBean findByName(String name) throws ApplicationException {
		log.debug("Model findByName Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_COLLEGE WHERE NAME=?");
		CollegeBean bean = null;
		try (Connection conn = JDBCDataSource.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
			pstmt.setString(1, name);// Set parameter for name
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					bean = new CollegeBean();
					bean.setId(rs.getLong(1));
					bean.setName(rs.getString(2));
					bean.setAddress(rs.getString(3));
					bean.setState(rs.getString(4));
					bean.setCity(rs.getString(5));
					bean.setPhoneNo(rs.getString(6));
					bean.setCreatedBy(rs.getString(7));
					bean.setModifiedBy(rs.getString(8));
					bean.setCreatedDatetime(rs.getTimestamp(9));
					bean.setModifiedDatetime(rs.getTimestamp(10));
				}
			}
		} catch (SQLException e) {
			log.error("Database Exception in findByName", e);
			throw new ApplicationException("Exception: Exception in getting College by name - " + e.getMessage());
		} catch (Exception e) {
			log.error("Error", e);
			throw new ApplicationException("Exception: Exception in getting College by name - " + e.getMessage());

		}
		log.debug("modal findByName End");
		return bean;
	}

	public CollegeBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findByPK Started");
		String sql = "SELECT * FROM ST_COLLEGE WHERE id=?";
		CollegeBean bean = null;
		try (Connection conn = JDBCDataSource.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, pk);// set parameter in pstmt
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					bean = new CollegeBean();
					bean.setId(rs.getLong(1));
					bean.setName(rs.getString(2));
					bean.setAddress(rs.getString(3));
					bean.setState(rs.getString(4));
					bean.setCity(rs.getString(5));
					bean.setPhoneNo(rs.getString(6));
					bean.setCreatedBy(rs.getString(7));
					bean.setModifiedBy(rs.getString(8));
					bean.setCreatedDatetime(rs.getTimestamp(9));
					bean.setModifiedDatetime(rs.getTimestamp(10));
				}
			}
		} catch (SQLException e) {
			log.error("Database Exception in findByPK", e);
			throw new ApplicationException("Exception: Error in getting College by PK - " + e.getMessage());
			}
		} catch (Exception e) {
			log.error("Database Exception ", e);
			throw new ApplicationException("Exception: Error in getting College by PK");
		} 
		
		finally
		{
            try {
                if (rs != null) rs.close();
            } catch (SQLException ex) {}
		}
		log.debug("Find By PK End");
		return bean;
	}

	/**
	 * Updates an existing college in the database.
	 *
	 * @param bean The CollegeBean object with updated college data.
	 * @throws ApplicationException    If a database error occurs.
	 * @throws DuplicateRecordException If a college with the same name already
	 *                                  exists.
	 */
	public void update(CollegeBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model update Started");
		// Check if the updated college name already exists
		CollegeBean beanExist = findByName(bean.getName());
		if (beanExist != null && beanExist.getId() != bean.getId()) {
			throw new DuplicateRecordException("College is already exist");
		}
		try (Connection conn = JDBCDataSource.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(
					 "UPDATE ST_COLLEGE SET NAME=?,ADDRESS=?,STATE=?,CITY=?,PHONE_NO=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?")) {
			conn.setAutoCommit(false); // Start transaction
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getAddress());
			pstmt.setString(3, bean.getState());
			pstmt.setString(4, bean.getCity());
			pstmt.setString(5, bean.getPhoneNo());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());
			pstmt.setLong(10, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // Commit transaction
		} catch (SQLException e) {
			log.error("Database Exception in update", e);
			try (Connection conn = JDBCDataSource.getConnection()) {
				conn.rollback();
			} catch (SQLException ex) {
				throw new ApplicationException("Exception : Error in rollback - " + ex.getMessage());
			}
			throw new ApplicationException("Exception: Exception in updating College - " + e.getMessage());
		}
		log.debug("Model update End");
	}
	public List search(CollegeBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	public List search(CollegeBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("model search Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_COLLEGE WHERE 1=1");
		ArrayList<CollegeBean> list = new ArrayList<>();
		int index = 1;

        if (pageNo < 0) {
            pageNo = 1;
        }
        if (pageSize < 0) {
            pageSize = 10;
        }

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" AND id = ?");
            }
            if (bean.getName() != null && bean.getName().length() > 0) {
                sql.append(" AND NAME like ?");
            }
            if (bean.getAddress() != null && bean.getAddress().length() > 0) {
                sql.append(" AND ADDRESS like ?");
            }
             if (bean.getCity() != null && !bean.getCity().isEmpty()) {
                sql.append(" AND CITY like ?");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT " + pageNo + ", " + pageSize);
        }

        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            if (bean != null) {

			if (bean.getName() != null && !bean.getName().isEmpty()) {
				pstmt.setString(index++, bean.getName() + "%");
			}
			if (bean.getAddress() != null && !bean.getAddress().isEmpty()) {
				pstmt.setString(index++, bean.getAddress() + "%");
			}
                 if (bean.getId() > 0) {
                     pstmt.setLong(index++, bean.getId());
                 }
                 if (bean.getName() != null && !bean.getName().isEmpty()) {
                     pstmt.setString(index++, bean.getName() + "%");
                 }
                 if (bean.getAddress() != null && !bean.getAddress().isEmpty()) {
                     pstmt.setString(index++, bean.getAddress() + "%");
                 }
                  if (bean.getCity() != null && !bean.getCity().isEmpty()) {
                     pstmt.setString(index++, bean.getCity() + "%");
					bean = new CollegeBean();
					bean.setId(rs.getLong(1));
					bean.setName(rs.getString(2));
					bean.setAddress(rs.getString(3));
					bean.setState(rs.getString(4));
					bean.setCity(rs.getString(5));
					bean.setPhoneNo(rs.getString(6));
					bean.setCreatedBy(rs.getString(7));
					bean.setModifiedBy(rs.getString(8));
					bean.setCreatedDatetime(rs.getTimestamp(9));
					bean.setModifiedDatetime(rs.getTimestamp(10));
					list.add(bean);
				}
			}
		} catch (SQLException e) {
			log.error("Database Exception in search", e);
			throw new ApplicationException("Exception: Exception in searching college - " + e.getMessage());
		}
		log.debug("model search End");
		return list;
	}

	public List list() throws ApplicationException {
		return list(1, 0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
        if (pageNo < 0) {
            pageNo = 1;
        }
        if (pageSize < 0) {
            pageSize = 10; // Default page size
        }
		ArrayList<CollegeBean> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select * from ST_COLLEGE");
		if (pageSize > 0) {
            // Calculate start record index
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT " + pageNo + "," + pageSize);
		}
		try (Connection conn = JDBCDataSource.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			 ResultSet rs = pstmt.executeQuery()) {
			CollegeBean bean;
			while (rs.next()) {
				bean = new CollegeBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setCity(rs.getString(5));
				bean.setPhoneNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
				list.add(bean);
			}
		} catch (SQLException e) {
			log.error("Database Exception in list", e);
			throw new ApplicationException("Exception: Exception in getting list of users - " + e.getMessage());
		}
		log.debug("Model list End");
		return list;
	}
}
