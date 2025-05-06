package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import com.rays.pro4.DTO.CollegeDTO;
import com.rays.pro4.Bean.CollegeBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;


public class CollegeModel extends BaseModel {

	private static Logger log = Logger.getLogger(CollegeModel.class);
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
            throw new DatabaseException("Exception : Exception in getting PK");
        }
        log.debug("Model nextPK End " + (pk + 1));
        return pk + 1;
    }

    public long add(CollegeDTO dto) throws ApplicationException, DuplicateRecordException {
        log.debug("Model add Started " + dto);
        Connection conn = null;
         long pk = 0;
        CollegeDTO duplicataRole = findByName(dto.getName());
        if (duplicataRole != null) {
            throw new DuplicateRecordException("College Name already exists");
        }
        try  {
              conn = JDBCDataSource.getConnection();
            pk = nextPK();
            conn.setAutoCommit(false);
            dto.setId(pk);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            dto.setCreatedDatetime(now);
            dto.setModifiedDatetime(now);

            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO ST_COLLEGE (ID,NAME,ADDRESS,STATE,CITY,PHONE_NO,CREATED_BY,MODIFIED_BY,CREATED_DATETIME,MODIFIED_DATETIME) " +
                            "VALUES(?,?,?,?,?,?,?,?,?,?)")) {
                pstmt.setLong(1, pk);
                pstmt.setString(2, dto.getName());
                pstmt.setString(3, dto.getAddress());
                pstmt.setString(4, dto.getState());
                pstmt.setString(5, dto.getCity());
                pstmt.setString(6, dto.getPhoneNo());
                pstmt.setString(7, dto.getCreatedBy());
                pstmt.setString(8, dto.getModifiedBy());
                pstmt.setTimestamp(9, dto.getCreatedDatetime());
                pstmt.setTimestamp(10, dto.getModifiedDatetime());
                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
               throw new ApplicationException("Exception : add rollback exception " + e1.getMessage());
            }
            log.error("Database Exception in add college", e);
            throw new ApplicationException("Exception: Exception in adding college - " + e.getMessage());
        } 
        log.debug("Model add End " + dto);
        return pk;
    }
    
public void delete(CollegeDTO dto) throws ApplicationException {
		log.debug("Model delete Started");
		try (Connection conn = JDBCDataSource.getConnection()) {
            conn.setAutoCommit(false); // Start transaction
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_COLLEGE WHERE ID=?")) {
                pstmt.setLong(1, dto.getId());
                pstmt.executeUpdate();
            }
            conn.commit(); // Commit transaction
		} catch (SQLException e) {
			log.error("Database Exception in delete college", e);
			JDBCDataSource.trnRollback();
			throw new ApplicationException("Exception :Exception in delete college " + e.getMessage());
		}
		log.debug("Modal delete End");
	}

    public void delete(long id) throws ApplicationException,DatabaseException {
        log.debug("Model delete by ID Started");
        CollegeDTO dto = findByPK(id);
        delete(dto);
        log.debug("Model delete by ID End ");
    }
    
	public CollegeDTO findByName(String name) throws ApplicationException {
        log.debug("Model findByName Started");
        CollegeDTO dto = null;
        try (Connection conn = JDBCDataSource.getConnection();PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ST_COLLEGE WHERE NAME=?")) {
            pstmt.setString(1, name);// Set parameter for name
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    dto = new CollegeDTO();
                    populateBean(rs, dto);
                }
                rs.close();
            }
        } catch (SQLException e) {
            log.error("Database Exception in find by Name ",e);
			throw new ApplicationException("Exception : Exception in getting College by Name");
        }
        return dto;
    }

    public CollegeDTO findByPK(long pk) throws ApplicationException {
        log.debug("Model findByPK Started");
        CollegeDTO dto = null;
        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ST_COLLEGE WHERE ID=?")) {
            pstmt.setLong(1, pk);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                   dto = new CollegeDTO();
                   populateBean(rs,dto);
                }
                rs.close();
              }
          } catch (SQLException e) {
              log.error("Database Exception in findByPK" + e);
              throw new ApplicationException("Exception: Error in getting College by PK - " + e.getMessage());
          }
        log.debug("Find By PK End");

        return dto;

    }

    public void update(CollegeDTO dto) throws ApplicationException, DuplicateRecordException {
    	log.debug("Model update Started");
        CollegeDTO existing = findByName(dto.getName()) != null? findByName(dto.getName()): null;
        if (existing != null && existing.getId() != dto.getId()) {
            throw new DuplicateRecordException("College already exists");
        }
        Connection conn = null;
        try {
               conn = JDBCDataSource.getConnection();
               conn.setAutoCommit(false);
           try( PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE ST_COLLEGE SET NAME=?,ADDRESS=?,STATE=?,CITY=?,PHONE_NO=?,MODIFIED_BY=?,MODIFIED_DATETIME=? WHERE ID=?")) {
                pstmt.setString(1, dto.getName());              pstmt.setString(2, dto.getAddress());
                pstmt.setString(3, dto.getState());
                pstmt.setString(4, dto.getCity());
                pstmt.setString(5, dto.getPhoneNo());
                pstmt.setString(6, dto.getModifiedBy());
                pstmt.setTimestamp(7, dto.getModifiedDatetime());
                pstmt.setLong(8, dto.getId());
                pstmt.executeUpdate();
            
            } conn.commit();
        } catch(Exception e) {
        	log.error("Database Exception", e);
              conn.rollback();
            throw new ApplicationException("Exception: Error updating college - " + e.getMessage());
        } finally {
            conn.setAutoCommit(true);
        }
        log.debug("Model update End");
    }
    public List search(CollegeBean bean) throws ApplicationException {
        return search(bean, 0, 0, null, null);
    }
    public List search(CollegeDTO dto, int pageNo, int pageSize, String orderBy, String sortOrder)
            throws ApplicationException {
        log.debug("Model search Started");
        List<CollegeDTO> list = new ArrayList<>();
       
        int index = 1;

        StringBuilder sql = new StringBuilder("SELECT * FROM ST_COLLEGE WHERE 1=1");
        if (dto != null) {
            if (dto.getId() > 0) sql.append(" AND ID=?");
            if (dto.getName() != null && !dto.getName().isEmpty()) sql.append(" AND NAME LIKE ?");
            if (dto.getAddress() != null && !dto.getAddress().isEmpty())
				sql.append(" AND ADDRESS like ?");
			if (dto.getCity() != null && !dto.getCity().isEmpty())
				sql.append(" AND CITY like ?");
		}
        if (orderBy != null && !orderBy.isEmpty()) {
        	sql.append(" ORDER BY ").append(orderBy);
            sql.append(" ").append("DESC".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC");
        } 
         sql.append(" ORDER BY NAME ASC");

        if (pageSize > 0) {
            pageNo = Math.max(1, pageNo);
            int offset = (pageNo - 1) * pageSize;
            sql.append(" LIMIT ").append(offset).append(",").append(pageSize);
        }

        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {            if (dto != null) {
                if (dto.getId() > 0) pstmt.setLong(index++, dto.getId());
                if (dto.getName() != null && dto.getName().length() > 0) pstmt.setString(index++, dto.getName()+"%");
				if (dto.getAddress() != null && dto.getAddress().length() > 0) pstmt.setString(index++, dto.getAddress() + "%");
				if (dto.getCity() != null && dto.getCity().length() > 0) pstmt.setString(index++, dto.getCity() + "%");
            
			}

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                 
                    populateBean(rs, dto);
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            log.error("Exception in college Search ", e);
            throw new ApplicationException("Exception: Error searching college - " + e.getMessage());
        }

        log.debug("Model search End");
        return list;
    }
    
    public List<CollegeDTO> list() throws ApplicationException {
        return list(1, 0, null, null);
    }

    public List list(int pageNo, int pageSize, String orderBy, String sortOrder) throws ApplicationException {    
     log.debug("Model list Started");


        List<CollegeDTO> list = new ArrayList<CollegeDTO>();
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_COLLEGE");

        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ").append(orderBy);
            sql.append(" ").append("DESC".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC");
        } else {
            sql.append(" ORDER BY NAME ASC");
        }

        if (pageSize > 0) {
            pageNo = Math.max(1, pageNo);
            int offset = (pageNo - 1) * pageSize;
            sql.append(" limit ").append(offset).append(",").append(pageSize);
        }

        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString());
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                CollegeDTO dto = new CollegeDTO();
                populateBean(rs, dto);
                list.add(dto);
            }
        }
        
        catch (Exception e) {
            log.error("Database Exception in List",e);
            throw new ApplicationException("Exception in get list of college" + e.getMessage());
        }
        log.debug("Model list End");
        return list;
    
    }
    @Override
    public String getTableName() {
        return "ST_COLLEGE";
    }
    

    private void populateBean(ResultSet rs, CollegeDTO dto) throws SQLException {
		dto.setId(rs.getLong(1));
		dto.setName(rs.getString(2));
		dto.setAddress(rs.getString(3));
		dto.setState(rs.getString(4));
		dto.setCity(rs.getString(5));
		dto.setPhoneNo(rs.getString(6));
		dto.setCreatedBy(rs.getString(7));
		dto.setModifiedBy(rs.getString(8));
		dto.setCreatedDatetime(rs.getTimestamp(9));
		dto.setModifiedDatetime(rs.getTimestamp(10));
	}
}