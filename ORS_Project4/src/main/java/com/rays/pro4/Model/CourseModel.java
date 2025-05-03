package com.rays.pro4.Model;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

/**
 * The Class CourseModel.
 * 
 * @author Lokesh Solanki
 */
public class CourseModel extends BaseModel {

    private String name;
    private String description;
    private String duration;

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
        try {
            conn = JDBCDataSource.getConnection();
            pk = nextPK();
            // check if the bean already exists
            CourseBean duplicateCourse = findByName(bean.getName());
            if (duplicateCourse != null) {
                throw new DuplicateRecordException("Course Name already exists");
            }
            conn.setAutoCommit(false); // Begin transaction
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_COURSE VALUES(?,?,?,?,?,?,?,?)")) {
                pstmt.setLong(1, pk);
                pstmt.setString(2, bean.getName());
                pstmt.setString(3, bean.getDescription());
                pstmt.setString(4, bean.getDuration());
                pstmt.setString(5, bean.getCreatedBy());
                pstmt.setString(6, bean.getModifiedBy());
                pstmt.setTimestamp(7, bean.getCreatedDatetime());
                pstmt.setTimestamp(8, bean.getModifiedDatetime());
                pstmt.executeUpdate();
                conn.commit(); // End transaction
            }
        } catch (Exception e) {
            conn.rollback();
            throw new ApplicationException("Exception: Exception in add Course " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    /**
     * Deletes a course from the database.
     *
     * @param bean                  The CourseBean object to be deleted.
     * @throws ApplicationException If a database error occurs.
     */
    public void delete(CourseBean bean) throws ApplicationException {
         CourseBean findBean = findByPK(bean.getId());
         if(findBean!=null){
        try {
            delete(findBean);
        } catch (Exception e) {
            log.error("Database Exception..", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception: Exception in delete Course");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }}
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
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = populate(rs);
            }
            rs.close();
        } catch (Exception e) {

            throw new ApplicationException("Exception: Exception in getting Course by name");
        } finally {
            JDBCDataSource.closeConnection(conn);
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
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false); // Begin transaction
            CourseBean duplicateCourse = findByName(bean.getName());
            if (duplicateCourse != null && duplicateCourse.getId() != bean.getId()) {
                throw new DuplicateRecordException("Course Name already exists");
            }
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE ST_COURSE SET NAME=?, DESCRIPTION=?, DURATION=?, MODIFIED_BY=?, MODIFIED_DATETIME=? WHERE ID=?")) {
                pstmt.setString(1, bean.getName());
                pstmt.setString(2, bean.getDescription());
                pstmt.setString(3, bean.getDuration());
                pstmt.setString(4, bean.getModifiedBy());
                pstmt.setTimestamp(5,new Timestamp(new Date().getTime()));
                pstmt.setLong(6, bean.getId());
               pstmt.executeUpdate();
            }
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
        Connection conn = null;
        try (Connection connection = JDBCDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
            preparedStatement.setLong(1, pk);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    bean = populate(resultSet);
                }
            }
        } catch (Exception e) {
            throw new ApplicationException("Exception: Exception in getting Course by pk");
                if (rs.next()) {
                    pk = rs.getInt(1);
                }
                
            }
         } catch (Exception e) {
            log.error("Database Exception while getting next PK for ST_COURSE", e);
            throw new DatabaseException("Exception: Unable to get next primary key for ST_COURSE: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model nextPK End");
        return pk + 1;
    }*/

    /**
     * Searches courses with pagination.
     *
     * @param bean                         The CourseBean to search with.
     * @param pageNo                       The page number.
     * @param pageSize The page size.
     * @return A list of CourseBeans matching the search criteria.
     * @throws ApplicationException If a general application exception occurs.
     */
    public List search(CourseBean bean, int pageNo, int pageSize) throws ApplicationException {
        log.debug("Model search Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE WHERE 1=1");
        ArrayList<CourseBean> list = new ArrayList<>();
        int index = 1;

        try (Connection conn = JDBCDataSource.getConnection();) {

            if (bean != null)
            {
                if (bean.getId() > 0) {
                    sql.append(" AND id = ?");
                }
                if (bean.getName() != null && !bean.getName().isEmpty()) {
                    sql.append(" AND NAME like ?");
                }
            }

            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                sql.append(" LIMIT " + pageNo + ", " + pageSize);
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                if (bean != null) {
                    if (bean.getId() > 0) {
                        pstmt.setLong(index++, bean.getId());
                    }
                    if (bean.getName() != null && !bean.getName().isEmpty()) {
                        pstmt.setString(index++, bean.getName() + "%");
                    }
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        bean = populate(rs);
                        list.add(bean);
                    }
                }
           }
        } catch (Exception e) {
            log.error("Database Exception..", e);
            throw new ApplicationException("Exception: Exception in search Course");
        } finally {
                JDBCDataSource.closeConnection(conn);
        } 
        log.debug("Model search End");
        return list;
    }

    public List list(int pageNo, int pageSize) throws ApplicationException {
        log.debug("Model list Started");
        ArrayList<CourseBean> list = new ArrayList<>();
        
        try (Connection conn = JDBCDataSource.getConnection();) {
            StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE");
            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                sql.append(" LIMIT " + pageNo + "," + pageSize);
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString());
                    ResultSet rs = pstmt.executeQuery();) {
                while (rs.next()) {
                    list.add(populate(rs));
                }
            }
        } catch (Exception e) {
            throw new ApplicationException("Exception: Exception in list Course");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model list End");
        return list;
    }

    public String getTableName() {
        return "ST_COURSE";
    }

    public CourseBean populate(ResultSet rs) throws Exception {
        CourseBean bean = new CourseBean();
        bean.setId(rs.getLong(1));
        bean.setName(rs.getString(2));
        bean.setDescription(rs.getString(3));
        bean.setDuration(rs.getString(4));
        bean.setCreatedBy(rs.getString(5));
        bean.setModifiedBy(rs.getString(6));
        bean.setCreatedDatetime(rs.getTimestamp(7));
        bean.setModifiedDatetime(rs.getTimestamp(8));
        return bean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public BaseModel populate(BaseModel model, PreparedStatement pstmt, ResultSet rs) throws Exception {

        return null;
    }
}