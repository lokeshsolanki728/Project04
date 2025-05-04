package com.rays.pro4.Model;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.rays.pro4.Bean.RoleBean;
import com.rays.pro4.DTO.RoleDTO;
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
import java.sql.SQLException;
public class RoleModel extends BaseModel {

    private  long nextPK() throws DatabaseException {
        BaseModel.log.debug("Model nextPK Started");
        long pk = 0;
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_ROLE")) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                pk = rs.getLong(1);
            }
        } catch (SQLException e) {
            BaseModel.log.error("Database Exception in nextPK", e);
            throw new DatabaseException("Exception: Unable to get PK - " + e.getMessage());
        }
        return pk + 1;
    }

    /**
     * add method to add role in database
     * @throws DuplicateRecordException 
     */
    
    public long add(RoleDTO dto) throws ApplicationException, DuplicateRecordException {
        
        long pk = 0;
        RoleDTO duplicateRole = findByName(dto.getName());
        if (duplicateRole != null) {
            throw new DuplicateRecordException("Role Name already exists");
        }

        Connection conn = null;;
        try {
            conn=JDBCDataSource.getConnection();
             conn.setAutoCommit(false);
                pk = nextPK();
                dto.setId(pk);
                dto.setCreatedDatetime(new java.sql.Timestamp(new java.util.Date().getTime()));
                dto.setModifiedDatetime(new java.sql.Timestamp(new java.util.Date().getTime()));
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_ROLE VALUES(?,?,?,?,?,?,?)")) {
                    pstmt.setString(2, dto.getName());
                    pstmt.setString(3, dto.getDescription());
                    pstmt.setString(4, dto.getCreatedBy());
                    pstmt.setString(5, dto.getModifiedBy());
                    pstmt.setTimestamp(6, dto.getCreatedDatetime());
                    pstmt.setTimestamp(7, dto.getModifiedDatetime());
                    pstmt.executeUpdate();
                conn.commit();
            }
        } catch (Exception e) {
            BaseModel.log.error("Database Exception in add", e);
            JDBCDataSource.trnRollback();
            throw new ApplicationException("Exception: Exception in add Role - " + e.getMessage());
        }finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }
    
    public void delete(RoleDTO bean) throws ApplicationException {
         if(findByPK(bean.getId())==null){
            throw new ApplicationException("Record not found");
        }
        Connection conn = null;
        try {
             conn=JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_ROLE WHERE ID=?")) {
                pstmt.setLong(1, bean.getId());
                pstmt.executeUpdate();
                conn.commit();
            }

        } catch (Exception e) {
            BaseModel.log.error("Database Exception in delete", e);
            JDBCDataSource.trnRollback();
           throw new ApplicationException("Exception : Exception in delete Role - " + e.getMessage());
        }finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public RoleDTO findByName(String name) throws ApplicationException {
        BaseModel.log.debug("Model findByName Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_ROLE WHERE NAME=?");

        RoleDTO dto = null;
        try (Connection conn = JDBCDataSource.getConnection();PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setString(1,name);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                dto=new RoleDTO();
                    populateBean(rs, dto);
               }
            }
        } catch (SQLException e) {
            BaseModel.log.error("Database Exception in findByName", e);
            throw new ApplicationException("Exception: Exception in getting Role by name - " + e.getMessage());
        } 
        return bean;
    


     * @param pk
     * @return RoleBean
     * @throws ApplicationException
     */
    public RoleDTO findByPK(long pk) throws ApplicationException {
        BaseModel.log.debug("Model findByPK Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_ROLE WHERE ID=?");
        RoleDTO dto = null;
        try (Connection conn = JDBCDataSource.getConnection();PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setLong(1,pk);
                ResultSet rs=pstmt.executeQuery();
                while(rs.next()) {
                dto=new RoleDTO();
                    populateBean(rs, dto);
                }
           
        } catch (SQLException e) {
            BaseModel.log.error("Database Exception in findByPK", e);
            throw new ApplicationException("Exception: Exception in getting Role by pk - " + e.getMessage());
        }
        BaseModel.log.debug("Model findByPK End");
        return dto;
    }

    /**
     * update method to update the role
     *
     * @param bean
     * @throws ApplicationException
     * @throws DuplicateRecordException
     */
    public void update(RoleDTO dto) throws ApplicationException, DuplicateRecordException {
        BaseModel.log.debug("Model update Started");
        RoleDTO duplicateRole = findByName(dto.getName());
        if (duplicateRole != null && duplicateRole.getId() != dto.getId()) {
            throw new DuplicateRecordException("Role Name already exists");
        }
         Connection conn=null;
        try {
             conn=JDBCDataSource.getConnection();
             conn.setAutoCommit(false);
           try (PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE ST_ROLE SET NAME=?,DESCRIPTION=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?")) {
                 pstmt.setString(1, dto.getName());
                pstmt.setString(2, dto.getDescription());
                pstmt.setString(3, dto.getCreatedBy());
                pstmt.setString(4, dto.getModifiedBy());
                pstmt.setTimestamp(5, dto.getCreatedDatetime());
                pstmt.setTimestamp(6, dto.getModifiedDatetime());
                pstmt.setLong(7, dto.getId());
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (Exception e) {
            BaseModel.log.error("Database Exception in update", e);
            JDBCDataSource.trnRollback();
            throw new ApplicationException("Exception in updating Role - " + e.getMessage());
        }finally {
            JDBCDataSource.closeConnection(conn);
        }
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
    public List<RoleDTO> search(RoleBean bean, int pageNo, int pageSize) throws ApplicationException {
        BaseModel.log.debug("Model search Started");
        StringBuilder sql = new StringBuilder("SELECT * FROM ST_ROLE WHERE 1=1");
        ArrayList<RoleBean> list = new ArrayList<>();
        int paramCount = 1;

        if (pageNo < 0) {
            pageNo = 1;
        }
        if (pageSize < 0) {
            pageSize = 10;
        }

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" AND id = ?");
            }
            if (bean.getName() != null && !bean.getName().isEmpty()) {
                sql.append(" AND NAME like ?");
             }  if (bean.getDescription() != null && !bean.getDescription().isEmpty()) {
                sql.append(" AND DESCRIPTION like ?");
            }
        }
        
        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT ?, ?");
        }

        try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            if (bean != null) {
                if (bean.getId() > 0) {pstmt.setLong(paramCount++, bean.getId());}
                if (bean.getName() != null && !bean.getName().isEmpty()) {pstmt.setString(paramCount++, bean.getName() + "%");}
                if (bean.getDescription() != null && !bean.getDescription().isEmpty()) {pstmt.setString(paramCount++, bean.getDescription() + "%");}
            }
            if (pageSize > 0) {pstmt.setInt(paramCount++, pageNo);pstmt.setInt(paramCount++, pageSize);}
             ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                RoleDTO roleDTO=new RoleDTO();
                populateBean(rs,roleDTO);       
                    list.add(roleDTO);                           
            }
            rs.close();
            pstmt.close();
            }

        } catch (SQLException e) {
            BaseModel.log.error("Database Exception in search", e);
            throw new ApplicationException("Exception: Exception in search Role - " + e.getMessage());
        }
        BaseModel.log.debug("Model search End");
        return list;
    }

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
    public List<RoleDTO> list(int pageNo, int pageSize) throws ApplicationException {
        BaseModel.log.debug("Model list Started");
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
            sql.append(" LIMIT ?, ?");
        }

        try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            if (pageSize > 0) {pstmt.setInt(1, pageNo);pstmt.setInt(2, pageSize);}
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {  RoleDTO dto=new RoleDTO();                  
                    populateBean(rs,dto);                    
                    list.add(dto);
                }
                 rs.close();
                 pstmt.close();
            }
        } catch (Exception e) {
             BaseModel.log.error("Database Exception in list", e);
            throw new ApplicationException("Exception: Exception in getting list of Role - " + e.getMessage());
        }
        BaseModel.log.debug("Model list End");
        return list;
    }
     @Override
    public String getTableName() {
        return "ST_ROLE";
    }

    private void populateBean(ResultSet rs, RoleDTO dto) throws SQLException {
        dto.setId(rs.getLong(1));
        dto.setName(rs.getString(2));
        dto.setDescription(rs.getString(3));
        dto.setCreatedBy(rs.getString(4));
        dto.setModifiedBy(rs.getString(5));
        dto.setCreatedDatetime(rs.getTimestamp(6));
        dto.setModifiedDatetime(rs.getTimestamp(7));
    }
}