package com.rays.pro4.Model;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import com.rays.pro4.Bean.CollegeBean;
import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/** JDBC Implementation of College Model.
 * @author Lokesh SOlanki
 */
 *
public class CollegeModel extends BaseModel {
	

	private synchronized long nextPK() throws DatabaseException {
		log.debug("Model nextPK Started");
		long pk = 0;
		try (Connection conn = JDBCDataSource.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_COLLEGE")) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getLong(1);
			}
		} catch (SQLException e) {
			log.error("Database Exception in nextPK", e);
			throw new DatabaseException("Exception: Unable to get PK - " + e.getMessage());
		}
		log.debug("Model nextPK End " + (pk + 1));
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
	public long add(CollegeBean bean) throws ApplicationException,
			DuplicateRecordException {
		log.debug("Model add Started"+bean);
	    long pk = 0;
	    CollegeBean duplicateCollegeName = findByName(bean.getName());
	    if (duplicateCollegeName != null) {
	        throw new DuplicateRecordException("College Name already exists");
	    }
	    Connection conn = null;
	    try {
	        conn = JDBCDataSource.getConnection();
	        pk = nextPK();
	        bean.setId(pk);
	        bean.setCreatedDatetime(new java.sql.Timestamp(new java.util.Date().getTime()));
	        conn.setAutoCommit(false); // Start transaction
	        bean.setModifiedDatetime(new java.sql.Timestamp(new java.util.Date().getTime()));
	        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_COLLEGE (ID,NAME,ADDRESS,STATE,CITY,PHONE_NO,CREATED_BY,MODIFIED_BY,CREATED_DATETIME,MODIFIED_DATETIME) VALUES(?,?,?,?,?,?,?,?,?,?)");
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
	        updateCreatesInfo(bean);
	        conn.commit();// Commit transaction
	        pstmt.close();
	    } catch (Exception e) {
	        log.error("Database Exception in add college", e);
	        JDBCDataSource.trnRollback(conn);
	        throw new ApplicationException("Exception: Exception in adding college - " + e.getMessage());
	    } finally {
	        JDBCDataSource.closeConnection(conn);
	    }
	    log.debug("Model add End" + bean);
	    return pk;
	}

	/**
	 * Deletes a college from the database.
	 * 
	 * @param bean The CollegeBean object to be deleted.
	 * @throws ApplicationException If a database error occurs.
	 */	
public void delete(CollegeBean bean) throws ApplicationException {
		log.debug("Model delete Started");
		try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false); // Start transaction
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_COLLEGE WHERE ID=?")) {
                pstmt.setLong(1, bean.getId());
                pstmt.executeUpdate();
            }
            conn.commit(); // Commit transaction

		} catch (Exception e) {
			log.error("Database Exception in delete college", e);
			JDBCDataSource.trnRollback();
			throw new ApplicationException("Exception :Exception in delete college " + e.getMessage());
		}
		log.debug("Modal delete End");
	}

	public void delete(long id) throws ApplicationException {
		CollegeBean bean = findByPK(id);
		delete(bean);
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
                    populate(rs, bean);
                }
            }
        } catch (Exception e) {
            log.error("Database Exception in find by Name ",e);
			throw new ApplicationException("Exception: Exception in getting College by name - " + e.getMessage());
        }finally {
			log.debug("modal findByName End");
		}
		
        return bean;
    }

	public CollegeBean findByPK(long pk) throws ApplicationException {
        log.debug("Model findByPK Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_COLLEGE WHERE id=?");
        CollegeBean bean = null;
        try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setLong(1, pk);// Set the primary key parameter
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bean = new CollegeBean();
                    populate(rs, bean);
                }
              }
          } catch (Exception e) {
              log.error("Database Exception in findByPK" + e);
              throw new ApplicationException("Exception: Error in getting College by PK - " + e.getMessage());
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
	public void update(CollegeBean bean) throws ApplicationException,
			DuplicateRecordException {
		log.debug("Model update Started");
		// Check if the updated college name already exists
		CollegeBean beanExist = findByName(bean.getName());
		if (beanExist != null && beanExist.getId() != bean.getId()) {
			throw new DuplicateRecordException("College is already exist");
		}
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Start transaction
			try (PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_COLLEGE SET NAME=?,ADDRESS=?,STATE=?,CITY=?,PHONE_NO=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?")) {
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
				updateModifiedInfo(bean);
			} catch (SQLException e) {
				log.error("Database Exception in update college", e);
				JDBCDataSource.trnRollback(conn);
				throw new ApplicationException("Exception: Exception in updating College - " + e.getMessage());
			}
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in updating college " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model update End");
	}

	public List search(CollegeBean bean) throws ApplicationException {
		return search(bean, 0, 0,null,null);
	}
	public List search(CollegeBean bean, int pageNo, int pageSize,String orderBy, String sortOrder) throws ApplicationException {
		ArrayList<CollegeBean> list = new ArrayList<>();
		int index = 1;       
        Connection conn = null;
		log.debug("model search Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_COLLEGE WHERE 1=1 ");
        
        if (bean != null) {
            if (bean.getId() > 0) sql.append(" AND id=?");
            if (bean.getName() != null && !bean.getName().isEmpty()) sql.append(" AND Name like ?");
            if (bean.getAddress() != null && !bean.getAddress().isEmpty()) sql.append(" AND Address like ?");
            if (bean.getCity() != null && !bean.getCity().isEmpty()) sql.append(" AND City like ?");
        }
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY " + orderBy);
            if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
                sql.append(" DESC");
            } else {
                sql.append(" ASC");
            }
        }
        else {
            sql.append(" ORDER BY NAME ASC");

        }
		if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT " + pageNo + "," + pageSize);
        }
        try {
            conn = JDBCDataSource.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                if (bean != null) {
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
                    }
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            CollegeBean collegeBean = new CollegeBean();
                            populate(rs, collegeBean);
                            list.add(collegeBean);
                        }
                    }
                }
            }
        } catch (Exception e){
			log.error("Database Exception in search college", e);
			throw new ApplicationException("Exception: Exception in searching college - " + e.getMessage());
		}
		log.debug("model search End");
		return list;
	}

	public List list() throws ApplicationException {
		return list(1, 0, null, null);
	}

	public List list(int pageNo, int pageSize,String orderBy, String sortOrder) throws ApplicationException {	
		log.debug("Model list Started");
		StringBuffer sql = new StringBuffer("select * from ST_COLLEGE");
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY " + orderBy);
            if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
                sql.append(" DESC");
            } else {
                sql.append(" ASC");
            }
        }
        else {
            sql.append(" ORDER BY NAME ASC");
        }

        if (pageSize > 0) {

            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT " + pageNo + "," + pageSize);
		}
		try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
			try (ResultSet rs = pstmt.executeQuery()) {
                ArrayList<CollegeBean> list = new ArrayList<CollegeBean>();
				while (rs.next()){
					CollegeBean bean = new CollegeBean();
					populate(rs, bean);
					list.add(bean);
				}
			}
        } catch (Exception e) {
			log.error("Database Exception in list ", e);
			throw new ApplicationException("Exception: Exception in getting list of users - " + e.getMessage());
		}
		log.debug("Model list End");
		
		return list;
	}
	@Override
	public String getTableName() {
		return "ST_COLLEGE";
	}

	private void populate(ResultSet rs, CollegeBean bean) throws SQLException {
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

    private void updateCreatesInfo(CollegeBean bean) throws ApplicationException{
        bean.setModifiedBy(bean.getCreatedBy());
        bean.setModifiedDatetime(bean.getCreatedDatetime());       
    }

    private void updateModifiedInfo(CollegeBean bean) throws ApplicationException {
        bean.setModifiedDatetime(new java.sql.Timestamp(new java.util.Date().getTime()));

    }

}
}
