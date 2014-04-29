package ufrgs.maslab.gsom.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Transmitter {
	
	/**
	 * <ul>
	 * <li>load the file with properties</li>
	 * </ul>
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	protected static Properties getProp(String filename) throws IOException {
		
		Properties props = new Properties();
		FileInputStream file = new FileInputStream("./properties/"+filename);
		props.load(file);
		
		return props;
		
	}
	
	/**
	 * returns the specified property from the specified file
	 * 
	 * @param filename
	 * @param property
	 * @return
	 */
	public static Double getDoubleConfigParameter(String filename, String property)
	{
		try{
			return new Double(getProp(filename).getProperty(property));
		}catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * returns the specified property from the specified file
	 * 
	 * @param filename
	 * @param property
	 * @return
	 */
	public static Integer getIntConfigParameter(String filename, String property)
	{
		try{
			return new Integer(getProp(filename).getProperty(property));
		}catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
		
	/**
	 * returns the specified property from the specified file
	 * 
	 * @param filename
	 * @param property
	 * @return
	 */
	public static String getProperty(String filename, String property)
	{
		try{
			return getProp(filename).getProperty(property);
		}catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * <ul>
	 * <li>print a message of the specified file of properties</li>
	 * </ul>
	 * @param filename
	 * @param property
	 */
	public static void message(String filename, String property)
	{
		try {
			System.out.println(getProp(filename).getProperty(property));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
