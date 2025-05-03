package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.MarksheetBean;
import com.rays.pro4.Bean.StudentBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

/**
 * JDBC Implementation of Marksheet Model.
 * 
 * @author Lokesh SOlanki
 *
 */
public class MarksheetModel {

	private static Logger log = Logger.getLogger(MarksheetModel.class);

	/**
	 * Generates the next primary key for the Marksheet table.
	 *
	 * @return The next primary key.
	 * @throws DatabaseException If a database error occurs.
	 */
	public synchronized Integer nextPK() throws DatabaseException {
		log.debug("Model nextPK Started");
		int pk = 0;
		try (Connection conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_MARKSHEET");
				ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				pk = rs.getInt(1);
			}
		} catch (SQLException e) {
			log.error("Database Exception in nextPK", e);
			throw new DatabaseException("Exception: Unable to get PK - " + e.getMessage());
		}
		log.debug("Model nextPk End");
		return pk + 1;
	}

	/**
	 * Adds a new marksheet to the database.
	 *
	 * @param bean The MarksheetBean object containing marksheet data.
	 * @return The primary key of the newly added marksheet.
	 * @throws ApplicationException    If an application error occurs.
	 * @throws DuplicateRecordException If a marksheet with the same roll number
	 *                                  already exists.
	 */
	public long add(MarksheetBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");
		Connection conn = null;
		long pk = 0;



		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			// Fetch student details
			StudentModel sModel = new StudentModel();
			

		// Check for duplicate roll number
		MarksheetBean duplicateMarksheet = findByRollNo(bean.getRollNo());
		if (duplicateMarksheet != null) {
			throw new DuplicateRecordException("Roll Number already exists");
		}

		
			StudentBean studentbean = sModel.findByPK(bean.getStudentld());
			if (studentbean == null) {
	            throw new ApplicationException("Student not found with ID: " + bean.getStudentld());
	        }
			String studentname = (studentbean.getFirstName() + " " + studentbean.getLastName());

		
			pk = nextPK();
			try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_MARKSHEET VALUES(?,?,?,?,?,?,?,?,?,?,?)"))  {
				pstmt.setLong(1, pk);
				pstmt.setString(2, bean.getRollNo());
				pstmt.setLong(3, bean.getStudentld());
				pstmt.setString(4, studentname);
				pstmt.setInt(5, bean.getPhysics());
				pstmt.setInt(6, bean.getChemistry());
				pstmt.setInt(7, bean.getMaths());
				pstmt.setString(8, bean.getCreatedBy());
				pstmt.setString(9, bean.getModifiedBy());
				pstmt.setTimestamp(10, bean.getCreatedDatetime());
				pstmt.setTimestamp(11, bean.getModifiedDatetime());
				pstmt.executeUpdate();
				conn.commit(); // Commit transaction
			}
		} catch (SQLException e) {
			log.error("Database Exception in add", e);
			try {
				conn.rollback(); // Rollback on error
			} catch (SQLException ex) {
				throw new ApplicationException("Exception : add rollback exception - " + ex.getMessage());
			}
			throw new ApplicationException("Exception: Exception in adding marksheet - " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model add End");
		return pk;
	}
	/**
     * Delete Marksheet by id
     * @param id
     * @throws ApplicationException
     */
    public void delete(long id) throws ApplicationException {
        log.debug("Model delete Started");       
        try (Connection conn = JDBCDataSource.getConnection()) {
             conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_MARKSHEET WHERE ID=?")) {
                pstmt.setLong(1, id);
                pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Database Exception in delete", e);
            try (Connection conn = JDBCDataSource.getConnection()) {
                conn.rollback();
            } catch (SQLException ex) {
                throw new ApplicationException("Exception : delete rollback exception - " + ex.getMessage());
            }
            throw new ApplicationException("Exception: Exception in deleting marksheet - " + e.getMessage());
        }
        log.debug("Model delete End");
    }

	/**
	 * Finds a marksheet by its roll number.
	 *
	 * @param rollNo The roll number to search for.
	 * @return The MarksheetBean object if found, null otherwise.
	 * @throws ApplicationException If a database error occurs.
	 */
	public MarksheetBean findByRollNo(String rollNo) throws ApplicationException {
		log.debug("Model findByRollNo Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_MARKSHEET WHERE ROLL_NO=?");
		MarksheetBean bean = null;
		try (Connection conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
			pstmt.setString(1, rollNo); // Set parameter for roll number
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					bean = new MarksheetBean();
					bean.setId(rs.getLong(1));
					bean.setRollNo(rs.getString(2));
					bean.setStudentld(rs.getLong(3));
					bean.setName(rs.getString(4));
					bean.setPhysics(rs.getInt(5));
					bean.setChemistry(rs.getInt(6));
					bean.setMaths(rs.getInt(7));
					bean.setCreatedBy(rs.getString(8));
					bean.setModifiedBy(rs.getString(9));
					bean.setCreatedDatetime(rs.getTimestamp(10));
					bean.setModifiedDatetime(rs.getTimestamp(11));
				}
			}
		} catch (SQLException e) {
			log.error("Database Exception in findByRollNo", e);
			throw new ApplicationException("Exception: Exception in getting marksheet by roll no - " + e.getMessage());
		}
		log.debug("Model findByRollNo End");
		return bean;
	}

	/**
	 * Finds a marksheet by its primary key.
	 *
	 * @param pk The primary key to search for.
	 * @return The MarksheetBean object if found, null otherwise.
	 * @throws ApplicationException If a database error occurs.
	 */
	public MarksheetBean findByPK(Long pk) throws ApplicationException {
		log.debug("Model findByPK Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_MARKSHEET WHERE ID=?");
		MarksheetBean bean = null;
		try (Connection conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
			pstmt.setLong(1, pk); // Set parameter for primary key
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					bean = new MarksheetBean();
					bean.setId(rs.getLong(1));
					bean.setRollNo(rs.getString(2));
					bean.setStudentld(rs.getLong(3));
					bean.setName(rs.getString(4));
					bean.setPhysics(rs.getInt(5));
					bean.setChemistry(rs.getInt(6));
					bean.setMaths(rs.getInt(7));
					bean.setCreatedBy(rs.getString(8));
					bean.setModifiedBy(rs.getString(9));
					bean.setCreatedDatetime(rs.getTimestamp(10));
					bean.setModifiedDatetime(rs.getTimestamp(11));
				}
			}
		} catch (SQLException e) {
			log.error("Database Exception in findByPK", e);
			throw new ApplicationException("Exception: Exception in getting marksheet by pk - " + e.getMessage());
		}
		log.debug("Model findByPK End");
		return bean;
	}

	/**
	 * Updates an existing marksheet in the database.
	 *
	 * @param bean The MarksheetBean object with updated marksheet data.
	 * @throws ApplicationException    If a database error occurs.
	 * @throws DuplicateRecordException If a marksheet with the same roll number
	 *                                  already exists.
	 */
	public void update(MarksheetBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model update Started");
		Connection conn = null;

		// Check for duplicate roll number
		MarksheetBean beanExist = findByRollNo(bean.getRollNo());
		if (beanExist != null && beanExist.getId() != bean.getId()) {
			throw new DuplicateRecordException("Roll No is already exist");
		}
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
		// Fetch and set Student Name
			StudentModel sModel = new StudentModel();
			
		
			
				StudentBean studentbean = sModel.findByPK(bean.getStudentld());
				if(studentbean==null) {
					throw new ApplicationException("Student not found");
				}
				bean.setName(studentbean.getFirstName() + " " + studentbean.getLastName());

			try (PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_MARKSHEET SET ROLL_NO=?,STUDENT_ID=?,NAME=?,PHYSICS=?,CHEMISTRY=?,MATHS=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?")) {
				
				pstmt.setString(1, bean.getRollNo());
				pstmt.setLong(2, bean.getStudentld());
				pstmt.setString(3, bean.getName());
				pstmt.setInt(4, bean.getPhysics());
				pstmt.setInt(5, bean.getChemistry());
				pstmt.setInt(6, bean.getMaths());
				pstmt.setString(7, bean.getCreatedBy());
				pstmt.setString(8, bean.getModifiedBy());
				pstmt.setTimestamp(9, bean.getCreatedDatetime());
				pstmt.setTimestamp(10, bean.getModifiedDatetime());
				pstmt.setLong(11, bean.getId());
				pstmt.executeUpdate();
				conn.commit(); // Commit transaction
			}
		} catch (SQLException e) {
			log.error("Database Exception in update", e);
			try {
				conn.rollback(); // Rollback on error
			} catch (SQLException ex) {
				throw new ApplicationException("Exception : update rollback exception - " + ex.getMessage());
			}
			throw new ApplicationException("Exception: Exception id updating Marksheet - " + e.getMessage());
		}finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model update End");
	}

	/**
	 * Searches for marksheets based on the provided criteria.
	 *
	 * @param bean     The MarksheetBean object containing search criteria.
	 * @param pageNo   The page number for pagination.
	 * @param pageSize The page size for pagination.
	 * @return A list of MarksheetBean objects matching the search criteria.
	 * @throws ApplicationException If a database error occurs.
	 */
	public List search(MarksheetBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model search Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_MARKSHEET WHERE 1=1");
        int index = 1;
		ArrayList<MarksheetBean> list = new ArrayList<>();


        if(bean != null){
            if(bean.getId() > 0){
                sql.append(" AND ID = ?");
            }
            if(bean.getRollNo() != null && bean.getRollNo().trim().length() > 0){
                sql.append(" AND ROLL_NO like ?");
            }
            if(bean.getName() != null && bean.getName().trim().length() > 0){
                sql.append(" AND NAME like ?");
            }
            if(bean.getPhysics() > 0){
                sql.append(" AND PHYSICS = ?");
            }
            if(bean.getChemistry() > 0){
                sql.append(" AND CHEMISTRY = ?");
            }
            if(bean.getMaths() > 0){
                sql.append(" AND MATHS = ?");
            }
		}
		

        // if page size is greater than zero then apply pagination
        if (pageSize > 0) {
            // Calculate start record index
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT " + pageNo + ", " + pageSize);
        }       
        try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
			
            index = 1;
			if (bean != null) {
                if (bean.getId() > 0) {
                    pstmt.setLong(index++, bean.getId());
                }
                if (bean.getRollNo() != null && !bean.getRollNo().isEmpty()) {
                    pstmt.setString(index++, bean.getRollNo() + "%");
                }
                if (bean.getName() != null && !bean.getName().isEmpty()) {
                    pstmt.setString(index++, bean.getName() + "%");
                }
                if (bean.getPhysics() > 0) {
                    pstmt.setInt(index++, bean.getPhysics());
                }
                if (bean.getChemistry() > 0) {
                    pstmt.setInt(index++, bean.getChemistry());
                }
                if (bean.getMaths() > 0) {
                    pstmt.setInt(index++, bean.getMaths());
                }
			}
			try(ResultSet rs = pstmt.executeQuery()){
				while (rs.next()) {
					bean = new MarksheetBean();
					bean.setId(rs.getLong(1));
					bean.setRollNo(rs.getString(2));
					bean.setStudentld(rs.getLong(3));
					bean.setName(rs.getString(4));
					bean.setPhysics(rs.getInt(5));
					bean.setChemistry(rs.getInt(6));
					bean.setMaths(rs.getInt(7));
					bean.setCreatedBy(rs.getString(8));
					bean.setModifiedBy(rs.getString(9));
					bean.setCreatedDatetime(rs.getTimestamp(10));
					bean.setModifiedDatetime(rs.getTimestamp(11));
					list.add(bean);
				}
			}

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {

		return list;
	}

		} catch (SQLException e) {
			log.error("Database Exception in search", e);
			throw new ApplicationException("Exception: Exception in searching marksheet - " + e.getMessage());
		}
		log.debug("Model search End");
	/**
	 * Retrieves a list of all marksheets.
	 *
	 * @param pageNo   The page number for pagination.
	 * @param pageSize The page size for pagination.
	 * @return A list of MarksheetBean objects.
	 * @throws ApplicationException If a database error occurs.
	 */
	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList<MarksheetBean> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_MARKSHEET");

        if (pageNo < 0) {
            pageNo = 1;
        }
        if (pageSize < 0) {
            pageSize = 10; // Default page size
        }
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" LIMIT " + pageNo + "," + pageSize);
		}

		try (Connection conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql.toString());
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				MarksheetBean bean = new MarksheetBean();
				bean.setId(rs.getLong(1));
				bean.setRollNo(rs.getString(2));
				bean.setStudentld(rs.getLong(3));
				bean.setName(rs.getString(4));
				bean.setPhysics(rs.getInt(5));
				bean.setChemistry(rs.getInt(6));
				bean.setMaths(rs.getInt(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
				list.add(bean);
            }
		} catch (SQLException e) {
			log.error("Database Exception in list", e);
			throw new ApplicationException("Exception: Exception in getting list of Marksheet - " + e.getMessage());
		}
		log.debug("Model list End");
		return list;
	}

	/**
	 * Retrieves the merit list of students based on their total marks.
	 *
	 * @param pageNo   The page number for pagination.
	 * @param pageSize The page size for pagination.
	 * @return A list of MarksheetBean objects sorted by total marks in descending
	 *         order.
	 * @throws ApplicationException If a database error occurs.
	 */
	public List getMeritList(int pageNo, int pageSize) throws ApplicationException {
		log.debug("model MeritList Started");
		ArrayList<MarksheetBean> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer(
				"SELECT ID,ROLL_NO,NAME,PHYSICS,CHEMISTRY,MATHS,(PHYSICS+CHEMISTRY+MATHS) as total from ST_MARKSHEET ORDER BY TOTAL DESC");

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT " + pageNo + "," + pageSize);
        }
        if (pageNo < 0) {
            pageNo = 1;
        }
        if (pageSize < 0) {
            pageSize = 10; // Default page size
        }
		try (Connection conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql.toString());
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				MarksheetBean bean = new MarksheetBean();
				bean.setId(rs.getInt(1));
				bean.setRollNo(rs.getString(2));
				bean.setName(rs.getString(3));
				bean.setPhysics(rs.getInt(4));
				bean.setChemistry(rs.getInt(5));
				bean.setMaths(rs.getInt(6));
				list.add(bean);
			}
		} catch (SQLException e) {
			log.error("Database Exception in getMeritList", e);
			throw new ApplicationException("Exception: Exception is getting meritList of Marksheet - " + e.getMessage());
		}
		log.debug("Model meritList End");
		return list;
	}

}