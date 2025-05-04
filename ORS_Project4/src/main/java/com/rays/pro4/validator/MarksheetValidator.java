package com.rays.pro4.validator;

import java.util.HashMap;
import java.util.Map;
import com.rays.pro4.Bean.MarksheetBean;
import com.rays.pro4.Util.DataValidator;

public class MarksheetValidator {

    public static Map<String, String> validate(MarksheetBean bean) {
        Map<String, String> errors = new HashMap<>();

        if (DataValidator.isNull(bean.getRollNo())) {
            errors.put("rollNo", "Roll number is required");
        } else if (!DataValidator.isRollNo(bean.getRollNo())) {
            errors.put("rollNo", "Invalid roll number format");
        }

        if (bean.getStudentld() <= 0) {
            errors.put("studentId", "Student is required");
        }

        if (bean.getPhysics() < 0) {
            errors.put("physics", "marks can not be negative");
        } else if (bean.getPhysics() > 100) {
            errors.put("physics", "marks can not be greater than 100");
        }

        if (bean.getChemistry() < 0) {
            errors.put("chemistry", "marks can not be negative");
        } else if (bean.getChemistry() > 100) {
            errors.put("chemistry", "marks can not be greater than 100");
        }

        if (bean.getMaths() < 0) {
            errors.put("maths", "marks can not be negative");
        } else if (bean.getMaths() > 100) {
            errors.put("maths", "marks can not be greater than 100");
        }

        return errors;
    }
}