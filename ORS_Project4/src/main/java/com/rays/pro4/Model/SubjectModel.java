package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.Bean.SubjectBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class SubjectModel extends BaseModel {

    private String subjectName;
    private String description;
    private long courseId;
    private String courseName;
    
    
	
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
    }
    
    public long add(SubjectBean bean) throws ApplicationException, DuplicateRecordException {
        
        Connection conn = null;
        try {
            
            CourseModel courseModel = new CourseModel();
            CourseBean courseBean = courseModel.FindByPK(bean.getCourseId());
            bean.setCourseName(courseBean.getName());

            long pk = 0;
            conn = JDBCDataSource.getConnection();
            pk = add(bean,conn);
            return pk;
        
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
            
                throw new ApplicationException("Exception: Exception in add Subject - " + e.getMessage());
            }
         }   
       
     public SubjectBean findByName(String name) throws ApplicationException {
         StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT WHERE SUBJECT_NAME=?");
         SubjectBean bean = null;
         Connection conn = null;
         try {
             conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString());
             pstmt.setString(1, name);
             ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                bean=new SubjectBean();
                bean.setId(rs.getLong(1));
                bean.setSubjectName(rs.getString(2));
                 populate(bean, rs);
             }
         } catch (Exception e) {
             throw new ApplicationException("Exception in getting subject by name " + e.getMessage());
         } finally {
             JDBCDataSource.closeConnection(conn);
         }
         return bean;
     }
     
    public void Delete(long id) throws ApplicationException {
        
        SubjectBean bean = findByPK(id);
        try {
             delete(bean);
        } catch (Exception e) {
           
            throw new ApplicationException("Exception: Exception in delete Subject - " + e.getMessage());
        }
    }

	public void update(SubjectBean bean) throws ApplicationException, DuplicateRecordException {
		try (Connection conn = JDBCDataSource.getConnection()){
		     CourseModel cModel = new CourseModel();
		     CourseBean CourseBean = cModel.FindByPK(bean.getCourseId());
		     bean.setCourseName(CourseBean.getName());	 
			  SubjectBean duplicateName = findByName(bean.getSubjectName());
			  if (duplicateName != null && duplicateName.getId() != bean.getId()) {
				  throw new DuplicateRecordException("Subject Name already exists");
			  }
			  update(bean,conn);
		}catch(DuplicateRecordException e) {
				log.error("Duplicate record exception",e);
				throw e;
			} catch (Exception e) {
			 
			 throw new ApplicationException("Exception :Exception in update subject " + e.getMessage());
		 }
	 }
	 
    public void update(SubjectBean bean, Connection conn) throws ApplicationException, DuplicateRecordException, Exception {
        
           PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_SUBJECT SET SUBJECT_NAME=?,DESCRIPTION=?,COURSE_ID=?,COURSE_NAME=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");
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
			
		}
	   
	    
	 public SubjectBean FindByPK(long pk) throws ApplicationException {
	     
	     String sql = "SELECT * FROM ST_SUBJECT WHERE ID=?";
	     SubjectBean bean = null;
	     try (Connection conn = JDBCDataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
	         
	         pstmt.setLong(1, pk);
	         ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                 bean = new SubjectBean();
                 populate(bean, rs);
            }
		 }catch(Exception e) {
			 throw new ApplicationException("Exception in getting subject by pk " + e.getMessage());
		 }
		 return bean;
	 }
	 
	 
	 public long add(SubjectBean bean, Connection conn) throws ApplicationException, DuplicateRecordException,Exception {
		 PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_SUBJECT VALUES(?,?,?,?,?,?,?,?,?)");
		 long pk=nextPK();
		 bean.setId(pk);
		         pstmt.setLong(1, bean.getId());
	            pstmt.setString(2, bean.getSubjectName());
	             bean.setDescription(rs.getString(3));
	             bean.setCourseId(rs.getLong(4));
	             bean.setCourseName(rs.getString(5));
	             bean.setCreatedBy(rs.getString(6));
	             bean.setModifiedBy(rs.getString(7));
	             bean.setCreatedDatetime(rs.getTimestamp(8));
	             bean.setModifiedDatetime(rs.getTimestamp(9));
	         }
				 
	    
	 }
	
	 
	 
	 public List search( SubjectBean bean) throws DatabaseException, ApplicationException {
		 return search(bean,0,0);
	 }
	 public List search(SubjectBean bean, int pageNo, int pageSize) throws ApplicationException{
	    StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT WHERE true");
	     ArrayList list = new ArrayList();
	     
	     if (pageNo < 0) {
	            pageNo = 1;
	        }
	        if (pageSize < 0) {
	            pageSize = 10; // Default page size
	        }
	     
	     try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

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
	    StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT");
	    
	    if (pageNo < 0) {
	            pageNo = 1;
	        }
	        if (pageSize < 0) {
	            pageSize = 10; // Default page size
	        }

	     ArrayList<SubjectBean> list = new ArrayList<SubjectBean>();
	     if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                sql.append(" LIMIT " + pageNo + ", " + pageSize);
            }
	     try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString());ResultSet rs = pstmt.executeQuery();){
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
	     } catch (Exception e) {
	         throw new ApplicationException("Exception : Exception in list Subject - " + e.getMessage());
	     }
	     return list;
	 }
	 public String getTableName() {
        return "ST_SUBJECT";
    }
 }
}