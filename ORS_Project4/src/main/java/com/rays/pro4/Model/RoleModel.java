package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.RoleBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

/**
 * JDBC Implementation of Role Model.
 * 
 * @author Lokesh SOlanki
 *
 */
public class RoleModel {

    private static Logger log = Logger.getLogger(RoleModel.class);

    /**
     * Generate next PK of Role
     *
     * @return long
     * @throws DatabaseException
     */
    public synchronized Integer nextPK() throws DatabaseException {
        log.debug("Model nextPK Started");
        int pk = 0;
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_ROLE");
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                pk = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.error("Database Exception in nextPK", e);
            throw new DatabaseException("Exception: Unable to get next primary key - " + e.getMessage());
        }
        log.debug("Model nextPK End");
        return pk + 1;
    }

    /**
     * add method to add role in database
     *
     * @param bean
     * @return long
     * @throws ApplicationException
     * @throws DuplicateRecordException
     */
    public long add(RoleBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("Model add Started");
        long pk = 0;
        RoleBean duplicateRole = findByName(bean.getName());
        if (duplicateRole != null) {
            throw new DuplicateRecordException("Role Name already exists");
        }

        try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false);
            pk = nextPK();
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_ROLE VALUES(?,?,?,?,?,?,?)")) {
                pstmt.setLong(1, pk);
                pstmt.setString(2, bean.getName());
                pstmt.setString(3, bean.getDescription());
                pstmt.setString(4, bean.getCreatedBy());
                pstmt.setString(5, bean.getModifiedBy());
                pstmt.setTimestamp(6, bean.getCreatedDatetime());
                pstmt.setTimestamp(7, bean.getModifiedDatetime());
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            log.error("Database Exception in add", e);
            JDBCDataSource.trnRollback();
            throw new ApplicationException("Exception: Exception in add Role - " + e.getMessage());
        } finally {
        }
        log.debug("Model add End");
        return pk;
    }

    /**
     * delete method to delete role by id
     *
     * @param id
     * @throws ApplicationException
     */
    public void delete(long id) throws ApplicationException {
        log.debug("Model delete Started");

        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_ROLE WHERE ID=?")) {
            conn.setAutoCommit(false);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            log.error("Database Exception in delete", e);
            JDBCDataSource.trnRollback();
            throw new ApplicationException("Exception : Exception in delete Role - " + e.getMessage());
        }
        log.debug("Model delete End");
    }

    /**
     * find by name method to get the role by name
     *
     * @param name
     * @return RoleBean
     * @throws ApplicationException
     */
    public RoleBean findByName(String name) throws ApplicationException {
        log.debug("Model findByName Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_ROLE WHERE NAME=?");

        RoleBean bean = null;
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bean = new RoleBean();
                    bean.setId(rs.getLong(1));
                    bean.setName(rs.getString(2));
                    bean.setDescription(rs.getString(3));
                    bean.setCreatedBy(rs.getString(4));
                    bean.setModifiedBy(rs.getString(5));
                    bean.setCreatedDatetime(rs.getTimestamp(6));
                    bean.setModifiedDatetime(rs.getTimestamp(7));
                }
            }
        } catch (SQLException e) {
            log.error("Database Exception in findByName", e);
            throw new ApplicationException("Exception: Exception in getting Role by name - " + e.getMessage());
        }
        log.debug("Model findByName End");
        return bean;
    }

    /**
     * find by pk method to get the role by pk
     *
     * @param pk
     * @return RoleBean
     * @throws ApplicationException
     */
    public RoleBean findByPK(long pk) throws ApplicationException {
        log.debug("Model findByPK Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_ROLE WHERE ID=?");
        RoleBean bean = null;
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setLong(1, pk);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bean = new RoleBean();
                    bean.setId(rs.getLong(1));
                    bean.setName(rs.getString(2));
                    bean.setDescription(rs.getString(3));
                    bean.setCreatedBy(rs.getString(4));
                    bean.setModifiedBy(rs.getString(5));
                    bean.setCreatedDatetime(rs.getTimestamp(6));
                    bean.setModifiedDatetime(rs.getTimestamp(7));
                }
            }
        } catch (SQLException e) {
            log.error("Database Exception in findByPK", e);
            throw new ApplicationException("Exception: Exception in getting Role by pk - " + e.getMessage());
        }
        log.debug("Model findByPK End");
        return bean;
    }

    /**
     * update method to update the role
     *
     * @param bean
     * @throws ApplicationException
     * @throws DuplicateRecordException
     */
    public void update(RoleBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("Model update Started");
        RoleBean duplicateRole = findByName(bean.getName());
        if (duplicateRole != null && duplicateRole.getId() != bean.getId()) {
            throw new DuplicateRecordException("Role Name already exists");
        }
        try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE ST_ROLE SET NAME=?,DESCRIPTION=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?")) {
                pstmt.setString(1, bean.getName());
                pstmt.setString(2, bean.getDescription());
                pstmt.setString(3, bean.getCreatedBy());
                pstmt.setString(4, bean.getModifiedBy());
                pstmt.setTimestamp(5, bean.getCreatedDatetime());
                pstmt.setTimestamp(6, bean.getModifiedDatetime());
                pstmt.setLong(7, bean.getId());
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            log.error("Database Exception in update", e);
            JDBCDataSource.trnRollback();
            throw new ApplicationException("Exception in updating Role - " + e.getMessage());
        }
        log.debug("Model update End");
    }

    /**
     * search method to search the role by values
     *
     * @param bean
     * @return List
     * @throws ApplicationException
     */
    public List search(RoleBean bean) throws ApplicationException {
        return search(bean, 1, 0);
    }

    /**
     * search method to search role by pagination
     *
     * @param bean
     * @param pageNo
     * @param pageSize
     * @return List
     * @throws ApplicationException
     */
    public List search(RoleBean bean, int pageNo, int pageSize) throws ApplicationException {
        log.debug("Model search Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_ROLE WHERE 1=1");
        ArrayList<RoleBean> list = new ArrayList<>();

        if (pageNo < 0) {
            pageNo = 1;
        }
        if (pageSize < 0) {
            pageSize = 10; // Default page size
        }
        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" AND id= " + bean.getId());
            }
            if (bean.getName() != null && !bean.getName().isEmpty()) {
                sql.append(" AND NAME like '" + bean.getName() + "%'");
            }
            if (bean.getDescription() != null && !bean.getDescription().isEmpty()) {
                sql.append(" AND DESCRIPTION like '" + bean.getDescription() + "%'");
            }
        }
        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT " + pageNo + "," + pageSize);
        }

        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString());
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                bean = new RoleBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setDescription(rs.getString(3));
                bean.setCreatedBy(rs.getString(4));
                bean.setModifiedBy(rs.getString(5));
                bean.setCreatedDatetime(rs.getTimestamp(6));
                bean.setModifiedDatetime(rs.getTimestamp(7));
                list.add(bean);
            }
        } catch (SQLException e) {
            log.error("Database Exception in search", e);
            throw new ApplicationException("Exception: Exception in search Role - " + e.getMessage());
        }
        log.debug("Model search End");
        return list;
    }

    /**
     * list method to get all role list
     *
     * @return List
     * @throws ApplicationException
     */
    public List list() throws ApplicationException {
        return list(1, 0);
    }

    /**
     * list method to get role list by pagination
     *
     * @param pageNo
     * @param pageSize
     * @return List
     * @throws ApplicationException
     */
    public List list(int pageNo, int pageSize) throws ApplicationException {
        log.debug("Model list Started");
        ArrayList<RoleBean> list = new ArrayList<>();
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_ROLE");

        if (pageNo < 0) {
            pageNo = 1;
        }
        if (pageSize < 0) {
            pageSize = 10; // Default page size
        }
        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT " + pageNo + " , " + pageSize);
        }

        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString());
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                RoleBean bean = new RoleBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setDescription(rs.getString(3));
                bean.setCreatedBy(rs.getString(4));
                bean.setModifiedBy(rs.getString(5));
                bean.setCreatedDatetime(rs.getTimestamp(6));
                bean.setModifiedDatetime(rs.getTimestamp(7));
                list.add(bean);
            }
        } catch (SQLException e) {
            log.error("Database Exception in list", e);
            throw new ApplicationException("Exception: Exception in getting list of Role - " + e.getMessage());
        }
        log.debug("Model list End");
        return list;
    }
}