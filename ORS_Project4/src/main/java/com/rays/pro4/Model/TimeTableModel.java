package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.rays.pro4.DTO.TimeTableDTO;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.Bean.SubjectBean;
import com.rays.pro4.Bean.TimeTableBean;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.JDBCDataSource;

/**
 * The Class TimeTableModel.
 * 
 * @author Lokesh SOlanki
 *
 */
public class TimeTableModel extends BaseModel {

	public String getTableName() {
		return "ST_TIMETABLE";
	}
	
	public TimeTableDTO populateBean(ResultSet rs, TimeTableDTO dto) throws SQLException{
		
		dto.setId(rs.getLong(1));
		dto.setCourseId(rs.getLong(2));
		dto.setCourseName(rs.getString(3));
		dto.setSubjectId(rs.getLong(4));
		dto.setSubjectName(rs.getString(5));
		dto.setSemester(rs.getString(6));
		dto.setExamDate(rs.getDate(7));
		dto.setExamTime(rs.getString(8));
		dto.setCreatedBy(rs.getString(9));
		dto.setModifiedBy(rs.getString(10));
		dto.setCreatedDatetime(rs.getTimestamp(11));
		dto.setModifiedDatetime(rs.getTimestamp(12));
		return dto;
		
	}

	

    
	public long add(TimeTableDTO bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");

		CourseModel cModel = new CourseModel();
		CourseBean CourseBean = cModel.findByPK(bean.getCourseId());
		bean.setCourseName(CourseBean.getName());
		
		SubjectModel smodel = new SubjectModel();
		SubjectBean subjectBean =smodel.findByPK(bean.getSubjectId());
		bean.setSubjectName(subjectBean.getSubjectName());
		TimeTableDTO bean1 = checkByCourseDate(bean.getCourseId(), bean.getSemester(),  new java.sql.Date(bean.getExamDate().getTime()));

		TimeTableBean bean2 = checkBySemesterSubject(bean.getCourseId(), bean.getSubjectId(), bean.getSemester());
		if(bean1 != null || bean2 != null){ 
			throw new DuplicateRecordException("TimeTable Already Exsist"); 
		}        
       long pk = 0;
       Connection conn = null;
        try (Connection con = JDBCDataSource.getConnection()){
             conn = con;
            pk = nextPK();            
            conn.setAutoCommit(false);
            try(PreparedStatement pstmt = conn.prepareStatement("INSERT st_timetable values(?,?,?,?,?,?,?,?,?,?,?,?)")){
                pstmt.setInt(1, (int)pk);
                pstmt.setLong(2, bean.getCourseId());
                pstmt.setString(3, bean.getCourseName());
                pstmt.setLong(4, bean.getSubjectId());
                pstmt.setString(5, bean.getSubjectName());
                pstmt.setString(6, bean.getSemester());
                pstmt.setDate(7, new java.sql.Date(bean.getExamDate().getTime()));
                pstmt.setString(8, bean.getExamTime());
                pstmt.setString(9, bean.getCreatedBy());
                pstmt.setString(10, bean.getModifiedBy());
                pstmt.setTimestamp(11, bean.getCreatedDatetime());
                pstmt.setTimestamp(12, bean.getModifiedDatetime());
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (Exception e) {
            log.error("Database Exception....", e);
            try {
                if(conn!=null)
                    conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : add rollback Exception" + ex.getMessage());
            }
            throw new ApplicationException("Exception : Exception in add timetable");
        }finally {
             JDBCDataSource.closeConnection(conn);
            log.debug("Model add End");
        }
        return pk;

    }

    public void delete(TimeTableDTO dto) throws ApplicationException { 
        log.debug("Model delete Started");      
        Connection conn = null;
        try {
                
                conn = JDBCDataSource.getConnection();
                TimeTableDTO existingDto = findByPK(dto.getId());
               if (existingDto != null) {
                 conn.setAutoCommit(false);
                 try (PreparedStatement pstmt = conn.prepareStatement("delete from ST_timetable where ID=?")) {
                     pstmt.setLong(1, dto.getId());
                     pstmt.executeUpdate();
                     conn.commit();
                 }
                 
                }
        } catch (Exception e) {
           log.error("Database Exception...", e);
            try {
                if(conn !=null)
                    conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : delete Rollback Exception" + ex.getMessage());
            }finally{
               
            }
            throw new ApplicationException("Exception : Exception in delete TimeTable");

	}finally{
           JDBCDataSource.closeConnection(conn);
            log.debug("Model delete End");
        }
	}

	public void update(TimeTableDTO bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model update Started");
	    CourseModel cModel = new CourseModel();
		CourseBean CourseBean = cModel.findByPK(bean.getCourseId());

		SubjectModel smodel = new SubjectModel();
		SubjectBean subjectBean = smodel.findByPK(bean.getSubjectId());
		bean.setSubjectName(subjectBean.getSubjectName());

        TimeTableDTO bean1 = checkByCourseDate(bean.getCourseId(), bean.getSemester(), new java.sql.Date(bean.getExamDate().getTime()));
        TimeTableDTO bean2 = checkBySemesterSubject(bean.getCourseId(), bean.getSubjectId(), bean.getSemester());
        if (bean1 != null || bean2 != null) {
			throw new DuplicateRecordException("TimeTable Already Exsist");		
	}
    	Connection conn = null;
	    try (Connection con = JDBCDataSource.getConnection();){
            conn = con;
            conn.setAutoCommit(false);
            try(PreparedStatement pstmt = conn.prepareStatement(
                    "update ST_timetable set course_id=?,course_name=?,subject_id=?,subject_name=?,semester=?,exam_date=?,exam_time=?,created_by=?,modified_by=?,created_datetime=?,modified_datetime=? where ID=?")){
            
                pstmt.setLong(1, bean.getCourseId());
                pstmt.setString(2, bean.getCourseName());
                pstmt.setLong(3, bean.getSubjectId());
                pstmt.setString(4, bean.getSubjectName());
                pstmt.setString(5, bean.getSemester());
                pstmt.setDate(6, new java.sql.Date(bean.getExamDate().getTime()));
                pstmt.setString(7, bean.getExamTime());
                pstmt.setString(8, bean.getCreatedBy());
                pstmt.setString(9, bean.getModifiedBy());
                pstmt.setTimestamp(10, bean.getCreatedDatetime());
                pstmt.setTimestamp(11, bean.getModifiedDatetime());
                pstmt.setLong(12, bean.getId());
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (Exception e) {
			log.error("Database Exception....", e);
			try{
				if(conn !=null)
                    conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : update rollback Exception" + ex.getMessage());
			}
            throw new ApplicationException("Exception in updating timetable");
		}finally{
            JDBCDataSource.closeConnection(conn);
		    log.debug("Model update End");
        }
	}

	public TimeTableDTO findByPK(long pk) throws ApplicationException {
		log.debug("Model findBypk started");
		StringBuffer sql = new StringBuffer("select * from ST_timetable where id=?");
		TimeTableDTO dto = null;

        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString());){
                pstmt.setLong(1, pk);
            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    dto = populateBean(rs, new TimeTableDTO());
                }
             }
            
		} catch (Exception e) {
			log.error("Database Exception in finding by PK", e);
			throw new ApplicationException("Exception : Exception in getting by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findBypk End");
		return dto;
	}

	public List list() throws ApplicationException {
		return list(0, 0,null,null);
	}

    public List list(int pageNo, int pageSize, String orderBy, String sortOrder) throws ApplicationException {
        log.debug("model list Started");
        ArrayList list = new ArrayList();
        StringBuffer sql = new StringBuffer("select * from ST_timetable");
        if (orderBy != null && orderBy.trim().length() > 0) {
            sql.append(" ORDER BY " + orderBy + " " + sortOrder);
        } else {
            sql.append(" ORDER BY CourseName ASC ");sortOrder="ASC";
        }
        Connection conn = null;
		if (pageNo > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}
		try (Connection con = JDBCDataSource.getConnection();PreparedStatement pstmt = con.prepareStatement(sql.toString());
			 ResultSet rs = pstmt.executeQuery();){

			while (rs.next()) {

				TimeTableDTO dto = populateBean(rs, new TimeTableDTO());
				list.add(dto);
			}
		} catch (Exception e) {
			log.error("Database Exception...", e);
			throw new ApplicationException("Exception : Exception in getting list");
		}finally{
            JDBCDataSource.closeConnection(conn);
        }

		log.debug("Model list End");
		return list;
	}

    public List search(TimeTableBean bean, int pageNo, int pageSize, String orderBy, String sortOrder) throws ApplicationException {
		log.debug("Model search started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_TIMETABLE WHERE 1=1 ");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id = ?");
			}
			if (bean.getCourseId() > 0) {
				sql.append(" AND Course_ID = ?");
			}
			if (bean.getCourseName() != null && bean.getCourseName().length() > 0) {
				sql.append(" AND courseName LIKE ?");
			}
			if (bean.getSubjectId() > 0) {
				sql.append(" AND Subject_ID = ?");
			}
			if (bean.getSubjectName() != null && bean.getSubjectName().length() > 0) {
				sql.append(" AND subjectName LIKE ?");
			}
			if (bean.getExamDate() != null && bean.getExamDate().getTime() > 0) {
				sql.append(" AND Exam_Date = ?");
			}
			if (bean.getExamTime() != null && bean.getExamTime().length() > 0) {
				sql.append(" AND EXAM_TIME LIKE ?");
			}
		}

		if (orderBy != null && orderBy.trim().length() > 0) {
			sql.append(" ORDER BY " + orderBy + " " + sortOrder);
		} else {
			sql.append(" ORDER BY CourseName ASC ");
		}

		if (pageNo > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" LIMIT " + pageNo + "," + pageSize);
		}

		ArrayList<TimeTableDTO> list = new ArrayList<TimeTableDTO>();
		try (Connection conn = JDBCDataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
			int index = 1;
			if (bean != null) {
				if (bean.getId() > 0) pstmt.setLong(index++, bean.getId());
				if (bean.getCourseId() > 0) pstmt.setLong(index++, bean.getCourseId());
				if (bean.getCourseName() != null && bean.getCourseName().length() > 0) pstmt.setString(index++, bean.getCourseName() + "%");
				if (bean.getSubjectId() > 0) pstmt.setLong(index++, bean.getSubjectId());
				if (bean.getSubjectName() != null && bean.getSubjectName().length() > 0) pstmt.setString(index++, bean.getSubjectName() + "%");
				if (bean.getExamDate() != null && bean.getExamDate().getTime() > 0) pstmt.setDate(index++, new Date(bean.getExamDate().getTime()));
				if (bean.getExamTime() != null && bean.getExamTime().length() > 0) pstmt.setString(index++, bean.getExamTime() + "%"); // Fixed typo ExamTime to examTime
			}
			try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) list.add(populateBean(rs, new TimeTableDTO()));
            }
		} catch (Exception e) {
			log.error("Database Exception in search.....", e);
			throw new ApplicationException("Exception in getting search");
		}
		log.debug("Model search End");
		return list;
	}

	public TimeTableDTO checkBySemesterSubject(long CourseId, long SubjectId, String semester) throws ApplicationException {
		Connection conn = null;
		TimeTableDTO dto = null;
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ST_TIMETABLE WHERE Course_ID=? AND Subject_ID=? AND Semester=?");
        
		try (Connection con = JDBCDataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql.toString());) {
            ps.setLong(1, CourseId);
            ps.setLong(2, SubjectId);
            ps.setString(3, semester);
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    dto = populateBean(rs,new TimeTableDTO());
                }
            }
		} catch (Exception e) {
			log.error("Database Exception....", e);
			throw new ApplicationException("Exception in list Method of timetable Model");
		}
		return dto;
	}

	public TimeTableDTO checkByCourseDate(long CourseId, String Semester, java.sql.Date ExamDate) throws ApplicationException {

		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ST_TIMETABLE WHERE Course_Id=? AND semester=? AND Exam_Date=?");

		TimeTableDTO dto = null;
		java.util.Date ExDate = new java.util.Date(ExamDate.getTime());

        try (Connection conn = JDBCDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString());) {
            ps.setLong(1, CourseId);
            ps.setString(2, Semester);
            ps.setDate(3, ExamDate);
             try(ResultSet rs = ps.executeQuery();){
                 while (rs.next()) dto = populateBean(rs, new TimeTableDTO());
            }
             
         } catch (Exception e) {

			log.error("database Exception....", e);
			throw new ApplicationException("Exception in list Method of timetable Model");
		}
		return dto;

	}

	public TimeTableDTO checkBysemester(long CourseId, long SubjectId, String semester,
			java.util.Date ExamDAte) throws ApplicationException{
        TimeTableDTO dto = null;
        try (Connection con = JDBCDataSource.getConnection();) {
            StringBuffer sql = new StringBuffer(
                    "SELECT * FROM ST_TIMETABLE WHERE COURSE_ID=? AND SUBJECT_ID=? AND" + " SEMESTER=?");

            try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
                ps.setLong(1, CourseId);
                ps.setLong(2, SubjectId);
                ps.setString(3, semester);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) dto = populateBean(rs, new TimeTableDTO());
                }
            }
		} catch (Exception e) {
            log.error("Database Exception....", e);
            throw new ApplicationException("Exception in checkBysemester Method of timetable Model");
		}
		return dto;
	}

	public TimeTableDTO checkByCourseDateAndTime(long CourseId, java.util.Date ExamDate) throws ApplicationException {
        TimeTableDTO dto = null;
		try (Connection con = JDBCDataSource.getConnection();) {

			StringBuffer sql = new StringBuffer("SELECT * FROM ST_TIMETABLE WHERE COURSE_ID=? " + "AND EXAM_DATE=?");

			try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
                ps.setLong(1, CourseId);
                ps.setDate(2, new java.sql.Date(ExamDate.getTime()));
                try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						dto = populateBean(rs, new TimeTableDTO());
					}
				}
            }
		} catch (Exception e) {
			log.error("database Exception....", e);
            throw new ApplicationException("Exception in checkByCourseDateAndTime Method of timetable Model");
		}
		return dto;
	}
}
