package com.rays.pro4.Model;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import java.util.List;


import com.rays.pro4.Bean.CourseBean;
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
     * @param bean                          The CourseBean to add.
     * @return The primary key (id) of the newly added course.
     * @throws ApplicationException     If a general application exception occurs.
     * @throws DuplicateRecordException If a course with the same name already exists.
     */
    public long add(CourseBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;       
        long pk = 0;        
        log.debug("Model add started");
        try {
            conn = JDBCDataSource.getConnection();
            CourseDTO duplicateCourse = findByName(dto.getName());
            if (duplicateCourse != null) throw new DuplicateRecordException("Course already exist");
            pk = nextPK();           
             bean.setId(pk);
            conn.setAutoCommit(false);           
            try (PreparedStatement pstmt = conn
                    .prepareStatement("INSERT INTO ST_COURSE VALUES(?,?,?,?,?,?,?,?)")) {
                pstmt.setLong(1, pk);
                pstmt.setString(2, bean.getName());
                pstmt.setString(3, bean.getDescription());
                pstmt.setString(4, bean.getDuration());
                pstmt.setString(5, bean.getCreatedBy());
                pstmt.setString(6, bean.getModifiedBy());
                pstmt.setTimestamp(7, dto.getCreatedDatetime());
                pstmt.setTimestamp(8, dto.getModifiedDatetime());
                pstmt.executeUpdate();               
                conn.commit(); 
               
            }           
           
        }catch (SQLException e) {
             log.error("Database Exception in add Course", e);
            if(conn!=null)
                conn.rollback();
            
            throw new ApplicationException("Exception: Exception in add Course " + e.getMessage());
           
        }
        return pk;
    }

    /**
     * Deletes a course from the database.
     *
     * @param bean                  The CourseBean object to be deleted.
     * @throws ApplicationException If a database error occurs.
     */
    public void delete(CourseBean bean) throws ApplicationException, DatabaseException {
        log.debug("Model delete Started");
        try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false); // Start transaction
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_COURSE WHERE ID = ?")) {
                pstmt.setLong(1, bean.getId());
                pstmt.executeUpdate();
           }
           
            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            log.error("Database Exception in delete Course", e);
            JDBCDataSource.trnRollback();
            throw new ApplicationException("Exception: Exception in delete Course - " + e.getMessage());
        }
    }


    /**
     * Finds a course by its name.
     *
     * @param name                    The name of the course to find.
     * @return The CourseBean if found, otherwise null.
     * @throws ApplicationException     If a general application exception occurs.
     */
    public CourseBean findByName(String name) throws ApplicationException {
        log.debug("Model findByName Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE WHERE NAME=?");
        CourseBean bean = null;
        try (Connection conn = JDBCDataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                pstmt.setString(1, name);                
                 try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        bean = new CourseBean();
                       bean.setId(rs.getLong(1));
                        dto.setName(rs.getString(2));
                        dto.setDescription(rs.getString(3));
                        dto.setDuration(rs.getString(4));
                        dto.setCreatedBy(rs.getString(5));
                        dto.setModifiedBy(rs.getString(6));
                        dto.setCreatedDatetime(rs.getTimestamp(7));
                        dto.setModifiedDatetime(rs.getTimestamp(8));
                    }
                }
                }
        } catch (SQLException e) {  
            log.error("Database Exception in find by name", e);
            e.printStackTrace();      
              throw new ApplicationException("Exception: Exception in getting Course by name" + e.getMessage());       
         }
        log.debug("Model findByName End");
        return bean;
    }   

    /**
     * Updates a course in the database.
     *
     * @param bean                          The CourseBean to update.
     * @throws ApplicationException     If a general application exception occurs.
     * @throws DuplicateRecordException If a course with the same name already exists.
     */   
    public void update(CourseBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("Model update Started");
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
             CourseBean duplicateCourse = findByName(bean.getName());
                if (duplicateCourse != null && duplicateCourse.getId() != bean.getId()) {
                    throw new DuplicateRecordException("Course Name already exists");
                }
                try (PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE ST_COURSE SET NAME=?, DESCRIPTION=?, DURATION=?, MODIFIED_BY=?, MODIFIED_DATETIME=? WHERE ID=?")) {
                   
                    pstmt.setString(1, bean.getName());
                    pstmt.setString(2, bean.getDescription());
                    pstmt.setString(3, bean.getDuration());
                    pstmt.setString(4, dto.getModifiedBy());
                    pstmt.setTimestamp(5, dto.getModifiedDatetime());
                    pstmt.setLong(6, dto.getId());
                    pstmt.executeUpdate();
                    conn.commit();
            }
        }catch (SQLException e) {
           conn.rollback();
        } catch (SQLException e) {
            log.error("Database Exception in update Course", e);
            if(conn!=null)
                 conn.rollback();               
            
            throw new ApplicationException("Exception in updating Course");
        } finally {
            }
    }

    /**
     * Finds a course by its primary key (id).
     *
     * @param pk                    The primary key (id) of the course to find.
     * @return The CourseBean if found, otherwise null.
     * @throws ApplicationException If a general application exception occurs.
     */
    public CourseBean findByPK(long pk) throws ApplicationException {
        log.debug("Model findByPK Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE WHERE ID=?");
        CourseBean bean = null;
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setLong(1, pk);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                  dto=  populate(resultSet,dto);
                  
                }
            } 
        } catch (SQLException e) {
            throw new ApplicationException("Exception: Exception in getting Course by pk");
        } finally {
            
            }
       
    }
    }

    /**
     * Searches courses with pagination.
     *
     * @param bean                         The CourseBean to search with.
     * @param pageNo                       The page number.
     * @param pageSize The page size.
     * @return A list of CourseBeans matching the search criteria.
     * @throws ApplicationException If a general application exception occurs.
     */
 public List search(CourseBean bean, int pageNo, int pageSize, String orderBy, String sortOrder) throws ApplicationException {
        log.debug("Model search Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE WHERE 1=1");
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
           sql.append(" ORDER BY " + orderBy + " " + sortOrder);
            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                sql.append(" LIMIT " + pageNo + ", " + pageSize);
            }
             try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                if (dto != null) {
                    if (dto.getId() > 0) {
                        pstmt.setLong(index++, dto.getId());
                    }
                    if (dto.getName() != null && !dto.getName().isEmpty()) {
                        pstmt.setString(index++, dto.getName() + "%");
                    }
                }
                    
                
                 try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                         CourseDTO dto = populate(rs, new CourseDTO());
                      
                        list.add(dto);
                    }
                } }           
        } catch (SQLException e) {
            log.error("Database Exception in search Course", e);
            throw new ApplicationException("Exception: Exception in search Course");
        }
        log.debug("Model search End");
        return list;
    }

  public List list(int pageNo, int pageSize, String orderBy, String sortOrder) throws ApplicationException {
        log.debug("Model list Started");
        ArrayList<CourseDTO> list = new ArrayList<>();
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE");
        orderBy = (orderBy == null || orderBy.trim().isEmpty()) ? "NAME" : orderBy;
        sortOrder = (sortOrder == null || sortOrder.trim().isEmpty()) ? "ASC" : sortOrder;

        sql.append(" ORDER BY " + orderBy + " " + sortOrder);

        if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                sql.append(" LIMIT " + pageNo + "," + pageSize);
            }
        try (Connection conn = JDBCDataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql.toString());
                ResultSet rs = pstmt.executeQuery();) {
                while (rs.next()) {
                     CourseDTO dto = populate(rs, new CourseDTO());
                    list.add(dto);
                }
            
                
        } catch (SQLException e) {
             log.error("Database Exception in list Course", e);
            throw new ApplicationException("Exception: Exception in list Course");
        }
        log.debug("Model list End");
        return list;
    }

    @Override
    public String getTableName() {
        return "ST_COURSE";
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
        }