package server.model;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;

import com.mysql.cj.jdbc.Driver;


/**
 * A very basic implementation of a MySQL Database reader.
 * For ENSF 409, we had very limited knowledge of how databases worked,
 * so the database we created only stores information for courses, offerings, and students.
 * The information about registrations is simulated through the use of functions, for a real application 
 * this would not be the case.
 * The database architecture is also basic- to improve this,
 * a database cache architecture could be implemented.
 * @author Joel Happ
 */
public class RealDB implements DBCredentials, DatabaseOperator{

		// Local array lists
		private ArrayList<Student> students;
		private ArrayList<Course> courses;
		
		
//		public static void main(String [] args) {
//			RealDB myApp = new RealDB();
//			myApp.initializeConnection();
//		}
		
		// Attributes
		private Connection conn;
		private ResultSet rs;
		
		public RealDB() {
			students = new ArrayList<Student>();
			courses = new ArrayList<Course>();
		}
		
		/**
		 * Loads the database and returns the filled Course Catalogue.
		 * @return The course catalogue, filled with all courses & offerings
		 */
		public CourseCatalogue loadDatabase() {
			initializeConnection();
			
			// Load from DB
			readAllStudents();
			readAllCourses();
			
			// Add all offerings for each course
			for(Course co: courses) {
				readOfferings(co);
			}
			
			// Create course catalogue
			CourseCatalogue c = new CourseCatalogue();
			c.setCourseList(courses);
			
			// Simulate loading registrations from DB
			try {
				addSampleCoursesToStudents(c);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// Prints message to server about success
			
			printSuccessMessage(c);
			
			return c;
		}
		
		private void printSuccessMessage(CourseCatalogue c) {
			System.out.println("Added " + c.getCourseCount() + " courses and " +
					students.size() + " students from database."); 
		}


		/**
		 * Initializes the connection with the MySQL Server via JDBC
		 */
		public void initializeConnection() {
			try {
				// Register JDBC driver
				Driver driver = new com.mysql.cj.jdbc.Driver();
				DriverManager.registerDriver(driver);
				// Open a connection
				conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			} catch (SQLException e) {
				System.out.println("Problem");
				e.printStackTrace();
			}

		}

		/**
		 * Closes the connection and the result set
		 */
		public void close() {
			try {
				rs.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Inserts user into MySQL Database
		 * @param id User ID
		 * @param name User's full name
		 * @param password User's password
		 */
		public void insertUserPreparedStatement(int id, String name, String password) {
			try {
				String query = "INSERT INTO STUDENTS (studentID, name, password) values(?,?,?)";
				PreparedStatement pStat = conn.prepareStatement(query);
				pStat.setInt(1, id);
				pStat.setString(2, name);
				pStat.setString(3, password);
				int rowCount = pStat.executeUpdate();
				System.out.println("row Count = " + rowCount);
				pStat.close();
			} catch (SQLException e) {
				System.out.println("problem inserting user");
				e.printStackTrace();
			}
		}
		
		/**
		 * Inserts a course into the MySQL Database
		 * @param subjCode Course name
		 * @param courseNum Course number
		 */
		public void insertCoursePreparedStatement(String subjCode, int courseNum) {
			try {
				String query = "INSERT INTO COURSETABLE (courseCode, courseNum) values(?,?)";
				PreparedStatement pStat = conn.prepareStatement(query);
				pStat.setString(1, subjCode);
				pStat.setInt(2, courseNum);
				int rowCount = pStat.executeUpdate();
				System.out.println("Course Row Count = " + rowCount);
				pStat.close();
			} catch (SQLException e) {
				System.out.println("problem inserting course" + subjCode + " " +courseNum);
				e.printStackTrace();
			}
		}
		
		/**
		 * Inserts an offering into the MySQL Database
		 * @param secNum Section number for offering
		 * @param secCap Section cap for offering
		 * @param c Course which offering is for
		 */
		public void insertOffering(int secNum, int secCap, Course c) {
			try {
				String query = "INSERT INTO offerings (secNum, secCap, courseID) values(?,?,?)";
				PreparedStatement pStat = conn.prepareStatement(query);
				pStat.setInt(1, secNum);
				pStat.setInt(2, secCap);
				pStat.setInt(3, c.getID());
				int rowCount = pStat.executeUpdate();
				System.out.println("Offering Row Count = " + rowCount);
				pStat.close();
			} catch (SQLException e) {
				System.out.println("problem inserting offering for" + c.getFullCourseName() + " section" + secNum);
				e.printStackTrace();
			} catch(NullPointerException e) {
				System.out.println("Could not insert offering with no ID");
			}
		}

		/**
		 * Code for creating a mySQL table for offerings.
		 * Should only be executed once.
		 */
		public void createTable() {
////			String sql = "CREATE TABLE courseTable" + "(id INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, " + " courseCode VARCHAR(30), " +" courseNum INTEGER)";
////
////			try {
////				Statement stmt = conn.createStatement(); // construct a statement
////				stmt.executeUpdate(sql); // execute my query (i.e. sql)
////				stmt.close();
////			} catch (SQLException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////				System.out.println("Table can NOT be created!");
////			}
////			System.out.println("Created table in given database...");
////			
////			
////			sql = "CREATE TABLE offerings" + "(id INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, " + " secNum INTEGER, " +
////			" secCap INTEGER," + "courseID INTEGER)";
////
////			try {
////				Statement stmt = conn.createStatement(); // construct a statement
////				stmt.executeUpdate(sql); // execute my query (i.e. sql)
////				stmt.close();
////			} catch (SQLException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////				System.out.println("Table can NOT be created!");
////			}
////			System.out.println("Created table in given database...");
		}
		
		/**
		 * Reads all students from database, stores them in the students arrayList
		 */
		private void readAllStudents() {
			try {
				String query = "SELECT * FROM STUDENTS";
				Statement st = conn.createStatement();
				rs = st.executeQuery(query);
				while(rs.next()) {
					Student s = new Student(rs.getString("name"), rs.getInt("studentID"), rs.getString("password"));
					//System.out.println(s);
					students.add(s);
				}
				st.close();
			} catch(SQLException e) {
				System.out.println("Problem loading students");
				e.printStackTrace();
			}
		}
		
		
		/**
		 * Reads all courses from database, stores them in the courses arrayList
		 */
		private void readAllCourses() {
			try {
				String query = "SELECT * FROM COURSETABLE";
				Statement st = conn.createStatement();
				rs = st.executeQuery(query);
				while(rs.next()) {
					Course c = new Course(rs.getString("courseCode"), rs.getInt("courseNum"), rs.getInt("id"));
					//System.out.println(c);
					courses.add(c);
				}
				st.close();
			} catch(SQLException e) {
				System.out.println("Problem reading courses");
				e.printStackTrace();
			}
		}
		
		
		/**
		 * Reads specific course from DB
		 */
		private Course readCourse(String courseCode, int courseNum) {
			try {
				String query = "SELECT * FROM COURSETABLE where courseCode='" + courseCode +"' AND courseNum=" +courseNum;
				Statement st = conn.createStatement();
				rs = st.executeQuery(query);
				while(rs.next()) {
					Course c = new Course(rs.getString("courseCode"), rs.getInt("courseNum"), rs.getInt("id"));
					return c;
				}
				st.close();
			} catch(SQLException e) {
				System.out.println("Problem reading courses");
				e.printStackTrace();
			}
			return null;
		}
		
		
		/**
		 * Reads all offerings for a particular course from database, and them to that course.
		 */
		private void readOfferings(Course c) {
			try {
				String idString = Integer.toString(c.getID());
				String query = "SELECT * FROM offerings WHERE courseID=" + idString;
				Statement st = conn.createStatement();
				rs = st.executeQuery(query);
				while(rs.next()) {
					c.addOffering(new CourseOffering(rs.getInt("secNum"), rs.getInt("secCap")));
					//System.out.println("read an offering for " + c);
				}
				st.close();
				rs.close();
			} catch(SQLException e) {
				System.out.println("Problem reading offerings for course" + c.getFullCourseName());
				e.printStackTrace();
			}
		}
		
////		/**
////		 * Creates a bunch of sample course offerings for the courses in
////		 * the array list. Only needs to be executed once.
////		 */
////		private void createSampleCourseOfferings() {
////			int i = 0;
////			for(Course c:courses) {
////				if(i%5 == 0) {
////					insertOffering(1, 200,c);
////					insertOffering(2, 250,c);
////				}
////				else if(i%5==1) {
////					insertOffering(1, 69,c);
////				}
////				else if(i%5==2) {
////					insertOffering(1, 300,c);
////					insertOffering(2, 300,c );
////					insertOffering(3, 300, c);
////					insertOffering(4, 300, c);
////				}
////				else if(i%5==3) {
////					insertOffering(1, 150, c);
////					insertOffering(2, 250,c);
////				}
////				else {
////					insertOffering(1, 500, c);
////					insertOffering(2, 100, c);
////					insertOffering(3, 150, c);
////				}
////				i++;
////			}
////		}
//		
////		/**
////		 * Inserts a bunch of test users into the students table. 
////		 * Only ever needs to be called once.
////		 */
////		private void insertSampleUsers() {
////			insertUserPreparedStatement(300769, "Timothy",  "1234");
////			insertUserPreparedStatement(300669, "Petrune",  "1234");
////			insertUserPreparedStatement(308008, "Donald", "1234");
////			insertUserPreparedStatement(300750, "Jebediah", "1234");
////			insertUserPreparedStatement(300456, "Luigi", "1234");
////			insertUserPreparedStatement(300333, "Zelda", "1234");
////			insertUserPreparedStatement(300777, "Moshi", "1234");
////			insertUserPreparedStatement(300543, "Egbert", "1234");
////			insertUserPreparedStatement(300228, "Zebulon", "1234");
////			insertUserPreparedStatement(420420, "Taylor Noel", "I Suk");
////			insertUserPreparedStatement(300123, "Danny DeVito", "Hot");
////		}
////		
////		/**
////		 * Inserts a bunch of test courses into the courses table.
////		 * Only ever needs to be called once.
////		 */
////		private void insertSampleCourses() {
////			insertCoursePreparedStatement("ENGG", 233);
////			insertCoursePreparedStatement("ENSF", 409);
////			insertCoursePreparedStatement("MATH", 277);
////			insertCoursePreparedStatement("ENGG", 202);
////			insertCoursePreparedStatement("PHYS", 259);
////			insertCoursePreparedStatement("SUCC", 69);
////			insertCoursePreparedStatement("ENGG", 201);
////			insertCoursePreparedStatement("ENCM", 369);
////			insertCoursePreparedStatement("ECON", 201);
////		}
		
		/**
		 * Returns the student based on the id.
		 * Since all the students are already stored in the students 
		 * arrayList, no query is necessary. In the future, this 
		 * architecture should be improved and modified to fit a database
		 * caching model. 
		 * @param id
		 * @return The student
		 */
		public Student getStudent(int id) {
			for(Student s:students) {
				if(s.getStudentId() == (id))
					return s;
			}
			return null;
		}
		
		
		/**
		 * Simulates registering students in a bunch of courses.
		 * @param cat Course catalogue
		 */
		public void addSampleCoursesToStudents(CourseCatalogue cat) throws Exception {
			int i = 0;
			for(Student st:students) {
				
				if(true) {
				CourseOffering co = cat.searchCatalogue("ENGG", 233).getCourseOfferingBySecNum(1);
				if(co!=null) {
					new Registration(st, co);
				}}
				
				if(i%2==0) {
					CourseOffering co = cat.searchCatalogue("MATH", 211).getCourseOfferingBySecNum(1);
					if(co!=null) {
						new Registration(st, co);
					}
				}
				
				if(i%2==1) {
					CourseOffering co = cat.searchCatalogue("MATH", 211).getCourseOfferingBySecNum(2);
					if(co!=null) {
						new Registration(st, co);
					}
				}
				
				if(i%3==0) {
					CourseOffering co = cat.searchCatalogue("ENSF", 409).getCourseOfferingBySecNum(1);
					if(co!=null) {
						new Registration(st, co);
					}
				}
				
				if(i%4==0) {
					CourseOffering co = cat.searchCatalogue("ENGG", 202).getCourseOfferingBySecNum(1);
					if(co!=null) {
						new Registration(st, co);
					}
				}
			
				i++;
				
				if(st.getStudentName().contentEquals("Taylor Noel"))
					new Registration(st, cat.searchCatalogue("SUCC", 69).getCourseOfferingBySecNum(1));
			}
		}
		
		/**
		 * Saves a passed course to the database.
		 */
		public void saveCourse(Course c) {
			insertCoursePreparedStatement(c.getCourseName(), c.getCourseNum());
			// Gets the course to ensure it has the right ID
			Course added = readCourse(c.getCourseName(), c.getCourseNum());
			try {
				for(int i= 0; true; i++) {
				CourseOffering co = c.getCourseOfferingByIndex(i);
				insertOffering(co.getSecNum(), co.getSecCap(), added);
				}
			} catch(Exception e) {
				System.out.println("Finished adding course's offerings");
			}
		}

	
}
