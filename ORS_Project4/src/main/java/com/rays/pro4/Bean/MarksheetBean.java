package com.rays.pro4.Bean;

import com.rays.pro4.DTO.MarksheetDTO;

import java.io.Serializable;

/**
 * Marksheet JavaBean encapsulates Marksheet attributes.
 *
 * @author Lokesh SOlanki
 *
 */ 
public class MarksheetBean extends BaseBean implements Serializable {

	/**
     * Default serial version ID
     */
	private static final long serialVersionUID = 1L;
	private String rollNo;
	private long studentId;
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
		return "MarksheetBean [rollNo=" + rollNo + ", studentId=" + studentId + ", physics="
				+ physics + ", chemistry=" + chemistry + ", maths=" + maths + ", id=" + id + ", createdBy="
				+ createdBy + ", modifiedBy=" + modifiedBy + ", createdDatetime=" + createdDatetime 
				+ ", modifiedDatetime=" + modifiedDatetime + "]";
	}
    public MarksheetDTO getDTO() {
		MarksheetDTO marksheetDTO = new MarksheetDTO();
		marksheetDTO.setId(id);
		marksheetDTO.setRollNo(rollNo);
        marksheetDTO.setMaths(maths);
		marksheetDTO.setStudentId(studentId);
		marksheetDTO.setChemistry(chemistry);
		marksheetDTO.setPhysics(physics);
		return marksheetDTO;
	}

    public void populateBean(MarksheetDTO dto) {
        this.id = dto.getId();
        this.rollNo = dto.getRollNo();
        this.maths = dto.getMaths();
        this.studentId = dto.getStudentId();
        this.chemistry = dto.getChemistry();
        this.physics = dto.getPhysics();
        this.createdBy = dto.getCreatedBy();
    }
}
