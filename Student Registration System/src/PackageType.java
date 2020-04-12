
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
	//third = offering index (int)
	
	REMOVECOURSE, 
	//Data is string[]
	//first = course name (String)
	//second = course number (int)
	
	SCHEDULE,
	//Data is ??
	
	REQUESTSCHEDULE,
	//Data is empty
	
	COURSE,
	//Data is ??
	
	FINDCOURSE,
	//Data is string[]
	//first = course name
	//second = course number
	
	MESSAGE
	//Data is string
	//Message to be printed on the server
	
}
