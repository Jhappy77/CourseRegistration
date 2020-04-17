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
			String name = clientPort.attemptLogin(id, password);
			// Code to perform to complete login
			
			// !! Name should be displayed somewhere on GUI
			System.out.println("Logged in as: " + name);
			
			model = new Model();
			view.setStudentMenu();
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
	
	
	public int getSelectedCourseOfferings() {
		return model.getSelectedCourse().getOfferingCount();
	}
	
	public CourseLite getSelectedCourse() {
		return model.getSelectedCourse();
	}
	
	public CourseLite[] getSchedule() {
		try {
			return clientPort.requestSchedule();
		}catch(Exception e) {
			
			// !! May want to make this display an error in a better way? Low priority though
			// !! This returns and error is the schedule is empty, could be good to say that
			System.err.println("Error getting schedule: " + e.getMessage());
		
		}
		return null;
	}

	
	/**
	 * Enrolls the student in the selected course, updates the student's schedule
	 */
	public void enroll() {
		try {
			String message = clientPort.addCourse(model.getSelectedCourseCode(),model.getSelectedCourseNumber(), model.getSecNumber());
			
			// !! This should be displayed on the GUI somewhere, just says that it was succesfull enrolling in course
			System.out.println(message);
		
		}catch(Exception e) {
			
			// !! Should display a relevant exception message to GUI
			// !! Message is either cant find course, student already in course or something similar
			
			System.err.println("Error enrolling: " + e.getMessage());
		}
	}
	
	
	/**
	 * Unenrolls student from selected course, 
	 * updates the student's schedule.
	 */
	public void unenroll() {
		try {
			String message = clientPort.removeCourse(model.getSelectedCourseCode(),model.getSelectedCourseNumber());
			
			// !! Should be displayed on the GUI somehows
			
			System.out.println(message);
			
		}catch(Exception e) {
			
			// !! Should display a relevant message to GUI instead of this
			
			System.err.println("Error unenrolling: " + e.getMessage());
		}
	}
	
	/**
	 * Makes a new course
	 */
	public void makeCourse() {
		try {
			
			// !! Needs to have actual data put in
			String message = clientPort.makeCourse("Temp",666, 2, 666);
			
			// !! This should be displayed on the GUI somewhere
			System.out.println(message);
		
		}catch(Exception e) {
			
			// !! Should display a relevant exception message to GUI, instead of here
			// !! Message is course already exists or other reason it didn't work
			
			System.err.println("Error making course: " + e.getMessage());
		}
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
