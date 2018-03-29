package Models;

import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;

public class Section {
    private static int DEFAULT_ID = -1;
    private static int SEMESTER_DIFF_THRESHOLD = 1;

    private int id;
    private String sectionId;
    private Course course;
    private int classSize;
    private Professor professor;
    private CourseTime courseTime;
    private ClassRoom classRoom;
    private IntVar classRoomAssignment;
    private IntVar timeAssignment;
    private int possibleClassroomSize;
    private ArrayList<BoolVar> possibleProfessors;

    public Section(String sectionId, Course course, int classSize) {
        this(DEFAULT_ID, sectionId, course, classSize);
    }

    public Section(int id, String sectionId, Course course, int classSize) {
        this.id = id;
        this.sectionId = sectionId.trim();
        this.course = course;
        this.classSize = classSize;
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

    public void setClassRoomAssignment(IntVar classRoomAssignment) {
        this.classRoomAssignment = classRoomAssignment;
    }

    public IntVar getClassRoomAssignment() {
        return classRoomAssignment;
    }

    public void setTimeAssignment(IntVar timeAssignment) {
        this.timeAssignment = timeAssignment;
    }

    public IntVar getTimeAssignment() {
        return timeAssignment;
    }

    public ArrayList<BoolVar> getPossibleProfessors() {
        return possibleProfessors;
    }

    public void addPossibleProfessor(BoolVar boolVar) {
        possibleProfessors.add(boolVar);
    }

    public boolean sectionsOverlap(Section section) {
        return courseTime.timeOverlap(section.getCourseTime()) && classRoom.equals(section.getClassRoom());
    }

    public int getOverlapValue(Section section) {
        if (!semestersOverlap(section)) {
            return 0;
        } else if (section.getId() == this.id) {
            return -5;
        }

        int diff = Math.abs(course.getSemesterId() - section.getCourse().getSemesterId());
        return diff - 2;
    }

    public int getOverlapScore(Section section) {
        return courseTime.timeOverlap(section.getCourseTime()) ? getOverlapValue(section) : 0;
    }

    public boolean semestersOverlap(Section section) {
        Course course1 = section.getCourse();
        int diff = Math.abs(course.getSemesterId() - course1.getSemesterId());
        return diff <= SEMESTER_DIFF_THRESHOLD;
    }

    public ArrayList<ClassRoom> possibleClassrooms(ArrayList<ClassRoom> classRooms) {
        ArrayList<ClassRoom> possibleClassRooms = new ArrayList<>();
        for (ClassRoom classRoom : classRooms) {
            if (classRoom.canFitCourse(this)) {
                possibleClassRooms.add(classRoom);
            }
        }
        possibleClassroomSize = possibleClassRooms.size();
        return possibleClassRooms;
    }

    public int getPossibleClassroomSize() {
        return possibleClassroomSize;
    }

    public ArrayList<Integer> possibleClassroomsIds(ArrayList<ClassRoom> classRooms) {
        ArrayList<Integer> possibleClassRoomIds = new ArrayList<>();
        for (ClassRoom classRoom : classRooms) {
            if (classRoom.canFitCourse(this)) {
                possibleClassRoomIds.add(classRoom.getId());
            }
        }
        return possibleClassRoomIds;
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
        return "Section: " + sectionId  + "\n\tProfessor: " + professor.toString() + "\n\tClassroom: " + classRoom.toString() + "\n\tTime: " + courseTime.toString() + "\n";
    }
}
