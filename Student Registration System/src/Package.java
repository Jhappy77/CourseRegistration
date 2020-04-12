import java.io.Serializable;

/**
 * Class meant to be sent between server and client
 * @author Jerome Gobeil
 *
 */
public class Package implements Serializable{

	//Will change to generic probably
	String data;
	
	//The type of package
	PackageType type;
	
	//Get the data
	public String getData()
	{
		return data;
	}
	
	//Get the type
	public PackageType getType()
	{
		return type;
	}
	
	//Constructor for the package
	public Package(PackageType type, String data)
	{
		this.type = type;
		this.data = data;
	}
	
}


