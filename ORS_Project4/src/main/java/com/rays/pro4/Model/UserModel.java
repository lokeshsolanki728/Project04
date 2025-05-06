package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.Logger;

import com.rays.pro4.Bean.RoleBean;
import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.DTO.UserDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;import com.rays.pro4.Util.EmailMessage;
import com.rays.pro4.Util.EmailUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.JDBCDataSource;

public class UserModel extends BaseModel {

    private static Logger log = Logger.getLogger(UserModel.class);

    /**
     * Find next PK of User
     *
     * @throws DatabaseException
     */
    public Integer nextPK() throws DatabaseException{
        log.debug("Model nextPK Started");
        Connection conn = null;
        int pk = 0;
        try {
           conn = JDBCDataSource.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_USER");
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pk = rs.getInt(1);
                }
            } catch (Exception e) {
                log.error("Database Exception in : nextPK", e);
                throw new DatabaseException("Exception : Exception in getting PK");
            }
        
         
        } catch (SQLException e) {
            log.error("Database Exception in : nextPK", e);
            throw new DatabaseException("Exception : Exception in getting PK");
        } finally {
            if(conn!=null){
              JDBCDataSource.closeConnection(conn);
            }
           
        }
        log.debug("Model nextPK End");
        return pk + 1;
    }

    /**
     * Add a User
     *
     * @param bean
     * @throws DuplicateRecordException
     * @throws ApplicationException
     */
    public long add(UserBean bean) throws DuplicateRecordException, ApplicationException {
        log.debug("Model add Started");
         Connection conn = null;
        long pk = 0;
        try {
           conn = JDBCDataSource.getConnection();
            pk = nextPK();
            conn.setAutoCommit(false); // Begin transaction
           try( PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_USER VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)")){
                 pstmt.setLong(1, pk);
                pstmt.setString(2, bean.getFirstName());
                pstmt.setString(3, bean.getLastName());
                pstmt.setString(4, bean.getLogin());
                pstmt.setString(5, bean.getPassword());
                pstmt.setDate(6, new java.sql.Date(bean.getDob().getTime()));
                pstmt.setString(7, bean.getMobileNo());
                pstmt.setLong(8, bean.getRoleId());
                pstmt.setString(9, bean.getGender());
                pstmt.setString(10, bean.getCreatedBy());
                pstmt.setString(11, bean.getModifiedBy());
                pstmt.setTimestamp(12, bean.getCreatedDatetime());
                pstmt.setTimestamp(13, bean.getModifiedDatetime());
                pstmt.executeUpdate();
           }
         
            conn.commit(); // End transaction
            pstmt.close();
            updateCreatesInfo(bean);
            sendMail(bean, "User Added", "User Added Successfully");

        } catch (SQLException e) {
            log.error("Database Exception in : add", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception: Exception in add user " + e.getMessage());
        } finally {
           if(conn!=null){
             JDBCDataSource.closeConnection(conn);
           }
        }
        log.debug("Model add End");
        return pk;
    }

    /**
     * Delete a User
     *
     * @param bean
     * @throws ApplicationException
     */
    public void delete(long id) throws ApplicationException {
        log.debug("Model delete Started");
        Connection conn = null;
         try {
             if (findByPK(id) == null) {
                throw new ApplicationException("Record not found");
             }
           conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false); // Begin transaction
           try( PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_USER WHERE ID = ?")){
                pstmt.setLong(1, id);
                pstmt.executeUpdate();
                 conn.commit(); // End transaction


        } catch (Exception e) {
            log.error("Database Exception in : delete", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception : Exception in delete User");
        } finally {
          if(conn!=null){
            JDBCDataSource.closeConnection(conn);
          }
        } catch(SQLException e){
          log.error("Database Exception in : delete", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception : Exception in delete User");
          
        }
        log.debug("Model delete End");
    }

    /**
     * Find User by login
     *
     * @param login
     * @throws ApplicationException
     */
    public UserDTO findByLogin(String login) throws ApplicationException {
        log.debug("Model findByLogin Started");
        Connection conn = null;
         UserDTO dto = null;
        try {
           conn = JDBCDataSource.getConnection();
            dto = new UserDTO();
           try(PreparedStatement pstmt = conn.prepareStatement("SELECT ID, FIRST_NAME, LAST_NAME, LOGIN, PASSWORD, DOB, MOBILE_NO, ROLE_ID, GENDER,CREATED_BY,MODIFIED_BY,CREATED_DATETIME,MODIFIED_DATETIME FROM ST_USER WHERE LOGIN = ?"))
          {  pstmt.setString(1, login);
             try(ResultSet rs = pstmt.executeQuery();){
               if (rs.next()) {
                    populateBean(rs, dto);
                }
             }
          }
        } catch (Exception e) {
            log.error("Database Exception in : findByLogin", e);
            throw new ApplicationException("Exception : Exception in getting User by login");
        } finally {
           if(conn!=null){
               JDBCDataSource.closeConnection(conn);
           }
        }
        log.debug("Model findByLogin End");

      

        return dto;
    }

    /**
     * Find User by PK
     *
     * @param pk
     * @throws ApplicationException
     */
    public UserDTO findByPK(long pk) throws ApplicationException {
        log.debug("Model findByPK Started");
        Connection conn = null;
        UserDTO dto= null;
        try {
          conn = JDBCDataSource.getConnection();
          dto=new UserDTO();
           try(PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ST_USER WHERE ID = ?")){
                pstmt.setLong(1, pk);
                try(ResultSet rs = pstmt.executeQuery()){
                     if (rs.next()) {
                        populateBean(rs, dto);
                     }
                }
            }
        } catch (Exception e) {
            log.error("Database Exception in : findByPK", e);
            throw new ApplicationException("Exception : Exception in getting User by pk");
        } finally {
           JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model findByPK End");
        return dto;
    }

    /**
     * Update a User
     *
     * @param bean
     * @throws DuplicateRecordException
     * @throws ApplicationException
     */
    public void update(UserBean bean) throws DuplicateRecordException, ApplicationException {
        log.debug("Model update Started");
        Connection conn = null;
         try {
           conn = JDBCDataSource.getConnection();
           conn.setAutoCommit(false);
          try( PreparedStatement pstmt = conn.prepareStatement(
                  "UPDATE ST_USER SET FIRST_NAME=?,LAST_NAME=?,LOGIN=?,PASSWORD=?,DOB=?,MOBILE_NO=?,ROLE_ID=?,GENDER=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?")){
                    
                    pstmt.setString(1, bean.getFirstName());
                    pstmt.setString(2, bean.getLastName());
                    pstmt.setString(3, bean.getLogin());
                    pstmt.setString(4, bean.getPassword());
                    pstmt.setDate(5, new java.sql.Date(bean.getDob().getTime()));
                    pstmt.setString(6, bean.getMobileNo());
                    pstmt.setLong(7, bean.getRoleId());
                    pstmt.setString(8, bean.getGender());
                    pstmt.setString(9, bean.getCreatedBy());
                    pstmt.setString(10, bean.getModifiedBy());
                    pstmt.setTimestamp(11, bean.getCreatedDatetime());
                    pstmt.setTimestamp(12, bean.getModifiedDatetime());
                    pstmt.setLong(13, bean.getId());
                    pstmt.executeUpdate();
                    conn.commit();
                    sendMail(bean, "User updated", "User Updated Successfully");
           }
           
          
        } catch (SQLException e) {
            log.error("Database Exception in : update", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception: Exception in update user " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model update End");
    }

    /**
     * Search Users
     *
     * @param bean
     * @throws ApplicationException
     */
    public List search(UserBean bean) throws ApplicationException {
        return search(bean, 0, 0);
    }

    /**
     * Search Users with pagination
     *
     * @param bean
     * @param pageNo
     * @param pageSize
     * @throws ApplicationException
     */
    public List search(UserBean bean, int pageNo, int pageSize) throws ApplicationException {
        log.debug("Model search Started");
        Connection conn = null;
        List<UserDTO> list = new ArrayList<>();
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_USER WHERE 1=1");
        int index = 1;
       try{
            if (bean != null) {
                if (bean.getId() > 0) {
                    sql.append(" AND ID = ?");
                }
                if (bean.getFirstName() != null && !bean.getFirstName().isEmpty()) {
                    sql.append(" AND FIRST_NAME like ?");
                }
                if (bean.getLastName() != null && !bean.getLastName().isEmpty()) {
                    sql.append(" AND LAST_NAME like ?");
                }
                if (bean.getLogin() != null && !bean.getLogin().isEmpty()) {
                    sql.append(" AND LOGIN like ?");
                }
                if (bean.getDob() != null) {
                    sql.append(" AND DOB = ?");
                }
                if (bean.getMobileNo() != null && !bean.getMobileNo().isEmpty()) {
                    sql.append(" AND MOBILE_NO like ?");
                }
                if (bean.getRoleId() > 0) {
                    sql.append(" AND ROLE_ID = ?");
                }
            }

            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                sql.append(" limit " + pageNo + "," + pageSize);
            }

            conn = JDBCDataSource.getConnection();           
             try( PreparedStatement pstmt = conn.prepareStatement(sql.toString())){
                if (bean != null) {
                    if (bean.getId() > 0) {
                        pstmt.setLong(index++, bean.getId());
                    }
                    if (bean.getFirstName() != null && !bean.getFirstName().isEmpty()) {
                        pstmt.setString(index++, bean.getFirstName() + "%");
                    }
                    if (bean.getLastName() != null && !bean.getLastName().isEmpty()) {
                        pstmt.setString(index++, bean.getLastName() + "%");
                    }
                    if (bean.getLogin() != null && !bean.getLogin().isEmpty()) {
                        pstmt.setString(index++, bean.getLogin() + "%");
                    }
                    if (bean.getDob() != null) {
                        pstmt.setDate(index++, new java.sql.Date(bean.getDob().getTime()));
                    }
                    if (bean.getMobileNo() != null && !bean.getMobileNo().isEmpty()) {
                        pstmt.setString(index++, bean.getMobileNo() + "%");
                    }
                    if (bean.getRoleId() > 0) {
                        pstmt.setLong(index++, bean.getRoleId());
                    }
                }
            
                 try(ResultSet rs = pstmt.executeQuery()){
                   while (rs.next()) {
                        UserDTO dto = new UserDTO();
                        populateBean(rs, dto);
                        list.add(dto);
                    }
                 }
             }
        } catch (SQLException e) {
            log.error("Database Exception in : search", e);
            throw new ApplicationException("Exception : Exception in search User");
        } finally {
           if(conn!=null){ JDBCDataSource.closeConnection(conn);}
        }
        log.debug("Model search End");
        return list;
    }

    /**
     * Get List of Users
     *
     * @throws ApplicationException
     */
    public List list() throws ApplicationException {
        return list(1, 0);
    }

    /**
     * Get List of Users with pagination
     *
     * @param pageNo
     * @param pageSize
     * @throws ApplicationException
     */
    public List list(int pageNo, int pageSize) throws ApplicationException {
        log.debug("Model list Started");
        List<UserDTO> list = new ArrayList<>();
        StringBuffer sql = new StringBuffer("select * from ST_USER");
        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT " + pageNo + "," + pageSize);
        }
        Connection conn = null;
        try {            
            conn = JDBCDataSource.getConnection();            
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())){
                try(ResultSet rs = pstmt.executeQuery()){
                    while (rs.next()) {
                        UserDTO dto = new UserDTO();
                         populateBean(rs, dto);
                         list.add(dto);
                    }
                }                
            }
           
        } catch (SQLException e) {
            log.error("Database Exception in : list", e);
            throw new ApplicationException("Exception : Exception in getting list of Users");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model list End");
        return list;
    }

    /**
     * User Authentication
     *
     * @param login
     * @param password
     * @return UserBean
     * @throws ApplicationException
     */
    public UserBean authenticate(String login, String password) throws ApplicationException {
        log.debug("Model authenticate Started");
        Connection conn = null;
        UserBean bean = null; 
        try {
            conn = JDBCDataSource.getConnection();            
           try( PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ST_USER WHERE LOGIN = ? AND PASSWORD = ?")){
                 pstmt.setString(1, login);
                 pstmt.setString(2, password);
                 try(ResultSet rs = pstmt.executeQuery()){
                    if (rs.next()) {
                        bean = new UserBean();
                        populateBean(rs, bean);
                     }
                 }
           }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            log.error("Database Exception in : authenticate", e);
            throw new ApplicationException("Exception : Exception in authenticate User");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model authenticate End");
        return bean;
    }

    /**
     * Get Role List by User
     *
     * @param bean
     * @return list
     * @throws ApplicationException
     */
    public List getRoleList(UserBean bean) throws ApplicationException {
        log.debug("Model getRoleList Started");
        Connection conn = null;
        ArrayList<RoleBean> list = new ArrayList<>();
        try {
            conn = JDBCDataSource.getConnection();
           try ( PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ST_USER WHERE ROLE_ID=?")){
                 pstmt.setLong(1, bean.getRoleId());
                 try(ResultSet rs = pstmt.executeQuery()){
                     while (rs.next()) {
                         RoleBean rolebean = new RoleBean();
                        rolebean.setId(rs.getLong(8));
                         rolebean.setName(rs.getString(9));
                        list.add(rolebean);
                      }
                 }
           }
           
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.error("Database Exception in : getRoleList", e);
            throw new ApplicationException("Exception : Exception in get Role List");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model getRoleList End");
        return list;
    }

    /**
     * Change password of user
     *
     * @param id
     * @param oldPassword
     * @param newPassword
     * @throws ApplicationException
     * @throws Exception
     */
    public void changePassword(Long id, String oldPassword, String newPassword) throws ApplicationException {
        log.debug("Model changePassword Started");
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            UserBean bean = findByPK(id);
            if (bean.getPassword().equals(oldPassword)) {
                conn.setAutoCommit(false); 
                try(PreparedStatement pstmt = conn
                        .prepareStatement("UPDATE ST_USER SET PASSWORD = ? WHERE ID = ?")){
                            pstmt.setString(1, newPassword);
                            pstmt.setLong(2, id);
                            pstmt.executeUpdate();
                            conn.commit(); 
                           sendMail(bean, "Password changed", "Password changed Successfully");
                }
               
               
            } else {
                throw new ApplicationException("Old Password does not matched");
            }
        } catch (SQLException e) {
            log.error("Database Exception in : changePassword", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception : Exception in change Password");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model changePassword End");
    }

    /**
     * User Registration
     *
     * @param bean
     * @return
     * @throws DuplicateRecordException
     * @throws ApplicationException
     */
    public Long registerUser(UserBean bean) throws DuplicateRecordException, ApplicationException {
        log.debug("Model registerUser Started");
        long pk = 0;
        Connection conn = null;
        try {
              UserDTO duplicateUser = findByLogin(bean.getLogin());
                
            if (DataValidator.isNull(bean.getFirstName())) {
               throw new ApplicationException("First Name should not be null");
            } else if (DataValidator.isName(bean.getFirstName())) {
                throw new ApplicationException("First Name should not contain numbers");
            }
            if (DataValidator.isNull(bean.getLastName())) {
                 throw new ApplicationException("Last Name should not be null");
            } else if (DataValidator.isName(bean.getLastName())) {
                throw new ApplicationException("Last Name should not contain numbers");
            }
            if (DataValidator.isNull(bean.getLogin())) {
                 throw new ApplicationException("Login should not be null");
            } else if (!DataValidator.isEmail(bean.getLogin())) {
                throw new ApplicationException("Login should have a valid email format");
            }
            if (DataValidator.isNull(bean.getPassword())) {
               throw new ApplicationException("Password should not be null");
            }
            if (DataValidator.isNull(bean.getMobileNo())) {
                 throw new ApplicationException("Mobile No should not be null");
            }
            if (duplicateUser != null) {
                throw new DuplicateRecordException("Login id already exists");
            }
            conn = JDBCDataSource.getConnection();
            pk = nextPK();
            conn.setAutoCommit(false);
           try( PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_USER VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)")){
                pstmt.setInt(1, (int) pk);
                pstmt.setString(2, bean.getFirstName());
                pstmt.setString(3, bean.getLastName());
                pstmt.setString(4, bean.getLogin());
                pstmt.setString(5, bean.getPassword());
                pstmt.setDate(6, new java.sql.Date(bean.getDob().getTime()));
                pstmt.setString(7, bean.getMobileNo());
                pstmt.setLong(8, 2);
                pstmt.setString(9, bean.getGender());
                pstmt.setString(10, "Self");
                pstmt.setString(11, "Self");
                pstmt.setTimestamp(12, bean.getCreatedDatetime());
                pstmt.setTimestamp(13, bean.getModifiedDatetime());
                pstmt.executeUpdate();
           }
          
            conn.commit();
           
            sendMail(bean, "Registration", "Registration Successfull");
        } catch (SQLException e) {
            log.error("Database Exception in : registerUser", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception : Exception in register User");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model registerUser End");
        return pk;
    }

    /**
     * Reset password of user
     *
     * @param login
     * @return
     * @throws ApplicationException
     * @throws Exception
     */
    public boolean resetPassword(String login) throws ApplicationException {

        log.debug("Model resetPassword Started");
        boolean flag = false;
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            UserDTO dto = findByLogin(login);
            if (dto != null) {
                conn.setAutoCommit(false);
               try( PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_USER SET PASSWORD = ? WHERE LOGIN = ?")){
                    pstmt.setString(1, "12345");
                    pstmt.setString(2, login);
                    int i = pstmt.executeUpdate();
                    conn.commit();
                   sendMail(dto, "Reset Password", "Password reset Successfull");
               }
                pstmt.close();
                sendMail(bean, "Reset Password", "Password reset Successfull");
                flag = true;
            }
        } catch (SQLException e) {
            log.error("Database Exception in : resetPassword", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception : Exception in reset Password");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model resetPassword End");
        return flag;
    }

    /**
     * @param rs
     * @param bean
     * @throws Exception
     */
    private void populateBean(ResultSet rs, UserDTO dto) throws SQLException {
        dto.setId(rs.getLong(1));
        dto.setFirstName(rs.getString(2));
        dto.setLastName(rs.getString(3));
        dto.setLogin(rs.getString(4));
        dto.setPassword(rs.getString(5));
        dto.setDob(rs.getDate(6));
        dto.setMobileNo(rs.getString(7));
        dto.setRoleId(rs.getLong(8));
        dto.setGender(rs.getString(9));
        dto.setCreatedBy(rs.getString(10));
        dto.setModifiedBy(rs.getString(11));
        dto.setCreatedDatetime(rs.getTimestamp(12));
        dto.setModifiedDatetime(rs.getTimestamp(13));
    }

    /**
     * @param bean`
     * @param subject
     * @param body
     * @throws Exception
     */
    private void sendMail(UserBean bean, String subject, String body) throws Exception {
        EmailMessage msg = new EmailMessage();
        msg.setTo(bean.getLogin());
        msg.setSubject(subject);
        msg.setMessage(body);
        EmailUtility.sendMail(msg);
    }
}