package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.rays.pro4.Bean.MarksheetBean;
import com.rays.pro4.Bean.StudentBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.DTO.MarksheetDTO;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

public class MarksheetModel extends BaseModel {

    @Override
    public String getTableName() {
        return "ST_MARKSHEET";
    }

    @Override
    public long nextPK() throws DatabaseException {
        log.debug("Model nextPK Started");
        long pk = 0;
        try (Connection conn = JDBCDataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM " + getTableName())) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) { pk = rs.getLong(1);
            }
        } catch (SQLException e) {
            log.error("Database Exception in nextPK", e);
            throw new DatabaseException("Exception: Exception in getting pk - " + e.getMessage());
        }
        log.debug("Model nextPK End");
        return pk + 1;
    }

    public long add(MarksheetDTO dto) throws ApplicationException, DuplicateRecordException {
        log.debug("Model add Started");
        long pk = 0;
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false); // Start transaction
            StudentModel sModel = new StudentModel();
            MarksheetDTO duplicateMarksheet = findByRollNo(dto.getRollNo());
            if (duplicateMarksheet != null) {
                throw new DuplicateRecordException("Roll Number already exists");
            }
            
            StudentDTO studentDTO = sModel.findByPK(dto.getStudentId()); //Corrected typo getStudentId
            if (studentDTO == null) {
                throw new ApplicationException("Exception: Student not found with ID " + dto.getStudentId());
            }
            String studentName = studentDTO.getFirstName() + " " + studentDTO.getLastName();
            pk = nextPK();
            dto.setId(pk);            
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_MARKSHEET VALUES(?,?,?,?,?,?,?,?,?,?,?)")){
                pstmt.setLong(1, pk);
                pstmt.setString(2, dto.getRollNo());
                pstmt.setLong(3, dto.getStudentId());
                pstmt.setString(4, studentName);
                pstmt.setInt(5, dto.getPhysics());
                pstmt.setInt(6, dto.getChemistry());
                pstmt.setInt(7, dto.getMaths());
                pstmt.setString(8, dto.getCreatedBy());
                pstmt.setString(9, dto.getModifiedBy());
                pstmt.setTimestamp(10, dto.getCreatedDatetime());
                pstmt.setTimestamp(11, dto.getModifiedDatetime());
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            log.error("Database Exception in add Marksheet", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception: Exception in adding marksheet - " + e.getMessage());
        } catch (DuplicateRecordException e) {
            log.error("DuplicateRecordException in add Marksheet", e);
            JDBCDataSource.trnRollback(conn);
            throw e;
        } catch (Exception e) {
            log.error("Exception in add Marksheet", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception : Exception in add Marksheet");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model add End");        return pk;
    }

   public void delete(long id) throws ApplicationException {
        Connection conn=null;
        log.debug("Model delete Started");
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_MARKSHEET WHERE ID=?")) {
             conn.setAutoCommit(false);
                pstmt.setLong(1, id);
                pstmt.executeUpdate();
                conn.commit();
        } catch (SQLException e) {
           
            log.error("Database Exception in delete Marksheet", e);
            JDBCDataSource.trnRollback();
            throw new ApplicationException("Exception : Exception in delete Marksheet");
        }
        
        log.debug("Model delete End");
    }

   public void delete(MarksheetBean bean) throws ApplicationException {
        log.debug("Model delete Started");
        Connection conn = null; // Initialize conn here
        try {   
             conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_MARKSHEET WHERE ID=?")) {
                pstmt.setLong(1, bean.getId());
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
             JDBCDataSource.trnRollback(conn);
            log.error("Database Exception in delete Marksheet", e);
            throw new ApplicationException("Exception : Exception in delete Marksheet");
        } catch (Exception e) {
            throw new ApplicationException("Exception : Exception in delete Marksheet");
        }
        log.debug("Model delete End");
    }

    public MarksheetDTO findByRollNo(String rollNo) throws ApplicationException {
        log.debug("Model findByRollNo Started");
        MarksheetDTO dto = null;
        Connection conn = null;
        try {
             conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ST_MARKSHEET WHERE ROLL_NO=?");
            
            pstmt.setString(1, rollNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    dto = new MarksheetDTO();
                    populateBean(rs, dto);
                }
            }           

        
        } catch (SQLException e) {
            log.error("Database Exception in findByRollNo", e);
            throw new ApplicationException("Exception: Exception in getting marksheet by roll no - " + e.getMessage());
        }
        log.debug("Model findByRollNo End");
        return dto;
    }

    public MarksheetDTO findByPK(long pk) throws ApplicationException {
        log.debug("Model findByPK Started");
        MarksheetDTO dto = null;       
        Connection conn = null;
        try {
             conn = JDBCDataSource.getConnection();
              PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ST_MARKSHEET WHERE ID=?");
           pstmt.setLong(1, pk);
           
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    dto = new MarksheetDTO();
                    populateBean(rs, dto);
                }
            }
        } catch (SQLException e) {
            log.error("Database Exception in findByPK", e);
            throw new ApplicationException("Exception: Exception in getting marksheet by pk - " + e.getMessage());
        }
       finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model findByPK End");
        return dto;
    }

    public void update(MarksheetDTO dto) throws ApplicationException, DuplicateRecordException {
        log.debug("Model update Started");
        MarksheetDTO duplicateMarksheet = findByRollNo(dto.getRollNo());
        if (duplicateMarksheet != null && duplicateMarksheet.getId() != dto.getId()) {
            throw new DuplicateRecordException("Roll No is already exist");
        }
         Connection conn=null;
        try {
            conn= JDBCDataSource.getConnection();
             conn.setAutoCommit(false);
              PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_MARKSHEET SET ROLL_NO=?, STUDENT_ID=?, NAME=?, PHYSICS=?, CHEMISTRY=?, MATHS=?, CREATED_BY=?, MODIFIED_BY=?, CREATED_DATETIME=?, MODIFIED_DATETIME=? WHERE ID=?");
            StudentModel sModel = new StudentModel();
            StudentDTO studentDTO = sModel.findByPK(dto.getStudentId());
            if (studentDTO == null) { //Corrected typo getStudentId
                throw new ApplicationException("Exception: Student not found");
            }
            String studentName = studentDTO.getFirstName() + " " + studentDTO.getLastName();
                pstmt.setString(1, dto.getRollNo());
                pstmt.setLong(2, dto.getStudentId());
                pstmt.setString(3, studentName);
                pstmt.setInt(4, dto.getPhysics());
                pstmt.setInt(5, dto.getChemistry());
                pstmt.setInt(6, dto.getMaths());
                pstmt.setString(7, dto.getCreatedBy());
                pstmt.setString(8, dto.getModifiedBy());
                pstmt.setTimestamp(9, dto.getCreatedDatetime());
                pstmt.setTimestamp(10, dto.getModifiedDatetime());
                pstmt.setLong(11, dto.getId());
                pstmt.executeUpdate();
                conn.commit();
        } catch (SQLException e) {
            log.error("Database Exception in update Marksheet", e);
            JDBCDataSource.trnRollback();
            throw new ApplicationException("Exception: Exception in updating Marksheet - " + e.getMessage());
        }
        finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model update End");
    }

    public List search(MarksheetDTO dto, int pageNo, int pageSize) throws ApplicationException {
        log.debug("Model search Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_MARKSHEET WHERE 1=1");
        ArrayList<MarksheetDTO> list = new ArrayList<>();
         Connection conn = null;
        int paramCount = 1;
        if (dto != null) {
            if (dto.getId() > 0) {
                sql.append(" AND ID = ?");
            }
            if (dto.getRollNo() != null && !dto.getRollNo().trim().isEmpty()) {
                sql.append(" AND ROLL_NO like ?");
            }
            if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
                sql.append(" AND NAME like ?");
            }
            if (dto.getPhysics() > 0) {
                sql.append(" AND PHYSICS = ?");
            }
            if (dto.getChemistry() > 0) {
                sql.append(" AND CHEMISTRY = ?");
            }
            if (dto.getMaths() > 0) {
                sql.append(" AND MATHS = ?");
            }
        }
        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT ?, ?");
        }
        try  {
             conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            if (dto != null) {
                if (dto.getId() > 0) {
                    pstmt.setLong(paramCount++, dto.getId());
                }
                if (dto.getRollNo() != null && !dto.getRollNo().isEmpty()) {
                    pstmt.setString(paramCount++, dto.getRollNo() + "%");
                }
                if (dto.getName() != null && !dto.getName().isEmpty()) {
                    pstmt.setString(paramCount++, dto.getName() + "%");
                }
                if (dto.getPhysics() > 0) {
                    pstmt.setInt(paramCount++, dto.getPhysics());
                }
                if (dto.getChemistry() > 0) {
                    pstmt.setInt(paramCount++, dto.getChemistry());
                }
                if (dto.getMaths() > 0) {
                    pstmt.setInt(paramCount++, dto.getMaths());
                }
            }
            if (pageSize > 0) {
                pstmt.setInt(paramCount++, pageNo);
                pstmt.setInt(paramCount++, pageSize);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MarksheetDTO resultDTO = new MarksheetDTO();
                    populateBean(rs, resultDTO);
                    list.add(resultDTO);
                }
            }           
        } catch (SQLException e) {
            log.error("Database Exception in search Marksheet", e);
            throw new ApplicationException("Exception: Exception in searching marksheet - " + e.getMessage());
        }
        finally{
                JDBCDataSource.closeConnection(conn);

        } 
        log.debug("Model search End");
        return list;
    }
    private void populateBean(ResultSet rs, MarksheetDTO dto) throws SQLException {
        dto.setId(rs.getLong(1));
        dto.setRollNo(rs.getString(2));
        dto.setStudentId(rs.getLong(3));
        dto.setName(rs.getString(4));
        dto.setPhysics(rs.getInt(5));
        dto.setChemistry(rs.getInt(6));
        dto.setMaths(rs.getInt(7));
        dto.setCreatedBy(rs.getString(8));
        dto.setModifiedBy(rs.getString(9));
        dto.setCreatedDatetime(rs.getTimestamp(10));
        dto.setModifiedDatetime(rs.getTimestamp(11));
    }
}
