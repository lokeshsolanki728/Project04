package com.rays.pro4.Bean;


import com.rays.pro4.Util.DataUtility;

/**
 * Marksheet JavaBean encapsulates Marksheet attributes.
 * 
 * @author Lokesh SOlanki
 *
 */
import javax.servlet.http.HttpServletRequest;
 */
public class MarksheetBean extends BaseBean{

	private String rollNo;
	private long studentId;
	private String name;
	private int physics;
    private int chemistry;
	private int maths;
	
    /**
	 * Gets the roll number of the marksheet.
	 *
	 * @return The roll number of the marksheet.
	 */
	public String getRollNo() {
		return rollNo;
	}
	/**
	 * Sets the roll number of the marksheet.
	 *
	 * @param rollNo The roll number to set.
	 */
	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
	/**
	 * Gets the student ID associated with the marksheet.
	 *
	 * @return The student ID.
	 */
	public long getStudentId() {
		return studentId;
	}
	/**
	 * Sets the student ID associated with the marksheet.
	 *
	 * @param studentId The student ID to set.
	 */
	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}
	/**
	 * Gets the name associated with the marksheet.
	 *
	 * @return The name associated with the marksheet.
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name associated with the marksheet.
	 *
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Gets the marks in physics.
	 *
	 * @return The marks in physics.
	 */
	public int getPhysics() {
		return physics;
	}
	/**
	 * Sets the marks in physics.
	 *
	 * @param physics The marks in physics to set.
	 */
	public void setPhysics(int physics) {
		this.physics = physics;
	}
	/**
	 * Gets the marks in chemistry.
	 *
	 * @return The marks in chemistry.
	 */
	public int getChemistry() {
		return chemistry;
	}
	/**
	 * Sets the marks in chemistry.
	 *
	 * @param chemistry The marks in chemistry to set.
	 */
	public void setChemistry(int chemistry) {
		this.chemistry = chemistry;
	}
	/**
	 * Gets the marks in maths.
	 *
	 * @return The marks in maths.
	 */
	public int getMaths() {
		return maths;
	}
	/**
	 * Sets the marks in maths.
	 *
	 * @param maths The marks in maths to set.
	 */
	public void setMaths(int maths) {
		this.maths = maths;
	}
	/**
	 * Returns the key (ID) of the marksheet as a String.
	 *
	 * @return The key (ID) of the marksheet as a String.
	 */
	@Override
	public String getkey() {
		return String.valueOf(id);
	}
	/**
	 * Returns the value (roll number) of the marksheet.
	 *
	 * @return The value (roll number) of the marksheet.
	 */
	@Override
	public String getValue() {
		return rollNo;
	}
	/**
	 * Returns a string representation of the MarksheetBean.
	 *
	 * @return A string representation of the MarksheetBean.
	 */
	@Override
	public String toString() {
		return "MarksheetBean [rollNo=" + rollNo + ", studentId=" + studentId + ", name=" + name + ", physics="
				+ physics + ", chemistry=" + chemistry + ", maths=" + maths + ", id=" + id + ", createdBy="
				+ createdBy + ", modifiedBy=" + modifiedBy + ", createdDatetime=" + createdDatetime
				+ ", modifiedDatetime=" + modifiedDatetime + "]";
	}
	/**
     * Populate bean object from request parameters
     * @param request the request
     */
    @Override
    public void populate(HttpServletRequest request) {
        setId(DataUtility.getLong(request.getParameter("id")));
        
        setRollNo(DataUtility.getString(request.getParameter("rollNo")));
        setStudentId(DataUtility.getLong(request.getParameter("studentId")));
        setName(DataUtility.getString(request.getParameter("name")));
        setPhysics(DataUtility.getInt(request.getParameter("physics")));
        setChemistry(DataUtility.getInt(request.getParameter("chemistry")));
        setMaths(DataUtility.getInt(request.getParameter("maths")));
        setCreatedBy(DataUtility.getString(request.getParameter("createdBy")));
        setModifiedBy(DataUtility.getString(request.getParameter("modifiedBy")));
        setCreatedDatetime(DataUtility.getTimestamp(request.getParameter("createdDatetime")));
        setModifiedDatetime(DataUtility.getTimestamp(request.getParameter("modifiedDatetime")));
       
    }
}
