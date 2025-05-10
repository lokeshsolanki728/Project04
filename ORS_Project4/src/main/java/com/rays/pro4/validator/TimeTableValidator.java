package com.rays.pro4.validator;

import java.util.HashMap;
import java.util.Map;

import com.rays.pro4.DTO.TimeTableDTO;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;

public class TimeTableValidator {

    public static boolean validate(final TimeTableDTO dto) {
        if (dto.getCourseId() == 0) {
        	dto.getErrorMessages().put("courseId", PropertyReader.getValue("error.require", "Course Name"));
        }else if (!DataValidator.isLong(String.valueOf(dto.getCourseId()))) {
            dto.getErrorMessages().put("courseId", "Course ID must be a number");
        }

        if (dto.getSubjectId() == 0) {
        	dto.getErrorMessages().put("subjectId", PropertyReader.getValue("error.require", "Subject Name"));
        }else if (!DataValidator.isLong(String.valueOf(dto.getSubjectId()))) {
            dto.getErrorMessages().put("subjectId", "Subject ID must be a number");
        }

 if (DataValidator.isNullOrEmpty(dto.getSemester())) {
        	dto.getErrorMessages().put("semester", PropertyReader.getValue("error.require", "Semester"));
            
        }

        if (DataValidator.isNull(DataValidator.dateToString(dto.getExamDate()))) {
        	dto.getErrorMessages().put("examDate", PropertyReader.getValue("error.require", "Exam Date"));
        } else if (!DataValidator.isDate(DataValidator.dateToString(dto.getExamDate()))) {
        	dto.getErrorMessages().put("examDate", PropertyReader.getValue("error.date", "Exam Date"));
        }

        if (DataValidator.isNull(dto.getDescription())) {
        	dto.getErrorMessages().put("description", PropertyReader.getValue("error.require", "Description"));
            pass = false;
        }


        return dto.getErrorMessages().isEmpty();
    }
}