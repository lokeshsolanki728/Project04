package com.rays.pro4.validator;

import java.util.HashMap;
import java.util.Map;

import com.rays.pro4.DTO.TimeTableDTO;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;

/**
 * TimeTable Validator class to validate TimeTable data.
 * 
 * @author Lokesh SOlanki
 *
 */
public class TimeTableValidator {

    /**
     * Validates the TimeTableBean data.
     *
     * @param dto The TimeTableDTO object.
     * @return True if the request attributes are valid, false otherwise.
     */
    public static boolean validate(final TimeTableDTO dto) {
        boolean pass = true;

        if (dto.getCourseId() == 0) {
        	dto.getErrorMessages().put("courseId", PropertyReader.getValue("error.require", "Course Name"));
            pass = false;
        }else if (!DataValidator.isLong(String.valueOf(dto.getCourseId()))) {
            dto.getErrorMessages().put("courseId", "Course ID must be a number");
            pass = false;
        }

        if (dto.getSubjectId() == 0) {
        	dto.getErrorMessages().put("subjectId", PropertyReader.getValue("error.require", "Subject Name"));
            pass = false;
        }else if (!DataValidator.isLong(String.valueOf(dto.getSubjectId()))) {
            dto.getErrorMessages().put("subjectId", "Subject ID must be a number");
            pass = false;
        }

        if (DataValidator.isNull(dto.getSemester())) {
        	dto.getErrorMessages().put("semester", PropertyReader.getValue("error.require", "Semester"));
            
        }

        if (DataValidator.isNull(DataValidator.dateToString(dto.getExamDate()))) {
        	dto.getErrorMessages().put("examDate", PropertyReader.getValue("error.require", "Exam Date"));
            pass = false;
        } else if (!DataValidator.isDate(DataValidator.dateToString(dto.getExamDate()))) {
        	dto.getErrorMessages().put("examDate", PropertyReader.getValue("error.date", "Exam Date"));
            pass = false;
        }

        if (DataValidator.isNull(dto.getExamTime())) {
        	dto.getErrorMessages().put("examTime", PropertyReader.getValue("error.require", "Exam Time"));
            pass = false;
        }

        if (DataValidator.isNull(dto.getDescription())) {
        	dto.getErrorMessages().put("description", PropertyReader.getValue("error.require", "Description"));
            pass = false;
        }


        return !dto.getErrorMessages().isEmpty();
    }
}