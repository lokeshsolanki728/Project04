package com.rays.pro4.DTO;

public class CourseDTO extends BaseDTO {
    
    private String name;
    private String description;
    private String duration;

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "CourseDTO [name=" + name + ", description=" + description + ", duration=" + duration + ", id=" + id + "]";
    }
}