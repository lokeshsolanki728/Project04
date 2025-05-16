package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.rays.pro4.DTO.FacultyDTO;

import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Exception.DatabaseException;

import com.rays.pro4.Util.JDBCDataSource;



/**
 *  * JDBC Implementation of FacultyModel.
 *
 *
 */
public class FacultyModel extends BaseModel {

    /**
     * Generate next PK of Faculty
     *
     * @return long
     * @throws DatabaseException
     */
    @Override
    public long nextPK() throws DatabaseException {
        log.debug("Model nextpk Started");
        long pk = 0;
        try (Connection conn = JDBCDataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM " + getTableName())) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                pk = rs.getLong(1);
            }
        } catch (SQLException e) {
            log.error("Database Exception in nextPK", e);
            throw new DatabaseException("Exception : Exception in getting pk - "
                    + e.getMessage());
        }
        log.debug("Model next pk End");
        return pk + 1;
    }

    /**
     * add method to add faculty in database
     *
     * @param bean
     * @return long
     * @throws ApplicationException
     * @throws DuplicateRecordException
     */
   public long add(FacultyDTO dto) throws ApplicationException, DuplicateRecordException {
        log.debug("Model add Started");
        long pk = 0;

        CollegeModel collegeModel = new CollegeModel();
        CourseModel courseModel = new CourseModel();
        SubjectModel subjectModel = new SubjectModel();
        
        CollegeDTO collegeDTO = collegeModel.findByPK(dto.getCollegeId());
        dto.setCollegeName(collegeDTO.getName());
        
        CourseDTO courseDTO = courseModel.findByPK(dto.getCourseId());
        dto.setCourseName(courseDTO.getName());
        
        SubjectDTO subjectDTO = subjectModel.findByPK(dto.getSubjectId());
        dto.setSubjectName(subjectDTO.getSubjectName());
        FacultyDTO beanExist = findByEmailId(dto.getEmailId());
        if (beanExist != null) {
            throw new DuplicateRecordException("Email already exists");
        }

       Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            pk = nextPK();
          try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO ST_FACULTY (ID,FIRST_NAME,LAST_NAME,GENDER,EMAIL_ID,MOBILE_NO,COLLEGE_ID,COLLEGE_NAME,COURSE_ID,COURSE_NAME,DOB,SUBJECT_ID,SUBJECT_NAME,CREATED_BY,MODIFIED_BY,CREATED_DATETIME,MODIFIED_DATETIME) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
                pstmt.setLong(1, pk);
                pstmt.setString(2, dto.getFirstName());
                pstmt.setString(3, dto.getLastName());
                pstmt.setString(4, dto.getGender());
                pstmt.setString(5, dto.getEmailId());
                pstmt.setString(6, dto.getMobileNo());
                pstmt.setLong(7, dto.getCollegeId());
                pstmt.setString(8, dto.getCollegeName());
                pstmt.setLong(9, dto.getCourseId());
                pstmt.setString(10, dto.getCourseName());
                pstmt.setDate(11, new java.sql.Date(dto.getDob().getTime()));
                pstmt.setLong(12, dto.getSubjectId());
                pstmt.setString(13, dto.getSubjectName());
                pstmt.setString(14, dto.getCreatedBy());
                pstmt.setString(15, dto.getModifiedBy());
                pstmt.setTimestamp(16, dto.getCreatedDatetime());
               pstmt.setTimestamp(17, dto.getModifiedDatetime());
              pstmt.executeUpdate();
              dto.setId(pk);

                conn.commit();
            }

        } catch (SQLException e) {
            log.error("Database Exception in add faculty", e);
                if (conn != null) {
                   conn.rollback();
                }

            throw new ApplicationException("Exception : Exception in add Faculty - " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model add End");
        return pk;
    }

    /**
     * delete method to delete faculty by id
     *
     * @param bean
     * @throws ApplicationException
     */
    public void delete(FacultyDTO dto) throws ApplicationException {
        log.debug("Model delete Started");
        try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_FACULTY WHERE ID=?")) {
              pstmt.setLong(1, dto.getId());
              pstmt.executeUpdate();

                conn.commit();
            }
        } catch (SQLException e) {
            log.error("Database Exception in delete faculty", e);
            JDBCDataSource.trnRollback();
            throw new ApplicationException("Exception : Exception in delete Faculty - " + e.getMessage());
        }
        log.debug("Model delete End");
    }

	/**
	 * update method to update the faculty
	 * @param bean
	 * @throws ApplicationException
	 */
    public void update(FacultyDTO dto) throws ApplicationException, DuplicateRecordException {
        log.debug("model update Started");
        FacultyDTO beanExist = findByEmailId(dto.getEmailId());
        if (beanExist != null && beanExist.getId() != dto.getId()) {
            throw new DuplicateRecordException("Email already exists");
        }   
        try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE ST_FACULTY SET FIRST_NAME=?,LAST_NAME=?,GENDER=?,EMAIL_ID=?,MOBILE_NO=?,COLLEGE_ID=?,COLLEGE_NAME=?,COURSE_ID=?,COURSE_NAME=?,DOB=?,SUBJECT_ID=?,SUBJECT_NAME=?, CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?")) {

              pstmt.setString(1, dto.getFirstName());
              pstmt.setString(2, dto.getLastName());
              pstmt.setString(3, dto.getGender());
              pstmt.setString(4, dto.getEmailId());
                pstmt.setString(5, dto.getMobileNo());
                pstmt.setLong(6, dto.getCollegeId());
                pstmt.setString(7, dto.getCollegeName());
                pstmt.setLong(8, dto.getCourseId());
                pstmt.setString(9, dto.getCourseName());
                pstmt.setDate(10, new java.sql.Date(dto.getDob().getTime()));
                pstmt.setLong(11, dto.getSubjectId());
                pstmt.setString(12, dto.getSubjectName());
                pstmt.setString(13, dto.getCreatedBy());
                pstmt.setString(14, dto.getModifiedBy());
                pstmt.setTimestamp(15, dto.getCreatedDatetime());
                pstmt.setTimestamp(16, dto.getModifiedDatetime());
                pstmt.setLong(17, dto.getId());

                pstmt.executeUpdate();
                
                conn.commit();
            }
        }catch (SQLException e) {
                log.error("Database Exception in update faculty", e);
                if (conn != null) {
                  conn.rollback();
                }

        }
        log.debug("Model update End");
    }

    /**
     * find by pk method to get the faculty by pk
     *
     * @param pk
     * @return FacultyDTO
     * @throws ApplicationException
     */
    public FacultyDTO findByPK(long pk) throws ApplicationException {
        log.debug("Model findByPK Started");
        String sql = "SELECT * FROM ST_FACULTY WHERE ID=?";
        FacultyDTO dto = null;
        try (Connection conn = JDBCDataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, pk);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    dto = new FacultyDTO();
                    populate(rs, dto);
                }
            }
        } catch (SQLException e) {
            log.error("Database Exception in findByPk faculty", e);
            throw new ApplicationException("Exception : Exception in getting Faculty by pk - " + e.getMessage());
        }
        log.debug("Model findByPK End");
        return dto;
    }

    /**
     * find by email id method to find the faculty by email id
     *
     * @param email
     * @return FacultyBean
     * @throws ApplicationException 
     */
    public FacultyDTO findByEmailId(String email) throws ApplicationException {
        log.debug("Model findBy Email Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE EMAIL_id=?");
        FacultyDTO dto = null;
        try (Connection conn = JDBCDataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    dto = new FacultyDTO();
                    populate(rs, dto);
                }
            }
        }catch (SQLException e) {
            log.error("Database Exception in findByEmail faculty", e);
            throw new ApplicationException(
                    "Exception : Exception in getting Faculty by Email - " + e.getMessage());
        }
        log.debug("Model findBy Email End");
        return dto;
    }
    private void handlePageSizeAndNumber(int pageNo, int pageSize) {
        if (pageNo < 0) {
            pageNo = 1;
        }
        if (pageSize < 0) {
            pageSize = 10; // Default page size
        }
    }

    /**
     * list method to get all faculty list
     *
     * @return List
     * @throws ApplicationException
     */
    public List list() throws ApplicationException {
        return list(1, 0);
    }

    /**
     * list method to get faculty list by pagination
     *
     * @param pageNo
     * @param pageSize
     * @return List
     * @throws ApplicationException
     */
    public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
        handlePageSizeAndNumber(pageNo,pageSize);
		ArrayList<FacultyDTO> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select * from ST_FACULTY");
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + "," + pageSize);
		}
		try (Connection conn = JDBCDataSource.getConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
               try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {                  
                    FacultyDTO dto = new FacultyDTO();
                    populate(rs, dto);
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            log.error("Database Exception in list faculty", e);
            throw new ApplicationException("Exception : Exception in getting list of faculty - " + e.getMessage());
        }
        log.debug("Model list End");
        return list;

    }

    /**
     * search method to search the faculty by values
     *
     * @param bean
     * @return List
     * @throws ApplicationException
     */   
    public List search(FacultyDTO bean) throws ApplicationException {
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
   public List search(FacultyDTO dto, int pageNo, int pageSize) throws ApplicationException {
        log.debug("Model search Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE 1=1");
		ArrayList<FacultyDTO> list=new ArrayList<>();
 handlePageSizeAndNumber(pageNo, pageSize);
 if (dto != null) {
 if (dto.getId() > 0) {
                sql.append(" AND id = ?");
            }
 if (dto.getCollegeId() > 0) {
                sql.append(" AND college_Id = ?");
            }
 if (dto.getFirstName() != null && !dto.getFirstName().isEmpty()) {
                sql.append(" AND FIRST_NAME like ?");
            }
 if (dto.getLastName() != null && !dto.getLastName().isEmpty()) {
                sql.append(" AND LAST_NAME like ?");
            }
 if (dto.getEmailId() != null && !dto.getEmailId().isEmpty()) {
                sql.append(" AND Email_Id like ?");
           }
 if (dto.getGender() != null && !dto.getGender().isEmpty()) {
                sql.append(" AND Gender like ?");
            }
 if (dto.getMobileNo() != null && !dto.getMobileNo().isEmpty()) {
               sql.append(" AND Mobile_No like ?");
           }
 if (dto.getCollegeName() != null && !dto.getCollegeName().isEmpty()) {
               sql.append(" AND college_Name like ?");
           }
 if (dto.getCourseId() > 0) {
               sql.append(" AND course_Id = ?");
           }
            if (bean.getCourseName() != null && !bean.getCourseName().isEmpty()) {
               sql.append(" AND course_Name like ?");
           }
            if (bean.getSubjectId() > 0) {
                sql.append(" AND Subject_Id = ?");
            }
            if (bean.getSubjectName() != null && !bean.getSubjectName().isEmpty()) {
                sql.append(" AND subject_Name like ?");
           }
		}
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
        }
      try(Connection conn=JDBCDataSource.getConnection(); PreparedStatement pstmt=conn.prepareStatement(sql.toString())){
        	int paramIndex = 1;
         if (dto != null) {
             if (dto.getId() > 0) {
                 pstmt.setLong(paramIndex++, dto.getId());
            }
            if (dto.getCollegeId() > 0) {
                pstmt.setLong(paramIndex++, dto.getCollegeId());
            }
            if (dto.getFirstName() != null && !dto.getFirstName().isEmpty()) {
               pstmt.setString(paramIndex++, dto.getFirstName() + "%");
            }
            if (dto.getLastName() != null && !dto.getLastName().isEmpty()) {
               pstmt.setString(paramIndex++, dto.getLastName() + "%");
            }
           if (dto.getEmailId() != null && !dto.getEmailId().isEmpty()) {
                pstmt.setString(paramIndex++, dto.getEmailId() + "%");
           }
            if (dto.getGender() != null && !dto.getGender().isEmpty()) {
               pstmt.setString(paramIndex++, dto.getGender() + "%");
            }
           if (dto.getMobileNo() != null && !dto.getMobileNo().isEmpty()) {
               pstmt.setString(paramIndex++, dto.getMobileNo() + "%");
           }
           if (dto.getCollegeName() != null && !dto.getCollegeName().isEmpty()) {
               pstmt.setString(paramIndex++, dto.getCollegeName() + "%");
           }
            if (dto.getCourseId() > 0) {
                pstmt.setLong(paramIndex++, dto.getCourseId());
            }
            if (dto.getCourseName() != null && !dto.getCourseName().isEmpty()) {
               pstmt.setString(paramIndex++, dto.getCourseName() + "%");
           }
            if (dto.getSubjectId() > 0) {
                pstmt.setLong(paramIndex++, dto.getSubjectId());
            }
           if (dto.getSubjectName() != null && !dto.getSubjectName().isEmpty()) {
                pstmt.setString(paramIndex++, dto.getSubjectName() + "%");
           }
       }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                  FacultyDTO resultDto = new FacultyDTO();
                   populate(rs, resultDto);
                    list.add(resultDto);
                }
             }
		}catch(SQLException e) {
			log.error("Database Exception in search faculty", e);
			throw new ApplicationException("Exception in the search - "+e.getMessage());
        }
        log.debug("Model search End");
        return list;
    }


   private void populate(ResultSet rs, FacultyDTO dto) throws SQLException {

       dto.setId(rs.getLong(1));
       dto.setFirstName(rs.getString(2));
       dto.setLastName(rs.getString(3));
       dto.setGender(rs.getString(4));
       dto.setEmailId(rs.getString(5));
       dto.setMobileNo(rs.getString(6));
       dto.setCollegeId(rs.getLong(7));
       dto.setCollegeName(rs.getString(8));
       dto.setCourseId(rs.getLong(9));
       dto.setCourseName(rs.getString(10));
       dto.setDob(rs.getDate(11));
       dto.setSubjectId(rs.getLong(12));
       dto.setSubjectName(rs.getString(13));
       dto.setCreatedDatetime(rs.getTimestamp(16));
       dto.setModifiedDatetime(rs.getTimestamp(17));
       dto.setCreatedBy(rs.getString(14));
       dto.setModifiedBy(rs.getString(15));
   }


    @Override
    public String getTableName() {
        return "ST_FACULTY";
    }
}
