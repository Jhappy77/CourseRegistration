package server.model;
import java.util.ArrayList;

/**
 * Manager for the database
 * @author Joel Happ
 *
 */
public class DBManager {
	
	/**
	 * The list of courses
	 */
	private ArrayList <Course> courseList;
	
	/**
	 * The list of students
	 */
	private ArrayList <Student> studentList;

	/**
	 * Basic constructor for the database
	 */
	public DBManager () {
		courseList = new ArrayList<Course>();
		studentList = new ArrayList<Student>();
		studentList.add(new Student("Administration", 0, "300"));
	}

	/**
	 * Simulates reading from a database and loading courses
	 * @return a list of courses
	 */
	public ArrayList<Course> readFromDataBase() {
		Course c = new Course ("ENGG", 233);
		c.addOffering(new CourseOffering(1, 200));
		c.addOffering(new CourseOffering(2, 250));
		courseList.add(c);
		
		c = new Course("ENSF", 409);
		c.addOffering(new CourseOffering(1, 300));
		courseList.add(c);
		
		c= new Course("MATH", 277);
		c.addOffering(new CourseOffering(1, 150));
		c.addOffering(new CourseOffering(2, 150));
		c.addOffering(new CourseOffering(3, 150));
		c.addOffering(new CourseOffering(4, 150));
		courseList.add(c);
		
		c = new Course ("ENGG", 202);
		c.addOffering(new CourseOffering(1, 250));
		c.addOffering(new CourseOffering(2, 250));
		courseList.add(c);
		
		c= new Course("PHYS", 259);
		c.addOffering(new CourseOffering(1, 150));
		c.addOffering(new CourseOffering(2, 150));
		c.addOffering(new CourseOffering(3, 150));
		c.addOffering(new CourseOffering(4, 150));
		courseList.add(c);
		
		c= new Course("SUCC", 69);
		c.addOffering(new CourseOffering(1, 69));
		courseList.add(c);
		
		return courseList;
	}
	
	/**
	 * Simulates loading several students from a database
	 */
	public void fillStudentArrayList() {
		studentList.add(new Student("Timothy", 300769, "1234"));
		studentList.add(new Student("Petrune", 300669, "1234"));
		studentList.add(new Student("Donald", 308008, "1234"));
		studentList.add(new Student("Jebediah", 300750, "1234"));
		studentList.add(new Student("Luigi", 300546, "1234"));
		studentList.add(new Student("Zelda", 300333, "1234"));
		studentList.add(new Student("Moshi", 300777, "1234"));
		studentList.add(new Student("Egbert", 300543, "1234"));
		studentList.add(new Student("Zebulon", 300228, "1234"));
		studentList.add(new Student("Taylor Noel", 420420, "I Suk"));
	}
	
	/**
	 * Simulates registering students in a bunch of courses.
	 * @param cat Course catalogue
	 */
	public void addSampleCoursesToStudents(CourseCatalogue cat) throws Exception {
		int i = 0;
		for(Student st:studentList) {
			
			if(true) {
			CourseOffering co = cat.searchCatalogue("ENGG", 233).getCourseOfferingBySecNum(1);
			if(co!=null) {
				new Registration(st, co);
			}}
			
			if(i%2==0) {
				CourseOffering co = cat.searchCatalogue("MATH", 277).getCourseOfferingBySecNum(1);
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
	 * Runs a sample database test
	 * @param cat Catalogue to sync with
	 */
	public void sampleDBTest(CourseCatalogue cat) {
		System.out.println("Courses loaded: " + courseList.size());
		fillStudentArrayList();
		System.out.println("Students loaded: " + studentList.size());
		try {
			addSampleCoursesToStudents(cat);
		}catch(Exception e) {
			System.out.println("Error registering students in courses");
		}
	}
	
	/**
	 * Returns the student based on the id
	 * @param id
	 * @return The student
	 */
	public Student getStudent(int id) {
		for(Student s:studentList) {
			if(s.getStudentId() == (id))
				return s;
		}
		return null;
	}
	
	/**
	 * Print a list of all the students
	 */
	public void printAllStudents() {
		for(Student s:studentList) {
			System.out.println(s);
		}
	}
	
}
