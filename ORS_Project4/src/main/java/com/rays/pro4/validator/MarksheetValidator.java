package com.rays.pro4.validator;

import java.util.Map;

import com.rays.pro4.Bean.MarksheetBean;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;

public class MarksheetValidator {

    public static boolean validate(MarksheetBean bean, Map<String, String> errors) {

        String rollNo = bean.getRollNo();
        long studentId = bean.getStudentId();
        int physics = bean.getPhysics();
        int chemistry = bean.getChemistry();
        int maths = bean.getMaths();

        if (DataValidator.isNull(rollNo)) {
            errors.put("rollNo", PropertyReader.getValue("error.require", "Roll No"));
        } else if (!DataValidator.isRollNo(rollNo)) {
            errors.put("rollNo", PropertyReader.getValue("error.invalid", "Roll No"));
        }
        
        if (studentId <= 0) {
            errors.put("studentId", PropertyReader.getValue("error.require", "Student Name"));
        }

        if (DataValidator.isNull(String.valueOf(physics))) {
            errors.put("physics", PropertyReader.getValue("error.require", "Physics Marks"));
        } else if (!DataValidator.isInteger(String.valueOf(physics))) {
            errors.put("physics", PropertyReader.getValue("error.integer", "Physics Marks"));
        } else {
            if (physics > 100 || physics < 0) {
                errors.put("physics", PropertyReader.getValue("error.range", "Physics Marks"));
            }
        }

        if (DataValidator.isNull(String.valueOf(chemistry))) {
            errors.put("chemistry", PropertyReader.getValue("error.require", "Chemistry Marks"));
        } else if (!DataValidator.isInteger(String.valueOf(chemistry))) {
            errors.put("chemistry", PropertyReader.getValue("error.integer", "Chemistry Marks"));
        } else {
            if (chemistry > 100 || chemistry < 0) {
                errors.put("chemistry", PropertyReader.getValue("error.range", "Chemistry Marks"));
            }
        }

        if (DataValidator.isNull(String.valueOf(maths))) {
            errors.put("maths", PropertyReader.getValue("error.require", "Maths Marks"));
        } else if (!DataValidator.isInteger(String.valueOf(maths))) {
            errors.put("maths", PropertyReader.getValue("error.integer", "Maths Marks"));
        } else {
            if (maths > 100 || maths < 0) {
                errors.put("maths", PropertyReader.getValue("error.range", "Maths Marks"));
            }
        }

        return errors.isEmpty();
    }
}
