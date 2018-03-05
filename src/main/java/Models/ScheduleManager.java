package Models;

import Storage.DatabaseConnection;

import java.util.ArrayList;
import java.util.Map;

public class ScheduleManager {
    private Map<Integer, Course> courses;
    private Map<Integer, Department> departments;
    private ArrayList<Professor> professors;
    private ArrayList<ClassRoom> classRooms;

    public ScheduleManager() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        departments = databaseConnection.getAllDepartments();
        courses = databaseConnection.getAllCourses(departments);
        professors = databaseConnection.getAllProfessors();
        classRooms = databaseConnection.getAllClassRooms();
        getAllPrereqs(databaseConnection);
        getAllQualifiedCourses(databaseConnection);
        getAllSections(databaseConnection);
        databaseConnection.closeConnection();
    }

    public Map<Integer, Course> getCourses() {
        return courses;
    }

    public ArrayList<ClassRoom> getClassRooms() {
        return classRooms;
    }

    private void getAllPrereqs(DatabaseConnection databaseConnection) {
        for (Course course : courses.values()) {
            databaseConnection.getPrerequisites(course, courses);
        }
    }

    private void getAllQualifiedCourses(DatabaseConnection databaseConnection) {
        for (Professor professor : professors) {
            databaseConnection.getQualifiedCourses(professor, courses);
        }
    }

    private void getAllSections(DatabaseConnection databaseConnection) {
        for (Course course : courses.values()) {
            databaseConnection.getSections(course);
        }
    }

    public boolean isValidAssignment() {
        for (Integer courseId : courses.keySet()) {
            Course course = courses.get(courseId);
            if (!course.isValidCourseAssignment()) {
                return false;
            }
        }
        return true;
    }
}
