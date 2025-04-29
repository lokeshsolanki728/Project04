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
	    log.debug("Model nextpk Started");
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    int pk = 0;
	    try {
	        conn = JDBCDataSource.getConnection();
	        pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_SUBJECT");
	        rs = pstmt.executeQuery();
	        while (rs.next()) {
	            pk = rs.getInt(1);
	        }
	        return pk + 1;
	    } catch (Exception e) {
	        log.error("Database Exception..", e);
	        throw new DatabaseException("Exception : Exception in getting next PK in Subject " + e.getMessage());
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
			   } catch (Exception e) {
				   log.error("Exception in closing resources", e);
			   }
			   JDBCDataSource.closeConnection(conn);
		   }
		   log.debug("Model next pk End" );
		   return pk = pk+1;
	}
	
	public long add(SubjectBean bean) throws ApplicationException, DuplicateRecordException {
		   log.debug("Model add Started");
		   Connection conn=null;
		   PreparedStatement pstmt = null;
		   int pk = 0;

		   CourseModel cModel = new CourseModel();
		   CourseBean CourseBean = cModel.FindByPK(bean.getCourseId());
		   bean.setCourseName(CourseBean.getName());

		   SubjectBean duplicateName = findByName(bean.getSubjectName());

		   if (duplicateName != null) {
		       throw new DuplicateRecordException("Subject Name already exists");
		   }
		   try {
		       conn = JDBCDataSource.getConnection();
		       pk = nextPK();
		       conn.setAutoCommit(false);
		       pstmt = conn.prepareStatement("INSERT  ST_SUBJECT VALUE(?,?,?,?,?,?,?,?,?)");
		       pstmt.setInt(1, pk);
		       pstmt.setString(2, bean.getSubjectName());
		       pstmt.setString(3, bean.getDescription());
		       pstmt.setLong(4, bean.getCourseId());
		       pstmt.setString(5, bean.getCourseName());
		       pstmt.setString(6, bean.getCreatedBy());
		       pstmt.setString(7, bean.getModifiedBy());
		       pstmt.setTimestamp(8, bean.getCreatedDatetime());
		       pstmt.setTimestamp(9, bean.getModifiedDatetime());
		       pstmt.executeUpdate();
		       conn.commit();
		   } catch (Exception e) {
		       log.error("Database Exception....", e);
		       try {
		           conn.rollback();
		       } catch (Exception ex) {
		           throw new ApplicationException("Exception : add rollback Exception " + ex.getMessage());
		       }
		       throw new ApplicationException("Exception : Exception in add Subject " + e.getMessage());
		   } finally {
		       if (pstmt != null) {
		           try {
		               pstmt.close();
		           } catch (Exception ex) {
		               log.error("Exception in closing statement", ex);
		           }
		       }
		       JDBCDataSource.closeConnection(conn);
		   }
		   log.debug("Model add End");
		   return pk;
	}
	 public void Delete(long id) throws ApplicationException {
		   log.debug("Model Delete Started");
		   Connection conn = null;
		   PreparedStatement pstmt = null;
		   try {
		       conn = JDBCDataSource.getConnection();
		       conn.setAutoCommit(false);
		       pstmt = conn.prepareStatement("DELETE  FROM ST_SUBJECT WHERE ID=?");
		       pstmt.setLong(1, id);
		       pstmt.executeUpdate();
		       conn.commit();
		   } catch (Exception e) {
		       log.error("Database Exception....", e);
			   try {
				   conn.rollback();
			   }catch(Exception ex) {
				   throw new ApplicationException("Exception : Delete rollback Wxception "+ ex.getMessage());
			   }
			   throw new ApplicationException("Exception in delete subjecte");
			   
		   }finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (Exception ex) {
						log.error("Exception in closing statement", ex);
					}
				}
			   JDBCDataSource.closeConnection(conn);
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

		     conn = JDBCDataSource.getConnection();
			 conn.setAutoCommit(false);
			 PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_SUBJECT SET SUBJECT_NAME=?,DESCRIPTION=?,COURSE_ID=?,COURSE_NAME=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");
			 
			 pstmt.setString(1, bean.getSubjectName());
			 pstmt.setString(2, bean.getDescription());
			 pstmt.setLong(3, bean.getCourseId());
			 pstmt.setString(4, bean.getCourseName());
			 pstmt.setString(5, bean.getCreatedBy());
			 pstmt.setString(6, bean.getModifiedBy());
			 pstmt.setTimestamp(7,bean.getCreatedDatetime());
			 pstmt.setTimestamp(8, bean.getModifiedDatetime());
			 pstmt.setLong(9, bean.getId());
		     pstmt.executeUpdate();
		 }catch(Exception e) {
			 e.printStackTrace();
			 log.error("Database Exception,,,,,,,",e);
			 try {
				 conn.rollback();
			 }catch(Exception ex) {
				 throw new ApplicationException("Exception : update rollback Exception " +ex.getMessage() );
			 }
			 throw new ApplicationException("Exception :Exception in update subject " + e.getMessage());
		 }finally {
		     try {
		         conn.commit();
		         if (pstmt != null) pstmt.close();
		     } catch (Exception ex) {
		         try {
		             conn.rollback();
		         } catch (Exception exx) {
		             log.error("Exception in rolling back", exx);
		         }
		     }
		 }
		 log.debug("Model upodate End");
		 
	 }
	 
	 public SubjectBean findByName(String name) throws ApplicationException {
		 log.debug("Model findByName Started");
		 StringBuffer sql=new StringBuffer("SELECT * FROM ST_SUBJECT WHERE SUBJECT_NAME=?");
		 SubjectBean bean = null;
		 Connection conn = null;
		 PreparedStatement pstmt = null;
		 ResultSet rs = null;
		 try {
			 conn=JDBCDataSource.getConnection();
			 pstmt = conn.prepareStatement(sql.toString());
			 pstmt.setString(1,name);
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
			 log.error("Database Exception...",e);
			 throw new ApplicationException("Exception in getting subject by name " + e.getMessage());
		 }finally {
		     try {
		         if (rs != null) rs.close();
		         if (pstmt != null) pstmt.close();
		     } catch (Exception ex) {
		         log.error("Exception in closing resources", ex);
		     }
		     JDBCDataSource.closeConnection(conn);
		     log.debug("Model findByName End");
		 }
		 return bean;		 
	 } 
	 public SubjectBean FindByPK(long pk) throws ApplicationException {
	     log.debug("Model FindByPK Started");
	     StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT WHERE ID=?");
	     Connection conn = null;
	     PreparedStatement pstmt = null;
	     ResultSet rs = null;
	     SubjectBean bean = null;
	     try {
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
			 log.error("Database Exception...",e);
			 throw new ApplicationException("Exception in getting subject by pk " + e.getMessage());
		 } finally {
			 try {
		         if (rs != null) rs.close();
		         if (pstmt != null) pstmt.close();
		     } catch (Exception ex) {
		         log.error("Exception in closing resources", ex);
		     }
			 JDBCDataSource.closeConnection(conn);
			 log.debug("Model FindbyPK End");
		 }
		 return bean;
	 }
	 
	 
	 
	 
	 
	 public List search( SubjectBean bean) throws DatabaseException, ApplicationException {
		 return search(bean,0,0);
	 }
	 public List search(SubjectBean bean, int pageNo, int pageSize) throws ApplicationException {
	     log.debug("Model search Started");
	     StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT WHERE true");
	     Connection conn = null;
	     PreparedStatement pstmt = null;
	     ResultSet rs = null;
	     ArrayList list = new ArrayList();
	     try {
	         conn = JDBCDataSource.getConnection();

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
	             list.add(bean);
	         }
	     } catch (Exception e) {
	         log.error("Database Exception...", e);
	         throw new ApplicationException("Exception in the search " + e.getMessage());
	     } finally {
	         try {
	             if (rs != null) rs.close();
	             if (pstmt != null) pstmt.close();
	         } catch (Exception ex) {
	             log.error("Exception in closing resources", ex);
	         }
	         JDBCDataSource.closeConnection(conn);
	     }
	     log.debug("MOdel search End");
	     return list;
	 }
	 
	 
	                                                                             
	 
	 
	 
	 public List list() throws Exception {
		 return list(0, 0);
	 }
	  public List list(int pageNo, int pageSize) throws Exception {
	     log.debug("model list started");
	     List list = new ArrayList();
	     StringBuffer sql = new StringBuffer("select * from st_subject");
	     Connection conn = null;
	     PreparedStatement pstmt = null;
	     ResultSet rs = null;
	     try {
	         conn = JDBCDataSource.getConnection();
	         if (pageSize > 0) {
	             pageNo = (pageNo - 1) * pageSize;
	             sql.append(" limit " + pageNo + " ," + pageSize);
	         }
	         pstmt = conn.prepareStatement(sql.toString());
	         rs = pstmt.executeQuery();
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
	         log.error("Database Exception...", e);
	         throw new ApplicationException("Exception : Exception in getting list " + e.getMessage());
	     } finally {
	         try {
	             if (rs != null) rs.close();
	             if (pstmt != null) pstmt.close();
	         } catch (Exception ex) {
	             log.error("Exception in closing resources", ex);
	         }
	         JDBCDataSource.closeConnection(conn);
	     }
	     return list;
	 }
}