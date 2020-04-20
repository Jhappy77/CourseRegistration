package client.view;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle.Control;

//CLASS IMPORTS
import client.controller.Controller;
import server.controller.CourseLite;
import server.model.Course;

// JAVAFX IMPORTS

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

// HOW TO MAKE JAVAFX ACCESSIBLE
/*right-click on the project and bring up the project properties dialog. Select "Build Path" in the left pane, and select the "Libraries" tab. You will see a "JRE System Library" entry. Expand that entry, and you will see an "Access Rules" subentry:

Select the "Access Rules" entry and click "Edit". Click "Add".

Under "Resolution", choose "Accessible", and under "Rule Pattern", enter javafx/** */


/**
 * Right now there is a bug when searching for courses, after you have
 * searched for one course and the information is displayed, is you try searching for another
 * it just writes the label on top of the first one rather than replacing it.
 * Main things that need to be added are:
 *
 *
 * ADMIN
 * 3. allow admin to create a new courses (# of offerings, spots per offering, prerequisites) // EVERYTHING WORKING EXCEPT ADDING PREREQS
 *
 * @author Taylor
 *
 */


public class MyGUI extends Application{


	Stage window;
	Scene login, studentScene, adminScene, studentMenu;
	int width = 1200;
	int height = 800;



	public static void main(String [] args) {
		launch(args);
	}

	private Controller control;

	@Override
	public void start(Stage primaryStage) {


		System.out.println("Client Started");


		// Creates new reference to ClientApp (Controller)
		control = new Controller(this);


		System.out.println("Building stage");


		window = primaryStage;
		window.setTitle("Login");

		//Layout 1 - Main window where user chooses between
		//			 Student and Admin
		login = new Scene(optionScene(), width, height);
		login.getStylesheets().add("dinos.css");

		//Layout 2 - Main Login window for Students where they
		//			 are prompted to input their id and password
		studentScene = new Scene(studentLogin(), width, height);
		studentScene.getStylesheets().add("dinos.css");


		window.setScene(login);
		window.show();
	}

///////////////////////////GRID PANES ////////////////////////////////////////


	/**
	 * Sets up the general layout of each window by setting
	 * the padding, vertical gap, and horizontal gap
	 * @return general layout
	 */
	private VBox makeLayout() {
		VBox layout = new VBox();
		layout.setPadding(new Insets(10,10,10,10));
		layout.setSpacing(15);
		return layout;
	}
	/**
	 * Creates the first scene which has the options for the
	 * user to pick the student route or the admin one
	 * @return layout for the window
	 */
	private VBox optionScene() {
		VBox layout = makeLayout();
		layout.setSpacing(10);
		layout.setAlignment(Pos.CENTER);

		//University of Calgary Title
		Label title = new Label("University of Calgary");
		title.setId("bold-label");

		//Student Button
		Button student = new Button();
		student.setText("Sign In");
		student.setOnAction(e -> window.setScene(studentScene));

		//Admin Button
		Button admin = new Button();
		admin.setText("Admin");
		admin.setOnAction(e -> adminScene());

		//HBox for the buttons so they are on the same line
		HBox buttons = new HBox();
		buttons.setSpacing(100);
		buttons.getChildren().addAll(student, admin);
		buttons.setAlignment(Pos.CENTER);

		//Adding all the components to the layout
		layout.getChildren().addAll(title, buttons);

		return layout;
	}

	/**
	 * Creates the first student scene which has the text fields to
	 * prompt the user to enter their id and password
	 * @return layout for the window
	 */
	private VBox studentLogin() {
		VBox layout = makeLayout();
		HBox topPanel = new HBox();
		VBox centrePanel = new VBox();
		centrePanel.setSpacing(10);
		centrePanel.setMinHeight(height);
		centrePanel.setAlignment(Pos.CENTER);

		//Student Id Block
		HBox idBlock = new HBox();
		idBlock.setSpacing(10);
		idBlock.setAlignment(Pos.CENTER);
		Label idLabel = new Label("Student ID");
		TextField idText = new TextField();
		idText.setId("text-input");
		idBlock.getChildren().addAll(idLabel,idText);

		//Student Password Block
		HBox passwordBlock = new HBox();
		passwordBlock.setSpacing(10);
		passwordBlock.setAlignment(Pos.CENTER);
		Label passwordLabel = new Label("Password");
		PasswordField passwordText = new PasswordField();
		passwordText.setId("text-input");
		passwordBlock.getChildren().addAll(passwordLabel, passwordText);

		//Login Response Label
		Label loginResponse = new Label("");

		//Login Button
		Button loginButton = new Button();
		loginButton.setText("Login");
		loginButton.setOnAction(e -> {
			loginStudent(idText, passwordText, loginResponse);
			});

		//Go Back Button
		Button goBack1 = new Button();
		goBack1.setText("Go Back");
		goBack1.setOnAction(e -> window.setScene(login));
		topPanel.getChildren().add(goBack1);

		centrePanel.getChildren().addAll(idBlock, passwordBlock, loginButton, loginResponse);

		//Adding all the components to the layout
		layout.getChildren().addAll(topPanel, centrePanel);

		return layout;
	}

	/**
	 * Creates the first admin scene which has the textfields to
	 * prompt the user to enter their username and password
	 * @return layout for the window
	 */
	private VBox studentMenu() {
		VBox layout = makeLayout();

		HBox title = setTitle();
		HBox panels = setPanels(setLeftPanel(), setRightPanel());

		//Adding all the components to the layout
		layout.getChildren().addAll(title, panels);

		return layout;

	}

	/**
	 * Shows the course display of a certain course
	 * @param courseName Name of the course
	 */
	private VBox courseDisplay(TextField courseName, int num) {
		VBox layout = makeLayout();
		HBox title = setTitle();

		VBox leftPanel = setLeftPanel();

		HBox panels = setPanels(leftPanel, setRightPanel());

		String input = courseName.getText();

		//Try and find the course
		try
		{
			control.selectCourse(splitCName(input), splitCNumber(input));
		}
		catch (Exception e)
		{
			makePopup("Error", e.getMessage());
		}

		HBox courseInfo = new HBox();
		courseInfo.setAlignment(Pos.CENTER);
		courseInfo.setSpacing(10);

		//Lecture Drop-Down
		ChoiceBox<String> lectures = new ChoiceBox<>();
		for(int i = 0; i < control.getSelectedCourseOfferings();) {
			lectures.getItems().add("Lecture " + (++i));
		}

		lectures.setValue("Lecture " + num); // default value

		//Listen for selection changes
		lectures.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> changeOffering(newValue, courseName));

		//Course Name Label
		Label course = new Label(control.getSelectedCourseName());

		courseInfo.getChildren().addAll(course, lectures);

		//Spots Available
		Label spots = new Label("Spots: " + control.getSelectedCourseSpots());

		//Enroll/Unenroll
		Button enroll = new Button();
		enroll.setText("Enroll/Unenroll");
		enroll.setAlignment(Pos.CENTER);
		enroll.setOnAction(e -> changeCourseEnrollment());

		leftPanel.getChildren().addAll(courseInfo, spots, enroll);

		layout.getChildren().addAll(title, panels);



		return layout;

	}


	/**
	 * Creates the main window for the users which displays their schedule and
	 * prompts the user to search for and enroll in courses
	 * @return layout for the window
	 */
	private VBox adminLogin() {
		VBox layout = makeLayout();
		layout.setSpacing(50);
		HBox topPanel = new HBox();
		VBox centrePanel = new VBox();
		centrePanel.setMinHeight(height - 100);
		centrePanel.setSpacing(10);
		centrePanel.setAlignment(Pos.CENTER);

		// Password Block
		HBox passwordBlock = new HBox();
		passwordBlock.setSpacing(10);
		passwordBlock.setAlignment(Pos.CENTER);
		Label passwordLabel = new Label("Password");
		PasswordField passwordText = new PasswordField();
		passwordBlock.getChildren().addAll(passwordLabel, passwordText);

		//Login Response Label
		Label loginResponse = new Label("");

		//Login Button
		Button loginButton = new Button();
		loginButton.setText("Login");
		loginButton.setOnAction(e -> {
			loginAdmin(passwordText, loginResponse); //!! MAKE FUNCTION
			});

		//Go Back Button
		Button goBack1 = new Button();
		goBack1.setText("Go Back");
		goBack1.setOnAction(e -> window.setScene(login));
		topPanel.getChildren().add(goBack1);

		centrePanel.getChildren().addAll(passwordBlock, loginButton, loginResponse);

		//Adding all the components to the layout
		layout.getChildren().addAll(topPanel, centrePanel);

		return layout;
	}

	/**
	 * ADMIN main menu
	 */
	private VBox adminMenu() {
		VBox layout = makeLayout();

		HBox title = new HBox();
		title.setSpacing(width*3/5);

		//Welcome label
		Label welcome = new Label("Welcome, " + control.getStudentName());

		//Log Out Button
		Button logoutButton = new Button();
		logoutButton.setText("Log Out");
		logoutButton.setOnAction(e -> logout());

		title.getChildren().addAll(welcome, logoutButton);


		VBox left = panelSetUp();
		VBox right = panelSetUp();
		HBox panels = new HBox(left, right);

		//Course Catalogue Label
		Label catalogue = new Label();
		catalogue.setText("Course Catalogue");
		right.getChildren().addAll(catalogue, buildTable());

		//Add a new course
		Button newCourse = new Button("Add New Course");
		newCourse.setOnAction(e -> addCourseWindow());

		left.getChildren().addAll(newCourse);

		//Adding all the components to the layout
		layout.getChildren().addAll(title, panels);

		return layout;

	}

	/**
	 * Logout logic
	 */
	private void logout()
	{
		control.logout();
		window.setScene(login);
	}




	/////////// END OF GRID PANES //////////////////////////////////

	/**
	 * add the welcome, logout button and browse button to the top panel
	 */
	private HBox setTitle() {
		HBox title = new HBox();
		title.setSpacing(200);

		//Welcome label
		Label welcome = new Label("Welcome, " + control.getStudentName());

		//Log Out Button
		Button logoutButton = new Button();
		logoutButton.setText("Log Out");
		logoutButton.setOnAction(e -> logout());

		//Browse Catalogue Button
		Button browse = new Button();
		browse.setText("Browse Catalogue");
		browse.setOnAction(e -> browseCatalogue());
		title.getChildren().addAll(welcome, browse, logoutButton);
		title.setAlignment(Pos.CENTER);

		return title;
	}
	/**
	 * add the course search to the left panel
	 * @return
	 */
	private VBox setLeftPanel() {
		VBox leftPanel = panelSetUp();

		//View Courses search
		Label courseLabel = new Label("View Course");

		HBox searchPanel = new HBox();
		searchPanel.setAlignment(Pos.CENTER);
		searchPanel.setSpacing(10);
		TextField searchTag = new TextField("search for a course");
		Button search = new Button("Search");
		search.setOnAction(e -> activateSearch(searchTag));
		searchPanel.getChildren().addAll(searchTag, search);
		leftPanel.getChildren().addAll(courseLabel, searchPanel);

		return leftPanel;
	}
	/**
	 * Set up that can be used for panels, such as the left and right ones in the courseDisplay
	 * @return
	 */
	private VBox panelSetUp() {
		VBox panel = new VBox();
		panel.setSpacing(10);
		panel.setMinWidth(width/2);
		panel.setPadding(new Insets(20,20,20,20));
		panel.setAlignment(Pos.TOP_CENTER);
		return panel;
	}

	/**
	 * add the schedule to the right panel
	 * @return
	 */
	private VBox setRightPanel() {
		VBox rightPanel = panelSetUp();

		//Schedule List
		ListView<String> courseList = new ListView<>();
		fillCourses(courseList);
		courseList.setMaxWidth(width/2 - 100);
		courseList.setMaxHeight(250);


		//Schedule Display
		Label schedule = new Label("Your Schedule");
		rightPanel.getChildren().addAll(schedule, courseList);

		return rightPanel;
	}
	/**
	 * add the left and right panel to the centre panel
	 * @param leftPanel
	 * @param rightPanel
	 * @return
	 */
	private HBox setPanels(VBox leftPanel, VBox rightPanel) {
		HBox panels = new HBox();
		panels.setPadding(new Insets(10,10,10,10));
		panels.getChildren().addAll(leftPanel, rightPanel);

		return panels;
	}


	/**
	 * Gets what the student has entered into log-in field, logs in student
	 * @param inputID The textfield to input ID
	 * @param inputPass The textfield to input Password
	 */
	private void loginStudent(TextField inputID, TextField inputPass, Label responseLabel) {
		try {
			int id = Integer.parseInt(inputID.getText());
			String password = inputPass.getText();
			inputID.clear();
			inputPass.clear();
			control.login(id, password);
			System.out.println("Passed control");

		}catch(NumberFormatException e) {
			responseLabel.setText("Invalid username entered! Please enter a student ID!");
		}catch(Exception e) {
			// Sets the response label to an appropriate message based on issue
			// Note: incomplete
			responseLabel.setText(e.getMessage());
		}
	}
	/**
	 * change the window to the student menu
	 */
	public void setStudentMenu() {
		Scene scene = new Scene(studentMenu(), width, height);
		styleAndSwitch(scene);
	}

	/**
	 * Splits course name from string
	 * @param courseName
	 * @return
	 */
	private String splitCName(String courseName) throws Exception{
		try
		{
			return courseName.split(" ")[0];
		}
		catch (Exception e)
		{
			throw new Exception("Input in invalid format");
		}
	}

	/**
	 * Splits course number from string
	 * @param courseName
	 * @return course number
	 */
	private int splitCNumber(String courseName) throws Exception {
		try
		{
			return Integer.parseInt(courseName.split(" ")[1]);
		}
		catch (Exception e)
		{
			throw new Exception("Input in invalid format");
		}
	}

	/**
	 * Splits course offering from string
	 * @param courseName
	 * @return course number
	 */
	private int splitCOffering(String courseName) {
		return Integer.parseInt(courseName.split(" ")[3]);
	}

	/**
	 * Checks to see if the student is enrolled and unenrolls is they are, otherwise enrolls them in the course.
	 */
	private void changeCourseEnrollment() {

		if(control.checkEnrolment())
		{
			//Try un enrolling from the course
			try
			{
				String text = control.unenroll();
				makePopup("Success", text);
			}
			catch (Exception e)
			{
				//Make popup
				makePopup("Error", e.getMessage());
			}
		}

		else
		{
			//Try enrolling for the course
			try
			{
				String text = control.enroll();
				makePopup("Success",text);
			}
			catch (Exception e)
			{
				//Make a popup
				makePopup("Error",e.getMessage());
			}
		}



		Scene scene = new Scene (studentMenu(), width, height);
		styleAndSwitch(scene);
	}
	/**
	 * Gets the student's schedule from the controller and then fills a ListView in order
	 * to display the information
	 * @param courseList Component used to display the student's schedule
	 */
	private void fillCourses(ListView<String> courseList){
		try
		{
			CourseLite [] courses = control.getSchedule();
			if(courses != null) {
				for(int i = 0; i < courses.length; i++) {
					courseList.getItems().add(courses[i].getName() + " " + courses[i].getNumber() + " Section: " + courses[i].getEnrolledSectionNumber());
				}
			}
			courseList.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> changeView(newValue));
		}
		catch (Exception e)
		{
			makePopup("Error", e.getMessage());
		}

	}

	/**
	 * based on clicking a course in your schedule, change the left side of the screen to display the information
	 * @param value
	 */
	private void changeView(String value) {
		String courseName = " ";
		int courseNum = 0;
		try
		{
			courseName = splitCName(value);
			courseNum = splitCNumber(value);
		}
		catch (Exception e)
		{
			makePopup("Error", e.getMessage());
		}

		TextField course = new TextField();
		course.setText(courseName + " " + courseNum);
		int num = splitCOffering(value);
		control.setOffering(num);

		Scene scene = new Scene(courseDisplay(course, num), width, height);
		styleAndSwitch(scene);
	}


	/**
	 * Window to display the course catalogue
	 */
	public void browseCatalogue() {
		Stage catalogue = new Stage();

		catalogue.initModality((Modality.APPLICATION_MODAL));
		catalogue.setTitle("Course Catalogue");
		catalogue.setMinWidth(250);

		VBox layout = new VBox();

		layout.getChildren().addAll(buildTable());

		Scene scene = new Scene(layout);
		scene.getStylesheets().add("dinos.css");
		catalogue.setScene(scene);
		catalogue.showAndWait();

	}
	/**
	 * Build the course catalogue table
	 * @return
	 */
	private TableView<CourseLite> buildTable(){
		ObservableList<CourseLite> courses = FXCollections.observableArrayList();
		try
		{
			CourseLite[] list = control.getCatalogue();
			if(list != null) {
				for(CourseLite c: list) {
					courses.add(c);
				}
			}
		}
		catch (Exception e)
		{
			makePopup("Error", e.getMessage());
		}



		TableView<CourseLite> table;

		TableColumn<CourseLite, String> nameCol = new TableColumn<>("Course");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<CourseLite, Integer> numberCol = new TableColumn<>("Number");
		numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));

		TableColumn<CourseLite, Integer> offeringsCol = new TableColumn<>("Number of Offerings");
		offeringsCol.setCellValueFactory(new PropertyValueFactory<>("offeringCount"));


		table = new TableView<>(courses);
		table.setMinWidth(width/2 - 100);
		table.setMinHeight(height - 200);
		numberCol.setMinWidth(table.getMinWidth()/2);
		offeringsCol.setMinWidth(table.getMinWidth()/2);
		table.getColumns().addAll(nameCol, numberCol, offeringsCol);
		return table;
	}

	/**
	 * Change the offering based on lecture drop down
	 * @param offering
	 * @param courseName
	 */
	private void changeOffering(String offering, TextField courseName) {
		int num = 1;
		try
		{
			num = splitCNumber(offering);
		}
		catch (Exception e)
		{
			makePopup("Error", e.getMessage());
		}

		control.setOffering(num);

		Scene scene = new Scene(courseDisplay(courseName, num), width, height);
		styleAndSwitch(scene);
	}

	/**
	 * Log an admin in based on if the password is correct
	 * @param inputPass Password inputted by user
	 * @param responseLabel
	 */
	private void loginAdmin(TextField inputPass, Label responseLabel) {
		try {
			String password = inputPass.getText();
			inputPass.clear();
			control.login(0, password);
			System.out.println("Passed control");

		}catch(NumberFormatException e) {
			responseLabel.setText("Invalid username entered! Please enter a student ID!");
		}catch(Exception e) {
			// Sets the response label to an appropriate message based on issue
			// Note: incomplete
			responseLabel.setText(e.getMessage());
		}

	}
	/**
	 * change the window to the administration menu
	 */
	public void setAdminMenu() {
		Scene scene = new Scene(adminMenu(), width, height);
		styleAndSwitch(scene);
	}

	/**
	 * From admin, add course to the catalogue.
	 * asks user for # of offerings, spots per offering, prerequisites
	 * @return
	 */
	private void addCourseWindow() {
		Stage newCourse = new Stage();

		newCourse.initModality((Modality.APPLICATION_MODAL));
		newCourse.setTitle("Add New Course");

		VBox layout = new VBox();
		layout.setPadding(new Insets(10,10,10,10));
		layout.setSpacing(10);
		layout.setAlignment(Pos.CENTER);

		//Course name
		HBox codeBlock = new HBox();
		codeBlock.setSpacing(10);
		Label codeLabel = new Label("Course Code");
		TextField codeText = new TextField();
		codeBlock.getChildren().addAll(codeLabel, codeText);

		//number of offerings
		HBox offeringsBlock = new HBox();
		offeringsBlock.setSpacing(10);
		Label offeringsLabel = new Label("Number of Offerings");
		TextField offeringsText = new TextField();
		offeringsBlock.getChildren().addAll(offeringsLabel, offeringsText);

		//number of spots per offering
		HBox spotsBlock = new HBox();
		spotsBlock.setSpacing(10);
		Label spotsLabel = new Label("Number of Spots");
		TextField spotsText = new TextField();
		spotsBlock.getChildren().addAll(spotsLabel, spotsText);


		HBox first = new HBox();
		first.setSpacing(10);
		first.setAlignment(Pos.CENTER);
		first.getChildren().add(codeBlock);

		HBox second = new HBox();
		second.setSpacing(10);
		second.setAlignment(Pos.CENTER);
		second.getChildren().addAll(offeringsBlock, spotsBlock);

		Button commit = new Button("Commit");
		commit.setOnAction(e -> addCourse(codeText, offeringsText, spotsText, newCourse));


		layout.getChildren().addAll(first, second, commit);

		Scene scene = new Scene(layout);
		scene.getStylesheets().add("dinos.css");
		newCourse.setScene(scene);
		newCourse.showAndWait();

	}
	/**
	 * Commit the new course to the catalogue
	 */
	private void addCourse(TextField course, TextField offering, TextField spot, Stage win) {

		try
		{
			String message = control.makeCourse(splitCName(course.getText()), splitCNumber(course.getText()), Integer.parseInt(offering.getText()), Integer.parseInt(spot.getText()));
			makePopup("Success", message);
		}
		catch (Exception e)
		{
			makePopup("Error", e.getMessage());
		}

		setAdminMenu();
		win.close();

	}

	/**
	 * change the screen based on searching for a new course
	 * @param searchTag
	 */
	public void activateSearch(TextField searchTag) {
		control.setOffering(1);
		Scene scene = new Scene(courseDisplay(searchTag, 1), width, height);
		styleAndSwitch(scene);

	}
	/**
	 * make whichever scene passed adopt the style from dinos.css
	 * @param scene
	 */
	public void styleAndSwitch(Scene scene) {
		scene.getStylesheets().add("dinos.css");
		window.setScene(scene);
	}
	/**
	 * switch to admin login
	 */
	public void adminScene() {
		Scene scene = new Scene(adminLogin(), width, height);
		styleAndSwitch(scene);
	}

	/**
	 * Makes a popup window
	 * @param name
	 * @param text
	 */
	private void makePopup(String name, String text)
	{

		Stage popUp = new Stage();

		//Male window
		popUp.initModality(Modality.APPLICATION_MODAL);
		popUp.setTitle(name);
		popUp.setMinWidth(350);
		popUp.setMinHeight(150);

		//Make the text label
		Label label = new Label();
		label.setText(text);

		//Make close button
		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> popUp.close());

		//Add to layout
		VBox layout = new VBox(30);
		layout.getChildren().addAll(label, closeButton);
		layout.setAlignment(Pos.CENTER);

		//Make window
		Scene scene = new Scene(layout);
		popUp.setScene(scene);
		popUp.showAndWait();
	}

}
