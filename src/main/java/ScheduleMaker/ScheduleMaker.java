package ScheduleMaker;

import Models.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScheduleMaker {
    private ScheduleManager scheduleManager;

    public ScheduleMaker() {
        scheduleManager = new ScheduleManager();
    }

    public static void main(String args[]) {
        ScheduleMaker scheduleMaker = new ScheduleMaker();
        scheduleMaker.startScheduleMaker();
    }

    public void startScheduleMaker() {
        if (makeSchedule()) {
            System.out.println("Solution found");
            printSchedule();
        } else {
            System.out.println("No Solution found");
        }
    }

    private boolean makeSchedule() {
        ArrayList<Section> sections = new ArrayList<>();
        ArrayList<ClassRoomTime> classRooms = scheduleManager.getClassRoomTimes();

        Model model = new Model("Scheduling");
        for (Course course : scheduleManager.getCourses().values()) {
            for (Section section : course.getSections()) {
                sections.add(section);
                ArrayList<ClassRoomTime> possibleClassrooms = section.possibleClassrooms(classRooms);
                for (ClassRoomTime classRoomTime : possibleClassrooms) {
                    BoolVar var = model.boolVar(section.getId() + " " + classRoomTime.getId());
                    section.sectionClassRooms.add(var);
                    classRoomTime.addSection(var);
                }
            }
        }

        for (Section section : sections) {
            // Ensure that each section is assigned to exactly 1 class room
            model.sum(arrayListToArray(section.sectionClassRooms), "=", 1).post();
        }

        for (ClassRoomTime classRoomTime : classRooms) {
            // Ensure that each class room only has 1 section at a time
            model.sum(arrayListToArray(classRoomTime.getSections()), "<=", 1).post();
        }

        System.out.println("Starting solver schedule");
        Solver solver = model.getSolver();
        while (solver.solve()) {
            for (Section section : sections) {
                ArrayList<BoolVar> sectionClassRooms = section.sectionClassRooms;
                for (BoolVar sectionClassRoom : sectionClassRooms) {
                    if (sectionClassRoom.getValue() == 1) {
                        String name = sectionClassRoom.getName();
                        int index = Integer.parseInt(name.substring(name.indexOf(" ") + 1));
                        ClassRoomTime classRoomTime = classRooms.get(index);
                        section.setClassRoom(classRoomTime.getClassRoom());
                        section.setCourseTime(classRoomTime.getCourseTime());
                        break;
                    }
                }
            }

            if (scheduleProfessors(sections)) {
                return true;
            }
        }

        return false;
    }

    private boolean scheduleProfessors(ArrayList<Section> sections) {
        ArrayList<Professor> professors = scheduleManager.getProfessors();

        Model model = new Model("Professor Assignment");
        for (Section section : sections) {
            for (Professor professor : professors) {
                if (professor.isQualifiedCourse(section.getCourse())) {
                    BoolVar boolVar = model.boolVar(section.getId() + " " + professor.getId());
                    professor.addPossibleCourse(section.getCourseTime(), boolVar);
                    section.addPossibleProfessor(boolVar);
                }
            }
        }

        for (Section section : sections) {
            // Ensure that each section has 1 professor assigned to it
            model.sum(arrayListToArray(section.getPossibleProfessors()), "=", 1).post();
        }

        for (Professor professor : professors) {
            if (professor.getPossibleCourses().size() == 0) {
                continue;
            }
            // Ensure that a professor teaches no more than 4 sections
            model.sum(arrayListToArray(professor.getPossibleCourses()), "<=", 4);
            ArrayList<ArrayList<BoolVar>> teachingTimes = professor.getTeachingTimes();
            for (ArrayList<BoolVar> teachingTime : teachingTimes) {
                if (teachingTime.size() == 0) {
                    continue;
                }
                // Ensure that a professor is not teaching more than 1 course at a time
                model.sum(arrayListToArray(teachingTime), "<=", 1);
            }
        }


        System.out.println("Starting solver professors");
        Solver solver = model.getSolver();
        if (solver.solve()) {
            for (Section section : sections) {
                ArrayList<BoolVar> possibleProfessors = section.getPossibleProfessors();
                for (BoolVar possibleProfessor : possibleProfessors) {
                    if (possibleProfessor.getValue() == 1) {
                        String name = possibleProfessor.getName();
                        int index = Integer.parseInt(name.substring(name.indexOf(" ") + 1)) - 1;
                        Professor professor = professors.get(index);
                        section.setProfessor(professor);
                        break;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private void printSchedule() {
        for (Course course : scheduleManager.getCourses().values()) {
            System.out.println(course.toString());
        }
    }

    private BoolVar[] arrayListToArray(ArrayList<BoolVar> arrayList) {
        BoolVar[] array = new BoolVar[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }
}
