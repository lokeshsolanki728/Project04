package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.CollegeBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

/**
 * JDBC Implementation of CollegeModel.
 * 
 * @author Lokesh SOlanki
 *
 */
public class CollegeModel {

	private static Logger log = Logger.getLogger(CollegeModel.class);

	public Integer nextPK() throws DatabaseException {
		log.debug("Modal nextPK Started");
		Connection conn = null;
		int pk = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = JDBCDataSource.getConnection();
			DatabaseMetaData dbmd = conn.getMetaData();
            if (dbmd.supportsAutoIncrementInIdentityColumns()) {
                return 1;
            } else {
            pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_COLLEGE");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
            }
			
		} catch (Exception e) {
			log.error("Database Exception", e);
			throw new DatabaseException("Exception: Unable to get PK");

		} finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (Exception ex) {}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPK End");
		return pk + 1;
	}

	public long add(CollegeBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");
		Connection conn = null;
		int pk = 0;
		PreparedStatement pstmt = null;
		CollegeBean duplicateCollegeName = findByName(bean.getName());

		if (duplicateCollegeName != null) {
			throw new DuplicateRecordException("College Name alredy exists");

		}
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			pk = nextPK();
            pstmt = conn.prepareStatement("INSERT INTO ST_COLLEGE (ID,NAME,ADDRESS,STATE,CITY,PHONE_NO,CREATED_BY,MODIFIED_BY,CREATED_DATETIME,MODIFIED_DATETIME) VALUES(?,?,?,?,?,?,?,?,?,?)");
			if(pk != 1)pstmt.setInt(1, pk);
            else pstmt.setLong(1,pk);
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
			conn.commit();			
		} catch (Exception e) {
			log.error("Database Exception", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ApplicationException("Exception : Error in rollback " + ex.getMessage());
			}
			throw new ApplicationException("Exception: Exception in adding college");
		} finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception ex) {}
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model add End");
		return pk;
	}
    public void delete(long id) throws ApplicationException {
        delete(findByPK(id));
    }

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
			log.error("Database Exception ", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Error in rollback " + ex.getMessage());
			}
			throw new ApplicationException("Exception: Exception in deleting College");
		} finally {
            try {
                
            } catch (Exception ex) {}
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Modal delete End");
	}
    
	public CollegeBean findByName(String name) throws ApplicationException {
		log.debug("Model findByName Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_COLLEGE WHERE NAME=?");
		CollegeBean bean = null;
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
		try {
			conn = JDBCDataSource.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CollegeBean();
				bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setAddress(rs.getString(3));
                bean.setState(rs.getString(4));
                bean.setCity(rs.getString(5));
                bean.setPhoneNo(rs.getString(6));
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
		} catch (Exception e) {
			log.error("Database Exception", e);
			throw new ApplicationException("Exception: Exception in getting College by name");

		} finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                
            } catch (Exception ex) {
                
            }
			JDBCDataSource.closeConnection(conn);

		}
		log.debug("modal findByName End");
		return bean;
	}

	public CollegeBean findByPK(long pk) throws ApplicationException {
		log.debug("Model Find BY Pk Stsrted");
		StringBuffer sql = new StringBuffer("SELECT*FROM ST_COLLEGE WHERE id=?");
		CollegeBean bean = null;
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
		try {
			conn = JDBCDataSource.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			rs = pstmt.executeQuery();
            while (rs.next()) {

				bean = new CollegeBean();
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
		} catch (Exception e) {
			log.error("Database Exception ", e);
			throw new ApplicationException("Exception: Error in getting College by PK");
		} finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (Exception ex) {
            }
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Find By PK End");
		return bean;
	}

	public void update(CollegeBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model update Started");
		Connection conn = null;

		CollegeBean beanExist = findByName(bean.getName());

		// Check if updated College already exist
		if (beanExist != null && beanExist.getId() != bean.getId()) {

			throw new DuplicateRecordException("College is already exist");
		}

		PreparedStatement pstmt = null;
		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
            pstmt = conn.prepareStatement(
                    "UPDATE ST_COLLEGE SET NAME=?,ADDRESS=?,STATE=?,CITY=?,PHONE_NO=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");
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
			log.error("Database Exception..", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Error in rollback " + ex.getMessage());
			}
			throw new ApplicationException("Exception: Exception in updating College ");
		} finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception ex) {
            }
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model update End");
	}
    public List search(CollegeBean bean) throws ApplicationException {
        return search(bean, 0, 0);
    }

    public List search(CollegeBean bean, int pageNo, int PageSize) throws ApplicationException {
        log.debug("model search Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_COLLEGE WHERE 1=1");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" AND id = " + bean.getId());
            }
            if (bean.getName() != null && bean.getName().length() > 0) {
                sql.append(" AND NAME like ?");
            }
            if (bean.getAddress() != null && bean.getAddress().length() > 0) {
                sql.append(" AND ADDRESS like ?");
			}
			if (bean.getState() != null && bean.getState().length() > 0) {
				sql.append("AND STATE like'" + bean.getState() + "%'");
			}
			if (bean.getCity() != null && bean.getCity().length() > 0) {
				sql.append(" AND CITY like '" + bean.getCity() + "%'");
			}
			if (bean.getPhoneNo() != null && bean.getPhoneNo().length() > 0) {
				sql.append(" AND PHONE_NO = " + bean.getPhoneNo());
			}
		}
        // if page size is greater than zero then apply pagination
        if (PageSize > 0) {
            // Calculate start record index
            pageNo = (pageNo - 1) * PageSize;
            sql.append(" Limit " + pageNo + "," + PageSize);
        }
        ArrayList list = new ArrayList();
        int index =1;
        Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            if (bean != null) {
                if (bean.getName() != null && bean.getName().length() > 0) {
                   pstmt.setString(index++, bean.getName() + "%");
                }
                if (bean.getAddress() != null && bean.getAddress().length() > 0) {
                    pstmt.setString(index++, bean.getAddress() + "%");
                }
                 if (bean.getCity() != null && bean.getCity().length() > 0) {
                    pstmt.setString(index++, bean.getCity() + "%");
                }
            }
			rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new CollegeBean();

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
				list.add(bean);
			}
			rs.close();
            
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception: Exception in searching college");
		} finally {
             try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (Exception ex) {
            }
            JDBCDataSource.closeConnection(conn);
        }
		log.debug("model search End");
		return list;
	}

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from ST_COLLEGE");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
		if (pageSize > 0) {
            // Calculate start record index
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + "," + pageSize);


		}

		Connection conn = null;
		CollegeBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CollegeBean();
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
				list.add(bean);
			}
			rs.close();
            
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception: Exception in getting list of users");
		} finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (Exception ex) {
            }
            JDBCDataSource.closeConnection(conn);
        }
		log.debug("Model list End");
		return list;
	}
}
