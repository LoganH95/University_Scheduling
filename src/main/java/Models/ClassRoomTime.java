package Models;

import org.chocosolver.solver.variables.BoolVar;

import java.util.ArrayList;

public class ClassRoomTime {
    private ClassRoom classRoom;
    private CourseTime courseTime;
    private ArrayList<BoolVar> sections;

    public ClassRoomTime(ClassRoom classRoom, CourseTime courseTime) {
        this.classRoom = classRoom;
        this.courseTime = courseTime;
        sections = new ArrayList<>();
    }

    public CourseTime getCourseTime() {
        return courseTime;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public void addSection(BoolVar section) {
        sections.add(section);
    }

    public ArrayList<BoolVar> getSections() {
        return sections;
    }

    public boolean canFitCourse(Section section) {
        return classRoom.canFitCourse(section);
    }

    public int getId() {
        return (classRoom.getId() - 1) * 16 + courseTime.getMeetingTime().ordinal();
    }
}
