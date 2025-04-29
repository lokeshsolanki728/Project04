
package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.CollegeBean;
import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.Bean.FacultyBean;
import com.rays.pro4.Bean.SubjectBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;



/**
 *  * JDBC Implementation of FacultyModel.
 * 
 * @author Lokesh SOlanki
 *
 */
public class FacultyModel {
	
	private static Logger log = Logger.getLogger(FacultyModel.class);
	
	/**
	 * Generate next PK of Faculty
	 * @return long
	 * @throws DatabaseException
	 */
	private synchronized long nextPK() throws DatabaseException {
		log.debug("Model nextpk Started");
		long pk=0;
		try(Connection conn=JDBCDataSource.getConnection(); PreparedStatement pstmt=conn.prepareStatement("SELECT MAX(ID) FROM ST_FACULTY")){
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()) {
				pk=rs.getLong(1);
			}	
		}catch(SQLException e){
			log.error("Database Exception in nextPK",e);
			throw new DatabaseException("Exception: Exception in getting pk - "+e.getMessage());
		}
		log.debug("Model next pk End");
		return pk + 1;
	}
	
	/**
	 * add method to add faculty in database
	 * @param bean
	 * @return long
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public long add(FacultyBean bean) throws  ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");
		long pk=0;
		CollegeModel collegeModel = new CollegeModel();	
		CollegeBean collegeBean = collegeModel.findByPK(bean.getCollegeId());
		bean.setCollegeName(collegeBean.getName());
		
		CourseModel courseModel = new CourseModel();	
		CourseBean courseBean = courseModel.FindByPK(bean.getCourseId());
		bean.setCourseName(courseBean.getName());
		
		SubjectModel subjectModel = new SubjectModel();
		SubjectBean subjectBean = subjectModel.FindByPK(bean.getSubjectId());
		bean.setSubjectName(subjectBean.getSubjectName());
		
		FacultyBean beanExist = findByEmailId(bean.getEmailId());
		if (beanExist != null) { 
			  throw new DuplicateRecordException("Email already exists"); 
		}	
		try(Connection conn=JDBCDataSource.getConnection()){
			conn.setAutoCommit(false);
			pk=nextPK();
			try(PreparedStatement pstmt=conn.prepareStatement("INSERT INTO ST_FACULTY (ID,FIRST_NAME,LAST_NAME,GENDER,EMAIL_ID,MOBILE_NO,COLLEGE_ID,COLLEGE_NAME,COURSE_ID,COURSE_NAME,DOB,SUBJECT_ID,SUBJECT_NAME,CREATED_BY,MODIFIED_BY,CREATED_DATETIME,MODIFIED_DATETIME) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")){
				pstmt.setLong(1, pk);
				pstmt.setString(2, bean.getFirstName());
				pstmt.setString(3, bean.getLastName());
				pstmt.setString(4, bean.getGender());
				pstmt.setString(5, bean.getEmailId());
				pstmt.setString(6, bean.getMobileNo());
				pstmt.setLong(7, bean.getCollegeId());
				pstmt.setString(8, bean.getCollegeName());
				pstmt.setLong(9, bean.getCourseId());
				pstmt.setString(10, bean.getCourseName());
				pstmt.setDate(11, new java.sql.Date(bean.getDob().getTime()));
				pstmt.setLong(12, bean.getSubjectId());
				pstmt.setString(13, bean.getSubjectName());
				pstmt.setString(14, bean.getCreatedBy());
				pstmt.setString(15, bean.getModifiedBy());
				pstmt.setTimestamp(16, bean.getCreatedDatetime());
				pstmt.setTimestamp(17, bean.getModifiedDatetime());
				pstmt.executeUpdate();
				conn.commit();	
			}	
		}catch(DuplicateRecordException e) {
			log.error("Duplicate record Exception in add faculty",e);
			JDBCDataSource.trnRollback();
			throw new DuplicateRecordException("Exception : Faculty already exists"+e.getMessage());
		} catch (SQLException e) {
				log.error("Database Exception in add faculty", e);
				try(Connection conn=JDBCDataSource.getConnection()){
					conn.rollback();
				}catch(SQLException ex) {
					throw new ApplicationException("Exception : add rollback Exception - " + ex.getMessage());
				}
				throw new ApplicationException("Exception : Exception in add Faculty " + e.getMessage());
		}
		log.debug("Model add End");
		return pk;
	}
	/**
	 * delete method to delete faculty by id
	 * @param id
	 * @throws ApplicationException
	 */
	public void delete(long id) throws ApplicationException {
		log.debug("Model delete Started");
		Connection conn = null;
		PreparedStatement pstmt=null;
		try {
			conn = JDBCDataSource.getConnection();
			pstmt=conn.prepareStatement("DELETE FROM ST_FACULTY WHERE ID=?");
			conn.setAutoCommit(false);
			try(Connection conn=JDBCDataSource.getConnection(); PreparedStatement pstmt=conn.prepareStatement("DELETE FROM ST_FACULTY WHERE ID=?")){
				pstmt.setLong(1, id);
				pstmt.executeUpdate();
				conn.commit();
			}
		}catch(SQLException e) {
			log.error("Database Exception in delete faculty",e);
			try(Connection conn=JDBCDataSource.getConnection()){
				conn.rollback();
			}catch(SQLException ex) {
				throw new ApplicationException("Exception : delete rollback exception  - " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete Student - "+e.getMessage());
		} finally {
			JDBCDataSource.closePreparedStatement(pstmt);
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model delete End");
	}
	/**
	 * update method to update the faculty
	 * @param bean
	 * @throws ApplicationException
	 */
	public void update(FacultyBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("model update Started");
		Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            try(PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_FACULTY SET FIRST_NAME=?,LAST_NAME=?,GENDER=?,EMAIL_ID=?,MOBILE_NO=?,COLLEGE_ID=?,COLLEGE_NAME=?,COURSE_ID=?,COURSE_NAME=?,DOB=?,SUBJECT_ID=?,SUBJECT_NAME=?, CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?")){
				pstmt.setString(1, bean.getFirstName());
				pstmt.setString(2, bean.getLastName());
				pstmt.setString(3, bean.getGender());
				pstmt.setString(4, bean.getEmailId());
				pstmt.setString(5, bean.getMobileNo());
				pstmt.setLong(6, bean.getCollegeId());
				pstmt.setString(7, bean.getCollegeName());
				pstmt.setLong(8, bean.getCourseId());
				pstmt.setString(9, bean.getCourseName());
				pstmt.setDate(10, new java.sql.Date(bean.getDob().getTime()));
				pstmt.setLong(11, bean.getSubjectId());
				pstmt.setString(12, bean.getSubjectName());
				pstmt.setString(13, bean.getCreatedBy());
				pstmt.setString(14, bean.getModifiedBy());
				pstmt.setTimestamp(15, bean.getCreatedDatetime());
				pstmt.setTimestamp(16, bean.getModifiedDatetime());
				pstmt.setLong(17, bean.getId());
	
				pstmt.executeUpdate();
				conn.commit();
            }
		}catch (DuplicateRecordException e) {
			log.error("Duplicate Record exception in update faculty",e);
			JDBCDataSource.trnRollback();
			throw new DuplicateRecordException("Exception : Faculty already exists"+e.getMessage());
		}catch(SQLException e) {
			log.error("Database Exception in update faculty",e);
			try(Connection conn=JDBCDataSource.getConnection()){
				conn.rollback();
			}catch(SQLException ex) {
				throw new ApplicationException("Exception : delete rollback exception  - " + ex.getMessage());
			}
			throw new ApplicationException("Exception in updating Faculty - " + e.getMessage());
		}finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model update End");
	}
	
	/**
	 * find by pk method to get the faculty by pk
	 * @param pk
	 * @return FacultyBean
	 * @throws ApplicationException
	 */
	public FacultyBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findByPK Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE ID=?");
		FacultyBean bean = null;
		try(Connection conn=JDBCDataSource.getConnection();PreparedStatement pstmt=conn.prepareStatement(sql.toString())){
			pstmt.setLong(1, pk);
			try(ResultSet rs=pstmt.executeQuery()){
				while (rs.next()) {
					bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setGender(rs.getString(4));
				bean.setEmailId(rs.getString(5));
				bean.setMobileNo(rs.getString(6));
				bean.setCollegeId(rs.getLong(7));
				bean.setCollegeName(rs.getString(8));
				bean.setCourseId(rs.getLong(9));
				bean.setCourseName(rs.getString(10));
				bean.setDob(rs.getDate(11));
				bean.setSubjectId(rs.getLong(12));
				bean.setSubjectName(rs.getString(13));
				bean.setCreatedBy(rs.getString(14));
				bean.setModifiedBy(rs.getString(15));
				bean.setCreatedDatetime(rs.getTimestamp(16));
				bean.setModifiedDatetime(rs.getTimestamp(17));
			}
			}
		}catch(SQLException e) {
			log.error("Database Exception in findByPk faculty",e);
			throw new ApplicationException("Exception : Exception in getting Faculty by pk - "+e.getMessage());
		}
		log.debug("Model findByPK End");
		return bean;
	}
	
	/**
	 * find by email id method to find the faculty by email id
	 * @param email
	 * @return FacultyBean
	 * @throws ApplicationException
	 */
	public FacultyBean findByEmailId(String email) throws ApplicationException {
		log.debug("Model findBy Email Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE EMAIL_id=?");
		FacultyBean bean = null;
		try(Connection conn=JDBCDataSource.getConnection(); PreparedStatement pstmt=conn.prepareStatement(sql.toString())){
			pstmt.setString(1, email);
			try(ResultSet rs=pstmt.executeQuery()){
				while (rs.next()) {
				
				bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setGender(rs.getString(4));
				bean.setEmailId(rs.getString(5));
				bean.setMobileNo(rs.getString(6));
				bean.setCollegeId(rs.getLong(7));
				bean.setCollegeName(rs.getString(8));
				bean.setCourseId(rs.getLong(9));
				bean.setCourseName(rs.getString(10));
				bean.setDob(rs.getDate(11));
				bean.setSubjectId(rs.getLong(12));
				bean.setSubjectName(rs.getString(13));
				bean.setCreatedBy(rs.getString(14));
				bean.setModifiedBy(rs.getString(15));
				bean.setCreatedDatetime(rs.getTimestamp(16));
				bean.setModifiedDatetime(rs.getTimestamp(17));
			}
			}	
		}catch(SQLException e) {
			log.error("Database Exception in findByEmail faculty",e);
			throw new ApplicationException("Exception : Exception in getting Faculty by Email - "+e.getMessage());
		}
		log.debug("Model findBy Email End");
		return bean;
	}
	
	/**
	 * list method to get all faculty list
	 * @return List
	 * @throws ApplicationException
	 */
	public List list() throws ApplicationException {
		return list(1, 0);
	}
	/**
	 * list method to get faculty list by pagination
	 * @param pageNo
	 * @param pageSize
	 * @return List
	 * @throws ApplicationException
	 */
	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
        if (pageNo < 0) {
            pageNo = 1;
        }
        if (pageSize < 0) {
            pageSize = 10; // Default page size
        }
        
		ArrayList<FacultyBean> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select * from ST_FACULTY");
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}
		try(Connection conn=JDBCDataSource.getConnection();PreparedStatement pstmt=conn.prepareStatement(sql.toString()); ResultSet rs=pstmt.executeQuery()){
			while(rs.next()) {
				FacultyBean bean=new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setGender(rs.getString(4));
				bean.setEmailId(rs.getString(5));
				bean.setMobileNo(rs.getString(6));
				bean.setCollegeId(rs.getLong(7));
				bean.setCollegeName(rs.getString(8));
				bean.setCourseId(rs.getLong(9));
				bean.setCourseName(rs.getString(10));
				bean.setDob(rs.getDate(11));
				bean.setSubjectId(rs.getLong(12));
				bean.setSubjectName(rs.getString(13));
				bean.setCreatedBy(rs.getString(14));
				bean.setModifiedBy(rs.getString(15));
				bean.setCreatedDatetime(rs.getTimestamp(16));
				bean.setModifiedDatetime(rs.getTimestamp(17));
				list.add(bean);
			}	
		}catch(SQLException e) {
			log.error("Database Exception in list faculty",e);
			throw new ApplicationException("Exception : Exception in getting list of faculty - "+e.getMessage());
		}
		log.debug("Model list End");
		return list;
	}
	/**
	 * search method to search the faculty by values
	 * @param bean
	 * @return List
	 * @throws ApplicationException
	 */
	public List search(FacultyBean bean) throws ApplicationException {
		return search(bean, 1, 0);
	}
	/**
	 * search method to search faculty by pagination
	 * @param bean
	 * @param pageNo
	 * @param pageSize
	 * @return List
	 * @throws ApplicationException
	 */
	public List search(FacultyBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model search Started");
		StringBuffer sql=new StringBuffer("select * from ST_FACULTY WHERE 1=1");
		ArrayList<FacultyBean> list=new ArrayList<>();
        if (pageNo < 0) {
            pageNo = 1;
        }
        if (pageSize < 0) {
            pageSize = 10; // Default page size
        }
		if(bean!=null) {
			if (bean.getId()>0) {
				sql.append(" AND id = " + bean.getId());
			}
			if (bean.getCollegeId() > 0) {
				sql.append(" AND college_Id = " + bean.getCollegeId());
			}
			if (bean.getFirstName() != null && bean.getFirstName().trim().length() > 0) {
				sql.append(" AND FIRST_NAME like '" + bean.getFirstName() + "%'");
			}
			if (bean.getLastName() != null && bean.getLastName().trim().length() > 0) {
				sql.append(" AND LAST_NAME like '" + bean.getLastName() + "%'");
			}
			
			if (bean.getEmailId()!=null && bean.getEmailId().length()>0) {
				sql.append(" AND Email_Id like '" + bean.getEmailId() + "%'");
			}
			
			if (bean.getGender()!=null && bean.getGender().length()>0) {
				sql.append(" AND Gender like '" + bean.getGender() + "%'");
			}
	
		
			if (bean.getMobileNo()!=null && bean.getMobileNo().length()>0) {
				sql.append(" AND Mobile_No like '" + bean.getMobileNo() + "%'");
			}
			
			if (bean.getCollegeName()!=null && bean.getCollegeName().length()>0) {
				sql.append(" AND college_Name like '" + bean.getCollegeName() + "%'");
			}
			if (bean.getCourseId() > 0) {
				sql.append(" AND course_Id = " + bean.getCourseId());
			}
			if (bean.getCourseName()!=null && bean.getCourseName().length()>0) {
				sql.append(" AND course_Name like '" + bean.getCourseName() + "%'");
			}
			if (bean.getSubjectId() > 0) {
				sql.append(" AND Subject_Id = " + bean.getSubjectId());
			}
			if (bean.getSubjectName()!=null && bean.getSubjectName().length()>0) {
				sql.append(" AND subject_Name like '" + bean.getSubjectName() + "%'");
			}
		}
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}
        try(Connection conn=JDBCDataSource.getConnection(); PreparedStatement pstmt=conn.prepareStatement(sql.toString());ResultSet rs=pstmt.executeQuery()){
			while(rs.next()){
                bean=new FacultyBean();
                bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
                bean.setLastName(rs.getString(3));
                bean.setGender(rs.getString(4));
                bean.setEmailId(rs.getString(5));
                bean.setMobileNo(rs.getString(6));
                bean.setCollegeId(rs.getLong(7));
                bean.setCollegeName(rs.getString(8));
                bean.setCourseId(rs.getLong(9));
                bean.setCourseName(rs.getString(10));
                bean.setDob(rs.getDate(11));
                bean.setSubjectId(rs.getLong(12));
                bean.setSubjectName(rs.getString(13));
                bean.setCreatedBy(rs.getString(14));
                bean.setModifiedBy(rs.getString(15));
                bean.setCreatedDatetime(rs.getTimestamp(16));
                bean.setModifiedDatetime(rs.getTimestamp(17));
                list.add(bean);
			}
		}catch(SQLException e) {
			
			log.error("Database Exception .....", e);
			throw new ApplicationException("Exception in the search - "+e.getMessage());
		} finally {
		
		}
		log.debug("Model search End");
		return list;
	}
	
}
