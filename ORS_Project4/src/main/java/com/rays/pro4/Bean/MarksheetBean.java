package com.rays.pro4.Bean;

/**
 * Marksheet JavaBean encapsulates Marksheet attributes.
 * 
 * @author Lokesh SOlanki
 *
 */
public class MarksheetBean extends BaseBean{

	private String rollNo;
	private long studentId;
	private String name;
	private Integer physics;
	private Integer chemistry;
	private Integer maths;
	public String getRollNo() {
		return rollNo;
	}
	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
	public long getStudentId() {
		return studentId;
	}
	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPhysics() {
		return physics;
	}
	public void setPhysics(Integer physics) {
		this.physics = physics;
	}
	public Integer getChemistry() {
		return chemistry;
	}
	public void setChemistry(Integer chemistry) {
		this.chemistry = chemistry;
	}
	public Integer getMaths() {
		return maths;
	}
	public void setMaths(Integer maths) {
		this.maths = maths;
	}
	@Override
	public String getkey() {
		return String.valueOf(id);
	}
	@Override
	public String getValue() {
		return rollNo;
	}
	@Override
	public String toString() {
		return "MarksheetBean [rollNo=" + rollNo + ", studentId=" + studentId + ", name=" + name + ", physics="
				+ physics + ", chemistry=" + chemistry + ", maths=" + maths + ", id=" + id + ", createdBy=" + createdBy
				+ ", modifiedBy=" + modifiedBy + ", createdDatetime=" + createdDatetime + ", modifiedDatetime="
				+ modifiedDatetime + "]";
	}
	
	
}
