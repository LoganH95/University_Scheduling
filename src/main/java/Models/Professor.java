package Models;

import org.chocosolver.solver.variables.BoolVar;

import java.util.ArrayList;

public class Professor {
    private static int DEFAULT_ID = -1;

    private int id;
    private String name;
    private ArrayList<Course> qualifiedCourses;  // List of all QualifiedCourses this professor can teach
    private ArrayList<BoolVar> possibleCourses;
    private ArrayList<ArrayList<BoolVar>> teachingTimes;

    public Professor(String name) {
        this(DEFAULT_ID, name);
    }

    public Professor(int id, String name) {
        this.id = id;
        this.name = name;
        this.qualifiedCourses = new ArrayList<>();
        possibleCourses = new ArrayList<>();
        teachingTimes = new ArrayList<>();
        for (int i = 0; i < CourseTime.MeetingTime.values().length; i++) {
            ArrayList<BoolVar> arrayList = new ArrayList<>();
            teachingTimes.add(arrayList);
        }
     }

    public void addQualifiedCourse(Course course) {
        qualifiedCourses.add(course);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Course> getQualifiedCourses() {
        return qualifiedCourses;
    }

    public ArrayList<BoolVar> getPossibleCourses() {
        return possibleCourses;
    }

    public ArrayList<ArrayList<BoolVar>> getTeachingTimes() {
        return teachingTimes;
    }

    public void addPossibleCourse(CourseTime time, BoolVar boolVar) {
        possibleCourses.add(boolVar);
        teachingTimes.get(time.getMeetingTime().ordinal()).add(boolVar);
    }

    public boolean isQualifiedCourse(Course course) {
        return qualifiedCourses.contains(course);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!Professor.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final Professor professor = (Professor) obj;


        return professor.getId() == this.getId();
    }

    @Override
    public String toString() {
        return name + "(" + id + ")";
    }
}
