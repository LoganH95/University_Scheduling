package Models;

import java.util.ArrayList;

public class Course {
    private static int DEFAULT_ID = -1;

    private int id;
    private String name;
    private String code;
    private Department department;
    private int credits;
    private ArrayList<Course> prerequisites;
    private ArrayList<Section> sections;

    public Course(String name, String code, Department department, int credits) {
        this(DEFAULT_ID, name, code, department, credits);
    }

    public Course(int id, String name, String code, Department department, int credits) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.department = department;
        this.credits = credits;
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

    public void addPrerequisites(Course course) {
        prerequisites.add(course);
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public boolean isValidCourseAssignment() {
        for (Section section : sections) {
            if (!section.isValidSectionAssignment()) {
                return false;
            }
        }

        return true;
    }

    public int[] possibleMeetingDays() {
        if (credits >= 3) {
            return new int[]{CourseTime.MeetingDays.MWF.ordinal(), CourseTime.MeetingDays.TTH.ordinal()};
        } else if (credits == 2){
            return new int[]{CourseTime.MeetingDays.T.ordinal(), CourseTime.MeetingDays.TH.ordinal(), CourseTime.MeetingDays.MW.ordinal(), CourseTime.MeetingDays.MF.ordinal(), CourseTime.MeetingDays.WF.ordinal()};
        } else {
            return new int[]{CourseTime.MeetingDays.M.ordinal(), CourseTime.MeetingDays.W.ordinal(), CourseTime.MeetingDays.F.ordinal()};
        }
    }

    public boolean coursesOverlap(Course course) {
        for (Section section : course.getSections()) {
            for (Section section1 : this.sections) {
                if (section.sectionsOverlap(section1)) {
                    return true;
                }
            }
        }
        return false;
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

}
