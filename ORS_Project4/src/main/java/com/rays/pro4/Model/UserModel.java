package com.rays.pro4.Model;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Date;

import org.apache.log4j.Logger;

import org.mindrot.jbcrypt.BCrypt;
import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.DTO.UserDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;import com.rays.pro4.Util.EmailMessage;
import com.rays.pro4.Util.HTMLUtility;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.EmailUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.JDBCDataSource;
import com.rays.pro4.Bean.RoleBean;

public class UserModel extends BaseModel {

    private static Logger log = Logger.getLogger(UserModel.class);
    
     /**
     * @param password
     * @return
     */
     private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Find next PK of User
     *
     * @throws DatabaseException
     */
    public long nextPK() throws DatabaseException{
        log.debug("Model nextPK Started");
        Connection conn = null;
          long pk = 0;
        try (Connection connection = JDBCDataSource.getConnection(); PreparedStatement pstmt = connection.prepareStatement("SELECT MAX(ID) FROM ST_USER"); ResultSet rs = pstmt.executeQuery()) {
            rs.next();

            pk = rs.getInt(1);
        } catch (SQLException e) {
            log.error("Database Exception in : nextPK", e);
            throw new DatabaseException("Exception : Exception in getting PK");
            }
        log.debug("Model nextPK End");
        return pk + 1;
    }

    /**
     * Add a User
     *
     * @param bean
     * @throws DuplicateRecordException
     */
    public long add(UserDTO dto) throws DuplicateRecordException, ApplicationException {
        log.debug("Model add Started");
         Connection conn = null;
         long pk = 0;
        try {
             conn = JDBCDataSource.getConnection();
             UserDTO duplicateUser = findByLogin(dto.getLogin());
             if (duplicateUser != null) {
                 throw new DuplicateRecordException("Login id already exists");
             }
            dto.setPassword(hashPassword(dto.getPassword()));
            pk = nextPK();
            dto.setId(pk);
             conn.setAutoCommit(false); // Begin transaction
             try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_USER VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
                 pstmt.setLong(1, pk);
                 pstmt.setString(2, dto.getFirstName());

                 pstmt.setString(3, dto.getLastName());
                 pstmt.setString(4, dto.getLogin());
                 pstmt.setString(5, dto.getPassword());
                 pstmt.setDate(6, new java.sql.Date(dto.getDob().getTime()));
                 pstmt.setString(7, dto.getMobileNo());
                 pstmt.setLong(8, dto.getRoleId());
                 pstmt.setString(9, dto.getGender());
                 updateCreatesInfo(dto,false);
                 pstmt.setString(10, dto.getCreatedBy());               
                 pstmt.setString(11, dto.getModifiedBy());               
                 pstmt.setTimestamp(12, dto.getCreatedDatetime());               
                 pstmt.setTimestamp(13, dto.getModifiedDatetime());

                pstmt.executeUpdate();

           }
           conn.commit(); // End transaction
            sendMail(dto, "User Added", "User Added Successfully");
        } catch (SQLException e) {
            log.error("Database Exception in : add", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception : Exception in add user " + e.getMessage());
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
              conn=JDBCDataSource.getConnection();
            if (findByPK(id) == null) {
                throw new ApplicationException("Record not found");
            }
            conn.setAutoCommit(false);
              try(PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_USER WHERE ID = ?")){
                  pstmt.setLong(1, id);
                  conn.commit();
                  pstmt.executeUpdate();
              }
         } catch (SQLException e) {
            log.error("Database Exception in : delete", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception : Exception in delete User");
        } finally {
           JDBCDataSource.closeConnection(conn);
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
        UserDTO dto = new UserDTO();
        String sql = "SELECT ID, FIRST_NAME, LAST_NAME, LOGIN, PASSWORD, DOB, MOBILE_NO, ROLE_ID, GENDER, CREATED_BY, MODIFIED_BY, CREATED_DATETIME, MODIFIED_DATETIME FROM ST_USER WHERE LOGIN = ?";
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    populateBean(rs, dto);
                    return dto; // Return the populated DTO
                }
            }
        } catch (SQLException e) {
            log.error("Database Exception in findByLogin: ", e);
            throw new ApplicationException("Exception: Error finding user by login");
        }
        log.debug("Model findByLogin End");
        return null; // Return null if no user found
    }


/**
     * Find User by PK
     *
     * @param pk
     * @throws ApplicationException
     */
    public UserDTO findByPK(long pk) throws ApplicationException {
        log.debug("Model findByPK Started");
        UserDTO dto = null;
        String sql = "SELECT * FROM ST_USER WHERE ID = ?";
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, pk);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    dto = new UserDTO();
                    populateBean(rs, dto);
                }
            }
        } catch (SQLException e) {
            log.error("Database Exception in : findByPK", e);
            throw new ApplicationException("Exception : Exception in getting User by pk");
        }
        log.debug("Model findByPK End");
         return null;
    }

    /**
     * Update a User
     *
     * @param bean
     * @throws DuplicateRecordException
     * @throws ApplicationException
     */
    public void update(UserDTO dto) throws DuplicateRecordException, ApplicationException {
        log.debug("Model update Started");
        
        try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false);

            StringBuilder sql = new StringBuilder("UPDATE ST_USER SET FIRST_NAME=?,LAST_NAME=?,LOGIN=?,");
            sql.append("PASSWORD=?,");
            sql.append("DOB=?,MOBILE_NO=?,ROLE_ID=?,GENDER=?,MODIFIED_BY=?,MODIFIED_DATETIME=? WHERE ID=?");
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                int index = 1;
                pstmt.setString(index++, dto.getFirstName());
                pstmt.setString(index++, dto.getLastName());
                pstmt.setString(index++, dto.getLogin());
                pstmt.setString(index++, hashPassword(dto.getPassword()));

                pstmt.setDate(index++, new java.sql.Date(dto.getDob().getTime()));
                pstmt.setString(index++, dto.getMobileNo());
                pstmt.setLong(index++, dto.getRoleId());
                pstmt.setString(index++, dto.getGender());
                pstmt.setString(index++, dto.getModifiedBy());
                pstmt.setTimestamp(index++, dto.getModifiedDatetime());
                pstmt.setLong(index, dto.getId());
                pstmt.executeUpdate();
                 conn.commit();
                sendMail(dto, "User updated", "User Updated Successfully");
            }
        } catch (SQLException e) {
            log.error("Database Exception in : update", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception : Exception in update user " + e.getMessage());
        }finally{
        log.debug("Model update End");
    }

    /**
     * Search Users
     *
     * @param bean
     * @throws ApplicationException
     */
    public List search(UserDTO bean) throws ApplicationException {
        return search(bean, 0, 0);
    }

    /**
     * Search Users with pagination
     *
     * @param bean
     * @param pageNo
     * @param pageSize
     * @throws ApplicationException
     **/
    public List search(UserDTO bean, int pageNo, int pageSize) throws ApplicationException {
        log.debug("Model search Started");
        Connection conn = null;
        List<UserDTO> list = new ArrayList<>();
        StringBuffer sql = new StringBuffer("SELECT ST_USER.ID, FIRST_NAME, LAST_NAME, LOGIN, PASSWORD, DOB, MOBILE_NO, ROLE_ID, GENDER, CREATED_BY, MODIFIED_BY, CREATED_DATETIME, MODIFIED_DATETIME, ST_ROLE.NAME AS ROLE_NAME FROM ST_USER INNER JOIN ST_ROLE ON ST_USER.ROLE_ID = ST_ROLE.ID WHERE 1=1");

        int index = 1;
        try {
            conn = JDBCDataSource.getConnection();
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

            if (pageSize > 0) { // Add parameters for pagination
                sql.append(" LIMIT ?, ?");
            }
            
            
            
             
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
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
                if(pageSize > 0) {
                    
                    pstmt.setInt(index++, (pageNo - 1) * pageSize);
                    pstmt.setInt(index, pageSize);
                }
                 try (ResultSet rs = pstmt.executeQuery()) {
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
           JDBCDataSource.closeConnection(conn);
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
            sql.append(" LIMIT ?, ?");
        }
        int index = 1;

        try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
           if (pageSize > 0){
                     pstmt.setInt(index++, (pageNo - 1) * pageSize);
                   pstmt.setInt(index, pageSize);
                }
             try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    UserDTO dto = new UserDTO(); 
                    populateBean(rs, dto);

                    list.add(dto);
                    
                }
                 if (pageSize > 0){
                     pstmt.setInt(1, (pageNo - 1) * pageSize);
                }           }
        } catch (SQLException e) {            log.error("Database Exception in : list", e);
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
    public UserDTO authenticate(String login, String password) throws ApplicationException {
        log.debug("Model authenticate Started");
        UserDTO dto = null;
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT ID, FIRST_NAME, LAST_NAME, LOGIN, PASSWORD, DOB, MOBILE_NO, ROLE_ID, GENDER, CREATED_BY, MODIFIED_BY, CREATED_DATETIME, MODIFIED_DATETIME FROM ST_USER WHERE LOGIN = ?")) {
            pstmt.setString(1, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    dto = new UserDTO();
                    populateBean(rs, dto);
                    if (dto != null) {
                        // Use BCrypt to compare the entered password with the stored hash
                         if (BCrypt.checkpw(password, dto.getPassword())) {
                           return dto;
                       }
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Database Exception in : authenticate", e);
            throw new ApplicationException("Exception : Exception in authenticate User");
        }
        log.debug("Model authenticate End");
        return dto;
    }

  /**
     * Get Role List by User 
     *
     * @param bean
     * @return list
     * @throws ApplicationException
     */
    public List getRoleList() throws ApplicationException { // Ignoring UserBean parameter
        log.debug("Model getRoleList Started");
        Connection conn = null;
          List<RoleBean> list = new ArrayList<>();
        try (Connection conn = JDBCDataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT ID, NAME FROM ST_ROLE")) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RoleBean roleBean = new RoleBean();
                    roleBean.setId(rs.getLong("ID"));
                    roleBean.setName(rs.getString("NAME"));
                    list.add(roleBean);
                    }
           
           
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
        try (Connection conn = JDBCDataSource.getConnection();) {
            UserDTO dto = findByPK(id);
            if (dto != null && BCrypt.checkpw(oldPassword, dto.getPassword())) {

                conn.setAutoCommit(false);
                try (PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_USER SET PASSWORD = ? WHERE ID = ?")) {

                    pstmt.setString(1, hashPassword(newPassword));
                    pstmt.setLong(2, id);
                    pstmt.executeUpdate();
                    conn.commit();
                            sendMail(dto, "Password changed", "Password changed Successfully");
                }
            } else {
                throw new ApplicationException("Old Password does not matched");
            }
        } finally {
                log.debug("Model changePassword End");
        } catch (SQLException e) {
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception : Exception in change Password");
        }
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model changePassword End");
    

    /**
     * User Registration
     *
     * @param bean
     * @return
     * @throws DuplicateRecordException
     * @throws ApplicationException
     */
    public Long registerUser(UserDTO dto) throws DuplicateRecordException, ApplicationException {
        log.debug("Model registerUser Started"); 
        long pk = 0;        
        try (Connection conn = JDBCDataSource.getConnection()) {
            if (findByLogin(dto.getLogin()) != null) {
                throw new DuplicateRecordException("Login id already exists");
            }
            dto.setPassword(hashPassword(dto.getPassword()));
            pk = nextPK();
            dto.setId(pk);
            conn.setAutoCommit(false);
            updateCreatesInfo(dto, true);
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_USER VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
                pstmt.setLong(1, pk);
                pstmt.setString(2, dto.getFirstName());
                pstmt.setString(3, dto.getLastName());
                pstmt.setString(4, dto.getLogin());
                pstmt.setString(5, dto.getPassword());
                pstmt.setDate(6, new java.sql.Date(dto.getDob().getTime()));
                pstmt.setString(7, dto.getMobileNo());
                pstmt.setLong(8, dto.getRoleId());
                pstmt.setString(9, dto.getGender());
                pstmt.setString(10, dto.getCreatedBy());
                pstmt.setString(11, dto.getModifiedBy());
                pstmt.setTimestamp(12, dto.getCreatedDatetime());
                pstmt.setTimestamp(13, dto.getModifiedDatetime());
                pstmt.executeUpdate();
                conn.commit();
            }
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
         try (Connection conn = JDBCDataSource.getConnection();) {
              UserDTO dto = findByLogin(login);
             if (dto != null) {
                  conn.setAutoCommit(false);
                  try (PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_USER SET PASSWORD = ? WHERE LOGIN = ?")) {
                        String newPassword = generateRandomPassword();
                        pstmt.setString(1, hashPassword(newPassword));
                         pstmt.setString(2, login);
                         pstmt.executeUpdate();

               }
                 flag = true;
            }
        } catch (SQLException e) {
            log.error("Database Exception in : resetPassword", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception : Exception in reset Password");

        }
        log.debug("Model resetPassword End");
        return flag;
    }
    
    /**
     * @param rs
     * @param dto
     * @throws SQLException
     */
    private void populateBean(ResultSet rs, UserDTO dto) throws SQLException {
          try {
             rs.findColumn("ROLE_NAME"); // Check if "ROLE_NAME" column exists
            dto.setRoleName(rs.getString("ROLE_NAME"));
        } catch (SQLException ignored) {}
        dto.setId(rs.getLong("ID"));
        dto.setFirstName(rs.getString("FIRST_NAME"));
        dto.setLastName(rs.getString("LAST_NAME")); 
        dto.setLogin(rs.getString("LOGIN"));
        dto.setPassword(rs.getString("PASSWORD"));
        dto.setDob(rs.getDate("DOB"));
        dto.setMobileNo(rs.getString("MOBILE_NO"));
        dto.setRoleId(rs.getLong("ROLE_ID"));
        dto.setGender(rs.getString("GENDER"));
        dto.setCreatedBy(rs.getString("CREATED_BY"));
        dto.setModifiedBy(rs.getString("MODIFIED_BY"));
        dto.setCreatedDatetime(rs.getTimestamp("CREATED_DATETIME"));
        dto.setModifiedDatetime(rs.getTimestamp("MODIFIED_DATETIME"));

      }

    /**
     * @param dto`
     * @param subject
     * @param body
     * @throws Exception
     */
    private void sendMail(UserDTO dto, String subject, String body) throws Exception {
        EmailMessage msg = new EmailMessage();
        msg.setTo(dto.getLogin());
        msg.setSubject(subject);
        msg.setMessage(body);
        EmailUtility.sendMail(msg);
    }
       private void updateCreatesInfo(UserDTO dto, boolean isNew) {
         if (dto != null) {
             if (isNew) {
              dto.setCreatedBy(dto.getLogin());
              dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
             }
                dto.setModifiedBy(dto.getLogin());
                dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
           
        
            }
        }
    }
    /**
     * @return
     */
    private static String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
}
        } catch (SQLException e) {
           JDBCDataSource.trnRollback(conn);
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
    public void update(UserDTO dto) throws DuplicateRecordException, ApplicationException {
        log.debug("Model update Started");
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            StringBuilder sql = new StringBuilder("UPDATE ST_USER SET FIRST_NAME=?,LAST_NAME=?,LOGIN=?,");
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                sql.append("PASSWORD=?,");
            }
            sql.append("DOB=?,MOBILE_NO=?,ROLE_ID=?,GENDER=?,MODIFIED_BY=?,MODIFIED_DATETIME=? WHERE ID=?");

            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                int index = 1;
                pstmt.setString(index++, dto.getFirstName());
                pstmt.setString(index++, dto.getLastName());
                pstmt.setString(index++, dto.getLogin());

                if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                    pstmt.setString(index++, hashPassword(dto.getPassword()));
                }
                pstmt.setDate(index++, new java.sql.Date(dto.getDob().getTime()));
                pstmt.setString(index++, dto.getMobileNo());
                pstmt.setLong(index++, dto.getRoleId());
                pstmt.setString(index++, dto.getGender());
                pstmt.setString(index++, dto.getModifiedBy());
                pstmt.setTimestamp(index++, dto.getModifiedDatetime());
                pstmt.setLong(index++, dto.getId());
                pstmt.executeUpdate();
                conn.commit();
                sendMail(dto, "User updated", "User Updated Successfully");
            }
        } catch (SQLException e) {
            log.error("Database Exception in : update", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception : Exception in update user " + e.getMessage());
        }finally{
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
    public List search(UserDTO bean) throws ApplicationException {
        return search(bean, 0, 0);
    }

    /**
     * Search Users with pagination
     *
     * @param bean
     * @param pageNo
     * @param pageSize
     * @throws ApplicationException
     **/
    public List search(UserDTO bean, int pageNo, int pageSize) throws ApplicationException {
        log.debug("Model search Started");
        Connection conn = null;
        List<UserDTO> list = new ArrayList<>();
        StringBuffer sql = new StringBuffer("SELECT ST_USER.ID, FIRST_NAME, LAST_NAME, LOGIN, PASSWORD, DOB, MOBILE_NO, ROLE_ID, GENDER, CREATED_BY, MODIFIED_BY, CREATED_DATETIME, MODIFIED_DATETIME, ST_ROLE.NAME AS ROLE_NAME FROM ST_USER INNER JOIN ST_ROLE ON ST_USER.ROLE_ID = ST_ROLE.ID WHERE 1=1");

        int index = 1;
        try {
            conn = JDBCDataSource.getConnection();
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
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
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
                 try (ResultSet rs = pstmt.executeQuery()) {
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
           JDBCDataSource.closeConnection(conn);
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

            sql.append("  LIMIT " + pageNo + "," + pageSize);

        }

        try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    UserDTO dto = new UserDTO();
                    populateBean(rs, dto);

                    list.add(dto);
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
    public UserDTO authenticate(String login, String password) throws ApplicationException {
        log.debug("Model authenticate Started");
        UserDTO dto = null;
        try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement("SELECT ID, FIRST_NAME, LAST_NAME, LOGIN, PASSWORD, DOB, MOBILE_NO, ROLE_ID, GENDER, CREATED_BY, MODIFIED_BY, CREATED_DATETIME, MODIFIED_DATETIME FROM ST_USER WHERE LOGIN = ?")) {
            pstmt.setString(1, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    dto = new UserDTO();
                    populateBean(rs, dto);
                    if (dto != null && BCrypt.checkpw(password, dto.getPassword())) {
                        return dto;
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Database Exception in : authenticate", e);
           throw new ApplicationException("Exception : Exception in authenticate User");        } 
        log.debug("Model authenticate End");
        return dto;
    }

    /**
     * Get Role List by User 
     *
     * @param bean
     * @return list
     * @throws ApplicationException
     */
    public List getRoleList() throws ApplicationException { // Ignoring UserBean parameter
        log.debug("Model getRoleList Started");
        Connection conn = null;
          List<RoleBean> list = new ArrayList<>();
        try (Connection conn = JDBCDataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT ID, NAME FROM ST_ROLE")) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RoleBean roleBean = new RoleBean();
                    roleBean.setId(rs.getLong("ID"));
                    roleBean.setName(rs.getString("NAME"));
                    list.add(roleBean);
                    }
           
           
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
        try (Connection conn = JDBCDataSource.getConnection()) {
            UserDTO dto = findByPK(id);
            if (dto != null && BCrypt.checkpw(oldPassword, dto.getPassword())) {

                conn.setAutoCommit(false);

                try (PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_USER SET PASSWORD = ? WHERE ID = ?")) {

                    pstmt.setString(1, hashPassword(newPassword));

                    pstmt.setLong(2, id);
                    pstmt.executeUpdate();

                    conn.commit();
                            sendMail(dto, "Password changed", "Password changed Successfully");
                }
            } else {
                throw new ApplicationException("Old Password does not matched");
            }
        } catch (SQLException e) {
            log.error("Database Exception in : changePassword", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception : Exception in change Password");
        }
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
    public Long registerUser(UserDTO dto) throws DuplicateRecordException, ApplicationException {
        log.debug("Model registerUser Started"); 
        long pk = 0;        
        try (Connection conn = JDBCDataSource.getConnection()) {
            if (findByLogin(dto.getLogin()) != null) {
                throw new DuplicateRecordException("Login id already exists");
            }
            dto.setPassword(hashPassword(dto.getPassword()));
            pk = nextPK();
            dto.setId(pk);
            conn.setAutoCommit(false);
            updateCreatesInfo(dto, true);
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_USER VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
                pstmt.setLong(1, pk);
                pstmt.setString(2, dto.getFirstName());
                pstmt.setString(3, dto.getLastName());
                pstmt.setString(4, dto.getLogin());
                pstmt.setString(5, dto.getPassword());
                pstmt.setDate(6, new java.sql.Date(dto.getDob().getTime()));
                pstmt.setString(7, dto.getMobileNo());
                pstmt.setLong(8, dto.getRoleId());
                pstmt.setString(9, dto.getGender());
                pstmt.setString(10, dto.getCreatedBy());
                pstmt.setString(11, dto.getModifiedBy());
                pstmt.setTimestamp(12, dto.getCreatedDatetime());
                pstmt.setTimestamp(13, dto.getModifiedDatetime());
                pstmt.executeUpdate();
                conn.commit();
            }
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
        try (Connection conn = JDBCDataSource.getConnection()) {
            UserDTO dto = findByLogin(login);
            if (dto!= null) {
               conn.setAutoCommit(false);
                try (PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_USER SET PASSWORD = ? WHERE LOGIN = ?")) {
                    String newPassword = generateRandomPassword();
                    pstmt.setString(1, hashPassword(newPassword));
                    pstmt.setString(2, login);
                    pstmt.executeUpdate();

                    conn.commit();
                    sendMail(dto, "Reset Password", "Your new password is: " + newPassword);

               }
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
     * @param dto
     * @throws SQLException
     */
    private void populateBean(ResultSet rs, UserDTO dto) throws SQLException {
          try {
             rs.findColumn("ROLE_NAME"); // Check if "ROLE_NAME" column exists
            dto.setRoleName(rs.getString("ROLE_NAME"));
        } catch (SQLException ignored) {}
        dto.setId(rs.getLong("ID"));
        dto.setFirstName(rs.getString("FIRST_NAME"));
        dto.setLastName(rs.getString("LAST_NAME")); 
        dto.setLogin(rs.getString("LOGIN"));
        dto.setPassword(rs.getString("PASSWORD"));
        dto.setDob(rs.getDate("DOB"));
        dto.setMobileNo(rs.getString("MOBILE_NO"));
        dto.setRoleId(rs.getLong("ROLE_ID"));
        dto.setGender(rs.getString("GENDER"));
        dto.setCreatedBy(rs.getString("CREATED_BY"));
        dto.setModifiedBy(rs.getString("MODIFIED_BY"));
        dto.setCreatedDatetime(rs.getTimestamp("CREATED_DATETIME"));
        dto.setModifiedDatetime(rs.getTimestamp("MODIFIED_DATETIME"));

      }

    /**
     * @param dto`
     * @param subject
     * @param body
     * @throws Exception
     */
    private void sendMail(UserDTO dto, String subject, String body) throws Exception {
        EmailMessage msg = new EmailMessage();
        msg.setTo(dto.getLogin());
        msg.setSubject(subject);
        msg.setMessage(body);
        EmailUtility.sendMail(msg);
    }
       private void updateCreatesInfo(UserDTO dto, boolean isNew) {
         if (dto != null) {
             if (isNew) {
              dto.setCreatedBy(dto.getLogin());
              dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
             }
                dto.setModifiedBy(dto.getLogin());
                dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
           
        
        
        }
    }
    /**
     * @return
     */
    private static String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
}
            dto = new UserDTO();
           try(PreparedStatement pstmt = conn.prepareStatement("SELECT ID, FIRST_NAME, LAST_NAME, LOGIN, PASSWORD, DOB, MOBILE_NO, ROLE_ID, GENDER, CREATED_BY, MODIFIED_BY, CREATED_DATETIME, MODIFIED_DATETIME FROM ST_USER WHERE LOGIN = ?"))
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
    public void update(UserDTO dto) throws DuplicateRecordException, ApplicationException {
        log.debug("Model update Started");
        Connection conn = null;
         try {
           conn = JDBCDataSource.getConnection();
           conn.setAutoCommit(false);
           
           StringBuilder sql = new StringBuilder("UPDATE ST_USER SET FIRST_NAME=?,LAST_NAME=?,LOGIN=?,");
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                sql.append("PASSWORD=?,");
            }
            sql.append("DOB=?,MOBILE_NO=?,ROLE_ID=?,GENDER=?,MODIFIED_BY=?,MODIFIED_DATETIME=? WHERE ID=?");

          try( PreparedStatement pstmt = conn.prepareStatement(sql.toString())){
                 int index = 1;
                 pstmt.setString(index++, dto.getFirstName());
                 pstmt.setString(index++, dto.getLastName());
                 pstmt.setString(index++, dto.getLogin());
                 
                 if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                  pstmt.setString(index++, dto.getPassword());
                 }
                 pstmt.setDate(index++, new java.sql.Date(dto.getDob().getTime()));
                 pstmt.setString(index++, dto.getMobileNo());
                 pstmt.setLong(index++, dto.getRoleId());
                 pstmt.setString(index++, dto.getGender());
                 pstmt.setString(index++, dto.getModifiedBy());
                 pstmt.setTimestamp(index++, dto.getModifiedDatetime()); 
                 pstmt.setLong(index++, dto.getId());
                    pstmt.executeUpdate();
                    conn.commit();
                    sendMail(dto, "User updated", "User Updated Successfully");
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
    public List search(UserDTO bean) throws ApplicationException {
        return search(bean, 0, 0);
    }

    /**
     * Search Users with pagination
     *
     * @param bean
     * @param pageNo
     * @param pageSize
     * @throws ApplicationException
     **/
    public List search(UserDTO bean, int pageNo, int pageSize) throws ApplicationException {
        log.debug("Model search Started");
        Connection conn = null;
        List<UserDTO> list = new ArrayList<>();
        StringBuffer sql = new StringBuffer("SELECT ST_USER.ID, FIRST_NAME, LAST_NAME, LOGIN, PASSWORD, DOB, MOBILE_NO, ROLE_ID, GENDER, CREATED_BY, MODIFIED_BY, CREATED_DATETIME, MODIFIED_DATETIME, ST_ROLE.NAME AS ROLE_NAME FROM ST_USER INNER JOIN ST_ROLE ON ST_USER.ROLE_ID = ST_ROLE.ID WHERE 1=1");

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
            throw new ApplicationException("Exception : Exception in search User"+e.getMessage());
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
           
            sql.append("  LIMIT " + pageNo + "," + pageSize);
          
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
    public UserDTO authenticate(String login, String password) throws ApplicationException {
        log.debug("Model authenticate Started");
        Connection conn = null;
        UserDTO dto = null; 
        try {
            conn = JDBCDataSource.getConnection();            
           try( PreparedStatement pstmt = conn.prepareStatement("SELECT ID, FIRST_NAME, LAST_NAME, LOGIN, PASSWORD, DOB, MOBILE_NO, ROLE_ID, GENDER, CREATED_BY, MODIFIED_BY, CREATED_DATETIME, MODIFIED_DATETIME FROM ST_USER WHERE LOGIN = ? AND PASSWORD = ?")){
                 pstmt.setString(1, login);
                 pstmt.setString(2, password);
                 try(ResultSet rs = pstmt.executeQuery()){
                    if (rs.next()) {
                         dto = new UserDTO();
                        populateBean(rs, dto);
                     }
                 }
           }
           
        } catch (Exception e) {
            log.error("Database Exception in : authenticate", e);
            throw new ApplicationException("Exception : Exception in authenticate User");
        }finally{
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model authenticate End");
        return dto;
    }

    /**
     * Get Role List by User 
     *
     * @param bean
     * @return list
     * @throws ApplicationException
     */
    public List getRoleList() throws ApplicationException { // Ignoring UserBean parameter
        log.debug("Model getRoleList Started");
        Connection conn = null;
        List<RoleBean> list = new ArrayList<>();
        try {
            conn = JDBCDataSource.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT ID, NAME FROM ST_ROLE")) {
                 try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        RoleBean roleBean = new RoleBean();
                        roleBean.setId(rs.getLong("ID"));
                        roleBean.setName(rs.getString("NAME"));
                        list.add(roleBean);
                    }
                 }
           }
           
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
            UserDTO dto = findByPK(id);
            
            if (dto.getPassword().equals(oldPassword)) {
                  conn.setAutoCommit(false); 
                try(PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_USER SET PASSWORD = ? WHERE ID = ?")){
                   
                    pstmt.setString(1, newPassword);
                    pstmt.setLong(2, id);
                    pstmt.executeUpdate();
                    conn.commit();
                            sendMail(dto, "Password changed", "Password changed Successfully");

                }
               
               
            } else {
                throw new ApplicationException("Old Password does not matched");
            }
        } catch (SQLException e) {
            log.error("Database Exception in : changePassword", e);
            System.out.println(e);
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
    public Long registerUser(UserDTO dto) throws DuplicateRecordException, ApplicationException {
        log.debug("Model registerUser Started"); 
        long pk = 0;
        Connection conn = null;
        try {
             if (findByLogin(dto.getLogin())!= null) {
                throw new DuplicateRecordException("Login id already exists");
            }
            dto.setId(pk);
            conn = JDBCDataSource.getConnection();
             pk = nextPK();
              dto.setId(pk);
            conn.setAutoCommit(false);
             updateCreatesInfo(dto,true);
           try( PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_USER VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)")){
                pstmt.setLong(1, pk);
                pstmt.setString(2, dto.getFirstName());
                pstmt.setString(3, dto.getLastName());
                pstmt.setString(4, dto.getLogin());
                pstmt.setString(5, dto.getPassword());
                pstmt.setDate(6, new java.sql.Date(dto.getDob().getTime()));
                pstmt.setString(7, dto.getMobileNo());
                 pstmt.setLong(8, dto.getRoleId());
                pstmt.setString(9, dto.getGender());
                 pstmt.setString(10, dto.getCreatedBy());               
                 pstmt.setString(11, dto.getModifiedBy());               
                 pstmt.setTimestamp(12, new Timestamp(new Date().getTime()));               
                 pstmt.setTimestamp(13, new Timestamp(new Date().getTime()));
            }
          
            sendMail(dto, "Registration", "Registration Successfull");
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
                 try(PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_USER SET PASSWORD = ? WHERE LOGIN = ?")){
                    String newPassword =  generateRandomPassword();
                     pstmt.setString(1, newPassword);
                    pstmt.setString(2, login);                    pstmt.executeUpdate();

                    conn.commit();
                     sendMail(dto, "Reset Password", "Your new password is: " + newPassword);
               }
                 flag = true;
            }
        } catch (SQLException e) {
            log.error("Database Exception in : resetPassword", e);
            JDBCDataSource.trnRollback(conn);
            throw new ApplicationException("Exception : Exception in reset Password");
        }catch(Exception e){
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model resetPassword End");
        return flag;
    }
    
    /**
     * @param rs
     * @param dto
     * @throws SQLException
     */
    private void populateBean(ResultSet rs, UserDTO dto) throws SQLException {
          try {
            rs.findColumn("ROLE_NAME"); // Check if "ROLE_NAME" column exists
            dto.setRoleName(rs.getString("ROLE_NAME"));
        } catch (SQLException ignored) {}
        dto.setId(rs.getLong("ID"));
        dto.setFirstName(rs.getString("FIRST_NAME"));
        dto.setLastName(rs.getString("LAST_NAME"));
        dto.setLogin(rs.getString("LOGIN"));
        dto.setPassword(rs.getString("PASSWORD"));
        dto.setDob(rs.getDate("DOB"));
        dto.setMobileNo(rs.getString("MOBILE_NO"));
        dto.setRoleId(rs.getLong("ROLE_ID"));
        dto.setGender(rs.getString("GENDER"));
        dto.setCreatedBy(rs.getString("CREATED_BY"));
        dto.setModifiedBy(rs.getString("MODIFIED_BY"));
        dto.setCreatedDatetime(rs.getTimestamp("CREATED_DATETIME"));
        dto.setModifiedDatetime(rs.getTimestamp("MODIFIED_DATETIME"));
      }

    /**
     * @param dto`
     * @param subject
     * @param body
     * @throws Exception
     */
    private void sendMail(UserDTO dto, String subject, String body) throws Exception {
        EmailMessage msg = new EmailMessage();
        msg.setTo(dto.getLogin());
        msg.setSubject(subject);
        msg.setMessage(body);
        EmailUtility.sendMail(msg);
    }
    private void updateCreatesInfo(UserDTO dto,boolean isNew){
        if (dto != null) {
           if(isNew){
                 dto.setCreatedBy(dto.getLogin());
                  dto.setCreatedDatetime(new java.sql.Timestamp(System.currentTimeMillis()));
           }
             
                dto.setModifiedBy(dto.getLogin());
                dto.setModifiedDatetime(new java.sql.Timestamp(System.currentTimeMillis()));
        }
    }
    /**
     * @return
     */
    private static String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
}