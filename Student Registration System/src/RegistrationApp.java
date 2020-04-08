import java.util.Scanner;

public class RegistrationApp {
	private Scanner scan;
	private CourseCatalogue cat;
	private DBManager db;
	
	public RegistrationApp() {
		scan = new Scanner(System.in);
		cat = new CourseCatalogue();
		db = new DBManager();
		db.sampleDBTest(cat);
	}
	
	/**
	 * Displays a menu for the user
	 */
	public void displayMenu() {
		System.out.println("Please select one of the following choices:");
		System.out.println("1. Search Catalogue Courses");
		System.out.println("2. Add course to a student's courses");
		System.out.println("3. Remove course from a student's courses");
		System.out.println("4. View all courses in the catalogue");
		System.out.println("5. View all courses taken by a student");
		System.out.println("6. View all students (FOR TESTING)");
		System.out.println("7. Quit Program");
		System.out.println("");
		System.out.println("Enter your selection:");
	}
	
	/**
	 * Lets user select different options to interact with backend
	 */
	public void menu() {
		while(true) {
			displayMenu();
			int selection = scan.nextInt();
			scan.nextLine();
			switch(selection) {
				case 1:
					searchCatalogueCourses();
					break;
				case 2:
					addCourseToStudent();
					break;
				case 3:
					removeCourseFromStudent();
					break;
				case 4:
					viewAllCoursesCatalogue();
					break;
				case 5:
					viewAllCoursesStudent();
					break;
				case 6:
					viewAllStudents();
				case 7:
					System.exit(0);
					break;
			}
		}
	}
	
	private void removeCourseFromStudent() {
		try {
		getStudent().removeCourse(getCourse());
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void searchCatalogueCourses() {
		try {
		System.out.println(getCourse());
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	private void addCourseToStudent() {
		try {
		getStudent().addCourseOffering(getCourseOffering());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void viewAllCoursesStudent() {
		System.out.println(getStudent().stringAllRegs());
	}
	
	private void viewAllCoursesCatalogue() {
		System.out.println(cat);
	}
	
	private void viewAllStudents() {
		db.printAllStudents();
	}
	
	//////////////////// GETTERS ////////////////////////
	private CourseOffering getCourseOffering() throws Exception{
		Course c = getCourse();
		System.out.println("Enter the lecture section you are searching for:");
		return c.getCourseOfferingByNum(scan.nextInt()-1);
	}
	
	private Course getCourse() throws Exception{
		return cat.searchCatalogue(scanCourseName(), scanCourseNumber());
	}
	
	private Student getStudent() {
		Student s = db.getStudent(scanStudentName());
		if(s==null) {
			System.err.println("The student you searched for does not exist");
			System.exit(1);
			return null;
		}
		return s;
	}
	
	
	///////////////////// SCANNERS //////////////////////
	private String scanStudentName() {
		System.out.println("Please enter the name of the student:");
		return scan.nextLine();
	}
	private String scanCourseName() {
		System.out.println("Please enter the name of the course: (e.g. ENSF)");
		return scan.nextLine().trim().toUpperCase();
	}
	
	private int scanCourseNumber() {
		System.out.println("Please enter the number of the course: (e.g. 409)");
		return scan.nextInt();
	}
	
	public static void main (String [] args) {
		RegistrationApp theApp = new RegistrationApp();
		theApp.menu();		
	}

}
