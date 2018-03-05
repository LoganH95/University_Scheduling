package Storage;

import Models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConnection {
	private static final String DATABASE_LOCATION = "//127.0.0.1:3306/";
	private static final String DATABASE_NAME = "UniversityScheduling";
	private static final String DATABASE_USERNAME = "test";
	private static final String DATABASE_PASSWORD = "test";
	private static final String DEPARTMENT_TABLE = "Departments";
	private static final String COURSES_TABLE = "Courses";
    private static final String SECTIONS_TABLE = "Sections";
	private static final String CLASS_ROOMS_TABLE = "ClassRooms";
	private static final String PROFESSORS_TABLE = "Professors";
	private static final String QUALIFIED_COURSES_TABLE = "QualifiedCourses";
	private static final String PREREQUISITES_TABLE = "Prerequisites";

	private Connection connection;

	public DatabaseConnection() {
		createConnection();
	}

	private void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql:" + DATABASE_LOCATION + DATABASE_NAME;
			connection = DriverManager.getConnection(url, DATABASE_USERNAME, DATABASE_PASSWORD);
		} catch (SQLException | ClassNotFoundException | ClassCastException e) {
			// SQL exceptions
			e.printStackTrace();
		}
	}

	public void exportDepartment(Department department) {
		String sql = "INSERT INTO " + DEPARTMENT_TABLE;
		String columns = "name,abbreviation";
		String values = "'" + department.getName() + "','" + department.getAbbreviation() + "'";
		sql += "(" + columns + ") VALUES(" + values + ")";
		export(sql);
	}

	public void exportCourse(Course course) {
		String sql = "INSERT INTO " + COURSES_TABLE;
		String columns = "departmentId,name,code,credits";
		String values = course.getDepartment().getId() + ",'" + course.getName() + "','" + course.getCode() + "'," + course.getCredits();
		sql += "(" + columns + ") VALUES(" + values + ")";
		export(sql);
	}

	public void exportSection(Section section) {
        String sql = "INSERT INTO " + SECTIONS_TABLE;
        String columns = "sectionId,courseId,classSize";
        String values = "'" + section.getSectionId() + "'," + section.getCourse().getId() + "," + section.getClassSize();
        sql += "(" + columns + ") VALUES(" + values + ")";
        export(sql);
    }

	public void exportProfessor(Professor professor) {
		String sql = "INSERT INTO " + PROFESSORS_TABLE;
		String columns = "name";
		String values = "'" + professor.getName() + "'";
		sql += "(" + columns + ") VALUES(" + values + ")";
		export(sql);
	}

	public void exportClassRoom(ClassRoom classRoom) {
		String sql = "INSERT INTO " + CLASS_ROOMS_TABLE;
		String columns = "buildingName,roomNumber,capacity";
		String values = "'" + classRoom.getBuildingName() + "','" + classRoom.getRoomNumber() + "'," + classRoom.getCapacity();
		sql += "(" + columns + ") VALUES(" + values + ")";
		export(sql);
	}

	public void exportPrerequisites(Course course, Course prereq) {
		String sql = "INSERT INTO " + PREREQUISITES_TABLE;
		String columns = "courseId,prerequisiteId";
		String values = course.getId() + "," + prereq.getId();
		sql += "(" + columns + ") VALUES(" + values + ")";
		export(sql);
	}

	public void exportQualifiedCourse(Professor professor, Course course) {
		String sql = "INSERT INTO " + QUALIFIED_COURSES_TABLE;
		String columns = "professorId,courseId";
		String values = professor.getId() + "," + course.getId();
		sql += "(" + columns + ") VALUES(" + values + ")";
		export(sql);
	}

	private void export(String query) {
		try {
			Statement statement = connection.createStatement();
			statement.execute(query);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Map<Integer, Department> getAllDepartments() {
		Map<Integer, Department> departments = new HashMap<>();
		try {
			String query = "SELECT * FROM " + DEPARTMENT_TABLE;
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				Department department = new Department(
						id,
						resultSet.getString("name"),
						resultSet.getString("abbreviation")
				);
				departments.put(id, department);
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return departments;
	}

	public Map<Integer, Course> getAllCourses(Map<Integer, Department> departments) {
		Map<Integer, Course> courses = new HashMap<>();
		try {
			String query = "SELECT * FROM " + COURSES_TABLE;
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				int departmentId = resultSet.getInt("departmentId");
				Department department = departments.get(departmentId);
				Course course = new Course(
						id,
						resultSet.getString("name"),
						resultSet.getString("code"),
						department,
						resultSet.getInt("credits")
				);
				courses.put(id, course);
				department.addCourse(course);
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return courses;
	}

	public void getSections(Course course) {
		try {
			String query = "SELECT * FROM " + SECTIONS_TABLE + " WHERE courseId=" + course.getId();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Section section = new Section(
						resultSet.getInt("id"),
						resultSet.getString("sectionId"),
						course,
						resultSet.getInt("classSize")
				);
				course.addSection(section);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Professor> getAllProfessors() {
		ArrayList<Professor> professors = new ArrayList<>();
		try {
			String query = "SELECT * FROM " + PROFESSORS_TABLE;
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Professor professor = new Professor(
						resultSet.getInt("id"),
						resultSet.getString("name")
				);
				professors.add(professor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return professors;
	}

	public ArrayList<ClassRoom> getAllClassRooms() {
		ArrayList<ClassRoom> classRooms = new ArrayList<>();
		try {
			String query = "SELECT * FROM " + CLASS_ROOMS_TABLE;
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				ClassRoom classRoom = new ClassRoom(
						resultSet.getInt("id"),
						resultSet.getString("buildingName"),
						resultSet.getString("roomNumber"),
						resultSet.getInt("capacity")
				);
				classRooms.add(classRoom);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classRooms;
	}

	public void getQualifiedCourses(Professor professor, Map<Integer, Course> courses) {
		try {
			String query = "SELECT * FROM " + QUALIFIED_COURSES_TABLE + " WHERE professorId=" + professor.getId();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("courseId");
				Course course = courses.get(id);
				professor.addQualifiedCourse(course);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getPrerequisites(Course course, Map<Integer, Course> courses) {
		try {
			String query = "SELECT * FROM " + PREREQUISITES_TABLE + " WHERE courseId=" + course.getId();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("prerequisiteId");
				Course prerequisite = courses.get(id);
				course.addPrerequisites(prerequisite);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Department getDepartment(String abbreviation) {
		try {
			String query = "SELECT * FROM " + DEPARTMENT_TABLE + " WHERE abbreviation='" + abbreviation + "'";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				int id = resultSet.getInt("id");
				return new Department(
						id,
						resultSet.getString("name"),
						resultSet.getString("abbreviation")
				);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Course getCourse(int departmentId, String courseCode) {
		try {
			String query = "SELECT * FROM " + COURSES_TABLE + " WHERE departmentId=" + departmentId + " AND code='" + courseCode + "'";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				int id = resultSet.getInt("id");
				return new Course(
						id,
						resultSet.getString("name"),
						resultSet.getString("code"),
						null,
						resultSet.getInt("credits")
				);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
