package client.model;

import server.controller.CourseLite;

public class Model {

	
	CourseLite selectedCourse;
	int selectedOffering;
	
	CourseLite[] catalogue;
	
	public void setSelectedCourse(CourseLite c){
		selectedCourse = c;
		selectedOffering = c.getOfferingSecNumber(0);	
	}
	
	public String getSelectedCourseName(){
		return selectedCourse.getName() + " " + selectedCourse.getNumber();
	}
	
	public String getSelectedCourseCode() {
		return selectedCourse.getName();
	}
	
	public int getSelectedCourseNumber() {
		return selectedCourse.getNumber();
	}
	
	public int getSecNumber() {
		return selectedOffering;
	}
	
	public String getSelectedCourseSpots() {
		return selectedCourse.getOfferingTakenSpots(selectedOffering) + "/" + selectedCourse.getOfferingTotalSpots(selectedOffering);
	}
	
	public void setCatalogue(CourseLite[] catalogue) {
		this.catalogue = catalogue;
	}
	
	public CourseLite[] getCatalogue() {
		return catalogue;
	}
}
