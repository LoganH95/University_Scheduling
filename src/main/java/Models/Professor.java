package Models;

import java.util.ArrayList;

public class Professor {
    private static int DEFAULT_ID = -1;

    private int id;
    private String name;
    private ArrayList<Course> qualifiedCourses;  // List of all QualifiedCourses this professor can teach
    private ArrayList<Course> courses; // List of all QualifiedCourses this professor is courses

    public Professor(String name) {
        this(DEFAULT_ID, name);
    }

    public Professor(int id, String name) {
        this.id = id;
        this.name = name;
        this.qualifiedCourses = new ArrayList<>();
        this.courses = new ArrayList<>();
        courses = new ArrayList<>();
    }

    public void addQualifiedCourse(Course course) {
        qualifiedCourses.add(course);
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Course> getQualifiedCourses() {
        return qualifiedCourses;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public boolean canTeachCourse(Course course) {
        if ( !qualifiedCourses.contains(course) ) {
            return false;
        }

        for (Course teachingCourse : courses) {
            if ( course.coursesOverlap(teachingCourse) && !course.equals(teachingCourse)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!Professor.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final Professor professor = (Professor) obj;


        return professor.getId() == this.getId();
    }
}
