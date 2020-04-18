package client.model;

import server.controller.CourseLite;

public class Model {

	
	CourseLite selectedCourse;
	int selectedOffering = 1;
	
	CourseLite[] catalogue;
	
	public void setSelectedCourse(CourseLite c){
		selectedCourse = c;	
	}
	
	public void setSelectedOffering(int num) {
		selectedOffering = num;
		//System.out.println("Course offering is: " + selectedOffering + " with " + selectedCourse.getOfferingTotalSpots(selectedOffering-1) + " spots");
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
		return selectedCourse.getOfferingTakenSpots(selectedOffering-1) + "/" + selectedCourse.getOfferingTotalSpots(selectedOffering-1);
	}
	
	public void setCatalogue(CourseLite[] catalogue) {
		this.catalogue = catalogue;
	}
	
	public CourseLite[] getCatalogue() {
		return catalogue;
	}
	
	public CourseLite getSelectedCourse() {
		return selectedCourse;
	}
}
