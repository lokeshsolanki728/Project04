package com.rays.pro4.Bean;

import java.io.Serializable;

import com.rays.pro4.DTO.CourseDTO;
/** 
 * Course JavaBean encapsulates Course attributes.
 * 
 * @author Lokesh SOlanki
 *
 */
public class CourseBean extends BaseBean implements Serializable {

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
    
    private static final long serialVersionUID = 1L;
	@Override
	public CourseDTO getDTO() {
    	CourseDTO courseDTO= new CourseDTO();
    	courseDTO.setName(name);
    	courseDTO.setDescription(description);
    	courseDTO.setDuration(duration);
    	courseDTO.setId(id);
    	courseDTO.setCreatedBy(createdBy);
    	courseDTO.setModifiedBy(modifiedBy);
		return courseDTO;
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
	
	
