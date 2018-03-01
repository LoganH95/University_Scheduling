package Models;

import java.util.HashMap;
import java.util.Map;

public class Department {
    private static int DEFAULT_ID = -1;

    private int id;
    private String name;
    private String abbreviation;
    private Map<Integer, Course> courses;

    public Department(String name, String abbreviation) {
        this(DEFAULT_ID, name, abbreviation);
    }

    public Department(int id, String name, String abbreviation) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
        this.courses = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public Map<Integer, Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        courses.put(course.getId(), course);
    }
}


