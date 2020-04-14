package client.controller;

import client.view.MyGUI;
import server.controller.CourseLite;
import client.model.Model;

public class Controller {

	private MyGUI view;
	private Model model;
	private ClientApp clientPort;
	
	/**
	 * 
	 * @param view
	 */
	public Controller(MyGUI view) {
		this.view = view;
		model = new Model();
		clientPort = new ClientApp("localhost", 8007, this);
		System.out.println("Launched client app");
	}
	
	/**
	 * Logs into account if possible, resets model
	 * @param id
	 * @param password
	 */
	public void login(int id, String password) {
		clientPort.attemptLogin(id, password);
		clientPort.communicateOnce();
		model = new Model();
	}
	
	/**
	 * Responds to login attempt
	 * @param b
	 */
	public void loginAttempt(Boolean b) {
		if(b) {
			view.setStudentMenu();
			updateSchedule();
		}
		//Program ELSE statement later
			//throw new Exception("Wrong password / User doesn't exist!");
		// Ideally, we upgrade it so that the system can tell which of these is the case.
	}
	
	
	/**
	 * Tells client port to try to find a course. Selects it.
	 * @param courseName
	 * @param courseNum
	 */
	public void selectCourse(String courseName, int courseNum) {
		clientPort.findCourse(courseName, courseNum);
		clientPort.communicateOnce();
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
	 * Shows schedule in view
	 * @param schedule
	 */
	public void showSchedule(CourseLite[] schedule) {
		view.updateSchedule(schedule);
	}
	
	/**
	 * Gets student's schedule from server
	 */
	private void updateSchedule() {
		clientPort.requestSchedule();
		clientPort.communicateOnce();
	}
	
	
	/**
	 * Enrolls the student in the selected course.
	 */
	public void enroll() {
		clientPort.addCourse(model.getSelectedCourseCode(),
				model.getSelectedCourseNumber(), model.getSecNumber());
		updateSchedule();
	}
	
	/**
	 * Unenrolls student from selected course
	 */
	public void unenroll() {
		clientPort.removeCourse(model.getSelectedCourseCode(),
				model.getSelectedCourseNumber());
		updateSchedule();
	}
	
	/**
	 * Returns the abridged version of full catalogue of courses
	 * @return
	 */
	public CourseLite[] getCatalogue() {
		clientPort.requestCatalogue();
		return model.getCatalogue();
	}
	
	/**
	 * Updates the catalogue copy held by the model
	 * @param catalogue
	 */
	public void updateCatalogue(CourseLite[] catalogue) {
		model.setCatalogue(catalogue);
	}
	
	
}
