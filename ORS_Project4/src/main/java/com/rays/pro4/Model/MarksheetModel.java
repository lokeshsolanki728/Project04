package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.MarksheetBean;
import com.rays.pro4.Bean.StudentBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;
import java.util.*;
import java.util.List;

public class MarksheetModel extends BaseModel {

    private static Logger log = Logger.getLogger(MarksheetModel.class);

    public MarksheetModel() {
        super();
    }

    public String getTableName() {
        return "ST_MARKSHEET";
    }

    private synchronized long nextPK() throws DatabaseException {
        log.debug("Model nextPK Started");
        long pk = 0;
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_MARKSHEET")) {
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

    public long add(MarksheetBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("Model add Started");
        long pk = 0;
        try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false);
            StudentModel sModel = new StudentModel();
            MarksheetBean duplicateMarksheet = findByRollNo(bean.getRollNo());
            if (duplicateMarksheet != null) {
                throw new DuplicateRecordException("Roll Number already exists");
            }
            StudentBean studentbean = sModel.findByPK(bean.getStudentld());
            if (studentbean == null) {
                throw new ApplicationException("Student not found with ID: " + bean.getStudentld());
            }
            String studentname = studentbean.getFirstName() + " " + studentbean.getLastName();
            bean.setName(studentname);
            pk = nextPK();
            
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_MARKSHEET VALUES(?,?,?,?,?,?,?,?,?,?,?)")) {
                pstmt.setLong(1, pk);
                pstmt.setString(2, bean.getRollNo());
                pstmt.setLong(3, bean.getStudentld());
                pstmt.setString(4, bean.getName());
                pstmt.setInt(5, bean.getPhysics());
                pstmt.setInt(6, bean.getChemistry());
                pstmt.setInt(7, bean.getMaths());
                pstmt.setString(8, bean.getCreatedBy());
                pstmt.setString(9, bean.getModifiedBy());
                pstmt.setTimestamp(10, bean.getCreatedDatetime());
                pstmt.setTimestamp(11, bean.getModifiedDatetime());
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            log.error("Database Exception in add", e);
            throw new ApplicationException("Exception: Exception in adding marksheet - " + e.getMessage());
        }
        log.debug("Model add End");
        return pk;
    }

    public void delete(long id) throws ApplicationException {
        log.debug("Model delete Started");
        delete(findByPK(id));
    }

    public void delete(MarksheetBean bean) throws ApplicationException {
        log.debug("Model delete Started");
        try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_MARKSHEET WHERE ID=?")) {
                pstmt.setLong(1, bean.getId());
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            log.error("Database Exception in delete marksheet", e);
            throw new ApplicationException("Exception : Exception in deleting marksheet - " + e.getMessage());
        }
        log.debug("Model delete End");
    }

    public MarksheetBean findByRollNo(String rollNo) throws ApplicationException {
        log.debug("Model findByRollNo Started");
        MarksheetBean bean = null;
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ST_MARKSHEET WHERE ROLL_NO=?")) {
            pstmt.setString(1, rollNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bean = new MarksheetBean();
                    populate(rs, bean);
                }
            }
        } catch (SQLException e) {
            log.error("Database Exception in findByRollNo", e);
            throw new ApplicationException("Exception: Exception in getting marksheet by roll no - " + e.getMessage());
        }
        log.debug("Model findByRollNo End");
        return bean;
    }

    public MarksheetBean findByPK(Long pk) throws ApplicationException {
        log.debug("Model findByPK Started");
        MarksheetBean bean = null;
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ST_MARKSHEET WHERE ID=?")) {
            pstmt.setLong(1, pk);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bean = new MarksheetBean();
                    populate(rs, bean);
                }
            }
        } catch (SQLException e) {
            log.error("Database Exception in findByPK", e);
            throw new ApplicationException("Exception: Exception in getting marksheet by pk - " + e.getMessage());
        }
        log.debug("Model findByPK End");
        return bean;
    }

    public void update(MarksheetBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("Model update Started");

        // Check for duplicate roll number
        MarksheetBean beanExist = findByRollNo(bean.getRollNo());
        if (beanExist != null && beanExist.getId() != bean.getId()) {
            throw new DuplicateRecordException("Roll No is already exist");
        }

        try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false);
            StudentModel sModel = new StudentModel();
            StudentBean studentbean = sModel.findByPK(bean.getStudentld());
            if (studentbean == null) {
                throw new ApplicationException("Student not found");
            }

            bean.setName(studentbean.getFirstName() + " " + studentbean.getLastName());

            try (PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_MARKSHEET SET ROLL_NO=?, STUDENT_ID=?, NAME=?, PHYSICS=?, CHEMISTRY=?, MATHS=?, CREATED_BY=?, MODIFIED_BY=?, CREATED_DATETIME=?, MODIFIED_DATETIME=? WHERE ID=?")) {
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
                conn.commit();
            }
        } catch (SQLException e) {
            log.error("Database Exception in update", e);
            throw new ApplicationException("Exception: Exception in updating Marksheet - " + e.getMessage());
        }
        log.debug("Model update End");
    }

    public List<MarksheetBean> search(MarksheetBean bean, int pageNo, int pageSize) throws ApplicationException {
        log.debug("Model search Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_MARKSHEET WHERE 1=1");
        int index = 1;
        ArrayList<MarksheetBean> list = new ArrayList<>();

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" AND ID = ?");
            }
            if (bean.getRollNo() != null && bean.getRollNo().trim().length() > 0) {
                sql.append(" AND ROLL_NO like ?");
            }
            if (bean.getName() != null && bean.getName().trim().length() > 0) {
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

        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

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
                    MarksheetBean resultBean = new MarksheetBean();
                    populate(rs, resultBean);
                    list.add(resultBean);
                }
            }
        } catch (SQLException e) {
            log.error("Database Exception in search", e);
            throw new ApplicationException("Exception: Exception in searching marksheet - " + e.getMessage());
        }
        log.debug("Model search End");
        return list;
    }

    private void populate(ResultSet rs, MarksheetBean bean) throws SQLException {
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
