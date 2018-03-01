package ScheduleMaker;

import Models.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;

public class ScheduleMaker {
    private ScheduleManager scheduleManager;

    public static void main(String args[]) {
        ScheduleMaker scheduleMaker = new ScheduleMaker();
        scheduleMaker.makeSchedule();
    }

    public ScheduleMaker() {
        scheduleManager = new ScheduleManager();
    }

    public void makeSchedule() {
        ArrayList<IntVar> sectionClassrooms = new ArrayList<>();
        ArrayList<IntVar> meetingDaysList = new ArrayList<>();
        ArrayList<IntVar> meetingTimes = new ArrayList<>();
        ArrayList<ClassRoom> classRooms = scheduleManager.getClassRooms();

        Model model = new Model("University Scheduling");

        for (Course course : scheduleManager.getCourses().values()) {
            for (Section section : course.getSections()) {
                ArrayList<ClassRoom> possibleClassrooms = section.possibleClassrooms(classRooms);
                int[] possibleClassroomIds = new int[possibleClassrooms.size()];
                for (int j = 0; j < possibleClassrooms.size(); j++) {
                    possibleClassroomIds[j] = possibleClassrooms.get(j).getId();
                }
                IntVar classRoom = model.intVar("Classrooms: " + section.getId(), possibleClassroomIds);
                sectionClassrooms.add(classRoom);

                IntVar meetingDays = model.intVar("MeetingDays: " + section.getId(), course.possibleMeetingDays());
                meetingDaysList.add(meetingDays);

                IntVar meetingTime = model.intVar("MeetingTime: " + section.getId(), CourseTime.meetingTimes());
                // Constraint for possible meeting times based on day the class is on
                model.ifThenElse(
                        model.arithm(meetingDays, "<", 3),
                        model.arithm(meetingTime, ">", 8),
                        model.arithm(meetingTime, "<", 9)
                );
                meetingTimes.add(meetingTime);
            }
        }

        for (int i = 0; i < sectionClassrooms.size(); i++) {
            IntVar classroomX = sectionClassrooms.get(i);
            IntVar meetingDayX = meetingDaysList.get(i);
            IntVar meetingTimeX = meetingTimes.get(i);
            for (int j = i + 1; j < sectionClassrooms.size(); j++) {
                IntVar classroomY = sectionClassrooms.get(j);
                IntVar meetingDayY = meetingDaysList.get(j);
                IntVar meetingTimeY = meetingTimes.get(j);
                model.ifThen(
                        model.and(
                                model.allEqual(classroomX, classroomY),
                                daysOverlap(model, meetingDayX, meetingDayY)
                        ),
                        model.allDifferent(
                                meetingTimeX,
                                meetingTimeY
                        )
                );
            }
        }
        // 2. Solving part
        Solver solver = model.getSolver();
        if (solver.solve()){
            System.out.println("Solution found");
        } else {
            System.out.println("Solution not found");
        }
    }

    public Constraint daysOverlap(Model model, IntVar meetingDayX, IntVar meetingDayY) {
        return model.or(
                model.and(
                        model.arithm(meetingDayX, "<", 3),
                        model.arithm(meetingDayY, "<", 3)
                ),
                model.and(
                        model.arithm(meetingDayX, ">", 2),
                        model.arithm(meetingDayY, ">", 5)
                ),
                model.and(
                        model.arithm(meetingDayX, ">", 5),
                        model.arithm(meetingDayY, ">", 2)
                )
        );
    }
}
