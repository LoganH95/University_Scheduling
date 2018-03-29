package Models;

import Storage.DatabaseConnection;

import java.util.*;

public class ScheduleManager {
    private static final int COMPUTER_SCIENCE = 43;

    private ArrayList<Course> courses;
    private Map<Integer, Department> departments;
    private ArrayList<Professor> professors;
    private ArrayList<ClassRoom> classRooms;
    private ArrayList<Section> sections;

    public ScheduleManager() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        departments = databaseConnection.getAllDepartments();
        courses = databaseConnection.getAllCoursesForDepartment(departments.get(COMPUTER_SCIENCE));
        professors = databaseConnection.getAllProfessors();
        classRooms = databaseConnection.getAllClassRooms();
        getAllQualifiedCourses(databaseConnection);
        getAllSections(databaseConnection);
        databaseConnection.closeConnection();
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public ArrayList<ClassRoom> getClassRooms() {
        return classRooms;
    }

    public ArrayList<Professor> getProfessors() {
        return professors;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public int scheduleScore() {
        int scheduleScore = 0;
        for (int i = 0; i < sections.size(); i++) {
            Section section = sections.get(i);
            for (int j = i + 1; j < sections.size(); j++) {
                scheduleScore += section.getOverlapScore(sections.get(j));
            }
        }

        return scheduleScore;
    }

    public boolean verifySchedule() {
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
        for (Course course : courses) {
            databaseConnection.getSections(course);
            sections.addAll(course.getSections());
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");

        for (Course course : courses) {
            stringBuilder.append(course.toString() + "\n");
        }
        return stringBuilder.toString();
    }

}
