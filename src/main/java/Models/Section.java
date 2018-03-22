package Models;

import org.chocosolver.solver.variables.BoolVar;

import java.util.ArrayList;

public class Section {
    private static int DEFAULT_ID = -1;
    private static int SEMESTER_DIFF_THRESHOLD = 1;

    public ArrayList<BoolVar> sectionClassRooms;
    private int id;
    private String sectionId;
    private Course course;
    private int classSize;
    private Professor professor;
    private CourseTime courseTime;
    private ClassRoom classRoom;
    private ArrayList<BoolVar> possibleProfessors;

    public Section(String sectionId, Course course, int classSize) {
        this(DEFAULT_ID, sectionId, course, classSize);
    }

    public Section(int id, String sectionId, Course course, int classSize) {
        this.id = id;
        this.sectionId = sectionId;
        this.course = course;
        this.classSize = classSize;
        sectionClassRooms = new ArrayList<>();
        possibleProfessors = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getSectionId() {
        return sectionId;
    }

    public Course getCourse() {
        return course;
    }

    public int getClassSize() {
        return classSize;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public CourseTime getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(CourseTime courseTime) {
        this.courseTime = courseTime;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public ArrayList<BoolVar> getPossibleProfessors() {
        return possibleProfessors;
    }

    public void addPossibleProfessor(BoolVar boolVar) {
        possibleProfessors.add(boolVar);
    }

    public boolean sectionsOverlap(Section section) {
        // If two sections are at the same time they overlap if they are in the same room
        // or have the same professor

        return courseTime.timeOverlap(section.getCourseTime()) && classRoom.equals(section.getClassRoom());
        //return courseTime.timeOverlap(section.getCourseTime()) &&
        //        (classRoom.equals(section.getClassRoom()) || professor.equals(section.getProfessor()));
    }

    public int getOverlapScore(Section section) {
        if (!courseTime.timeOverlap(section.getCourseTime())) {
            return 0;
        }

        int diff = Math.abs(course.getSemesterId() - section.getCourse().getSemesterId());
        return diff - 10;
    }

    public boolean semestersOverlap(Section section) {
        Course course1 = section.getCourse();
        int diff = Math.abs(course.getSemesterId() - course1.getSemesterId());
        return diff <= SEMESTER_DIFF_THRESHOLD;
    }

    public ArrayList<ClassRoomTime> possibleClassrooms(ArrayList<ClassRoomTime> classRooms) {
        ArrayList<ClassRoomTime> possibleClassRooms = new ArrayList<>();
        for (ClassRoomTime classRoom : classRooms) {
            if (classRoom.canFitCourse(this)) {
                possibleClassRooms.add(classRoom);
            }
        }
        return possibleClassRooms;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!Section.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final Section section = (Section) obj;

        return section.getId() == this.getId();
    }

    @Override
    public String toString() {
        if (professor == null) {
            return sectionId + "\n\t Time: " + courseTime.toString() + "\n\t classroom: " + classRoom.toString();
        }
        return sectionId + "\n\t Time: " + courseTime.toString() + "\n\t" + professor.toString() + "\n\t classroom: " + classRoom.toString();
    }
}
