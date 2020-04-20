package server.controller;

import java.io.Serializable;

/**
 * Represents a message meant to be sent between server and client
 * @author Jerome Gobeil
 *
 */
public class Package <T> implements Serializable{

	/**
	 * The data to include in the package
	 */
	private T data;
	
	/**
	 * The type of package being sent
	 */
	private PackageType type;
	
	/**
	 * Getter for the data
	 * @return the data
	 */
	public T getData()
	{
		return data;
	}
	
	/**
	 * Getter for the type of package
	 * @return
	 */
	public PackageType getType()
	{
		return type;
	}
	
	/**
	 * Basic constructor for the package
	 * @param type
	 * @param data
	 */
	public Package(PackageType type, T data)
	{
		this.type = type;
		this.data = data;
	}
	
}

