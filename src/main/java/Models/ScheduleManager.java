package Models;

import Storage.DatabaseConnection;

import java.util.ArrayList;
import java.util.Map;

public class ScheduleManager {
    private static final int COMPUTER_SCIENCE = 43;

    private Map<Integer, Course> courses;
    private Map<Integer, Department> departments;
    private ArrayList<Professor> professors;
    private ArrayList<ClassRoom> classRooms;
    private ArrayList<ClassRoomTime> classRoomTimes;

    public ScheduleManager() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        departments = databaseConnection.getAllDepartments();
        courses = databaseConnection.getAllCoursesForDepartment(departments.get(COMPUTER_SCIENCE));
        //courses = databaseConnection.getAllCourses(departments);
        professors = databaseConnection.getAllProfessors();
        classRooms = databaseConnection.getAllClassRooms();
        makeClassRoomTimes();
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

    public ArrayList<ClassRoomTime> getClassRoomTimes() {
        return classRoomTimes;
    }

    public ArrayList<Professor> getProfessors() {
        return professors;
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

    private void makeClassRoomTimes() {
        classRoomTimes = new ArrayList<>();
        for (ClassRoom classRoom : classRooms) {
            for (CourseTime.MeetingTime meetingTime : CourseTime.MeetingTime.values()) {
                CourseTime courseTime = new CourseTime(meetingTime);
                ClassRoomTime classRoomTime = new ClassRoomTime(classRoom, courseTime);
                classRoomTimes.add(classRoomTime);
            }
        }
    }
}
