package Models;

import java.sql.Time;
import java.util.ArrayList;

public class Section {
    private static int DEFAULT_ID = -1;

    private int id;
    private String sectionId;
    private Course course;
    private int classSize;
    private Professor professor;
    private ArrayList<CourseTime> courseTimes;
    private ClassRoom classRoom;

    public Section(String sectionId, Course course, int classSize) {
        this(DEFAULT_ID, sectionId, course, classSize);
    }

    public Section(int id, String sectionId, Course course, int classSize) {
        this.id = id;
        this.sectionId = sectionId;
        this.course = course;
        this.classSize = classSize;
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

    public ArrayList<CourseTime> getCourseTimes() {
        return courseTimes;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public boolean isValidSectionAssignment() {
        if (!professor.canTeachCourse(course)) {
            return false;
        }

        Time startTime = courseTimes.get(0).getStartTime();
        Time endTime = courseTimes.get(0).getEndTime();
        for (CourseTime courseTime : courseTimes) {
            if (!courseTime.getStartTime().equals(startTime)) {
                return false;
            } else if (!courseTime.getEndTime().equals(endTime)) {
                return false;
            }
        }

        if (classRoom.canFitCourse(this)) {
            return true;
        }

        return false;
    }

    public boolean sectionsOverlap(Section section) {
        for (CourseTime courseTime : courseTimes) {
            for (CourseTime courseTime1 : section.getCourseTimes()) {
                if (!courseTime.timeOverlap(courseTime1)) {
                    return false;
                }
            }
        }
        return true;
    }


    public ArrayList<ClassRoom> possibleClassrooms(ArrayList<ClassRoom> classRooms) {
        ArrayList<ClassRoom> possibleClassRooms = new ArrayList<>();
        for (ClassRoom classRoom : classRooms) {
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
}
