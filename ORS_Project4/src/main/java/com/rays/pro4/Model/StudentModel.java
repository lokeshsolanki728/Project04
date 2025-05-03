package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.CollegeBean;
import com.rays.pro4.Bean.StudentBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

/**
 * JDBC Implementation of Student Model.
 * 
 * @author Lokesh SOlanki
 *
 */
public class StudentModel extends BaseModel{

    
    public String getTableName() {
		return "ST_STUDENT";
	}
	
    private synchronized long nextPK() throws DatabaseException {
        log.debug("Model nextPK Started");
        long pk = 0;
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_STUDENT")) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                pk = rs.getLong(1);
            }
        } catch (SQLException e) {
            log.error("Database Exception in nextPK", e);
            throw new DatabaseException("Exception: Unable to get PK - " + e.getMessage());
        }
        log.debug("Model nextPK End");
        return pk + 1;
    }

    
		return pk + 1;

	}

	public long add(StudentBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");
		long pk = 0;
		// Fetch college details
		CollegeModel cModel = new CollegeModel();
		CollegeBean collegeBean = cModel.findByPK(bean.getCollegeId());
		bean.setCollegeName(collegeBean.getName());
		Connection conn = null;
		StudentBean duplicateName = findByEmailId(bean.getEmail());
		if (duplicateName != null) {
			throw new DuplicateRecordException("Email already exists");
		}
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            pk = nextPK();
            bean.setId(pk);
            bean.setCreatedDatetime(new java.sql.Timestamp(new java.util.Date().getTime()));
            bean.setModifiedDatetime(new java.sql.Timestamp(new java.util.Date().getTime()));

            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_STUDENT VALUES(?,?,?,?,?,?,?,?,?,?,?,?)")) {
                pstmt.setLong(1, pk);
                pstmt.setLong(2, bean.getCollegeId());
                pstmt.setString(3, bean.getCollegeName());
                pstmt.setString(4, bean.getFirstName());
                pstmt.setString(5, bean.getLastName());
                pstmt.setDate(6, new java.sql.Date(bean.getDob().getTime()));
                pstmt.setString(7, bean.getMobileNo());
                pstmt.setString(8, bean.getEmail());
                pstmt.setString(9, bean.getCreatedBy());
                pstmt.setString(10, bean.getModifiedBy());
                pstmt.setTimestamp(11, bean.getCreatedDatetime());
                pstmt.setTimestamp(12, bean.getModifiedDatetime());
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            log.error("Database Exception..", e);
            JDBCDataSource.trnRollback();
            throw new ApplicationException("Exception : Exception in add Student " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
		log.debug("Model add End");
		return pk;
	}


	public void delete(StudentBean bean) throws ApplicationException {
		log.debug("Model delete Started");
        Connection conn = null;
		try {
		    conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            if (findByPK(bean.getId()) == null) {
               throw new ApplicationException("Bean not found");
            }
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_STUDENT WHERE ID=?")) {
                pstmt.setLong(1, bean.getId());
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            log.error("Database Exception..", e);
			JDBCDataSource.trnRollback();
            throw new ApplicationException("Exception : Exception in delete Student");
        }
		}
		log.debug("Model delete End");
	}

	public StudentBean findByEmailId(String Email) throws ApplicationException {
		log.debug("Model findBy Email Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_STUDENT WHERE EMAIL_id=?");
		StudentBean bean = null;
        try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setString(1, Email);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bean = new StudentBean();
                    populate(rs, bean);


                }
            }
        } catch (SQLException e) {
            log.error("Database Exception in findByEmailId", e);
            throw new ApplicationException("Exception: Exception in getting student by email - " + e.getMessage());
        }
        log.debug("Model findBy Email End");
        return bean;
	}
	public StudentBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findByPK Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_STUDENT WHERE ID=?");
		StudentBean bean = null;
		try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setLong(1, pk);// Set the primary key parameter
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bean = new StudentBean();
                    populate(rs, bean);
                }
            }
		} catch (SQLException e) {
			log.error("Database Exception in findByPK", e);
			throw new ApplicationException("Exception: Exception in getting student by pk - " + e.getMessage());
		}
		log.debug("Model findByPK End");
		return bean;
	}

	public void Update(StudentBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model update Started");
		Connection conn = null;
		StudentBean beanExist = findByEmailId(bean.getEmail());
		if (beanExist != null && beanExist.getId() != bean.getId()) {
			throw new DuplicateRecordException("Email already exists");
		}
		CollegeModel cModel = new CollegeModel();
		CollegeBean collegeBean = cModel.findByPK(bean.getCollegeId());
		bean.setCollegeName(collegeBean.getName());
		try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false);// Begin transaction
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE ST_STUDENT SET COLLEGE_ID=?,COLLEGE_NAME=?,FIRST_NAME=?,LAST_NAME=?,DATE_OF_BIRTH=?,MOBILE_NO=?,EMAIL_ID=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?")) {

                pstmt.setLong(1, bean.getCollegeId());
                pstmt.setString(2, bean.getCollegeName());
                pstmt.setString(3, bean.getFirstName());
                pstmt.setString(4, bean.getLastName());
                pstmt.setDate(5, new java.sql.Date(bean.getDob().getTime()));
                pstmt.setString(6, bean.getMobileNo());
                pstmt.setString(7, bean.getEmail());
                pstmt.setString(8, bean.getCreatedBy());
                pstmt.setString(9, bean.getModifiedBy());
                pstmt.setTimestamp(10, bean.getCreatedDatetime());
                pstmt.setTimestamp(11, bean.getModifiedDatetime());
                pstmt.setLong(12, bean.getId());
                pstmt.executeUpdate();
                conn.commit();// End transaction
            }
		} catch (SQLException e) {
			log.error("Database Exception in update", e);
            JDBCDataSource.trnRollback();
			throw new ApplicationException("Exception : Delete rollback exception" + e.getMessage());
		} 
        log.debug("Model update End");
    }

	public List search(StudentBean bean) throws ApplicationException {
		return search(bean, 1, 0);
	}

	/**
	 * Searches for students based on the provided criteria with pagination.
	 *
	 * @param bean     The StudentBean object containing search criteria.
	 * @param pageNo   The page number for pagination.
	 * @param pageSize The page size for pagination.
	 * @return A list of StudentBean objects matching the search criteria.
	 * @throws ApplicationException If a database error occurs.
	 */
	public List search(StudentBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model search Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_STUDENT WHERE 1=1");
		ArrayList<StudentBean> list = new ArrayList<>();
		int index = 1;
		if (pageNo < 0) {
            pageNo = 1;
        }
        if (pageSize < 0) {
            pageSize = 10;
        }

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id = ? ");
			}
			if (bean.getFirstName() != null && !bean.getFirstName().isEmpty()) {
				sql.append(" AND FIRST_NAME like ?");
			}
			if (bean.getLastName() != null && !bean.getLastName().isEmpty()) {
				sql.append(" AND LAST_NAME like ?");
			}
			if (bean.getMobileNo() != null && !bean.getMobileNo().isEmpty()) {
				sql.append(" AND MOBILE_NO like ?");
			}
			if (bean.getEmail() != null && !bean.getEmail().isEmpty()) {
				sql.append(" AND EMAIL_ID like ?");
			}
			if (bean.getCollegeId() > 0 ) {
                sql.append(" AND COLLEGE_ID = ?");
            }
		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" LIMIT " + pageNo + ", " + pageSize);		}
		try (Connection conn = JDBCDataSource.getConnection();PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
			index=1;
            if (bean != null) {
                if (bean.getId() > 0) {pstmt.setLong(index++, bean.getId());}
                if (bean.getFirstName() != null && !bean.getFirstName().isEmpty()) {pstmt.setString(index++, bean.getFirstName() + "%");}if (bean.getLastName() != null && !bean.getLastName().isEmpty()) {pstmt.setString(index++, bean.getLastName() + "%");}if (bean.getMobileNo() != null && !bean.getMobileNo().isEmpty()) {pstmt.setString(index++, bean.getMobileNo() + "%");}if (bean.getEmail() != null && !bean.getEmail().isEmpty()) {pstmt.setString(index++, bean.getEmail() + "%");}if (bean.getCollegeId() > 0) {pstmt.setLong(index++, bean.getCollegeId());}
            }
			try(ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bean = new StudentBean();
                    populate(rs, bean);
                    list.add(bean);
                }
            }
        }catch (SQLException e){
			log.error("Database Exception in search", e);
			throw new ApplicationException("Exception: Exception in searching student - " + e.getMessage());
		}
		log.debug("Model search End");
		return list;
	}

	public List list() throws ApplicationException {
		return list(1, 0);
	}
	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList<StudentBean> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select * from ST_STUDENT");
		if (pageNo < 0) {
            pageNo = 0;
        }
        if (pageSize < 0) {
            pageSize = 10;
        }
		if (pageSize > 0) {
            pageNo = pageNo * pageSize;
            sql.append(" LIMIT " + pageNo + " , " + pageSize);
        }
        try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    StudentBean bean = new StudentBean();
                    populate(rs, bean);
                    list.add(bean);
                }
            }
        } catch (SQLException e) {
            log.error("Database Exception in list", e);
            throw new ApplicationException("Exception: Exception in getting list of student - " + e.getMessage());
        }
        log.debug("Model list End");
        return list;
    }
    private void populate(ResultSet rs, StudentBean bean) throws SQLException {
        bean.setId(rs.getLong(1));
        bean.setCollegeId(rs.getLong(2));
        bean.setCollegeName(rs.getString(3));
        bean.setFirstName(rs.getString(4));
        bean.setLastName(rs.getString(5));
        bean.setDob(rs.getDate(6));
        bean.setMobileNo(rs.getString(7));
        bean.setEmail(rs.getString(8));
        bean.setCreatedBy(rs.getString(9));
        bean.setModifiedBy(rs.getString(10));
        bean.setCreatedDatetime(rs.getTimestamp(11));
        bean.setModifiedDatetime(rs.getTimestamp(12));
    }
}
