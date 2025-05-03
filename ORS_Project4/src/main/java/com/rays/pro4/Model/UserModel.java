
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
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getFirstName());
			pstmt.setString(3, bean.getLastName());
			pstmt.setString(4, bean.getLogin());
			pstmt.setString(5, bean.getPassword());
			// date of birth caste by sql date
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

		try (Connection conn = JDBCDataSource.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setLong(1, id);
				pstmt.executeUpdate();
				conn.commit();
			}

		} catch (SQLException e) {
			log.error("Database Exception in delete ", e);
			try {
				if(conn!=null) {
				conn.rollback();
				}
				throw new ApplicationException("Exception: Error while delete user - " + e.getMessage());
			}
			catch(SQLException e1) {
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
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				if (bean == null){
					bean = new UserBean();
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
		try (Connection conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
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
		try (Connection conn = JDBCDataSource.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

				pstmt.setString(1, bean.getFirstName());
				pstmt.setString(2, bean.getLastName());
				pstmt.setString(3, bean.getLogin());
				pstmt.setString(4, bean.getPassword());
				pstmt.setDate(5, new java.sql.Date(bean.getDob().getTime()));
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
				pstmt.setTimestamp(16, bean.getModifiedDatetime());
				pstmt.setTimestamp(17, bean.getModifiedDatetime());
				pstmt.setLong(18, bean.getId());
				pstmt.executeUpdate();
				conn.commit();
			}
			
		} catch (SQLException e) {
			log.error("Database Exception in update", e);
			try {
				JDBCDataSource.trnRollback();
                throw new ApplicationException("Exception: Exception in updating user. " + e.getMessage());
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
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_USER WHERE LOGIN =? AND PASSWORD =?");
		UserBean bean = null;
		Connection conn = null;
		try (Connection conn1 = JDBCDataSource.getConnection()){
			conn = conn1;
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, login);
			pstmt.setString(2, DataUtility.getHashedPassword(password));
			
			ResultSet rs = pstmt.executeQuery();
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

			}
		} catch (Exception e) {
			log.error("Database Exception in authenticate", e);
			throw new ApplicationException("Exception: Error while authenticating user - " + e.getMessage());
		} finally {
			
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
		UserBean beanexist = findByPK(id);
		Connection conn = null;

		if (beanexist != null) {
			try (Connection conn1 = JDBCDataSource.getConnection()){
				conn = conn1;
				if (DataUtility.getHashedPassword(oldPassword).equals(beanexist.getPassword())) {
					beanexist.setPassword(DataUtility.getHashedPassword(newPassword));
					try {
						update(beanexist);
					} catch (Exception e) {
						log.error(e);
						throw new ApplicationException("Error: The user exist with this login.");
					}
					flag = true;
				} else {
					throw new RecordNotFoundException("Error: User does not exist or old password is wrong.");
				}
			

			} catch (Exception e) {
				log.error(e);
				throw new ApplicationException("Error: The user exist with this login.");
			}
			flag = true;
		} else {
			throw new RecordNotFoundException("Error: User does not exist.");
		}finally {
			JDBCDataSource.closeConnection(conn);
		}
		

		HashMap<String, String> map = new HashMap<String, String>();

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
		log.debug("Model add Started");
		long pk = add(bean);

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
		return pk;
	}

	/**
	 * forgot user password
	 */
	public boolean forgetPassword(String login) throws ApplicationException, RecordNotFoundException {
		UserBean userData = findByLogin(login);
		boolean flag = false;

		if (userData == null) {
			throw new RecordNotFoundException("Error: User does not exist.");

		}

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", userData.getLogin());
		map.put("password", userData.getPassword());
		map.put("firstName", userData.getFirstName());
		map.put("lastName", userData.getLastName());

		String message = EmailBuilder.getForgetPasswordMessage(map);

		EmailMessage msg = new EmailMessage();
		msg.setTo(login);
		msg.setSubject("Sunrays ORS Password reset");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);

		EmailUtility.sendMail(msg);
		log.info("Email sent for forgot password to: " + login);
		flag = true;
		return flag;
	}
}
