package com.jostens.dam.shared.properties;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map;

import com.jostens.dam.shared.common.ExceptionHelper;

/**
 * Use this PropertyFile class if the order of the Properties in the file need to be
 * maintained. This object will also maintain comments throughout the file unlike the
 * base ProfileFile class which will not keep those comments.  This class does not
 * use the comments defined in an implementation of getPropertyComments().
 * 
 * Normally order shouldn't matter and most property files are read only,
 * so use this class appropriately.
 * 
 * ** WARNING ** Not all base Properties methods have been re-implemented to maintain order
 * using the properties object.  Add those additional methods as needed.
 */
public abstract class PropertyFileOrdered extends PropertyFile
{

	/**
	 * Constructor for loading property file with the specified full path
	 */
	public PropertyFileOrdered(String fullPath)
	{
		super(fullPath);
	}
	/**
	 * Constructor for finding property file using environment objects
	 */
	public PropertyFileOrdered()
	{
		super();
	}

	/**
	 * Store the property files in order entries were found as the file was loaded
	 */
	public void storePropertyValues()
	{
		// Open the properties base object on the specified property file name
		String fullPath = propertyFilePath;

		try
		{
			OutputStream os = new FileOutputStream(fullPath);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

	        // Write out the properties themselves
	        Iterator<Map.Entry<Object, Object>> iter = properties.entrySet().iterator();
	        while (iter.hasNext())
	        {
	        	Map.Entry<Object, Object> entry = iter.next();
	        	String key = (String)entry.getKey();
	        	String value = (String)entry.getValue();
	        	// Handle a comment
	        	if (key.startsWith("comment"))
	        	{
	        		bw.write(value);
		        	bw.newLine();
	        		continue;
	        	}
	        	// Handle a blank line
	        	if (key.startsWith("empty"))
	        	{
		        	bw.newLine();
	        		continue;
	        	}
	        	bw.write(key + "=" + value);
	        	bw.newLine();
	        }
			
			bw.close();
			os.close();
		}
		catch (Exception e)
		{
			ExceptionHelper.logExceptionToFile("storePropertyValues (PropertyFileOrdered) File: " + getPropertyFileName() + " Path: " + fullPath, e);
		}
	}

	/**
	 * Display what the property file would look like if it was stored
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
        // Write out the properties themselves
        Iterator<Map.Entry<Object, Object>> iter = properties.entrySet().iterator();
        while (iter.hasNext())
        {
        	Map.Entry<Object, Object> entry = iter.next();
        	String key = (String)entry.getKey();
        	String value = (String)entry.getValue();
        	// Handle a comment
        	if (key.startsWith("comment"))
        	{
        		sb.append(value + "\n");
        		continue;
        	}
        	// Handle a blank line
        	if (key.startsWith("empty"))
        	{
        		sb.append("" + "\n");
        		continue;
        	}
        	sb.append(key + "=" + value + "\n");
        }
 		
		return sb.toString();
	}


}
