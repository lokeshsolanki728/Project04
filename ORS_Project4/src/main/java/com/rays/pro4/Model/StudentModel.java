package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;import java.util.List;
import org.apache.log4j.Logger;

import com.rays.pro4.DTO.StudentDTO;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.DTO.CollegeDTO;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;
import java.sql.SQLException;

/**
 * JDBC Implementation of Student Model.
 * 
 * @author Lokesh Solanki
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


	public long add(StudentDTO dto) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");
		long pk = 0;
		// Fetch college details
		CollegeModel cModel = new CollegeModel();
		CollegeDTO collegeDTO = cModel.findByPK(dto.getCollegeId());
		dto.setCollegeName(collegeDTO.getName());
		StudentDTO duplicate = findByEmailId(dto.getEmail());
		if (duplicate != null) {
			throw new DuplicateRecordException("Email already exists"+duplicate.getEmail());
		}
        try (Connection conn = JDBCDataSource.getConnection();) {
            conn.setAutoCommit(false);// Begin transaction
            pk = nextPK();
            dto.setId(pk);


            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_STUDENT VALUES(?,?,?,?,?,?,?,?,?,?,?,?)")) {
                pstmt.setLong(1, pk);
                pstmt.setLong(2, dto.getCollegeId());
                pstmt.setString(3, dto.getCollegeName());
                pstmt.setString(4, dto.getFirstName());
                pstmt.setString(5, dto.getLastName());
                pstmt.setDate(6, new java.sql.Date(dto.getDob().getTime()));
                pstmt.setString(7, dto.getMobileNo());
                pstmt.setString(8, dto.getEmail());
                pstmt.setString(9, dto.getCreatedBy());
                pstmt.setString(10, dto.getModifiedBy());
                pstmt.setTimestamp(11, dto.getCreatedDatetime());
                pstmt.setTimestamp(12, dto.getModifiedDatetime());
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            log.error("Database Exception in Add..", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception : Exception in add Student " + e.getMessage());        
        }
		log.debug("Model add End");
		return pk;
	}	
	public void delete(StudentDTO dto) throws ApplicationException {
		log.debug("Model delete Started");
		try (Connection conn = JDBCDataSource.getConnection();) {
			conn.setAutoCommit(false);
			if (findByPK(dto.getId()) == null) {
				throw new ApplicationException("Bean not found");
			}
			try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_STUDENT WHERE ID=?")) {
				pstmt.setLong(1, dto.getId());
				pstmt.executeUpdate();
				conn.commit();
			}
		} catch (SQLException e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in delete Student");
        }
		
		log.debug("Model delete End");
	}
		public StudentDTO findByEmailId(String Email) throws ApplicationException {
		log.debug("Model findBy Email Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_STUDENT WHERE EMAIL_id=?");
		StudentDTO dto = null;
        try (Connection conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setString(1, Email);
            try (ResultSet rs = pstmt.executeQuery();)
            {
                while (rs.next()) {
                     dto = new StudentDTO();
                    populateDTO(rs, dto);


                }
            }
        } catch (SQLException e) {
            log.error("Database Exception in findByEmailId", e);
            throw new ApplicationException("Exception: Exception in getting student by email - " + e.getMessage());
        }
        log.debug("Model findBy Email End");
        return dto;
	}
		
	public StudentDTO findByPK(long pk) throws ApplicationException {
		log.debug("Model findByPK Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_STUDENT WHERE ID=?");
		StudentDTO dto = null;
		try (Connection conn = JDBCDataSource.getConnection(); 
        PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setLong(1, pk);// Set the primary key parameter
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    dto = new StudentDTO();
                    populateDTO(rs, dto);
                }
            }
		} catch (SQLException e) {
			log.error("Database Exception in findByPK", e);
			throw new ApplicationException("Exception: Exception in getting student by pk - " + e.getMessage());
		}

		log.debug("Model findByPK End");
		return dto;
	}

	public void update(StudentDTO dto) throws ApplicationException, DuplicateRecordException {
		log.debug("Model update Started");
		StudentDTO beanExist = findByEmailId(dto.getEmail());
		if (beanExist != null && beanExist.getId() != dto.getId()) {
			throw new DuplicateRecordException("Email already exists");
		}
		CollegeModel cModel = new CollegeModel();
        CollegeDTO collegeDTO = cModel.findByPK(dto.getCollegeId());
		dto.setCollegeName(collegeDTO.getName());
		try ( Connection conn = JDBCDataSource.getConnection();) {
            conn.setAutoCommit(false);// Begin transaction
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE ST_STUDENT SET COLLEGE_ID=?,COLLEGE_NAME=?,FIRST_NAME=?,LAST_NAME=?,DATE_OF_BIRTH=?,MOBILE_NO=?,EMAIL_ID=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?")) {

                pstmt.setLong(1, dto.getCollegeId());
                pstmt.setString(2, dto.getCollegeName());
                pstmt.setString(3, dto.getFirstName());
                pstmt.setString(4, dto.getLastName());
                pstmt.setDate(5, new java.sql.Date(dto.getDob().getTime()));
                pstmt.setString(6, dto.getMobileNo());
                pstmt.setString(7, dto.getEmail());
                 pstmt.setString(8, dto.getCreatedBy());
                pstmt.setString(9, dto.getModifiedBy());
                pstmt.setTimestamp(10, dto.getCreatedDatetime());
                pstmt.setTimestamp(11, dto.getModifiedDatetime());
                pstmt.setLong(12, dto.getId());
                pstmt.executeUpdate();
                conn.commit();// End transaction
            }
		} catch (SQLException e) {
			log.error("Database Exception in update", e);
            JDBCDataSource.trnRollback(conn);
			throw new ApplicationException("Exception : Delete rollback exception" + e.getMessage());
		} 
        log.debug("Model update End");
    }

	public List<StudentDTO> search(StudentDTO dto,String orderBy,String sortOrder) throws ApplicationException {
		return search(dto, 1, 0,orderBy,sortOrder);
	}
	
	/**
	 * Search for students based on the provided criteria with pagination.
	 *
	 * @param bean     The StudentBean object containing search criteria.
	 * @param pageNo   The page number for pagination.
	 * @param pageSize The page size for pagination.
	 * @return A list of StudentBean objects matching the search criteria.
	 * @throws ApplicationException If a database error occurs.
	 * @throws DatabaseException 
	 */
    public List<StudentDTO> search(StudentDTO dto, int pageNo, int pageSize, String orderBy, String sortOrder) throws ApplicationException {
		log.debug("Model search Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_STUDENT WHERE 1=1");
        ArrayList<StudentDTO> list = new ArrayList<>();
        int index = 1;
        CollegeModel collegeModel = new CollegeModel();
        if (pageNo < 0) {pageNo = 1;}
        if (pageSize < 0) {
            pageSize = 10;
        }
        if (dto != null) {
            if (dto.getId() > 0) {sql.append(" AND id = ? ");}
            if (dto.getFirstName() != null && !dto.getFirstName().isEmpty()) {sql.append(" AND FIRST_NAME like ?");}
            if (dto.getLastName() != null && !dto.getLastName().isEmpty()) {sql.append(" AND LAST_NAME like ?");}
            if (dto.getMobileNo() != null && !dto.getMobileNo().isEmpty()) {sql.append(" AND MOBILE_NO like ?");}
            if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {sql.append(" AND EMAIL_ID like ?");}
            if (dto.getCollegeId() > 0) {
                sql.append(" AND COLLEGE_ID=?");
            }
        }

		// Add sorting logic
        if (orderBy != null && !orderBy.isEmpty() && isValidOrderByColumn(orderBy)) {
            sql.append(" ORDER BY " + orderBy);
            if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
                sql.append(" DESC");
            } else {
                sql.append(" ASC");
            }
        }
		if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT " + pageNo + ", " + pageSize);
        }

		try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
			index=1;
			if (dto != null) {
                if (dto.getId() > 0) {pstmt.setLong(index++, dto.getId());}
                if (dto.getFirstName() != null && !dto.getFirstName().isEmpty()) {pstmt.setString(index++, dto.getFirstName() + "%");}
                if (dto.getLastName() != null && !dto.getLastName().isEmpty()) {pstmt.setString(index++, dto.getLastName() + "%");}
                if (dto.getMobileNo() != null && !dto.getMobileNo().isEmpty()) {pstmt.setString(index++, dto.getMobileNo() + "%");}
                if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {pstmt.setString(index++, dto.getEmail() + "%");}
                if (dto.getCollegeId() > 0) {pstmt.setLong(index++, dto.getCollegeId());}
           

            }
            try(ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    dto = new StudentDTO();
                    populateDTO(rs, dto);                    
                    list.add(dto);
                }
            // Iterate through the list to fetch and set college names
            for (StudentDTO studentDTO : list) {
                // Fetch the corresponding CollegeBean using the collegeId
                CollegeDTO college = collegeModel.findByPK(studentDTO.getCollegeId());
                if (college != null) {
                    studentDTO.setCollegeName(college.getName());
                }
            }
            }   
        }catch (SQLException e){
			log.error("Database Exception in search", e);
			throw new ApplicationException("Exception: Exception in searching student - " + e.getMessage());
		}
		log.debug("Model search End");
		return list;
	}
    public List<StudentDTO> list(String orderBy,String sortOrder) throws ApplicationException {
		return list(1, 0, null, null);
	}
	
	public List<StudentDTO> list(int pageNo, int pageSize,String orderBy,String sortOrder) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList<StudentDTO> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select * from ST_STUDENT");
		if (pageNo < 1) {
            pageNo = 0;
        }
        if (pageSize < 0) {
            pageSize = 10;
        }
		if (pageSize > 0 ) {
           pageNo = (pageNo - 1) * pageSize;        
            sql.append(" LIMIT " + pageNo + ", " + pageSize);
        }
        if (orderBy != null && !orderBy.isEmpty()&& isValidOrderByColumn(orderBy)) {
          sql.append(" ORDER BY " + orderBy);
          if (sortOrder != null && !sortOrder.isEmpty()) {
               if (sortOrder.equalsIgnoreCase("DESC")) {
                  sql.append(" DESC");
               } else if(sortOrder.equalsIgnoreCase("ASC"))
                {sql.append(" ASC");}
              }
                else
                {sql.append(" ASC");}

        }
        try (Connection conn = JDBCDataSource.getConnection(); 
            PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    StudentDTO dto = new StudentDTO();                 
                    populateDTO(rs, dto);
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            log.error("Database Exception in list", e);
            throw new ApplicationException("Exception: Exception in getting list of student - " + e.getMessage());
        } 
        log.debug("Model list End");
        return list;
    }
    private  void populateDTO(ResultSet rs, StudentDTO dto) throws SQLException {
        dto.setId(rs.getLong(1));
        dto.setCollegeId(rs.getLong(2));
        dto.setCollegeName(rs.getString(3));
        dto.setFirstName(rs.getString(4));
        dto.setLastName(rs.getString(5));
        dto.setDob(rs.getDate(6));
        dto.setMobileNo(rs.getString(7));
        dto.setEmail(rs.getString(8));
        dto.setCreatedBy(rs.getString(9));
        dto.setModifiedBy(rs.getString(10));
        dto.setCreatedDatetime(rs.getTimestamp(11));
        dto.setModifiedDatetime(rs.getTimestamp(12));
        
        
    }

    
    /**
     *  Check the given column name is a valid column for sorting in the ST_STUDENT table.
     *
     * @param columnName The column name to check.
     * @return True if the column name is valid, false otherwise.
     */
    private boolean isValidOrderByColumn(String columnName) {
        final String[] validColumns = {
                "ID", "COLLEGE_ID", "COLLEGE_NAME", "FIRST_NAME",
                "LAST_NAME", "DATE_OF_BIRTH", "MOBILE_NO", "EMAIL_ID",
                "CREATED_BY", "MODIFIED_BY", "CREATED_DATETIME", "MODIFIED_DATETIME"
        };
        for (String validColumn : validColumns) {
            if (validColumn.equalsIgnoreCase(columnName)) {
                return true;
            }
        }
        return false;
    }
}
