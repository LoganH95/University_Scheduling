package ScheduleMaker;

import Models.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;

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
            if (scheduleManager.verifySchedule()) {
                System.out.println("Schedule verified");
                System.out.println("Schedule Score: " + scheduleManager.scheduleScore());
                printSchedule();
                printToFile();
            } else {
                System.out.println("Schedule found to have conflicts");
            }
        } else {
            System.out.println("No Solution found");
        }
    }

    private boolean makeSchedule() {
        ArrayList<Section> sections = scheduleManager.getSections();
        ArrayList<ClassRoom> classRooms = scheduleManager.getClassRooms();
        ArrayList<IntVar> objectiveVars = new ArrayList<>();
        int[] courseTimes = CourseTime.meetingTimes();

        Model model = new Model("Scheduling");
        for (Section section : sections) {
            ArrayList<Integer> possibleClassRoomIds = section.possibleClassroomsIds(classRooms);
            IntVar var = model.intVar(section.getId() + " room", arrayListToArray(possibleClassRoomIds));
            section.setClassRoomAssignment(var);
            var = model.intVar(section.getId() + " time", courseTimes);
            section.setTimeAssignment(var);
        }

        for (int i = 0; i < sections.size(); i++) {
            Section section = sections.get(i);
            for (int j = i + 1; j < sections.size(); j++) {
                Section otherSection = sections.get(j);
                model.ifThen(
                      model.allEqual(section.getClassRoomAssignment(), otherSection.getClassRoomAssignment()).reify(),
                      model.allDifferent(section.getTimeAssignment(), otherSection.getTimeAssignment())
                );

                // Create the IntVars used for the objective function
                if (section.semestersOverlap(otherSection)) {
                    int minValue = section.getOverlapValue(otherSection);
                    IntVar var = model.intVar(new int[]{minValue, 0});
                    model.ifThenElse(
                            model.allEqual(section.getTimeAssignment(), otherSection.getTimeAssignment()).reify(),
                            model.arithm(var, "=", minValue),
                            model.arithm(var, "=", 0)
                    );

                    objectiveVars.add(var);
                }
            }
        }
        IntVar objective = model.intVar("Objective", objectiveVars.size() * -2, 0);
        model.sum(arrayListToArrayIntVar(objectiveVars), "=", objective).post();
        model.setObjective(true, objective);

        System.out.println("Starting solver schedule");
        Solver solver = model.getSolver();
        solver.limitTime(150000);
        solver.setSearch(Search.inputOrderLBSearch(searchOrderClassrooms(sections)));
        solver.showStatistics();

        boolean solutionFound = false;
        while (solver.solve()) {
            solutionFound = true;
            for (Section section : sections) {
                ClassRoom classRoom = classRooms.get(section.getClassRoomAssignment().getValue() - 1);
                section.setClassRoom(classRoom);
                CourseTime courseTime = new CourseTime(section.getTimeAssignment().getValue());
                section.setCourseTime(courseTime);
            }

            if (objective.getValue() == 0) {
                break;
            }
        }

        return solutionFound && scheduleProfessors(sections);
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
            model.sum(arrayListToArrayBoolVar(section.getPossibleProfessors()), "=", 1).post();
        }

        for (Professor professor : professors) {
            if (professor.getPossibleCourses().size() == 0) {
                continue;
            }
            // Ensure that a professor teaches no more than 4 sections
            model.sum(arrayListToArrayBoolVar(professor.getPossibleCourses()), "<=", 4).post();
            ArrayList<ArrayList<BoolVar>> teachingTimes = professor.getTeachingTimes();
            for (ArrayList<BoolVar> teachingTime : teachingTimes) {
                if (teachingTime.size() == 0) {
                    continue;
                }
                // Ensure that a professor is not teaching more than 1 course at a time
                model.sum(arrayListToArrayBoolVar(teachingTime), "<=", 1).post();
            }
        }

        System.out.println("Starting solver professors");
        Solver solver = model.getSolver();
        solver.limitTime(3600000);
        solver.setSearch(Search.inputOrderLBSearch(searchOrderProfessors(sections)));
        solver.showStatistics();
        if (solver.solve()) {
            solver.showStatistics();
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
        System.out.println(scheduleManager.toString());
    }

    private void printToFile() {
        try {
            PrintWriter writer = new PrintWriter("schedule.txt", "UTF-8");
            writer.println(scheduleManager.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int[] arrayListToArray(ArrayList<Integer> arrayList) {
        int[] array = new int[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }

    private IntVar[] arrayListToArrayIntVar(ArrayList<IntVar> arrayList) {
        IntVar[] array = new IntVar[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }

    private BoolVar[] arrayListToArrayBoolVar(ArrayList<BoolVar> arrayList) {
        BoolVar[] array = new BoolVar[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }

    private IntVar[] searchOrderClassrooms(ArrayList<Section> sections) {
        sections.sort(new SectionPossibleClassRoomSizeComparator());
        IntVar[] vars = new IntVar[sections.size() * 2];
        for (int i = 0; i < sections.size(); i++) {
            vars[2 * i] = sections.get(i).getClassRoomAssignment();
            vars[2 * i + 1] = sections.get(i).getTimeAssignment();
        }
        return vars;
    }

    private BoolVar[] searchOrderProfessors(ArrayList<Section> sections) {
        ArrayList<BoolVar> searchOrder = new ArrayList<>();
        sections.sort(new SectionPossibleProfessorSizeComparator());
        for (Section section : sections) {
            searchOrder.addAll(section.getPossibleProfessors());
        }
        return arrayListToArrayBoolVar(searchOrder);
    }

    private class SectionPossibleClassRoomSizeComparator implements  Comparator<Section>  {

        @Override
        public int compare(Section o1, Section o2) {
            return o2.getPossibleClassroomSize() - o1.getPossibleClassroomSize();
        }
    }

    private class SectionPossibleProfessorSizeComparator implements  Comparator<Section>  {

        @Override
        public int compare(Section o1, Section o2) {
            return o2.getPossibleProfessors().size() - o1.getPossibleProfessors().size();
        }
    }
}
