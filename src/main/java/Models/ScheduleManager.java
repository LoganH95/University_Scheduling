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
    private ArrayList<Section> sections;
    private ArrayList<ClassRoomTime> classRoomTimes;

    public ScheduleManager() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        departments = databaseConnection.getAllDepartments();
        courses = databaseConnection.getAllCoursesForDepartment(departments.get(COMPUTER_SCIENCE));
        professors = databaseConnection.getAllProfessors();
        classRooms = databaseConnection.getAllClassRooms();
        makeClassRoomTimes();
        getAllQualifiedCourses(databaseConnection);
        getAllSections(databaseConnection);
        databaseConnection.closeConnection();
    }

    public Map<Integer, Course> getCourses() {
        return courses;
    }

    public ArrayList<ClassRoomTime> getClassRoomTimes() {
        return classRoomTimes;
    }

    public ArrayList<Professor> getProfessors() {
        return professors;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public int scheduleScore() {
        System.out.println("Scoring Schedule");
        int scheduleScore = 0;
        for (int i = 0; i < sections.size(); i++) {
            Section section = sections.get(i);
            for (int j = i; j < sections.size(); j++) {
                scheduleScore += section.getOverlapScore(sections.get(j));
            }
        }

        return scheduleScore;
    }

    public boolean verifySchedule() {
        System.out.println("Verifying Schedule");
        for (int i = 0; i < sections.size(); i++) {
            Section section = sections.get(i);
            if (!section.getClassRoom().canFitCourse(section)) {
                System.out.println("Classroom too small");
                System.out.println("Section:\n" + section.toString());
                System.out.println("Classroom:\n" + section.getClassRoom().toString());
                return false;
            }

            for (int j = i + 1; j < sections.size(); j++) {
                Section compareSection = sections.get(j);
                if (section.sectionsOverlap(compareSection)) {
                    System.out.println("Section Overlap");
                    System.out.println("Section 1:\n" + section.toString());
                    System.out.println("Section 2:\n" + compareSection.toString());
                    return false;
                }
            }
        }
        return true;
    }

    private void getAllQualifiedCourses(DatabaseConnection databaseConnection) {
        for (Professor professor : professors) {
            databaseConnection.getQualifiedCourses(professor, courses);
        }
    }

    private void getAllSections(DatabaseConnection databaseConnection) {
        sections = new ArrayList<>();
        for (Course course : courses.values()) {
            databaseConnection.getSections(course);
            sections.addAll(course.getSections());
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
