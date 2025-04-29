package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.Bean.SubjectBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

/**
 * JDBC Implementation of Subject Model.
 * 
 * @author Lokesh SOlanki
 *
 */
public class SubjectModel {

	
	private static Logger log= Logger.getLogger(SubjectModel.class);
	
	
	private Integer nextPK() throws DatabaseException {
        log.debug("Model nextPK Started");
        int pk = 0;
        try (Connection connection = JDBCDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(ID) FROM ST_SUBJECT");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                pk = resultSet.getInt(1);
            }
        } catch (Exception e) {
            log.error("Database Exception while getting next PK in Subject", e);
            throw new DatabaseException("Exception: Exception in getting next PK in Subject - " + e.getMessage());
        }
        log.debug("Model nextPK End");
        return pk + 1; // Incrementing PK only once
    }

    public long add(SubjectBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("Model add Started");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int pk = 0;

        try {
            CourseModel courseModel = new CourseModel();
            CourseBean courseBean = courseModel.FindByPK(bean.getCourseId());
            bean.setCourseName(courseBean.getName());

            SubjectBean duplicateName = findByName(bean.getSubjectName());
            if (duplicateName != null) {
                throw new DuplicateRecordException("Subject Name already exists");
            }
            connection = JDBCDataSource.getConnection();
            pk = nextPK();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("INSERT INTO ST_SUBJECT VALUES(?,?,?,?,?,?,?,?,?)");
            preparedStatement.setInt(1, pk);
            preparedStatement.setString(2, bean.getSubjectName());
            preparedStatement.setString(3, bean.getDescription());
            preparedStatement.setLong(4, bean.getCourseId());
            preparedStatement.setString(5, bean.getCourseName());
            preparedStatement.setString(6, bean.getCreatedBy());
            preparedStatement.setString(7, bean.getModifiedBy());
            preparedStatement.setTimestamp(8, bean.getCreatedDatetime());
            preparedStatement.setTimestamp(9, bean.getModifiedDatetime());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (DuplicateRecordException e) {
            log.error("Duplicate Record Exception in add Subject", e);
            JDBCDataSource.closeConnection(connection);
            throw e;

        }catch (Exception e) {
            log.error("Database Exception in add Subject", e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (Exception ex) {
                    throw new ApplicationException("Exception: add rollback Exception - " + ex.getMessage());
                }
            }
            throw new ApplicationException("Exception: Exception in add Subject - " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(connection);
             log.debug("Model add End");
        }
        return pk;
    }
    public void Delete(long id) throws ApplicationException {
        log.debug("Model delete Started");
        try (Connection connection = JDBCDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM ST_SUBJECT WHERE ID=?")) {

            connection.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();

        } catch (Exception e) {
            log.error("Database Exception in delete Subject", e);
            try (Connection connection = JDBCDataSource.getConnection()) {
                connection.rollback();

            } catch (Exception ex) {
                throw new ApplicationException("Exception: Delete rollback Exception - " + ex.getMessage());
            }
            throw new ApplicationException("Exception: Exception in delete Subject - " + e.getMessage());
        }
        log.debug("Model delete End");
    }
	 
	 public void update(SubjectBean bean) throws ApplicationException, DuplicateRecordException {
		 log.debug("model update Started");
		 Connection conn = null;
		 PreparedStatement pstmt = null;
		 try {
		     CourseModel cModel = new CourseModel();
		     CourseBean CourseBean = cModel.FindByPK(bean.getCourseId());
		     bean.setCourseName(CourseBean.getName());	 
			 SubjectBean duplicateName = findByName(bean.getSubjectName());
			 if (duplicateName != null && duplicateName.getId() != bean.getId()) {
				 throw new DuplicateRecordException("Subject Name already exists");
			 }
		    conn = JDBCDataSource.getConnection();
			 conn.setAutoCommit(false);
			 pstmt = conn.prepareStatement("UPDATE ST_SUBJECT SET SUBJECT_NAME=?,DESCRIPTION=?,COURSE_ID=?,COURSE_NAME=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");
			 pstmt.setString(1, bean.getSubjectName());
	         pstmt.setString(2, bean.getDescription());
	         pstmt.setLong(3, bean.getCourseId());
	         pstmt.setString(4, bean.getCourseName());
	         pstmt.setString(5, bean.getCreatedBy());
	         pstmt.setString(6, bean.getModifiedBy());
	         pstmt.setTimestamp(7, bean.getCreatedDatetime());
	         pstmt.setTimestamp(8, bean.getModifiedDatetime());
	         pstmt.setLong(9, bean.getId());
			 pstmt.executeUpdate();
			 conn.commit();
		 } catch(DuplicateRecordException e) {
				log.error("Duplicate record exception",e);
				JDBCDataSource.closeConnection(conn);
				throw e;
			}catch (Exception e) {
			 log.error("Database Exception in update Subject",e);
			 try {
				 conn.rollback();
			 }catch(Exception ex) {
				 throw new ApplicationException("Exception : update rollback Exception " +ex.getMessage() );
			 }
			 throw new ApplicationException("Exception :Exception in update subject " + e.getMessage());
		}finally {
			JDBCDataSource.closeConnection(conn);
		}
		 log.debug("Model upodate End");
		 
	 }
	 
	 public SubjectBean findByName(String name) throws ApplicationException {
		 log.debug("Model findByName Started");
		 String sql="SELECT * FROM ST_SUBJECT WHERE SUBJECT_NAME=?";
		 SubjectBean bean = null;
		 Connection conn = null;
		 PreparedStatement pstmt = null;
		 ResultSet rs = null;
		 try {
			 conn=JDBCDataSource.getConnection();
			 pstmt = conn.prepareStatement(sql.toString());
			 pstmt.setString(1, name);
			 rs=pstmt.executeQuery();
			 while(rs.next()) {
				 bean=new SubjectBean();
				 bean.setId(rs.getLong(1));
				 bean.setSubjectName(rs.getString(2));
				 bean.setDescription(rs.getString(3));
				 bean.setCourseId(rs.getLong(4));
				 bean.setCourseName(rs.getString(5));
				 bean.setCreatedBy(rs.getString(6));
				 bean.setModifiedBy(rs.getString(7));
				 bean.setCreatedDatetime(rs.getTimestamp(8));
				 bean.setModifiedDatetime(rs.getTimestamp(9));			 
			 }
		 }catch(Exception e) {
			 log.error("Database Exception in findByName",e);
			 throw new ApplicationException("Exception in getting subject by name " + e.getMessage());
		 }finally {
		     JDBCDataSource.closeConnection(conn);
	     log.debug("Model findByName End");
	    }
	    return bean;		
	}
	 public SubjectBean FindByPK(long pk) throws ApplicationException {
	     log.debug("Model FindByPK Started");
	     String sql = "SELECT * FROM ST_SUBJECT WHERE ID=?";
	     SubjectBean bean = null;
	     try (Connection conn = JDBCDataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
	         conn = JDBCDataSource.getConnection();
	         pstmt = conn.prepareStatement(sql.toString());
	         pstmt.setLong(1, pk);
	         rs = pstmt.executeQuery();

	         while (rs.next()) {
	             bean = new SubjectBean();
	             bean.setId(rs.getLong(1));
	             bean.setSubjectName(rs.getString(2));
	             bean.setDescription(rs.getString(3));
	             bean.setCourseId(rs.getLong(4));
	             bean.setCourseName(rs.getString(5));
	             bean.setCreatedBy(rs.getString(6));
	             bean.setModifiedBy(rs.getString(7));
	             bean.setCreatedDatetime(rs.getTimestamp(8));
	             bean.setModifiedDatetime(rs.getTimestamp(9));
	         }
				 
		 }catch(Exception e) {
			 log.error("Database Exception in findByPk",e);
			 throw new ApplicationException("Exception in getting subject by pk " + e.getMessage());
		 } finally {
			 log.debug("Model FindByPK End");
		 }
		 return bean;
	 }
	 
	 public List search( SubjectBean bean) throws DatabaseException, ApplicationException {
		 return search(bean,0,0);
	 }
	 public List search(SubjectBean bean, int pageNo, int pageSize) throws ApplicationException{
	     log.debug("Model search Started");
	     StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT WHERE true");
	     ArrayList list = new ArrayList();
	     
	     if (pageNo < 0) {
	            pageNo = 1;
	        }
	        if (pageSize < 0) {
	            pageSize = 10; // Default page size
	        }
	     
	     try (Connection conn = JDBCDataSource.getConnection();) {

	         if (bean != null) {
	             if (bean.getId() > 0) {
	                 sql.append(" AND ID = ?");
	             }
	             if (bean.getSubjectName() != null && bean.getSubjectName().length() > 0) {
	                 sql.append(" AND Subject_Name like ?");
	             }
	             if (bean.getDescription() != null && bean.getDescription().length() > 0) {
	                 sql.append(" AND Description like ?");
	             }
	             if (bean.getCourseId() > 0) {
	                 sql.append(" AND Course_id = ?");
	             }
	             if (bean.getCourseName() != null && bean.getCourseName().length() > 0) {
	                 sql.append(" AND course_Name like ?");
	             }
	         }

	         if (pageSize > 0) {
	             pageNo = (pageNo - 1) * pageSize;
	             sql.append(" limit " + pageNo + "," + pageSize);
	         }
	         pstmt = conn.prepareStatement(sql.toString());
	         ResultSet rs = pstmt.executeQuery();
	         int index = 1;
	         if (bean.getId() > 0) {
	             pstmt.setLong(index++, bean.getId());
	         }
	         if (bean.getSubjectName() != null && bean.getSubjectName().length() > 0) {
	             pstmt.setString(index++, bean.getSubjectName() + "%");
	         }
	         if (bean.getDescription() != null && bean.getDescription().length() > 0) {
	             pstmt.setString(index++, bean.getDescription() + "%");
	         }
	         if (bean.getCourseId() > 0) {
	             pstmt.setLong(index++, bean.getCourseId());
	         }
	         if (bean.getCourseName() != null && bean.getCourseName().length() > 0) {
	             pstmt.setString(index++, bean.getCourseName() + "%");
	         }
	         while (rs.next()) {
	             bean = new SubjectBean();
	             bean.setId(rs.getLong(1));
	             bean.setSubjectName(rs.getString(2));
	             bean.setDescription(rs.getString(3));
	             bean.setCourseId(rs.getLong(4));
	             bean.setCourseName(rs.getString(5));
	             bean.setCreatedBy(rs.getString(6));
	             bean.setModifiedBy(rs.getString(7));
	             bean.setCreatedDatetime(rs.getTimestamp(8));
	             bean.setModifiedDatetime(rs.getTimestamp(9));
	             list.add(bean);
	         }
	     } catch (Exception e) {
	         log.error("Database Exception...", e);
	         throw new ApplicationException("Exception in the search " + e.getMessage());
	     } finally {
	        
	         log.debug("Model search End");
	     }
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
	        
	     ArrayList list = new ArrayList();
	     String sql = "SELECT * FROM ST_SUBJECT";

	     try (Connection connection = JDBCDataSource.getConnection();
	            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
	          if (pageSize > 0) {
                sql += " LIMIT " + ((pageNo - 1) * pageSize) + ", " + pageSize;
            }
	         ResultSet rs= preparedStatement.executeQuery(sql);
	         SubjectBean bean;
	         while (rs.next()) {
	             bean = new SubjectBean();
	             bean.setId(rs.getLong(1));
	             bean.setSubjectName(rs.getString(2));
	             bean.setDescription(rs.getString(3));
	             bean.setCourseId(rs.getLong(4));
	             bean.setCourseName(rs.getString(5));
	             bean.setCreatedBy(rs.getString(6));
	             bean.setModifiedBy(rs.getString(7));
	             bean.setCreatedDatetime(rs.getTimestamp(8));
	             bean.setModifiedDatetime(rs.getTimestamp(9));
	             list.add(bean);
	         }
	         log.debug("Model list End");
	         return list;
	     } catch (Exception e) {
	         log.error("Database Exception in list Subject", e);
	         throw new ApplicationException("Exception : Exception in list Subject - " + e.getMessage());
	     }
	     return list;
	 }
}