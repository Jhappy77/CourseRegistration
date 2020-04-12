
/**
 * Enum to say what kind of package has been passed
 * @author Jerome Gobeil
 *
 */
public enum PackageType {
	LOGINREQUEST, //Data should be id (integer) and password separated by a space
	LOGINRESULT, //Data is SUCCESS or FAIL
	ADDCOURSE, //Data is the course name, course number and offering index seperated by spaces
	MESSAGE //Change/Remove this
	
}
