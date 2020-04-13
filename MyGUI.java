import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Right now there is a bug when searching for courses, after you have 
 * searched for one course and the information is displayed, is you try searching for another 
 * it just writes the label on top of the first one rather than replacing it.
 * Main things that need to be added are:
 * 1. Sending the student information to the server and checking whether or not it is valid
 * 2. once you confirm they are a student, reading their information about their schedule and name
 * 3. adding/removing new courses to their schedule once the button enroll/unenroll is clicked then updating the courseList to display
 * 4. adding information to the course catalogue
 * @author taylo
 *
 */


public class MyGUI extends Application{
	
	Stage window;
	Scene login, studentScene, adminScene, studentMenu;
	int width = 700;
	int height = 300;
	GridPane layout4;
	ListView<String> courseList;
	Label course;
	


	public static void main(String [] args) {
		launch(args);
	}
	
	@Override 
	public void start(Stage primaryStage) {
		window = primaryStage;
		window.setTitle("Login");
		course = new Label();
	
		//Layout 1 - Main window where user chooses between 
		//			 Student and Admin
		login = new Scene(optionScene(), width, height);
		
		//Layout 2 - Main Login window for Students where they
		//			 are prompted to input their id and password
		studentScene = new Scene(studentLogin(), width, height);
		
		//Layout 3 - Admin's main window (INCOMPLETE)
		adminScene = new Scene(adminLogin(), width, height);
		
		//Layout 4 - Student's main window with course search,
		// 			 their schedule, and where they can enroll in courses
		studentMenu = new Scene(studentMenu(), width, height);

		window.setScene(login);
		window.show();
	}
	/**
	 * Sets up the general layout of each window by setting 
	 * the padding, vertical gap, and horizontal gap
	 * @return general layout 
	 */
	private GridPane makeLayout() {
		GridPane layout = new GridPane();
		layout.setPadding(new Insets(10,10,10,10));
		layout.setVgap(8);
		layout.setHgap(10);
		
		return layout;
	}
	/**
	 * Creates the first scene which has the options for the 
	 * user to pick the student route or the admin one
	 * @return layout for the window
	 */
	private GridPane optionScene() {
		GridPane layout = makeLayout();
		
		//University of Calgary Title
		Label title = new Label("University of Calgary");
		GridPane.setConstraints(title, 22, 5);
				
		//Student Button
		Button student = new Button();
		student.setText("Sign In");
		student.setOnAction(e -> window.setScene(studentScene));
		GridPane.setConstraints(student, 18, 10);
		
		//Admin Button
		Button admin = new Button();
		admin.setText("Admin");
		admin.setOnAction(e -> window.setScene(adminScene));
		GridPane.setConstraints(admin, 26, 10);
		
		//Adding all the components to the layout
		layout.getChildren().addAll(title, student, admin);
		
		return layout;
	}
	
	/**
	 * Creates the first student scene which has the textfields to 
	 * prompt the user to enter their id and password
	 * @return layout for the window
	 */
	private GridPane studentLogin() {
		GridPane layout = makeLayout();
		
		//Student Id Block
		Label idLabel = new Label("Student ID");
		GridPane.setConstraints(idLabel, 16, 8);
		TextField idText = new TextField();
		GridPane.setConstraints(idText, 18, 8);
		
		//Student Password Block
		Label passwordLabel = new Label("Password");
		GridPane.setConstraints(passwordLabel, 16, 10);
		TextField passwordText = new TextField();
		GridPane.setConstraints(passwordText, 18, 10);
			
		//Login Button
		Button loginButton = new Button();
		loginButton.setText("Login");
		loginButton.setOnAction(e -> sendStudent(idText, passwordText));
		GridPane.setConstraints(loginButton, 18, 14);
		
		//Go Back Button
		Button goBack1 = new Button();
		goBack1.setText("Go Back");
		goBack1.setOnAction(e -> window.setScene(login));
		GridPane.setConstraints(goBack1, 0, 0);
		
		//Adding all the components to the layout
		layout.getChildren().addAll(idLabel, idText, passwordLabel, passwordText, goBack1, loginButton);
		
		return layout;
	}
	
	/**
	 * Creates the first admin scene which has the textfields to 
	 * prompt the user to enter their username and password
	 * @return layout for the window
	 */
	private GridPane studentMenu() {
		layout4 = makeLayout();
				
		//Welcome label
		Label welcome = new Label("Welcome"); // Add name of student
		GridPane.setConstraints(welcome, 0, 0);
				
		//Log Out Button
		Button logoutButton = new Button();
		logoutButton.setText("Log Out");
		logoutButton.setOnAction(e -> window.setScene(login));
		GridPane.setConstraints(logoutButton, 23, 0);
		
		//Browse Catalogue Button
		Button browse = new Button();
		browse.setText("Browse");
		browse.setOnAction(e -> browseCatalogue());
		GridPane.setConstraints(browse, 23, 13);
				
		//View Courses search
		Label course = new Label("View Course"); // Add name of student
		GridPane.setConstraints(course, 1, 3);
		TextField searchTag = new TextField("search for a course");
		GridPane.setConstraints(searchTag, 1, 5);
		Button search = new Button();
		search.setText("Search");
		search.setOnAction(e -> courseDisplay(searchTag));
		GridPane.setConstraints(search, 2, 5);
				
		//Schedule Display
		Label schedule = new Label("Your Schedule");
		GridPane.setConstraints(schedule, 15, 3);
		
		//Adding all the components to the layout
		layout4.getChildren().addAll(browse, searchTag, schedule, welcome, logoutButton, course, search);
		
		return layout4;
		
	}
	/**
	 * Creates the main window for the users which displays their schedule and
	 * prompts the user to search for and enroll in courses
	 * @return layout for the window
	 */
	private GridPane adminLogin() {
		GridPane layout = makeLayout();
		
		//Username Block
		Label idLabel = new Label("Username");
		GridPane.setConstraints(idLabel, 16, 8);
		TextField idText = new TextField();
		GridPane.setConstraints(idText, 18, 8);
		
		//Student Password Block
		Label passwordLabel = new Label("Password");
		GridPane.setConstraints(passwordLabel, 16, 10);
		TextField passwordText = new TextField();
		GridPane.setConstraints(passwordText, 18, 10);
			
		//Login Button
		Button loginButton = new Button();
		loginButton.setText("Login");
		loginButton.setOnAction(e -> window.setScene(login));//FIX
		GridPane.setConstraints(loginButton, 18, 14);
		
		//Go Back Button
		Button goBack2 = new Button();
		goBack2.setText("Go Back");
		goBack2.setOnAction(e -> window.setScene(login));
		GridPane.setConstraints(goBack2, 0, 0);
		
		//Adding all the components to the layout
		layout.getChildren().addAll(goBack2, loginButton, passwordLabel, passwordText, idLabel, idText);
		
		return layout;
	}
	
	
	private void sendStudent(TextField inputID, TextField inputPass) {
		try {
			int id = Integer.parseInt(inputID.getText());
			String password = inputPass.getText();
			//CALL FUNCTION TO SEND ID AND PASSWORD
			window.setScene(studentMenu);
		}catch(NumberFormatException e) {
			
		}
	}
	
	private void courseDisplay(TextField courseName) {
		//Lecture Drop-Down
		ChoiceBox<String> lectures = new ChoiceBox<>();
		lectures.getItems().add("Lecture 1");
		lectures.getItems().add("Lecture 2");
		GridPane.setConstraints(lectures, 2, 8);
		lectures.setValue("Lecture 1"); // default value
		
		//Course Name Label
		Label course = new Label(courseName.getText());
		//course.setText(courseName.getText());
		//course.textProperty().bind(courseName.textProperty());//FIX
		GridPane.setConstraints(course, 1, 8);
		
		//Spots Available
		Label spots = new Label("Spots: ");
		GridPane.setConstraints(spots, 1, 10);
		
		//Course Pre-Requisites
		Label preReqs = new Label("Pre-Requisites: ");
		GridPane.setConstraints(preReqs, 1, 11);
		
		//Other Available
		Label other = new Label("Other: ");
		GridPane.setConstraints(other, 1, 12);
		
		//Enroll/Unenroll 
		Button enroll = new Button();
		enroll.setText("Enroll/Unenroll");
		GridPane.setConstraints(enroll, 1, 13);
		enroll.setOnAction(e -> changeCourseEnrollment(courseName, lectures));
		
		layout4.getChildren().addAll(course, enroll, lectures, spots, preReqs, other);
	}
	/**
	 * 
	 * @param course
	 * @param lecture
	 */
	private void changeCourseEnrollment(TextField course, ChoiceBox<String> lecture) {
		String lec = lecture.getValue();
		String name = course.getText();
		updateSchedule(lec, name);
	}
	//Updates the student's schedule that is displayed on the right hand side of the 
	//window
	private void updateSchedule(String lecture, String name) {
		courseList = new ListView<>();
		courseList.setMaxWidth(100);
		courseList.getItems().add(name);
		GridPane.setConstraints(courseList, 15, 5);
		layout4.getChildren().add(courseList);
		
	}
	
	/**
	 * MAY NOT BE NEEDED IF WE DELETE THE POP UP WINDOW FOR COURSE CATALOGUE
	 */
	public void browseCatalogue() {
		Stage catalogue = new Stage();
		
		catalogue.initModality((Modality.APPLICATION_MODAL));
		catalogue.setTitle("Course Catalogue");
		catalogue.setMinWidth(250);
		
		
	}


}
