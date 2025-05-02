package com.rays.pro4.Bean;
import javax.servlet.http.HttpServletRequest;

import com.rays.pro4.Util.DataUtility;

/** 
 * Course JavaBean encapsulates Course attributes.
 * 
 * @author Lokesh SOlanki
 *
 */
public class CourseBean extends BaseBean{

    private String name;
    private String description;
    private String duration;

    /**
     * Gets the name of the course.
     *
     * @return The name of the course.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the course.
     *
     * @param name The name of the course to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the course.
     *
     * @return The description of the course.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the course.
     *
     * @param description The description of the course to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the duration of the course.
     *
     * @return The duration of the course.
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the course.
     *
     * @param duration The duration of the course to set.
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * Returns the key (ID) of the course.
     *
     * @return The key (ID) of the course.
     */
    @Override 
    public String getkey() { 
        return String.valueOf(id);
    }
    
    /**
     * Returns the value (name) of the course.
     *
     * @return The value (name) of the course.
     */
    @Override
    public String getValue() {
        return name;
    }

    /**
     * Populate bean object from request parameters.
     *
     * @param request the request
     */
	@Override
	public void populate(HttpServletRequest request) {
		setId(DataUtility.getLong(request.getParameter("id")));
		setName(DataUtility.getString(request.getParameter("name")));
		setDescription(DataUtility.getString(request.getParameter("description")));
		setDuration(DataUtility.getString(request.getParameter("duration")));
		
	}

	/**
	 * Returns a string representation of the CourseBean.
	 *
	 * @return A string representation of the CourseBean.
	 */
	@Override
	public String toString() {
		return "CourseBean [name=" + name + ", description=" + description + ", duration=" + duration + ", id=" + id
				+ ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", createdDatetime=" + createdDatetime
				+ ", modifiedDatetime=" + modifiedDatetime + "]";
	}	
}
	
	
