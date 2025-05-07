package com.rays.pro4.DTO;

public class MarksheetDTO extends BaseDTO {

    private String rollNo;
    private long studentId;
    private int physics;
    private int chemistry;
    private int maths;


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

    public int getPhysics() {
        return physics;
    }

    public void setPhysics(int physics) {
        this.physics = physics;
    }

    public int getChemistry() {
        return chemistry;
    }

    public void setChemistry(int chemistry) {
        this.chemistry = chemistry;
    }

    public int getMaths() {
        return maths;
    }

    public void setMaths(int maths) {
        this.maths = maths;
    }

    @Override
    public String toString() {
        return "MarksheetDTO [rollNo=" + rollNo + ", studentId=" + studentId + ", physics=" + physics
                + ", chemistry=" + chemistry + ", maths=" + maths + ", id=" + id + ", createdBy=" + createdBy
                + ", modifiedBy=" + modifiedBy + ", createdDatetime=" + createdDatetime + ", modifiedDatetime="
                + modifiedDatetime + "]";
    }
}