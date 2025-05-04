package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
	
	public TimeTableBean populate(ResultSet rs) throws SQLException{
		
			TimeTableBean bean = new TimeTableBean();
			bean.setId(rs.getLong(1));
			bean.setCourseId(rs.getLong(2));
			bean.setCourseName(rs.getString(3));
			bean.setSubjectId(rs.getLong(4));
			bean.setSubjectName(rs.getString(5));
		    bean.setSemester(rs.getString(6));
			bean.setExamDate(rs.getDate(7));
			bean.setExamTime(rs.getString(8));
			bean.setCreatedBy(rs.getString(9));
			bean.setModifiedBy(rs.getString(10));
			bean.setCreatedDatetime(rs.getTimestamp(11));
			bean.setModifiedDatetime(rs.getTimestamp(12));
		}
		
	}

	

	public long add(TimeTableBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");
		
		CourseModel cModel = new CourseModel();
		CourseBean CourseBean = cModel.FindByPK(bean.getCourseId());
		bean.setCourseName(CourseBean.getName());
		
		SubjectModel smodel = new SubjectModel();
		SubjectBean SubjectBean = smodel.FindByPK(bean.getSubjectId());
		bean.setSubjectName(SubjectBean.getSubjectName());
		
		TimeTableBean bean1 = checkByCourseDate(bean.getCourseId(), bean.getSemester(),  new java.sql.Date(bean.getExamDate().getTime()));
		TimeTableBean bean2 = checkBySemesterSubject(bean.getCourseId(), bean.getSubjectId(), bean.getSemester());
		if(bean1 != null || bean2 != null){ 
			throw new DuplicateRecordException("TimeTable Already Exsist"); 
		}
		
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK(conn);
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("INSERT st_timetable values(?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
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
		} catch (Exception e) {
			log.error("Database Exception....", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : add rollback Exception" + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add timetable");
		} 
		log.debug("Model add End");
		return pk;

	}

	public void delete(TimeTableBean bean) throws ApplicationException{
		log.debug("Model delete Started");
		try{
			TimeTableBean beann = findByPK(bean.getId());
			if(beann != null){
				Connection conn = null;
				conn = JDBCDataSource.getConnection();
				conn.setAutoCommit(false);
				PreparedStatement pstmt = conn.prepareStatement("delete from ST_timetable where ID=?");
				pstmt.setLong(1, bean.getId());
				pstmt.executeUpdate();
				conn.commit();
			}
		} catch (Exception e) {
			log.error("Database Exception...", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : delete Rollback Exception" + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete TimeTable");
		
		}
		log.debug("Model delete End");
	}

	public void update(TimeTableBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model update Started");
		Connection conn = null;

		CourseModel cModel = new CourseModel();
		CourseBean CourseBean = cModel.FindByPK(bean.getCourseId());
		bean.setCourseName(CourseBean.getName());

		SubjectModel smodel = new SubjectModel();
		SubjectBean SubjectBean = smodel.FindByPK(bean.getSubjectId());
		bean.setSubjectName(SubjectBean.getSubjectName());

		TimeTableBean bean1 = checkByCourseDate(bean.getCourseId(), bean.getSemester(),
				new java.sql.Date(bean.getExamDate().getTime()));
		TimeTableBean bean2 = checkBySemesterSubject(bean.getCourseId(), bean.getSubjectId(), bean.getSemester());
		if (bean1 != null || bean2 != null) {
			throw new DuplicateRecordException("TimeTable Already Exsist");
		}
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update ST_timetable set course_id=?,course_name=?,subject_id=?,subject_name=?,semester=?,exam_date=?,exam_time=?,created_by=?,modified_by=?,created_datetime=?,modified_datetime=? where ID=?");
			pstmt.setLong(1, bean.getCourseId());
			pstmt.setString(2, bean.getCourseName());
			pstmt.setLong(3, bean.getSubjectId());
			pstmt.setString(4, bean.getSubjectName());
			pstmt.setString(5, bean.getSemester());
			pstmt.setString(7, bean.getExamTime());
			pstmt.setString(8, bean.getCreatedBy());
			pstmt.setString(9, bean.getModifiedBy());
			pstmt.setTimestamp(10, bean.getCreatedDatetime());
			pstmt.setTimestamp(11, bean.getModifiedDatetime());
			pstmt.setLong(12, bean.getId());

			pstmt.setDate(6, new java.sql.Date(bean.getExamDate().getTime()));
			pstmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception....", e);
			try{
				conn.rollback();

			} catch (Exception ex) {
				throw new ApplicationException("Exception : update rollback Exception" + ex.getMessage());
			}
			// throw new ApplicationException("Exception in updating timetable");
		} finally {
		log.debug("Model update End");
	}
	}

	public TimeTableBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findBypk started");
		StringBuffer sql = new StringBuffer("select * from ST_timetable where id=?");
		TimeTableBean bean = null;
		Connection conn = null;
		try (Connection con = JDBCDataSource.getConnection()){
			conn = con;
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) bean = populate(rs);
		} catch (Exception e) {
			log.error("Database Exception .....", e);
			throw new ApplicationException("Exception : Exception in getting by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findBypk End");
		return bean;
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
            sql.append(" ORDER BY CourseName ASC ");
        }

		if (pageNo > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}
		Connection conn = null;
		try (Connection con = JDBCDataSource.getConnection()){
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				TimeTableBean bean = populate(rs);

				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception...", e);
			// throw new ApplicationException("Exception : Exception in getting list");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model list End");
		return list;
	}

	public List search(TimeTableBean bean, int pageNo, int pageSize, String orderBy, String sortOrder) throws ApplicationException {
		log.debug("Model search started");
        if (orderBy != null && orderBy.length() > 0) {
            orderBy = orderBy;
        } else {
            orderBy = "CourseName";
		StringBuffer sql = new StringBuffer("select * from ST_timetable where 1=1 ");
		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append("AND id =" + bean.getId());
			}
			if (bean.getCourseId() > 0) {
				sql.append(" AND Course_ID =" + bean.getCourseId());
			}
			if (bean.getCourseName() != null && bean.getCourseName().length() > 0) {
				sql.append("AND courseName like '" + bean.getCourseName() + "%'");
			}
			if (bean.getSubjectId() > 0) {
				sql.append(" AND Subject_ID =" + bean.getSubjectId());
			}
			if (bean.getSubjectName() != null && bean.getSubjectName().length() > 0) {
				sql.append("AND subjectName like '" + bean.getSubjectName() + "%'");
			}
			if (bean.getExamDate() != null && bean.getExamDate().getTime() > 0) {
				Date d = new Date(bean.getExamDate().getTime());
				sql.append("AND Exam_Date = '" + DataUtility.getDateString(d) + "'");
			}

			if (bean.getExamTime() != null && bean.getExamTime().length() > 0) {
				sql.append("AND EXAM_TIME like '" + bean.getExamTime() + "%'");
			}
		}
        if (orderBy != null && orderBy.trim().length() > 0) {
            sql.append(" ORDER BY " + orderBy + " " + sortOrder);
        } else {
            sql.append(" ORDER BY CourseName ASC ");

		}
		if (pageNo > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		ArrayList list = new ArrayList();

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
            ArrayList list = new ArrayList();
          
			while (rs.next()) {
				list.add(populate(rs));			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception.....", e);
			// throw new ApplicationException("Exception in getting search");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model search End");
		return list;

	}

	public TimeTableBean checkBySemesterSubject(long CourseId, long SubjectId, String semester) throws ApplicationException {
		Connection conn = null;
		TimeTableBean bean = null;
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ST_TIMETABLE WHERE Course_ID=? AND Subject_ID=? AND Semester=?");

		try {
			Connection con = JDBCDataSource.getConnection();
			PreparedStatement ps = con.prepareStatement(sql.toString());
			ps.setLong(1, CourseId);
			ps.setLong(2, SubjectId);
			ps.setString(3, semester);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				bean = populate(rs);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception....", e);
			throw new ApplicationException("Exception in list Method of timetable Model");
		} 
		return bean;
	}

	public TimeTableBean checkByCourseDate(long CourseId, String Semester, Date ExamDate) throws ApplicationException {

		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ST_TIMETABLE WHERE Course_Id=? AND semester=? AND Exam_Date=?");

		TimeTableBean bean = null;
		Date ExDate = new Date(ExamDate.getTime());

		try (Connection conn = JDBCDataSource.getConnection()){
			Connection con = JDBCDataSource.getConnection();
			PreparedStatement ps = con.prepareStatement(sql.toString());
			ps.setLong(1, CourseId);
			ps.setString(2, Semester);
			ps.setDate(3, (java.sql.Date) ExamDate);
			ResultSet rs = ps.executeQuery();

            TimeTableBean bean = null;
			while (rs.next()) {
				bean = populate(rs);
			}
			rs.close();
		} catch (Exception e) {
			log.error("database Exception....", e);
			throw new ApplicationException("Exception in list Method of timetable Model");
		} 
		return bean;

	}

	public TimeTableBean checkBysemester(long CourseId, long SubjectId, String semester,
			java.util.Date ExamDAte) {

		TimeTableBean bean = null;
		try (Connection con = JDBCDataSource.getConnection()){
			StringBuffer sql = new StringBuffer("SELECT * FROM TIMETABLE WHERE COURSE_ID=? AND SUBJECT_ID=? AND" + " SEMESTER=?");
			Connection con = JDBCDataSource.getConnection();
			PreparedStatement ps = con.prepareStatement(sql.toString());
			ps.setLong(1, CourseId);
			ps.setLong(2, SubjectId);
			ps.setString(3, semester);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) bean = populate(rs);
		} catch (Exception e) {
			log.error("database Exception....", e);
		}
		return bean;
	}

	public TimeTableBean checkByCourseDateAndTime(long CourseId, java.util.Date ExamDate) {
		TimeTableBean bean = null;
		try(Connection con = JDBCDataSource.getConnection()){
			StringBuffer sql = new StringBuffer("SELECT * FROM TIMETABLE WHERE COURSE_ID=? " + "AND EXAM_DATE=?");
			PreparedStatement ps = con.prepareStatement(sql.toString());
			ps.setLong(1, CourseId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) bean = populate(rs);
		} catch (Exception e){
			log.error("database Exception....", e);
		}
		return bean;
	}

}
