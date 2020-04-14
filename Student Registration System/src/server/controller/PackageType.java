package server.controller;


/**
 * Enum to say what kind of package has been passed
 * @author Jerome Gobeil
 *
 */
public enum PackageType {
	LOGINREQUEST, 
	//Data is a string []: 
	//first element = student id (int) 
	//second element = password (String)
	
	LOGINRESULT, 
	//Data is boolean: 
	//true is success, false if failure
	
	ADDCOURSE, 
	//Data is string[]: 
	//first element = course name, (String)
	//second = course number (int)
	//third = offering section number (int)
	
	REMOVECOURSE, 
	//Data is string[]
	//first = course name (String)
	//second = course number (int)
	
	SCHEDULE,
	//Data is CourseLite[], null if no registrations
	//CourseLite only has one offering which is the one the student is registered in
	//CourseLite is used to reduce the number of classes that both client and server must have
	
	REQUESTSCHEDULE,
	//Data is empty
	
	COURSE,
	//Data is CourseLite, null if course not found
	
	FINDCOURSE,
	//Data is string[]
	//first = course name
	//second = course number
	
	REQUESTCOURSECATALOGUE,
	//Data is empty
	
	CATALOGUE,
	//Data is CourseLite[], null if no courses exist
	
	MESSAGE
	//Data is string
	//Message to be printed when received
	
}
