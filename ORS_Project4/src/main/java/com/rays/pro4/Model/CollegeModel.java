package com.rays.pro4.Model;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;



import com.rays.pro4.Bean.CollegeBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * JDBC Implementation of CollegeModel.
 * 
 * @author Lokesh SOlanki
 *
 */
public class CollegeModel extends BaseModel {
	
	private String name;
	private String address;
	private String state;
	private String city;
	private String phoneNo;


	/**
	 * Adds a new college to the database.
	 * 
	 * @param bean The CollegeBean object containing college data.
	 * @return The primary key of the newly added college.
	 * @throws ApplicationException    If an application error occurs.
	 * @throws DuplicateRecordException If a college with the same name already
	 *                                  exists.
	 */
	public long add(CollegeBean bean) throws ApplicationException,
			DuplicateRecordException {
		log.debug("Model add Started"+bean);
		long pk = 0;
		// Check for duplicate college name
		CollegeBean duplicateCollegeName = findByName(bean.getName());
		if (duplicateCollegeName != null) {
			throw new DuplicateRecordException("College Name already exists");
		}

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_COLLEGE (ID,NAME,ADDRESS,STATE,CITY,PHONE_NO,CREATED_BY,MODIFIED_BY,CREATED_DATETIME,MODIFIED_DATETIME) VALUES(?,?,?,?,?,?,?,?,?,?)");
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
			pstmt.close();
			conn.commit();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(conn);
			throw new ApplicationException("Exception: Exception in adding college - " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model add End"+bean);
		return pk;
	}

    public void delete(long id) throws ApplicationException {
        delete(findByPK(id));
    }

	/**
	 * Deletes a college from the database.
	 * 
	 * @param bean The CollegeBean object to be deleted.
	 * @throws ApplicationException If a database error occurs.
	 */
	public void delete(CollegeBean bean) throws ApplicationException {
		log.debug("Model delete Started");
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_COLLEGE WHERE ID=?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(conn);
			throw new ApplicationException("Exception: Exception in deleting College - " + e.getMessage());
		}
		log.debug("Modal delete End");
	}
    
	public CollegeBean findByName(String name) throws ApplicationException {
		log.debug("Model findByName Started");
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ST_COLLEGE WHERE NAME=?");
		CollegeBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);// Set parameter for name
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = populate(rs);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception: Exception in getting College by name - " + e.getMessage());
		} catch (Exception e) {
			log.error("Error", e);
			throw new ApplicationException("Exception: Exception in getting College by name - " + e.getMessage());

		}
		log.debug("modal findByName End");
		return bean;
	}

	public CollegeBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findByPK Started");
		String sql = "SELECT * FROM ST_COLLEGE WHERE id=?";
		Connection conn = null;
		CollegeBean bean = null;		
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = populate(rs);
			}
			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception: Error in getting College by PK - " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Find By PK End");
		return bean;
	}

	/**
	 * Updates an existing college in the database.
	 *
	 * @param bean The CollegeBean object with updated college data.
	 * @throws ApplicationException    If a database error occurs.
	 * @throws DuplicateRecordException If a college with the same name already
	 *                                  exists.
	 */
	public void update(CollegeBean bean) throws ApplicationException,
			DuplicateRecordException {
		log.debug("Model update Started");
		// Check if the updated college name already exists
		CollegeBean beanExist = findByName(bean.getName());
		if (beanExist != null && beanExist.getId() != bean.getId()) {
			throw new DuplicateRecordException("College is already exist");
		}
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_COLLEGE SET NAME=?,ADDRESS=?,STATE=?,CITY=?,PHONE_NO=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");
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
			conn.commit();
		} catch (Exception e) {
			JDBCDataSource.trnRollback(conn);
			throw new ApplicationException("Exception: Exception in updating College - " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model update End");
	}

	public List search(CollegeBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	public List search(CollegeBean bean, int pageNo, int pageSize) throws ApplicationException {
		ArrayList<CollegeBean> list = new ArrayList<>();
		int index = 1;
		
        if(bean!=null){
            if(bean.getId()>0){
                sql.append(" AND id=?");
                
            }
            if(bean.getName()!=null && bean.getName().length()>0){
                sql.append(" AND Name like ?");
            }
            if(bean.getAddress()!=null && bean.getAddress().length()>0){
                sql.append(" AND Address like ?");
            }
            if(bean.getCity()!=null && bean.getCity().length()>0){
                sql.append(" AND City like ?");
            }
        }        

		log.debug("model search Started");
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ST_COLLEGE WHERE 1=1");
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			if (bean != null) {
				if (bean.getId() > 0) {
					pstmt.setLong(index++, bean.getId());
				}
				if (bean.getName() != null && !bean.getName().isEmpty()) {
					pstmt.setString(index++, bean.getName() + "%");
				}
				if (bean.getAddress() != null && !bean.getAddress().isEmpty()) {
					pstmt.setString(index++, bean.getAddress() + "%");
				}
				if (bean.getCity() != null && !bean.getCity().isEmpty()) {
					pstmt.setString(index++, bean.getCity() + "%");
				}
			ResultSet rs = pstmt.executeQuery();
					while (rs.next()) {
					CollegeBean bean = new CollegeBean();
					bean.setCreatedBy(rs.getString(7));
					bean.setModifiedBy(rs.getString(8));
					bean.setCreatedDatetime(rs.getTimestamp(9));
					bean.setModifiedDatetime(rs.getTimestamp(10));
					list.add(bean);
					}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception in search", e);
			throw new ApplicationException("Exception: Exception in searching college - " + e.getMessage());
		}
		log.debug("model search End");
		return list;
	}

	public List list() throws ApplicationException {
		return list(1, 0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {	
		log.debug("Model list Started");		
		ArrayList<CollegeBean> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select * from ST_COLLEGE");		
		if (pageSize > 0) {
			// Calculate start record index
			
			
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT " + pageNo + "," + pageSize);
		}
		try (Connection conn = JDBCDataSource.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			 ResultSet rs = pstmt.executeQuery()) {
			CollegeBean bean;
			while (rs.next()) {				
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
				list.add(bean);
			}
		} catch (SQLException e) {
			log.error("Database Exception in list", e);
			throw new ApplicationException("Exception: Exception in getting list of users - " + e.getMessage());
		}
		log.debug("Model list End");
		return list;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	@Override
	public String getTableName() {
		return "ST_COLLEGE";
	}

	@Override
	public CollegeBean populate(ResultSet rs) throws SQLException {
		CollegeBean bean = new CollegeBean();
		bean.setId(rs.getLong(1));
		bean.setName(rs.getString(2));
		bean.setAddress(rs.getString(3));
		bean.setState(rs.getString(4));
		bean.setCity(rs.getString(5));
		bean.setPhoneNo(rs.getString(6));
		return (CollegeBean)populate((BaseModel) bean,null,rs);
	}
}
