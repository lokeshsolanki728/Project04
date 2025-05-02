package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

/**
 * The Class CourseModel.
 * @author Lokesh Solanki
 */
public class CourseModel {
    private static Logger log = Logger.getLogger(CourseModel.class);

    /**
     * Adds a course to the database.
     *
     * @param bean The CourseBean to add.
     * @return The primary key (id) of the newly added course.
     * @throws ApplicationException     If a general application exception occurs.
     * @throws DuplicateRecordException If a course with the same name already exists.
     */
    public long add(CourseBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("Model add Started");
        Connection conn = null;
        int pk = 0;
        try {
            conn = JDBCDataSource.getConnection();
            pk = nextPK();
            conn.setAutoCommit(false); // Begin transaction
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_COURSE VALUES(?,?,?,?,?,?,?)");
            pstmt.setInt(1, pk);
            pstmt.setString(2, bean.getName());
            pstmt.setString(3, bean.getDescription());
            pstmt.setString(4, bean.getDuration());
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.getTimestamp(7);
            pstmt.executeUpdate();
            conn.commit(); // End transaction
            pstmt.close();
        } catch (Exception e) {
            log.error("Database Exception..", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception: add rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception: Exception in add Course " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model add End");
        return pk;
    }

    /**
     * Updates a course in the database.
     *
     * @param bean The CourseBean to update.
     * @throws ApplicationException     If a general application exception occurs.
     * @throws DuplicateRecordException If a course with the same name already exists.
     */
    public void update(CourseBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("Model update Started");
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false); // Begin transaction
            PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_COURSE SET NAME=?, DESCRIPTION=?, DURATION=?, MODIFIED_BY=?, MODIFIED_DATETIME=? WHERE ID=?");
            pstmt.setString(1, bean.getName());
            pstmt.setString(2, bean.getDescription());
            pstmt.setString(3, bean.getDuration());
            pstmt.setString(4, bean.getModifiedBy());
            pstmt.getTimestamp(5);
            pstmt.setLong(6, bean.getId());
            pstmt.executeUpdate();
            conn.commit(); // End transaction
            pstmt.close();
        } catch (Exception e) {
            log.error("Database Exception..", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception: update rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in updating Course");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model update End");
    }

    /**
     * Finds a course by its primary key (id).
     *
     * @param pk The primary key (id) of the course to find.
     * @return The CourseBean if found, otherwise null.
     * @throws ApplicationException If a general application exception occurs.
     */
    public CourseBean findByPK(long pk) throws ApplicationException {
        log.debug("Model findByPK Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE WHERE ID=?");
        CourseBean bean = null;
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, pk);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new CourseBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setDescription(rs.getString(3));
                bean.setDuration(rs.getString(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDatetime(rs.getTimestamp(7));
            }
            rs.close();
        } catch (Exception e) {
            log.error("Database Exception..", e);
            throw new ApplicationException("Exception: Exception in getting Course by pk");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model findByPK End");
        return bean;
    }

private Integer nextPK() throws DatabaseException {
    log.debug("Model nextPK Started");
    Connection conn = null;
    int pk = 0;
    try {
        conn = JDBCDataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'ORS_Project4' AND TABLE_NAME = 'ST_COURSE'");
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            pk = rs.getInt(1);
        }
        rs.close();
        pstmt.close();
    } catch (Exception e) {
        log.error("Database Exception while getting next PK for ST_COURSE", e);
        throw new DatabaseException("Exception: Unable to get next primary key for ST_COURSE: " + e.getMessage());
    } finally {
        JDBCDataSource.closeConnection(conn);
    }
    log.debug("Model nextPK End");
    return pk;
}