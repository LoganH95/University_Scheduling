package Models;

import java.util.ArrayList;

public class Course {
    private static int DEFAULT_ID = -1;

    private int id;
    private String name;
    private String code;
    private Department department;
    private int credits;
    private int semesterId;
    private ArrayList<Course> prerequisites;
    private ArrayList<Section> sections;

    public Course(String name, String code, Department department, int credits) {
        this(DEFAULT_ID, name, code, department, credits, 0);
    }

    public Course(int id, String name, String code, Department department, int credits, int semesterId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.department = department;
        this.credits = credits;
        this.semesterId = semesterId;
        prerequisites = new ArrayList<>();
        sections = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Department getDepartment() {
        return department;
    }

    public int getCredits() {
        return credits;
    }

    public ArrayList<Course> getPrerequisites() {
        return prerequisites;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public int getSemesterId() {
        return semesterId;
    }

    public void addPrerequisites(Course course) {
        prerequisites.add(course);
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!Course.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final Course course = (Course) obj;


        return course.id == this.id;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(department.getAbbreviation() + " " + code);
        stringBuilder.append(": ");
        stringBuilder.append(this.getName());
        for (Section section : sections) {
            stringBuilder.append("\n\t");
            stringBuilder.append(section.toString());
        }
        return stringBuilder.toString();
    }
}
