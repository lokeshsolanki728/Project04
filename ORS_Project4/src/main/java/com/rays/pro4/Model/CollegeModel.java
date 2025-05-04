package com.rays.pro4.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.rays.pro4.Bean.CollegeBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

/** JDBC Implementation of College Model.
 * @author Lokesh
 */
public class CollegeModel extends BaseModel {

    private synchronized long nextPK() throws DatabaseException {
        log.debug("Model nextPK Started");
        long pk = 0;
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_COLLEGE");
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                pk = rs.getLong(1);
            }
        } catch (SQLException e) {
            log.error("Database Exception in nextPK", e);
            throw new DatabaseException("Exception: Unable to get PK - " + e.getMessage());
        }
        log.debug("Model nextPK End " + (pk + 1));
        return pk + 1;
    }

    public long add(CollegeBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("Model add Started " + bean);
        long pk;
        if (findByName(bean.getName()) != null) {
            throw new DuplicateRecordException("College Name already exists");
        }
        try (Connection conn = JDBCDataSource.getConnection()) {
            pk = nextPK();
            bean.setId(pk);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            bean.setCreatedDatetime(now);
            bean.setModifiedDatetime(now);

            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO ST_COLLEGE (ID,NAME,ADDRESS,STATE,CITY,PHONE_NO,CREATED_BY,MODIFIED_BY,CREATED_DATETIME,MODIFIED_DATETIME) " +
                            "VALUES(?,?,?,?,?,?,?,?,?,?)")) {
                pstmt.setLong(1, pk);
                pstmt.setString(2, bean.getName());
                pstmt.setString(3, bean.getAddress());
                pstmt.setString(4, bean.getState());
                pstmt.setString(5, bean.getCity());
                pstmt.setString(6, bean.getPhoneNo());
                pstmt.setString(7, bean.getCreatedBy());
                pstmt.setString(8, bean.getModifiedBy());
                pstmt.setTimestamp(9, bean.getCreatedDatetime());
                pstmt.setTimestamp(10, bean.getModifiedDatetime());
                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            log.error("Database Exception in add college", e);
            throw new ApplicationException("Exception: Exception in adding college - " + e.getMessage());
        }
        log.debug("Model add End " + bean);
        return pk;
    }

    public void delete(CollegeBean bean) throws ApplicationException {
        log.debug("Model delete Started");
        try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_COLLEGE WHERE ID=?")) {
                pstmt.setLong(1, bean.getId());
                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            log.error("Database Exception in delete college", e);
            throw new ApplicationException("Exception in delete college " + e.getMessage());
        }
        log.debug("Model delete End");
    }

    public void delete(long id) throws ApplicationException {
        log.debug("Model delete by ID");
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_COLLEGE WHERE ID=?")) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("Database Exception in delete by ID", e);
            throw new ApplicationException("Exception in delete by ID: " + e.getMessage());
        }
        log.debug("Model delete by ID End");
    }

    public CollegeBean findByName(String name) throws ApplicationException {
        log.debug("Model findByName Started");
        CollegeBean bean = null;
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ST_COLLEGE WHERE NAME=?")) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    bean = new CollegeBean();
                    populate(rs, bean);
                }
            }
        } catch (Exception e) {
            log.error("Database Exception in find by Name", e);
            throw new ApplicationException("Exception: Exception in getting College by name - " + e.getMessage());
        }
        log.debug("Model findByName End");
        return bean;
    }

    public CollegeBean findByPK(long pk) throws ApplicationException {
        log.debug("Model findByPK Started");
        CollegeBean bean = null;
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ST_COLLEGE WHERE ID=?")) {
            pstmt.setLong(1, pk);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    bean = new CollegeBean();
                    populate(rs, bean);
                }
            }
        } catch (Exception e) {
            log.error("Database Exception in findByPK", e);
            throw new ApplicationException("Exception: Error in getting College by PK - " + e.getMessage());
        }
        log.debug("Model findByPK End");
        return bean;
    }

    public void update(CollegeBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("Model update Started");
        CollegeBean existing = findByName(bean.getName());
        if (existing != null && existing.getId() != bean.getId()) {
            throw new DuplicateRecordException("College already exists");
        }

        bean.setModifiedDatetime(new Timestamp(System.currentTimeMillis()));

        try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE ST_COLLEGE SET NAME=?,ADDRESS=?,STATE=?,CITY=?,PHONE_NO=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?")) {
                pstmt.setString(1, bean.getName());
                pstmt.setString(2, bean.getAddress());
                pstmt.setString(3, bean.getState());
                pstmt.setString(4, bean.getCity());
                pstmt.setString(5, bean.getPhoneNo());
                pstmt.setString(6, bean.getCreatedBy());
                pstmt.setString(7, bean.getModifiedBy());
                pstmt.setTimestamp(8, bean.getCreatedDatetime());
                pstmt.setTimestamp(9, bean.getModifiedDatetime());
                pstmt.setLong(10, bean.getId());
                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            log.error("Exception in updating college", e);
            throw new ApplicationException("Exception: Error updating college - " + e.getMessage());
        }
        log.debug("Model update End");
    }

    public List<CollegeBean> search(CollegeBean bean) throws ApplicationException {
        return search(bean, 1, 0, null, null);
    }

    public List<CollegeBean> search(CollegeBean bean, int pageNo, int pageSize, String orderBy, String sortOrder)
            throws ApplicationException {
        log.debug("Model search Started");
        List<CollegeBean> list = new ArrayList<>();
        int index = 1;

        StringBuilder sql = new StringBuilder("SELECT * FROM ST_COLLEGE WHERE 1=1");
        if (bean != null) {
            if (bean.getId() > 0) sql.append(" AND ID=?");
            if (bean.getName() != null && !bean.getName().isEmpty()) sql.append(" AND NAME LIKE ?");
            if (bean.getAddress() != null && !bean.getAddress().isEmpty()) sql.append(" AND ADDRESS LIKE ?");
            if (bean.getCity() != null && !bean.getCity().isEmpty()) sql.append(" AND CITY LIKE ?");
        }

        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ").append(orderBy);
            sql.append(" ").append("DESC".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC");
        } else {
            sql.append(" ORDER BY NAME ASC");
        }

        if (pageSize > 0) {
            pageNo = Math.max(1, pageNo);
            int offset = (pageNo - 1) * pageSize;
            sql.append(" LIMIT ").append(offset).append(",").append(pageSize);
        }

        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            if (bean != null) {
                if (bean.getId() > 0) pstmt.setLong(index++, bean.getId());
                if (bean.getName() != null && !bean.getName().isEmpty()) pstmt.setString(index++, bean.getName() + "%");
                if (bean.getAddress() != null && !bean.getAddress().isEmpty()) pstmt.setString(index++, bean.getAddress() + "%");
                if (bean.getCity() != null && !bean.getCity().isEmpty()) pstmt.setString(index++, bean.getCity() + "%");
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CollegeBean cb = new CollegeBean();
                    populate(rs, cb);
                    list.add(cb);
                }
            }
        } catch (Exception e) {
            log.error("Database Exception in search", e);
            throw new ApplicationException("Exception: Error searching college - " + e.getMessage());
        }

        log.debug("Model search End");
        return list;
    }

    public List<CollegeBean> list() throws ApplicationException {
        return list(1, 0, null, null);
    }

    public List<CollegeBean> list(int pageNo, int pageSize, String orderBy, String sortOrder) throws ApplicationException {
        log.debug("Model list Started");
        List<CollegeBean> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM ST_COLLEGE");

        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ").append(orderBy);
            sql.append(" ").append("DESC".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC");
        } else {
            sql.append(" ORDER BY NAME ASC");
        }

        if (pageSize > 0) {
            pageNo = Math.max(1, pageNo);
            int offset = (pageNo - 1) * pageSize;
            sql.append(" LIMIT ").append(offset).append(",").append(pageSize);
        }

        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString());
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                CollegeBean bean = new CollegeBean();
                populate(rs, bean);
                list.add(bean);
            }
        } catch (Exception e) {
            log.error("Database Exception in list", e);
            throw new ApplicationException("Exception: Error listing colleges - " + e.getMessage());
        }

        log.debug("Model list End");
        return list;
    }

    @Override
    public String getTableName() {
        return "ST_COLLEGE";
    }

    private void populate(ResultSet rs, CollegeBean bean) throws SQLException {
        bean.setId(rs.getLong(1));
        bean.setName(rs.getString(2));
        bean.setAddress(rs.getString(3));
        bean.setState(rs.getString(4));
        bean.setCity(rs.getString(5));
        bean.setPhoneNo(rs.getString(6));
        bean.setCreatedBy(rs.getString(7));
        bean.setModifiedBy(rs.getString(8));
        bean.setCreatedDatetime(rs.getTimestamp(9));
        bean.setModifiedDatetime(rs.getTimestamp(10));
    }
}
