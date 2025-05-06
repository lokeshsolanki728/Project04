package com.rays.pro4.Model;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import org.apache.log4j.Logger;
import com.rays.pro4.DTO.SubjectDTO;

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
	
	
	private long nextPK() throws DatabaseException {
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
        return pk+1;
    }

	public SubjectDTO findByName(String name) throws ApplicationException {
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT WHERE SUBJECT_NAME=?");
        SubjectDTO dto = null;
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery()) {


            if (rs.next()) {
                dto = new SubjectDTO();
                populateBean(rs, dto);
            }
        } catch (Exception e) {
            throw new ApplicationException("Exception in getting subject by name " + e.getMessage());
        }
        return dto;
    }
     
    public void delete(long id) throws ApplicationException {
        log.debug("Model delete Started");
        Connection conn = null;
        try {
             conn = JDBCDataSource.getConnection();
                conn.setAutoCommit(false);
                try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_SUBJECT WHERE ID=?")) {
                    pstmt.setLong(1, id);
                    pstmt.executeUpdate();
                }
               conn.commit();
        }catch (Exception e) {
                JDBCDataSource.trnRollback(conn);
                throw new ApplicationException("Exception: Exception in delete Subject - " + e.getMessage());
        }finally {
                JDBCDataSource.closeConnection(conn);
            }
     }
    

	public void update(SubjectDTO dto) throws ApplicationException,
			DuplicateRecordException {
		log.debug("Model update Started");
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			try (PreparedStatement pstmt = conn
					.prepareStatement("UPDATE ST_SUBJECT SET SUBJECT_NAME=?,DESCRIPTION=?,COURSE_ID=?,COURSE_NAME=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?")) {
				pstmt.setString(1, dto.getSubjectName());
				pstmt.setString(2, dto.getDescription());
				pstmt.setLong(3, dto.getCourseId());
				pstmt.setString(4, dto.getCourseName());
				pstmt.setString(5, dto.getCreatedBy());
				pstmt.setString(6, dto.getModifiedBy());
				pstmt.setTimestamp(7, dto.getCreatedDatetime());
				pstmt.setTimestamp(8, dto.getModifiedDatetime());
				pstmt.setLong(9, dto.getId());
				pstmt.executeUpdate();
				conn.commit();
			}
		} catch (Exception e) {
			JDBCDataSource.trnRollback(conn);
			throw new ApplicationException(
					"Exception :Exception in update subject " + e.getMessage());
		}finally{
            if(conn!=null) {
                JDBCDataSource.closeConnection(conn);
            }
        }
    }
	
  
    public long add(SubjectDTO dto) throws ApplicationException, DuplicateRecordException {
        log.debug("Model add Started");
        Connection conn = null;
        long pk = 0;
        try {
            conn = JDBCDataSource.getConnection();
             conn.setAutoCommit(false);
             pk = nextPK();
            dto.setId(pk);
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_SUBJECT VALUES(?,?,?,?,?,?,?,?,?)")){
                 pstmt.setLong(1, dto.getId());
                 pstmt.setString(2, dto.getSubjectName());
                 pstmt.setString(3, dto.getDescription());
                 pstmt.setLong(4, dto.getCourseId());
                 pstmt.setString(5, dto.getCourseName());
                 pstmt.setString(6, dto.getCreatedBy());
                 pstmt.setString(7, dto.getModifiedBy());
                 pstmt.setTimestamp(8, dto.getCreatedDatetime());
                 pstmt.setTimestamp(9, dto.getModifiedDatetime());
                pstmt.executeUpdate();
                }
            conn.commit();
        } catch (Exception e) {
            log.error("Database Exception while adding Subject", e);
            JDBCDataSource.trnRollback();
           throw new ApplicationException("Exception: Exception in add Subject - " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }
	   
	    
	 public SubjectDTO findByPK(long pk) throws ApplicationException {
	     
	     String sql = "SELECT * FROM ST_SUBJECT WHERE ID=?";
	     SubjectDTO dto = null;
	     try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString());
                ResultSet rs = pstmt.executeQuery();) {
	         
                pstmt.setLong(1, pk);
	         

            if(rs.next()) {
                dto = new SubjectDTO();
                populateBean(rs, dto);
            }
		 }catch(Exception e) {
			 throw new ApplicationException("Exception in getting subject by pk " + e.getMessage());
		 }
		 return dto;
	 }
	 

	 public List search( SubjectBean bean) throws ApplicationException {
		 return search(bean,0,0);
	 }
	 public List search(SubjectBean bean, int pageNo, int pageSize) throws ApplicationException{	
	    StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT WHERE true");
	     ArrayList<SubjectDTO> list = new ArrayList<SubjectDTO>();
	     
	     if (pageNo < 0) {
	            pageNo = 1;
	        }
	        if (pageSize < 0) {
	            pageSize = 10; // Default page size
	        }
	     
	     try (Connection conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString()); ResultSet rs = pstmt.executeQuery()) {

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
	         
	           int index = 1;

	         if(bean.getId() > 0){
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
                 SubjectDTO dto = new SubjectDTO();
                 populateBean(rs, dto);
                 list.add(dto); }


	     } finally {
	        
	         log.debug("Model search End");
	     }
		return list;
	 }

	  public List list() throws ApplicationException {
			return list(0, 0);
		} public List list(int pageNo, int pageSize) throws ApplicationException {
	     log.debug("Model list Started");
	    StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT");
	    
	    if (pageNo < 0) {
	            pageNo = 1;
	        }
	        if (pageSize < 0) {
	            pageSize = 10; // Default page size
	        }

	     ArrayList<SubjectDTO> list = new ArrayList<SubjectDTO>();
	     if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                sql.append(" LIMIT " + pageNo + ", " + pageSize);
            }
	     try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString());ResultSet rs = pstmt.executeQuery();){
	        
	         while (rs.next()) {
	            SubjectDTO dto = new SubjectDTO(); populateBean(rs, dto); list.add(dto);

	        }
	     } catch (Exception e) {
	         throw new ApplicationException("Exception : Exception in list Subject - " + e.getMessage());
	     }
	     return list;
	 }
	 public String getTableName() {
        return "ST_SUBJECT";
    }

    private void populateBean(ResultSet rs, SubjectDTO dto) throws SQLException {
        dto.setId(rs.getLong(1));
        dto.setSubjectName(rs.getString(2));
        dto.setDescription(rs.getString(3));
        dto.setCourseId(rs.getLong(4));
        dto.setCourseName(rs.getString(5));
        dto.setCreatedBy(rs.getString(6));
        dto.setModifiedBy(rs.getString(7));
        dto.setCreatedDatetime(rs.getTimestamp(8));
        dto.setModifiedDatetime(rs.getTimestamp(9));
    }

    public void delete(SubjectBean bean) throws ApplicationException {
        delete(bean.getId());    
    }
   }