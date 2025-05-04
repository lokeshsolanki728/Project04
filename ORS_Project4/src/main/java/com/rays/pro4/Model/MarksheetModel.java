package com.rays.pro4.Model;

import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.MarksheetDTO;
import com.rays.pro4.Bean.MarksheetBean;
import com.rays.pro4.Bean.StudentBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.DTO.MarksheetDTO;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;
import java.util.List;
import java.sql.Connection;

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
    try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false);
            StudentModel sModel = new StudentModel();
            MarksheetBean bean = new MarksheetBean();
            MarksheetBean duplicateMarksheet = findByRollNo(bean.getRollNo());
            if (duplicateMarksheet != null) {
                throw new DuplicateRecordException("Roll Number already exists");
            }
            StudentBean studentbean = sModel.findByPK(dto.getStudentld());
            if (studentbean == null) {
                throw new ApplicationException("Exception: Student not found with ID " + bean.getStudentld());
            }
            String studentname = studentbean.getFirstName() + " " + studentbean.getLastName();
            bean.setName(studentname);

            pk = nextPK();

            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_MARKSHEET VALUES(?,?,?,?,?,?,?,?,?,?,?)")) {                
                pstmt.setLong(1, pk);                
                pstmt.setString(2, dto.getRollNo());                
                pstmt.setLong(3, dto.getStudentld());                
                pstmt.setString(4, studentname);                
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
            if (conn != null) {
                conn.rollback();
            }
            throw new ApplicationException("Exception: Exception in adding marksheet - " + e.getMessage());
        } finally { JDBCDataSource.closeConnection(conn); }
        log.debug("Model add End");
        return pk;
    }

    public void delete(long id) throws ApplicationException {
        log.debug("Model delete Started");
        MarksheetBean bean = findByPK(id);
         try (Connection conn = JDBCDataSource.getConnection()) {
            
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_MARKSHEET WHERE ID=?")) {
                pstmt.setLong(1, bean.getId());
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            log.error("Database Exception in delete Marksheet", e);
            if (conn != null) {
                conn.rollback();
            }
            throw new ApplicationException("Exception : Exception in deleting marksheet - " + e.getMessage());
        }finally{
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model delete End");
    }

    public MarksheetDTO findByRollNo(String rollNo) throws ApplicationException {
        log.debug("Model findByRollNo Started");
        MarksheetDTO dto = null;
        MarksheetBean bean = new MarksheetBean();
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ST_MARKSHEET WHERE ROLL_NO=?")) {
                pstmt.setString(1, rollNo);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        bean = new MarksheetBean();
                        populate(rs, bean);
                    }
                   dto=bean.getDTO();
                }
        } catch (SQLException e) {
            log.error("Database Exception in findByRollNo", e);
            throw new ApplicationException("Exception: Exception in getting marksheet by roll no - " + e.getMessage());
        }finally {JDBCDataSource.closeConnection(conn);}
        log.debug("Model findByRollNo End");        return dto;
    }

    public MarksheetDTO findByPK(Long pk) throws ApplicationException {
        log.debug("Model findByPK Started");
        MarksheetDTO dto = null;
        MarksheetBean bean =new MarksheetBean();
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ST_MARKSHEET WHERE ID=?")) {
                pstmt.setLong(1, pk);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                         populateBean(rs, dto);
                         dto = bean.getDTO();
                    }
                }
        } catch (SQLException e) {
            log.error("Database Exception in findByPK", e);
            throw new ApplicationException("Exception: Exception in getting marksheet by pk - " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model findByPK End");
        return dto;
    }

    public void update(MarksheetDTO dto) throws ApplicationException, DuplicateRecordException {
        log.debug("Model update Started");
        MarksheetBean bean = new MarksheetBean();
        // Check for duplicate roll number
        MarksheetBean beanExist = findByRollNo(dto.getRollNo());
        if (beanExist != null && beanExist.getId() != bean.getId()) {
            throw new DuplicateRecordException("Roll No is already exist");
        }       

        try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false);
            StudentModel sModel = new StudentModel();
            StudentBean studentbean = sModel.findByPK(dto.getStudentld());
            if (studentbean == null) {
                throw new ApplicationException("Exception: Student not found");
            }

            String studentName = studentbean.getFirstName() + " " + studentbean.getLastName();

            try (PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_MARKSHEET SET ROLL_NO=?, STUDENT_ID=?, NAME=?, PHYSICS=?, CHEMISTRY=?, MATHS=?, CREATED_BY=?, MODIFIED_BY=?, CREATED_DATETIME=?, MODIFIED_DATETIME=? WHERE ID=?")) {
                pstmt.setString(1, dto.getRollNo());
                pstmt.setLong(2, dto.getStudentld());
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
                bean.getDTO();
            }
        } catch (SQLException e) {
            log.error("Database Exception in update Marksheet", e);
            if (conn != null) {
                conn.rollback();
            }
            throw new ApplicationException("Exception: Exception in updating Marksheet - " + e.getMessage());
        }finally{
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model update End");
    }

    public List search(MarksheetDTO dto, int pageNo, int pageSize) throws ApplicationException {
        log.debug("Model search Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_MARKSHEET WHERE 1=1");
        int index = 1;
        Connection conn = null;
        ArrayList<MarksheetBean> list = new ArrayList<>();

        try {
            conn = JDBCDataSource.getConnection();
            if (bean != null) {
                if (bean.getId() > 0) {
                    sql.append(" AND ID = ?");
                }
                if (bean.getRollNo() != null && !bean.getRollNo().trim().isEmpty()) {
                    sql.append(" AND ROLL_NO like ?");
                }
                if (bean.getName() != null && !bean.getName().trim().isEmpty()) {
                    sql.append(" AND NAME like ?");
                }
                if (bean.getPhysics() > 0) {
                    sql.append(" AND PHYSICS = ?");
                }
                if (bean.getChemistry() > 0) {
                    sql.append(" AND CHEMISTRY = ?");
                }
                if (bean.getMaths() > 0) {
                    sql.append(" AND MATHS = ?");
                }
            }

            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                sql.append(" LIMIT " + pageNo + ", " + pageSize);
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

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

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MarksheetDTO resultBean = new MarksheetDTO();
                    populateBean(rs, resultBean);
                    list.add(resultBean);
                }
            }
        } }catch (SQLException e) {
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
        MarksheetBean bean=new MarksheetBean();        bean.setId(rs.getLong(1));
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
        dto=bean.getDTO();
    }
    
}
