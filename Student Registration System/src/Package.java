import java.io.Serializable;

/**
 * Class meant to be sent between server and client
 * @author Jerome Gobeil
 *
 */
public class Package <T> implements Serializable{

	//Data to send
	private T data;
	
	//The type of package
	private PackageType type;
	
	//Get the data
	public T getData()
	{
		return data;
	}
	
	//Get the type
	public PackageType getType()
	{
		return type;
	}
	
	//Constructor for the package
	public Package(PackageType type, T data)
	{
		this.type = type;
		this.data = data;
	}
	
}

