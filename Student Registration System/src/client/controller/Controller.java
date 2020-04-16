package client.controller;

import client.view.MyGUI;
import server.controller.CourseLite;
import client.model.Model;

/**
 * Controls the client side of the application
 * Manages messages between port, model, and view.
 * @author Joel Happ
 */
public class Controller {

	private MyGUI view;
	private Model model;
	private ClientPort clientPort;
	
	/**
	 * 
	 * @param view
	 */
	public Controller(MyGUI view) {
		this.view = view;
		model = new Model();
		clientPort = new ClientPort("localhost", 8007);
		System.out.println("Launched client port to server");
	}
	
	/**
	 * Logs into account if possible, resets model
	 * @param id
	 * @param password
	 */
	public void login(int id, String password) throws Exception {
		try{
			// If this doesn't throw an exception, login is successful.
			clientPort.attemptLogin(id, password);
			// Code to perform to complete login
			model = new Model();
			view.setStudentMenu();
			updateSchedule();
		}catch(Exception e){
			// Throws exception to tell GUI to display exception message.
			throw new Exception(e.getMessage());
		}
	}
	
	
	/**
	 * Tells client port to try to find a course. Sets it to
	 * the selected course in the model.
	 * @param courseName Course code for the course
	 * @param courseNum Course number to search for
	 */
	public void selectCourse(String courseName, int courseNum) {
		
		try {
			model.setSelectedCourse(
					clientPort.findCourse(courseName, courseNum)
					);
		} catch (Exception e) {
			// !! Need better error handling, to show
			// !! GUI that course was not found.
			System.err.println("Error in select course:" + e.getMessage());
		}
		
	}
	
	/**
	 * Sets the passed course to be the selected course in the 
	 * client model
	 * @param c
	 */
	public void setSelectedCourse(CourseLite c) {
		model.setSelectedCourse(c);
	}
	
	public String getSelectedCourseName() {
		return model.getSelectedCourseName();
	}
	
	public String getSelectedCourseSpots() {
		return model.getSelectedCourseSpots();
	}
	
	
	/**
	 * Gets student's schedule from server, updates it in view.
	 */
	private void updateSchedule() {
		try {
		view.updateSchedule(clientPort.requestSchedule());
		} catch(Exception e) {
			// !! Add functionality so it can display error message on GUI
			// !! And delete this line:
			System.err.println("Error updating schedule " + e.getMessage());
		}
	}
	
	
	/**
	 * Enrolls the student in the selected course, updates the student's schedule
	 */
	public void enroll() {
		try {
		clientPort.addCourse(model.getSelectedCourseCode(),
				model.getSelectedCourseNumber(), model.getSecNumber());
		}catch(Exception e) {
			// !! Should display a relevant exception message to GUI, instead of here
			System.err.println("Error in enroll function" + e.getMessage());
		}
		updateSchedule();
	}
	
	
	/**
	 * Unenrolls student from selected course, 
	 * updates the student's schedule.
	 */
	public void unenroll() {
		try {
		clientPort.removeCourse(model.getSelectedCourseCode(),
				model.getSelectedCourseNumber());
		}catch(Exception e) {
			// !! Should display a relevant message to GUI instead of this
			System.err.println("Error in enroll function" + e.getMessage());
		}
		updateSchedule();
	}
	
	/**
	 * Gets the catalogue from the server and places it in the model.
	 * (Updates)
	 * Then, returns the catalogue from the model.
	 * @return The full course catalogue (an array of CourseLites)
	 */
	public CourseLite[] getCatalogue() {
		// 
		try {
		model.setCatalogue(clientPort.requestCatalogue());
		}catch(Exception e) {
			System.err.println("Error getting catalogue." + e.getMessage());
		}
		return model.getCatalogue();
	}
	
	
}
