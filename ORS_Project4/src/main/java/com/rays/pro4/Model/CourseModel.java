package com.rays.pro4.Model;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;

import java.util.List;

import com.rays.pro4.DTO.CourseDTO;
import com.rays.pro4.Exception.DatabaseException;
import java.sql.SQLException;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

/**
 * The Class CourseModel.
 * 
 * @author Lokesh Solanki
 */
public class CourseModel extends BaseModel {

    /**
     * Adds a course to the database.
     *
     * @param dto The CourseDTO to add.
     * @return The primary key (id) of the newly added course.
     * @throws ApplicationException     If a general application exception occurs.
     * @throws DuplicateRecordException If a course with the same name already exists.
     */
    public long add(CourseDTO dto) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;       
        long pk = 0;        
        log.debug("Model add started");
        try {
            conn = JDBCDataSource.getConnection();
            CourseDTO duplicateCourse = findByName(dto.getName());

            if (duplicateCourse != null) throw new DuplicateRecordException("Course already exist");
            pk = nextPK();
            dto.setId(pk);
            conn.setAutoCommit(false);           
            try (PreparedStatement pstmt = conn
                    .prepareStatement("INSERT INTO ST_COURSE VALUES(?,?,?,?,?,?,?,?)")) {
                pstmt.setLong(1, pk);
                pstmt.setString(2, dto.getName());
                pstmt.setString(3, dto.getDescription());
                pstmt.setString(4, dto.getDuration());
                pstmt.setString(5, dto.getCreatedBy());
                pstmt.setString(6, dto.getModifiedBy());
                pstmt.setTimestamp(7, dto.getCreatedDatetime());
                pstmt.setTimestamp(8, dto.getModifiedDatetime());
                pstmt.executeUpdate();               
                conn.commit();

            }
        } catch (SQLException e) {
            if(conn!=null)
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    log.error("Error during rollback in add Course", ex);
                }
            
            throw new ApplicationException("Exception: Exception in add Course", e);
           
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    /**
     * Deletes a course from the database.
     *
     * @param dto The CourseDTO object to be deleted.
     * @throws ApplicationException If a database error occurs.
     */
    public void delete(CourseDTO dto) throws ApplicationException, DatabaseException {
        log.debug("Model delete Started");
        try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false); // Start transaction
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_COURSE WHERE ID = ?")) {
                pstmt.setLong(1, dto.getId());
                pstmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            log.error("Database Exception in delete Course", e);
            throw new ApplicationException("Exception: Exception in delete Course - " + e.getMessage());
        }
    }


    /**
     * Finds a course by its name.
     *
     * @param name The name of the course to find.
     * @return The CourseBean if found, otherwise null.
     * @throws ApplicationException     If a general application exception occurs.
     */
    public CourseDTO findByName(String name) throws ApplicationException {
        log.debug("Model findByName Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE WHERE NAME=?");
        CourseDTO dto = null;
        try (Connection conn = JDBCDataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                pstmt.setString(1, name);
                try (ResultSet rs = pstmt.executeQuery()) { 
                    if (rs.next()) { 
                         dto = populate(rs, new CourseDTO());
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
              throw new ApplicationException("Exception: Exception in getting Course by name" + e.getMessage());
         }
        log.debug("Model findByName End");
        return dto;
    }

    /**
     * Updates a course in the database.
     *
     * @param bean                          The CourseBean to update.
     * @throws ApplicationException     If a general application exception occurs.
     * @throws DuplicateRecordException If a course with the same name already exists.
     */   
    public void update(CourseDTO dto) throws ApplicationException, DuplicateRecordException {
        log.debug("Model update Started");
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            CourseDTO duplicateCourse = findByName(dto.getName());
            if (duplicateCourse != null && duplicateCourse.getId() != dto.getId()) {
                throw new DuplicateRecordException("Course Name already exists");
            }
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE ST_COURSE SET NAME=?, DESCRIPTION=?, DURATION=?, MODIFIED_BY=?, MODIFIED_DATETIME=? WHERE ID=?")) {

                pstmt.setString(1, dto.getName());
                pstmt.setString(2, dto.getDescription());
                pstmt.setString(3, dto.getDuration());
                pstmt.setString(4, dto.getModifiedBy());
                pstmt.setTimestamp(5, dto.getModifiedDatetime());
                pstmt.setLong(6, dto.getId());
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            log.error("Database Exception in update Course", e);
            if(conn!=null)
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                     log.error("Error during rollback in update Course", ex);
                }


            throw new ApplicationException("Exception in updating Course");
        }

    }
    
    /**
     * Finds a course by its primary key (id).
     *
     * @param pk The primary key (id) of the course to find.
     * @return The CourseBean if found, otherwise null.
     * @throws ApplicationException If a general application exception occurs.
     */
    public CourseDTO findByPK(long pk) throws ApplicationException {
        log.debug("Model findByPK Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE WHERE ID=?");
        CourseDTO dto = null;

        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setLong(1, pk);
            try (ResultSet rs = pstmt.executeQuery()) {                if (rs.next()) {                   dto = populate(rs, new CourseDTO());                }
            }
        } catch (SQLException e) {
             log.error("Database Exception in update Course", e);
            if(conn != null){
 try {
 conn.rollback();
 } catch (SQLException ex) {
 log.error("Error during rollback in findByPK", ex);
 }
            }
            throw new ApplicationException("Exception: Exception in getting Course by pk");
        }
        return dto;
    }




    public List search(CourseDTO dto, int pageNo, int pageSize, String orderBy, String sortOrder) throws ApplicationException {
        log.debug("Model search Started");
        StringBuilder sql = new StringBuilder("SELECT * FROM ST_COURSE WHERE 1=1");
        ArrayList<CourseDTO> list = new ArrayList<>();
        int index = 1;       

        orderBy = (orderBy == null || orderBy.trim().isEmpty()) ? "NAME" : orderBy;
        sortOrder = (sortOrder == null || sortOrder.trim().isEmpty()) ? "ASC" : sortOrder;
        
        try (Connection conn = JDBCDataSource.getConnection()) {
           if (dto != null) {
                if (dto.getId() > 0)
                    sql.append(" AND id = ?");
                if (dto.getName() != null && !dto.getName().isEmpty()) {
                     sql.append(" AND NAME like ?");
                }
            }
           sql.append(" ORDER BY ").append(orderBy).append(" ").append(sortOrder);

            if (pageSize > 0) {
                pageNo = Math.max(1, pageNo);
                 pageNo = (pageNo - 1) * pageSize;
                sql.append(" LIMIT ").append(pageNo).append(", ").append(pageSize);
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
               if (dto != null) {
                    if (dto.getId() > 0) {
                        pstmt.setLong(index++, dto.getId());
                    }
                    if (dto.getName() != null && !dto.getName().isEmpty()) {
                        pstmt.setString(index, dto.getName() + "%");
                        index++;
                    }
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                   while (rs.next()) {
                      list.add(populate(rs, new CourseDTO()));
                   }
               }  
            }

        } catch (SQLException e) {
             log.error("Database Exception in search Course", e);
            throw new ApplicationException("Exception: Exception in search Course\n" + e.getMessage());
        }
        log.debug("Model search End");
        return list;
    }    

    
    public List list() throws ApplicationException {
        return search(null, 0, 0, null, null);
    }

    private CourseDTO populate(ResultSet rs, CourseDTO dto) throws SQLException {
        dto.setId(rs.getLong(1));
        dto.setName(rs.getString(2));
        dto.setDescription(rs.getString(3));
        dto.setDuration(rs.getString(4));
        dto.setCreatedBy(rs.getString(5));
        dto.setModifiedBy(rs.getString(6));
        dto.setCreatedDatetime(rs.getTimestamp(7));
        dto.setModifiedDatetime(rs.getTimestamp(8));
        return dto;
    }
    
    
     @Override
    public String getTableName() {
        return "ST_COURSE";
    }

}
