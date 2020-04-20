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
	private String studentName;
	private Model model;
	private ClientPort clientPort;
	
	/**
	 * Constructor for the controller
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
			studentName = clientPort.attemptLogin(id, password);
			// Code to perform to complete login
			
			// !! Name should be displayed somewhere on GUI
			System.out.println("Logged in as: " + studentName);
			
			model = new Model();
			if(studentName.equals("Administration")){
				view.setAdminMenu();
			}else
				view.setStudentMenu();
		}catch(Exception e){
			// Throws exception to tell GUI to display exception message.
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Returns the name of the student who is logged in
	 */
	public String getStudentName() {
		return studentName;
	}
	
	/**
	 * Tells client port to try to find a course. Sets it to
	 * the selected course in the model.
	 * @param courseName Course code for the course
	 * @param courseNum Course number to search for
	 */
	public void selectCourse(String courseName, int courseNum) throws Exception {

		model.setSelectedCourse(clientPort.findCourse(courseName, courseNum));

	}
	
	/**
	 * Sets the passed course to be the selected course in the 
	 * client model
	 * @param c
	 */
	public void setSelectedCourse(CourseLite c) {
		model.setSelectedCourse(c);
	}
	
	/**
	 * Getter for the selected courses name
	 * @return the name
	 */
	public String getSelectedCourseName() {
		return model.getSelectedCourseName();
	}
	
	/**
	 * Getter for the selected courses Spots
	 * @return
	 */
	public String getSelectedCourseSpots() {
		return model.getSelectedCourseSpots();
	}
	
	/**
	 * Getter for the selected courses offerings
	 * @return
	 */
	public int getSelectedCourseOfferings() {
		return model.getSelectedCourse().getOfferingCount();
	}
	
	/**
	 * Getter for the selected course
	 * @return
	 */
	public CourseLite getSelectedCourse() {
		return model.getSelectedCourse();
	}
	
	/**
	 * Getter for the schedule
	 * @return list of enrolled courses
	 * @throws Exception if no courses are present
	 */
	public CourseLite[] getSchedule() throws Exception{

		return clientPort.requestSchedule();

	}
	
	/**
	 * Logs out the current student
	 */
	public void logout()
	{
		clientPort.logout();
	}
	
	/**
	 * Enrolls the student in the selected course, updates the student's schedule
	 */
	public String enroll() throws Exception{
		
		String message = clientPort.addCourse(model.getSelectedCourseCode(),model.getSelectedCourseNumber(), model.getSecNumber());
			
		return message;
		
	}
	
	/**
	 * Unenrolls student from selected course, 
	 * updates the student's schedule.
	 */
	public String unenroll() throws Exception {
		String message = clientPort.removeCourse(model.getSelectedCourseCode(),model.getSelectedCourseNumber());
			
		return message;
	}
	
	/**
	 * Makes a new course
	 */
	public String makeCourse(String name, int num, int offerings, int spots) throws Exception{
			
			String message = clientPort.makeCourse(name, num, offerings, spots);
			return message;
	}
	
	/**
	 * Gets the catalogue from the server and places it in the model.
	 * (Updates)
	 * Then, returns the catalogue from the model.
	 * @return The full course catalogue (an array of CourseLites)
	 */
	public CourseLite[] getCatalogue() throws Exception{
			model.setCatalogue(clientPort.requestCatalogue());
		return model.getCatalogue();
	}
	
	/**
	 * Set the offering based on the drop down selection
	 */
	public void setOffering(int num) {
		model.setSelectedOffering(num);
	}
	
	/**
	 * Check whether or not the student is enrolled in the course
	 * @return
	 */
	public boolean checkEnrolment() {
		// TODO Auto-generated method stub
		return model.getSelectedCourse().isStudentEnrolled();
	}
	
}
