
package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import org.apache.log4j.Logger;

import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.Exception.ApplicationException;
import java.sql.SQLException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Exception.RecordNotFoundException;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.EmailBuilder;
import com.rays.pro4.Util.EmailMessage;
import com.rays.pro4.Util.EmailUtility;
import com.rays.pro4.Util.JDBCDataSource;

/**
 * JDBC Implementation of UserModel.
 * 
 * @author Lokesh SOlanki
 *
 */

public class UserModel {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UserModel.class);

	/**
	 * Generate next PK of User
	 *
	 * @throws DatabaseException
	 */
	public synchronized Integer nextPK() throws DatabaseException {
		log.debug("Model nextPK Started");
		int pk = 0;
		try (Connection conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_USER");
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
     * Utility method to execute SQL for inserting user data.
     *
     * @param conn  Database connection
     * @param sql   SQL query string
     * @param bean  UserBean containing user data
     * @param pk    Primary key of the user
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
	private void executeUserInsert(Connection conn, String sql, UserBean bean, int pk) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getFirstName());
			pstmt.setString(3, bean.getLastName());
			pstmt.setString(4, bean.getLogin());
			pstmt.setString(5, bean.getPassword());
			pstmt.setDate(6, new Date(bean.getDob().getTime()));
			pstmt.setString(7, bean.getMobileNo());
			pstmt.setLong(8, bean.getRoleId());
			pstmt.setInt(9, bean.getUnSuccessfulLogin());
			pstmt.setString(10, bean.getGender());
			pstmt.setTimestamp(11, bean.getLastLogin());
			pstmt.setString(12, bean.getLock());
			pstmt.setString(13, bean.getRegisterdIP());
			pstmt.setString(14, bean.getLastLoginIP());
			pstmt.setString(15, bean.getCreatedBy());
			pstmt.setString(16, bean.getModifiedBy());
			pstmt.setTimestamp(17, bean.getCreatedDatetime());
			pstmt.setTimestamp(18, bean.getModifiedDatetime());
			pstmt.executeUpdate();
		}
	}
	/**sd
	 * add new user 
	 */
	public long add(UserBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");

		String sql = "INSERT INTO ST_USER VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Connection conn = null;
		int pk = 0;

		UserBean existbean = findByLogin(bean.getLogin());
		if (existbean != null) {
			throw new DuplicateRecordException("Error: User with this login already exists.");

		}

		try (Connection conn1 = JDBCDataSource.getConnection()){
			conn = conn1;
			pk = nextPK();
			conn.setAutoCommit(false);
			
			// Call utility method to execute the user insert SQL
			executeUserInsert(conn, sql, bean, pk);

			
			
			conn.commit();

		} catch (SQLException e) {
			log.error("Database Exception ...", e);
			try {
				conn.rollback();
				throw new ApplicationException("Exception:Error while rolling back in add method. " + e.getMessage());
			} catch (Exception ex) {
				throw new ApplicationException("Exception: Error while rolling back in add method. " + ex.getMessage());
			}
		}

		finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Add End");
		return pk;

	}

	/**
	 * delete user by id
	 * @throws ApplicationException
	 */
	public void delete(long id) throws ApplicationException {
		log.debug("Model delete start");
		String sql = "DELETE FROM ST_USER WHERE ID=?";
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);			
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setLong(1, id);
				pstmt.executeUpdate();
				conn.commit();
			}

		} catch (SQLException se) {
			log.error("Database Exception in delete ", se);
			try {
				if(conn!=null) {
				conn.rollback();
				}
				throw new ApplicationException("Exception: Error while delete user - " + se.getMessage());
			}
			catch(SQLException e1) { //this is for connection rollback 
				log.error("Database Exception in delete - rollback", e1);
				throw new ApplicationException("Exception: Error while rolling back in delete method." + e1.getMessage());
			}
			catch (Exception ex) {
				log.error("Exception: Error while rolling back in delete method. ", ex);
			} catch (Exception e2){
				throw new ApplicationException("Exception: Error while rolling back in delete method." + e2.getMessage());
			}		
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Delete End");
	}

	/**
	 * find user by login id
	 * @throws ApplicationException
	 */
	public UserBean findByLogin(String login) throws ApplicationException {
		log.debug("Model findByLohin Started");
		String sql = "SELECT * FROM ST_USER WHERE login=?";
		UserBean bean = null;
		try (Connection conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, login);
			ResultSet rs = pstmt.executeQuery();
		
			if(rs.next()) {
				bean = new UserBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setLogin(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setDob(rs.getDate(6));
				bean.setMobileNo(rs.getString(7));
				bean.setRoleId(rs.getLong(8));
				bean.setUnSuccessfulLogin(rs.getInt(9));
				bean.setGender(rs.getString(10));
				bean.setLastLogin(rs.getTimestamp(11));
				bean.setLock(rs.getString(12));
				bean.setRegisterdIP(rs.getString(13));
				bean.setLastLoginIP(rs.getString(14));
				bean.setCreatedBy(rs.getString(15));
				bean.setModifiedBy(rs.getString(16));
				bean.setCreatedDatetime(rs.getTimestamp(17));
				bean.setModifiedDatetime(rs.getTimestamp(18));
				}

		} catch (SQLException e) {
			log.error("Database Exception in findByLogin", e);
			throw new ApplicationException("Exception: Exception in getting User by name - " + e.getMessage());

		}
		log.debug("Model findby login end");
		return bean;
	}

	public UserBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findBy PK start");
		String sql = "SELECT * FROM ST_USER WHERE ID=?";
		UserBean bean = null;
		try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, pk);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					bean = new UserBean();
					bean.setId(rs.getLong(1));
					bean.setFirstName(rs.getString(2));
					bean.setLastName(rs.getString(3));
					bean.setLogin(rs.getString(4));
					bean.setPassword(rs.getString(5));
					bean.setDob(rs.getDate(6));
					bean.setMobileNo(rs.getString(7));
					bean.setRoleId(rs.getLong(8));
					bean.setUnSuccessfulLogin(rs.getInt(9));
					bean.setGender(rs.getString(10));
					bean.setLastLogin(rs.getTimestamp(11));
					bean.setLock(rs.getString(12));
					bean.setRegisterdIP(rs.getString(13));
					bean.setLastLoginIP(rs.getString(14));
					bean.setCreatedBy(rs.getString(15));
					bean.setModifiedBy(rs.getString(16));
					bean.setCreatedDatetime(rs.getTimestamp(17));
					bean.setModifiedDatetime(rs.getTimestamp(18));
				}
			}
		} catch (SQLException e) {
			log.error("Database Exception in findByPK", e);
			throw new ApplicationException("Exception: Exception in getting User by pk - " + e.getMessage());
		}
		log.debug("Model Find By PK end");
		return bean;
	}

	public void update(UserBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model Update Start");
		String sql = "UPDATE ST_USER SET FIRST_NAME=?,LAST_NAME=?,LOGIN=?,PASSWORD=?,DOB=?,MOBILE_NO=?,ROLE_ID=?,UNSUCCESSEFUL_LOGIN=?,GENDER=?,LAST_LOGIN=?,USER_LOCK=?,REGISTERED_IP=?,LAST_LOGIN_IP=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=?  WHERE ID=?";
		Connection conn = null;
		
		UserBean existBean = findByLogin(bean.getLogin());
		
		if (existBean != null && !(existBean.getId() == bean.getId())) {
			throw new DuplicateRecordException("Error: Cannot update user. User with this login already exists.");
		}
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, bean.getFirstName());
				pstmt.setString(2, bean.getLastName());
				pstmt.setString(3, bean.getLogin());
				pstmt.setString(4, bean.getPassword());
				pstmt.setDate(5, new Date(bean.getDob().getTime()));
				pstmt.setString(6, bean.getMobileNo());
				pstmt.setLong(7, bean.getRoleId());
				pstmt.setInt(8, bean.getUnSuccessfulLogin());
				pstmt.setString(9, bean.getGender());
				pstmt.setTimestamp(10, bean.getLastLogin());
				pstmt.setString(11, bean.getLock());
				pstmt.setString(12, bean.getRegisterdIP());				
				pstmt.setString(13, bean.getLastLoginIP());				
				pstmt.setString(14, bean.getCreatedBy());
				pstmt.setString(15, bean.getModifiedBy());
				pstmt.setTimestamp(16, bean.getCreatedDatetime());
				pstmt.setTimestamp(17, bean.getModifiedDatetime());
				pstmt.setLong(18, bean.getId());
				pstmt.executeUpdate();
				conn.commit();
				log.debug("User updated successfully.");
			}
			
		} catch (SQLException e) {
			log.error("Database Exception in update method", e);
			try {
				if(conn != null) {
					conn.rollback();
				}
                throw new ApplicationException("Exception: Error while updating user - " + e.getMessage());
			} catch (SQLException rollbackEx) {
				log.error("Database Exception in update - rollback", rollbackEx);
				throw new ApplicationException("Exception: Error while rolling back in update method." + rollbackEx.getMessage());
			} catch (Exception ex) {
				log.error("Exception: Error while rolling back in update method. ", ex);
				throw new ApplicationException("Exception: Exception in updating user. " + ex.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn) ;
        }
		log.debug("Model Update End ");
	}

	public List search(UserBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	} /**
	 * Search user with pagination
	 * @throws ApplicationException
	 */
	public List search(UserBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model Search Start");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_USER WHERE 1=1");
		ArrayList<UserBean> list = new ArrayList<>();
		if (pageNo < 0) {
			pageNo = 1;
		}
		if (pageSize < 0) {
			pageSize = 10; // Default page size
		}
		String sqlstr="SELECT * FROM ST_USER WHERE 1=1";
		if (bean != null) {
			if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
				sql.append(" AND FIRST_NAME like '" + bean.getFirstName() + "%'");

			}
			if (bean.getLogin() != null && bean.getLogin().length() > 0) {
				sql.append(" AND LOGIN like '" + bean.getLogin() + "%'");

			}
			if (bean.getRoleId() > 0) {
				sql.append(" AND ROLE_ID = " + bean.getRoleId());

			}
			if (bean.getLastName() != null && bean.getLastName().length() > 0) {
				sql.append(" AND LAST_NAME like '" + bean.getLastName() + "%'");

			}
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());

			}	
			
	      if (bean.getDob() != null) {
				sql.append(" AND DOB = '" + DataUtility.getDateString(bean.getDob()) + "'");
			}
			if (bean.getMobileNo() != null && bean.getMobileNo().length() > 0) {
				sql.append(" AND MOBILE_NO = '" + bean.getMobileNo() + "'");

			}
			if (bean.getUnSuccessfulLogin() > 0) {
				sql.append(" AND UNSUCCESSFUL_LOGIN = " + bean.getUnSuccessfulLogin());

			}
			if (bean.getGender() != null && bean.getGender().length() > 0) {
				sql.append(" AND GENDER like '" + bean.getGender() + "%'");

			}
		}

	    // if page size is greater than zero then apply pagination
	    if (pageSize > 0) {
	        // Calculate start record index
	        pageNo = (pageNo - 1) * pageSize;
	        sql.append(" Limit " + pageNo + ", " + pageSize);
	    }

	    try (Connection conn = JDBCDataSource.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

	        log.info("Executing SQL query: " + sql.toString());
	        try (ResultSet rs = pstmt.executeQuery()) {

	            while (rs.next()) {
	                UserBean userBean = new UserBean();
	                userBean.setId(rs.getLong(1));
	                userBean.setFirstName(rs.getString(2));
	                userBean.setLastName(rs.getString(3));
	                userBean.setLogin(rs.getString(4));
	                userBean.setPassword(rs.getString(5));
	                userBean.setDob(rs.getDate(6));
	                userBean.setMobileNo(rs.getString(7));
	                userBean.setRoleId(rs.getLong(8));
	                userBean.setUnSuccessfulLogin(rs.getInt(9));
	                userBean.setGender(rs.getString(10));
	                userBean.setLastLogin(rs.getTimestamp(11));
	                userBean.setLock(rs.getString(12));
	                userBean.setRegisterdIP(rs.getString(13));
	                userBean.setLastLoginIP(rs.getString(14));
	                userBean.setCreatedBy(rs.getString(15));
	                userBean.setModifiedBy(rs.getString(16));
	                userBean.setCreatedDatetime(rs.getTimestamp(17));
	                userBean.setModifiedDatetime(rs.getTimestamp(18));

	                list.add(userBean);
	            }
	        }
	    } catch (SQLException e) {
	        log.error("Database Exception in search", e);
	        throw new ApplicationException("Exception: Error while searching for users - " + e.getMessage());
	    } catch (Exception e) {
	        log.error("Unexpected Exception in search", e);
	        throw new ApplicationException("Unexpected Exception: " + e.getMessage());
	    }
	    log.debug("Model Search End");
	    return list;
	}

	
	
	/**
	 * get role of user
	 * @throws ApplicationException
	 */
	public List getRoleList(UserBean bean) throws ApplicationException {
		log.debug("Model getRoleList Start");
		String sql = "SELECT * FROM ST_USER WHERE ROLE_ID=?";

		List list = new ArrayList();
		try (Connection conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, bean.getRoleId());
			ResultSet rs = pstmt.executeQuery();;
			while (rs.next()) {
				bean = new UserBean();
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setLogin(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setDob(rs.getDate(6));
				bean.setMobileNo(rs.getString(7));
				bean.setRoleId(rs.getLong(8));
				bean.setUnSuccessfulLogin(rs.getInt(9));
				bean.setGender(rs.getString(10));
				bean.setLastLogin(rs.getTimestamp(11));
				bean.setLock(rs.getString(12));
				bean.setRegisterdIP(rs.getString(13));
				bean.setLastLoginIP(rs.getString(14));
				bean.setCreatedBy(rs.getString(15));
				bean.setModifiedBy(rs.getString(16));
				bean.setCreatedDatetime(rs.getTimestamp(17));
				bean.setModifiedDatetime(rs.getTimestamp(18));

				list.add(bean);
			}
		} catch (SQLException e) {
			log.error("Database Exception in getRoleList", e);
			throw new ApplicationException("Exception: Exception in getting list of user by roles - " + e.getMessage());
		}
		log.debug("Model getRoleList End");
		
			return list;

	}
	/** authenticate user */
	public UserBean authenticate(String login, String password) throws ApplicationException {
	    log.debug("Model authenticate Started");
	    String sql = "SELECT * FROM ST_USER WHERE LOGIN =? AND PASSWORD =?";
	    UserBean bean = null;
	    try (Connection conn = JDBCDataSource.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, login);
	        pstmt.setString(2, DataUtility.getHashedPassword(password));
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                bean = new UserBean();
	                bean.setId(rs.getLong(1));
	                bean.setFirstName(rs.getString(2));
	                bean.setLastName(rs.getString(3));
	                bean.setLogin(rs.getString(4));
	                bean.setPassword(rs.getString(5));
	                bean.setDob(rs.getDate(6));
	                bean.setMobileNo(rs.getString(7));
	                bean.setRoleId(rs.getLong(8));
	                bean.setUnSuccessfulLogin(rs.getInt(9));
	                bean.setGender(rs.getString(10));
	                bean.setLastLogin(rs.getTimestamp(11));
	                bean.setLock(rs.getString(12));
	                bean.setRegisterdIP(rs.getString(13));
	                bean.setLastLoginIP(rs.getString(14));
	                bean.setCreatedBy(rs.getString(15));
	                bean.setModifiedBy(rs.getString(16));
	                bean.setCreatedDatetime(rs.getTimestamp(17));
	                bean.setModifiedDatetime(rs.getTimestamp(18));
	            }
	        }
	    } catch (SQLException e) {
	        log.error("Database Exception in authenticate", e);
	        throw new ApplicationException("Exception: Error while authenticating user. " + e.getMessage());
	    }
	    log.debug("Model authenticate End");
	    return bean;

	}

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		if (pageNo < 0) {
			pageNo = 1;
		}
		if (pageSize < 0) {
			pageSize = 10; // Default page size
		}
		StringBuffer sql = new StringBuffer("select * from ST_USER");

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" LIMIT " + pageNo + "," + pageSize);
		}

		try (Connection conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql.toString());
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				UserBean bean = new UserBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setLogin(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setDob(rs.getDate(6));
				bean.setMobileNo(rs.getString(7));
				bean.setRoleId(rs.getLong(8));
				bean.setUnSuccessfulLogin(rs.getInt(9));
				bean.setGender(rs.getString(10));

				bean.setGender(rs.getString(10));
				bean.setLastLogin(rs.getTimestamp(11));
				bean.setLock(rs.getString(12));
				bean.setRegisterdIP(rs.getString(13));
				bean.setLastLoginIP(rs.getString(14));
				bean.setCreatedBy(rs.getString(15));
				bean.setModifiedBy(rs.getString(16));
				bean.setCreatedDatetime(rs.getTimestamp(17));
				bean.setModifiedDatetime(rs.getTimestamp(18));
				list.add(bean);
			}
		} catch (SQLException e) {
			log.error("Database Exception in list", e);
			throw new ApplicationException("Exception: Error while getting the list of users. - " + e.getMessage());
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model list End");
		return list;
	}

	public boolean changePassword(Long id, String oldPassword, String newPassword) throws ApplicationException, RecordNotFoundException {

	    log.debug("Model changePassword Started");
	    boolean flag = false;
	    Connection conn = null;
	    UserBean beanexist = null;
	
	    try {
	        conn = JDBCDataSource.getConnection();
	        conn.setAutoCommit(false); // Start transaction
	        beanexist = findByPK(id);
	
	        if (beanexist == null) {
	            throw new RecordNotFoundException("Error: User does not exist.");
	        }
	
	        if (!DataUtility.getHashedPassword(oldPassword).equals(beanexist.getPassword())) {
	            throw new RecordNotFoundException("Error: Incorrect old password.");
	        }
	
	        beanexist.setPassword(DataUtility.getHashedPassword(newPassword));
	        update(beanexist);
	        conn.commit(); // Commit transaction
	        flag = true;
	
	    } catch (RecordNotFoundException rnfe) {
	        log.error("Record Not Found Exception in changePassword", rnfe);
	        throw rnfe; // Re-throw the RecordNotFoundException
	    } catch (Exception e) {
	        log.error("Exception in changePassword", e);
	        try {
	            if (conn != null) {
	                conn.rollback();
	            }
	        } catch (SQLException ex) {
	            log.error("Rollback failed in changePassword", ex);
	            throw new ApplicationException("Error: Rollback operation failed while changing password - " + ex.getMessage());
	        }
	        throw new ApplicationException("Error: An error occurred while changing password - " + e.getMessage());
	    } finally {
	        JDBCDataSource.closeConnection(conn);
	    }
	
	    HashMap<String, String> map = new HashMap<>();
	    map.put("login", beanexist.getLogin());
	    map.put("password", beanexist.getPassword());
		EmailMessage msg = new EmailMessage();
		msg.setTo(beanexist.getLogin());
		msg.setSubject("SUNRAYS ORS Password has been changed Successfuly.");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);

		EmailUtility.sendMail(msg);
		log.info("Email sent for password change to: " + beanexist.getLogin());

		log.debug("Model changePassword End");
		return flag;
	}

	/**
	 * Register new user
	 */
	public long registerUser(UserBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model registerUser Started");
		long pk = add(bean);

		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("login", bean.getLogin());
			map.put("password", bean.getPassword());
	
			String message = EmailBuilder.getUserRegistrationMessage(map);
			EmailMessage msg = new EmailMessage();
	
			msg.setTo(bean.getLogin());
			msg.setSubject("Registration is Successful for ORS Project Sunilos");
			msg.setMessage(message);
			msg.setMessageType(EmailMessage.HTML_MSG);
	
			EmailUtility.sendMail(msg);
			log.info("Email sent for registration to: " + bean.getLogin());
		} catch(Exception e) {
			log.error("Exception: Error in sending email to register user." + e.getMessage());
		}

		log.debug("Model registerUser End");
		return pk;
	}

	/**
	 * Reset User password
	 * 
	 * @throws ApplicationException
	 * @throws RecordNotFoundException
	 * 
	 **/
	public boolean resetPassword(String login) throws ApplicationException, RecordNotFoundException {
		log.debug("Model resetPassword Started");
		boolean flag = false;
		UserBean bean = null;
		try {
			bean = findByLogin(login);

			if (bean == null) {
				throw new RecordNotFoundException("Error: User with this login does not exist.");
			}

			String password = DataUtility.generateRandomString();
			bean.setPassword(DataUtility.getHashedPassword(password));

			update(bean);

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("login", bean.getLogin());
            map.put("password", password);
            map.put("firstName", bean.getFirstName());
            map.put("lastName", bean.getLastName());

			String message = EmailBuilder.getForgetPasswordMessage(map);

			EmailMessage msg = new EmailMessage();
			msg.setTo(login);
			msg.setSubject("Sunrays ORS Password Reset");
			msg.setMessage(message);
			msg.setMessageType(EmailMessage.HTML_MSG);

			EmailUtility.sendMail(msg);
			log.info("Email sent for forgot password to: " + login);
			flag = true;
		} catch (RecordNotFoundException e) {
			log.error("RecordNotFoundException in resetPassword", e);
			throw e;
		} catch (Exception e) {
			log.error("Exception in resetPassword", e);
			throw new ApplicationException("Error: An error occurred while resetting password - " + e.getMessage());
		}
		log.debug("Model resetPassword End");
		return flag;
	}
}
				bean = new UserBean();
				bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setLogin(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setDob(rs.getDate(6));
				bean.setMobileNo(rs.getString(7));
				bean.setRoleId(rs.getLong(8));
				bean.setUnSuccessfulLogin(rs.getInt(9));
				bean.setGender(rs.getString(10));
				bean.setLastLogin(rs.getTimestamp(11));
				bean.setLock(rs.getString(12));
				bean.setRegisterdIP(rs.getString(13));
				bean.setLastLoginIP(rs.getString(14));
				bean.setCreatedBy(rs.getString(15));
				bean.setModifiedBy(rs.getString(16));
				bean.setCreatedDatetime(rs.getTimestamp(17));
				bean.setModifiedDatetime(rs.getTimestamp(18));

			}
		} catch (SQLException e) {
			log.error("Database Exception in findByPK", e);
			throw new ApplicationException("Exception: Exception in getting User by pk - " + e.getMessage());
		}
		log.debug("Model Find By PK end");
		return bean;
	}

	public void update(UserBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model Update Start");
		String sql = "UPDATE ST_USER SET FIRST_NAME=?,LAST_NAME=?,LOGIN=?,PASSWORD=?,DOB=?,MOBILE_NO=?,ROLE_ID=?,UNSUCCESSEFUL_LOGIN=?,GENDER=?,LAST_LOGIN=?,USER_LOCK=?,REGISTERED_IP=?,LAST_LOGIN_IP=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=?  WHERE ID=?";
		Connection conn = null;
		
		UserBean existBean = findByLogin(bean.getLogin());
		
		if (existBean != null && !(existBean.getId() == bean.getId())) {
			throw new DuplicateRecordException("Error: Cannot update user. User with this login already exists.");
		}
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, bean.getFirstName());
				pstmt.setString(2, bean.getLastName());
				pstmt.setString(3, bean.getLogin());
				pstmt.setString(4, bean.getPassword());
				pstmt.setDate(5, new Date(bean.getDob().getTime()));
				pstmt.setString(6, bean.getMobileNo());
				pstmt.setLong(7, bean.getRoleId());
				pstmt.setInt(8, bean.getUnSuccessfulLogin());
				pstmt.setString(9, bean.getGender());
				pstmt.setTimestamp(10, bean.getLastLogin());
				pstmt.setString(11, bean.getLock());
				pstmt.setString(12, bean.getRegisterdIP());				
				pstmt.setString(13, bean.getLastLoginIP());				
				pstmt.setString(14, bean.getCreatedBy());
				pstmt.setString(15, bean.getModifiedBy());
				pstmt.setTimestamp(16, bean.getCreatedDatetime());
				pstmt.setTimestamp(17, bean.getModifiedDatetime());
				pstmt.setLong(18, bean.getId());
				pstmt.executeUpdate();
				conn.commit();
				log.debug("User updated successfully.");
			}
			
		} catch (SQLException e) {
			log.error("Database Exception in update method", e);
			try {
				if(conn != null) {
					conn.rollback();
				}
                throw new ApplicationException("Exception: Error while updating user - " + e.getMessage());
			} catch (SQLException rollbackEx) {
				log.error("Database Exception in update - rollback", rollbackEx);
				throw new ApplicationException("Exception: Error while rolling back in update method." + rollbackEx.getMessage());
			} catch (Exception ex) {
				log.error("Exception: Error while rolling back in update method. ", ex);
				throw new ApplicationException("Exception: Exception in updating user. " + ex.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn) ;
        }
		log.debug("Model Update End ");
	}

	public List search(UserBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Search user with pagination
	 * @throws ApplicationException
	 */
	public List search(UserBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model Search Start");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_USER WHERE 1=1");
		ArrayList<UserBean> list = new ArrayList<>();
		if (pageNo < 0) {
			pageNo = 1;
		}
		if (pageSize < 0) {
			pageSize = 10; // Default page size
		}
		
		if (bean != null) {
			if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
				sql.append(" AND FIRST_NAME like '" + bean.getFirstName() + "%'");

			}
			if (bean.getLogin() != null && bean.getLogin().length() > 0) {
				sql.append(" AND LOGIN like '" + bean.getLogin() + "%'");

			}
			if (bean.getRoleId() > 0) {
				sql.append(" AND ROLE_ID = " + bean.getRoleId());

			}
			if (bean.getLastName() != null && bean.getLastName().length() > 0) {
				sql.append(" AND LAST_NAME like '" + bean.getLastName() + "%'");

			}
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());

			}

			if (bean.getPassword() != null && bean.getPassword().length() > 0) {
				sql.append(" AND PASSWORD like '" + bean.getPassword() + "%'");

			}
			if (bean.getDob() != null) {
				sql.append(" AND DOB = '" + DataUtility.getDateString(bean.getDob()) + "'");
			}
			if (bean.getMobileNo() != null && bean.getMobileNo().length() > 0) {
				sql.append(" AND MOBILE_NO = '" + bean.getMobileNo() + "'");

			}
			if (bean.getUnSuccessfulLogin() > 0) {
				sql.append(" AND UNSUCCESSFUL_LOGIN = " + bean.getUnSuccessfulLogin());

			}
			if (bean.getGender() != null && bean.getGender().length() > 0) {
				sql.append(" AND GENDER like '" + bean.getGender() + "%'");

			}
		}

		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
		}

		try (Connection conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql.toString());
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				bean = new UserBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setLogin(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setDob(rs.getDate(6));
				bean.setMobileNo(rs.getString(7));
				bean.setRoleId(rs.getLong(8));
				bean.setUnSuccessfulLogin(rs.getInt(9));
				bean.setGender(rs.getString(10));
				bean.setLastLogin(rs.getTimestamp(11));
				bean.setLock(rs.getString(12));
				bean.setRegisterdIP(rs.getString(13));
				bean.setLastLoginIP(rs.getString(14));
				bean.setCreatedBy(rs.getString(15));
				bean.setModifiedBy(rs.getString(16));
				bean.setCreatedDatetime(rs.getTimestamp(17));
				bean.setModifiedDatetime(rs.getTimestamp(18));

				list.add(bean);

			}
		} catch (SQLException e) {
			log.error("Database Exception in search ", e);
			throw new ApplicationException("Exception: Error while searching for users - " + e.getMessage());
		}
		log.debug("Model Search end");
		return list;

	}
	/**
	 * get role of user
	 * @throws ApplicationException
	 */
	public List getRoleList(UserBean bean) throws ApplicationException {
		log.debug("Model getRoleList Start");
		String sql = "SELECT * FROM ST_USER WHERE ROLE_ID=?";

		List list = new ArrayList();
		try (Connection conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, bean.getRoleId());
			ResultSet rs = pstmt.executeQuery();;
			while (rs.next()) {
				bean = new UserBean();
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setLogin(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setDob(rs.getDate(6));
				bean.setMobileNo(rs.getString(7));
				bean.setRoleId(rs.getLong(8));
				bean.setUnSuccessfulLogin(rs.getInt(9));
				bean.setGender(rs.getString(10));
				bean.setLastLogin(rs.getTimestamp(11));
				bean.setLock(rs.getString(12));
				bean.setRegisterdIP(rs.getString(13));
				bean.setLastLoginIP(rs.getString(14));
				bean.setCreatedBy(rs.getString(15));
				bean.setModifiedBy(rs.getString(16));
				bean.setCreatedDatetime(rs.getTimestamp(17));
				bean.setModifiedDatetime(rs.getTimestamp(18));

				list.add(bean);
			}
		} catch (SQLException e) {
			log.error("Database Exception in getRoleList", e);
			throw new ApplicationException("Exception: Exception in getting list of user by roles - " + e.getMessage());
		}
		log.debug("Model getRoleList End");
		
			return list;

	}
	/** authenticate user */
	public UserBean authenticate(String login, String password) throws ApplicationException {
	    log.debug("Model authenticate Started");
	    String sql = "SELECT * FROM ST_USER WHERE LOGIN =? AND PASSWORD =?";
	    UserBean bean = null;
	    try (Connection conn = JDBCDataSource.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, login);
	        pstmt.setString(2, DataUtility.getHashedPassword(password));
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                bean = new UserBean();
	                bean.setId(rs.getLong(1));
	                bean.setFirstName(rs.getString(2));
	                bean.setLastName(rs.getString(3));
	                bean.setLogin(rs.getString(4));
	                bean.setPassword(rs.getString(5));
	                bean.setDob(rs.getDate(6));
	                bean.setMobileNo(rs.getString(7));
	                bean.setRoleId(rs.getLong(8));
	                bean.setUnSuccessfulLogin(rs.getInt(9));
	                bean.setGender(rs.getString(10));
	                bean.setLastLogin(rs.getTimestamp(11));
	                bean.setLock(rs.getString(12));
	                bean.setRegisterdIP(rs.getString(13));
	                bean.setLastLoginIP(rs.getString(14));
	                bean.setCreatedBy(rs.getString(15));
	                bean.setModifiedBy(rs.getString(16));
	                bean.setCreatedDatetime(rs.getTimestamp(17));
	                bean.setModifiedDatetime(rs.getTimestamp(18));
	            }
	        }
	    } catch (SQLException e) {
	        log.error("Database Exception in authenticate", e);
	        throw new ApplicationException("Exception: Error while authenticating user. " + e.getMessage());
	    }
	    log.debug("Model authenticate End");
	    return bean;

	}
	/**
	 * get all list
	 */
	public List list() throws ApplicationException {
		return list(0, 0);
	}
    /**
     * get all list with pagination
     * @throws ApplicationException
     */
	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		List<UserBean> list = new ArrayList<>();
		
		int actualPageNo = pageNo;
	    int actualPageSize = pageSize;
	    if (actualPageNo < 0) {
	        actualPageNo = 1;
	    }
	    if (actualPageSize < 0) {
	        actualPageSize = 10; // Default page size
	    }
	    
	    StringBuffer sql = new StringBuffer("SELECT * FROM ST_USER");
	    if (actualPageSize > 0) {
	        actualPageNo = (actualPageNo - 1) * actualPageSize;
	        sql.append(" LIMIT " + actualPageNo + ", " + actualPageSize);
	    }
	    
	    try (Connection conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	            ResultSet rs = pstmt.executeQuery()) {
	        while (rs.next()) {
	            UserBean bean = new UserBean();
	            bean.setId(rs.getLong(1));
	            bean.setFirstName(rs.getString(2));
	            bean.setLastName(rs.getString(3));
	            bean.setLogin(rs.getString(4));
	            bean.setPassword(rs.getString(5));
	            bean.setDob(rs.getDate(6));
	            bean.setMobileNo(rs.getString(7));
	            bean.setRoleId(rs.getLong(8));
	            bean.setUnSuccessfulLogin(rs.getInt(9));
	            bean.setGender(rs.getString(10));
	            bean.setLastLogin(rs.getTimestamp(11));
	            bean.setLock(rs.getString(12));
	            bean.setRegisterdIP(rs.getString(13));
	            bean.setLastLoginIP(rs.getString(14));
	            bean.setCreatedBy(rs.getString(15));
	            bean.setModifiedBy(rs.getString(16));
	            bean.setCreatedDatetime(rs.getTimestamp(17));
	            bean.setModifiedDatetime(rs.getTimestamp(18));
	            list.add(bean);
	        }
		} catch (SQLException e) {
			log.error("Database Exception in list", e);
			throw new ApplicationException("Exception: Error while getting the list of users. - " + e.getMessage());
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model list End");
		return list;
	}
    
	public boolean changePassword(Long id, String oldPassword, String newPassword) throws ApplicationException, RecordNotFoundException {

	    log.debug("Model changePassword Started");
	    boolean flag = false;
	    Connection conn = null;
	    UserBean beanexist = null;
	
	    try {
	        conn = JDBCDataSource.getConnection();
	        conn.setAutoCommit(false); // Start transaction
	        beanexist = findByPK(id);
	
	        if (beanexist == null) {
	            throw new RecordNotFoundException("Error: User does not exist.");
	        }
	
	        if (!DataUtility.getHashedPassword(oldPassword).equals(beanexist.getPassword())) {
	            throw new RecordNotFoundException("Error: Incorrect old password.");
	        }
	
	        beanexist.setPassword(DataUtility.getHashedPassword(newPassword));
	        update(beanexist);
	        conn.commit(); // Commit transaction
	        flag = true;
	
	    } catch (RecordNotFoundException rnfe) {
	        log.error("Record Not Found Exception in changePassword", rnfe);
	        throw rnfe; // Re-throw the RecordNotFoundException
	    } catch (Exception e) {
	        log.error("Exception in changePassword", e);
	        try {
	            if (conn != null) {
	                conn.rollback();
	            }
	        } catch (SQLException ex) {
	            log.error("Rollback failed in changePassword", ex);
	            throw new ApplicationException("Error: Rollback operation failed while changing password - " + ex.getMessage());
	        }
	        throw new ApplicationException("Error: An error occurred while changing password - " + e.getMessage());
	    } finally {
	        JDBCDataSource.closeConnection(conn);
	    }
	
	    HashMap<String, String> map = new HashMap<>();
	    map.put("login", beanexist.getLogin());
	    map.put("password", beanexist.getPassword());
	    map.put("firstname", beanexist.getFirstName());
	    map.put("lastName", beanexist.getLastName());

		String message = EmailBuilder.getChangePasswordMessage(map);
		EmailMessage msg = new EmailMessage();
		msg.setTo(beanexist.getLogin());
		msg.setSubject("SUNRAYS ORS Password has been changed Successfuly.");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);

		EmailUtility.sendMail(msg);
		log.info("Email sent for password change to: " + beanexist.getLogin());

		log.debug("Model changePassword End");
		return flag;
	}

	/**
	 * Register new user
	 */
	public long registerUser(UserBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model registerUser Started");
		long pk = add(bean);

		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("login", bean.getLogin());
			map.put("password", bean.getPassword());
	
			String message = EmailBuilder.getUserRegistrationMessage(map);
			EmailMessage msg = new EmailMessage();
	
			msg.setTo(bean.getLogin());
			msg.setSubject("Registration is Successful for ORS Project Sunilos");
			msg.setMessage(message);
			msg.setMessageType(EmailMessage.HTML_MSG);
	
			EmailUtility.sendMail(msg);
			log.info("Email sent for registration to: " + bean.getLogin());
		} catch(Exception e) {
			log.error("Exception: Error in sending email to register user." + e.getMessage());
		}

		log.debug("Model registerUser End");
		return pk;
	}

	/**
	 * Reset User password
	 * 
	 * @throws ApplicationException
	 * @throws RecordNotFoundException
	 * 
	 **/
	public boolean resetPassword(String login) throws ApplicationException, RecordNotFoundException {
		log.debug("Model resetPassword Started");
		boolean flag = false;
		UserBean bean = null;
		try {
			bean = findByLogin(login);

			if (bean == null) {
				throw new RecordNotFoundException("Error: User with this login does not exist.");
			}

			String password = DataUtility.generateRandomString();
			bean.setPassword(DataUtility.getHashedPassword(password));

			update(bean);

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("login", bean.getLogin());
            map.put("password", password);
            map.put("firstName", bean.getFirstName());
            map.put("lastName", bean.getLastName());

			String message = EmailBuilder.getForgetPasswordMessage(map);

			EmailMessage msg = new EmailMessage();
			msg.setTo(login);
			msg.setSubject("Sunrays ORS Password Reset");
			msg.setMessage(message);
			msg.setMessageType(EmailMessage.HTML_MSG);

			EmailUtility.sendMail(msg);
			log.info("Email sent for forgot password to: " + login);
			flag = true;
		} catch (RecordNotFoundException e) {
			log.error("RecordNotFoundException in resetPassword", e);
			throw e;
		} catch (Exception e) {
			log.error("Exception in resetPassword", e);
			throw new ApplicationException("Error: An error occurred while resetting password - " + e.getMessage());
		}
		log.debug("Model resetPassword End");
		return flag;
	}
}
