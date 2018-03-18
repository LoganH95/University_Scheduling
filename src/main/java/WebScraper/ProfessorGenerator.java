package WebScraper;

import Models.Course;
import Models.Department;
import Models.Professor;
import Storage.DatabaseConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class ProfessorGenerator {
    private DatabaseConnection databaseConnection;
    private RandomNameGenerator randomNameGenerator;
    private int totalNumProfessors;


    private ProfessorGenerator() {
        databaseConnection = new DatabaseConnection();
        randomNameGenerator = new RandomNameGenerator();
        totalNumProfessors = 0;
    }

    public static void main(String args[]) {
        ProfessorGenerator professorGenerator = new ProfessorGenerator();
        professorGenerator.generate();
    }

    private void generate() {
        Random rand = new Random();
        Map<Integer, Department> departments = databaseConnection.getAllDepartments();
        databaseConnection.getAllCourses(departments);
        for (Department department : departments.values()) {
            int courseCount = department.getCourses().size();
            int professorCount = 1 + courseCount / 2 + rand.nextInt(4);
            ArrayList<Professor> professors = new ArrayList<>();
            for (int i = 0; i < professorCount; i++) {
                Professor professor = new Professor(++totalNumProfessors, randomNameGenerator.generateName());
                professors.add(professor);
            }

            for (Course course : department.getCourses().values()) {
                int qualifiedCount = 1 + rand.nextInt(professors.size() / 3 + 1);
                Collections.shuffle(professors);
                for (int i = 0; i < qualifiedCount; i++) {
                    if (i == professors.size()) {
                        break;
                    }
                    professors.get(i).addQualifiedCourse(course);
                }
            }

            for (Professor professor : professors) {
                databaseConnection.exportProfessor(professor);
            }

            for (Professor professor : professors) {
                for (Course course : professor.getQualifiedCourses()) {
                    databaseConnection.exportQualifiedCourse(professor, course);
                }
            }
        }
    }

}
