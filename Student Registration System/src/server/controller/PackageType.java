package server.controller;


/**
 * Enum to states what kind of package has been passed
 * @author Jerome Gobeil
 *
 */
public enum PackageType {
	LOGINREQUEST, 
	//Data is a string []: 
	//first element = student id (int) 
	//second element = password (String)
	
	LOGINRESULT, 
	//Data is String: 
	//String is the students name
	
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
	//CourseLite[] is the list of currently enrolled courses
	
	REQUESTSCHEDULE,
	//Data is empty
	
	COURSE,
	//Data is CourseLite, null if course not found
	
	COURSECHANGED,
	//Data is String message
	//Sent when a course has been successfully added, removed or created
	
	FINDCOURSE,
	//Data is string[]
	//first = course name
	//second = course number
	
	REQUESTCOURSECATALOGUE,
	//Data is empty
	
	CATALOGUE,
	//Data is CourseLite[], null if no courses exist
	
	LOGOUT,
	//Makes the server deselect the current student
	//Data is null
	
	NEWCOURSE,
	//Makes a new course
	//Data is a string[]
	//First element is course name
	//Second is course number (int)
	//Third is the number of sections (int)
	//Fourth is the maximum spots for each section
	
	ERROR,
	//Data is String to be printed/displayed on GUI
	
	MESSAGE
	//Data is string
	//Message to be printed when received
	
}
