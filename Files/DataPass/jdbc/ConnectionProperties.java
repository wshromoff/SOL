package com.jostens.dam.shared.jdbc;

import java.io.File;

import com.jostens.dam.shared.folders.SharedFolderPath;
import com.jostens.dam.shared.properties.PropertyFile;

public class ConnectionProperties extends PropertyFile
{
	// Define constants for the properties that can be found in the property file.
	public static String MB_URL = "MB_URL";		// This and following 2 are for finding MB connection information
	public static String MB_USER = "MB_USER";
	public static String MB_PASSWORD = "MB_PASSWORD";

	public static String CP_URL = "CP_URL";		// This is for finding customer profile connection information
	public static String CP_USER = "CP_USER";
	public static String CP_PASSWORD = "CP_PASSWORD";

	public static String JDAM_URL = "JDAM_URL";		// This is for finding JDAM connection information
	public static String JDAM_USER = "JDAM_USER";
	public static String JDAM_PASSWORD = "JDAM_PASSWORD";

	public static String WEBSERVICEURL = "WEBSERVICEURL";	// URL for web service connections
	
	public static String MEDIABINLO_USER = "MEDIABINLO_USER";	// This and following 3 are login information for MediaBin service accounts
	public static String MEDIABINLO_PASSWORD = "MEDIABINLO_PASSWORD";
	public static String MEDIABINPR_USER = "MEDIABINPR_USER";
	public static String MEDIABINPR_PASSWORD = "MEDIABINPR_PASSWORD";
	
	/**
	 * Return the path to the property file.  Extender's can over-ride if the property file is not in a default location
	 */
	protected String getFullPath()
	{
		// Attempt to find the full name using primary connection path
		String fullPath = SharedFolderPath.getPrimaryConnectionPath() + getPropertyFileName();
		File file = new File(fullPath);
		if (file.exists())
		{
			return fullPath;
		}
		// Primary path not found return backup path
		fullPath = SharedFolderPath.getBackupConnectionPath() + getPropertyFileName();
		return fullPath;
	}

	@Override
	public String getPropertyFileName()
	{
		return CONNECTION_PROPERTIES;
	}

}
