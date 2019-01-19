package com.jostens.dam.brand.jdbc;

import java.util.ArrayList;
import java.util.List;

import com.jostens.dam.shared.properties.PropertyFile;

public class RevisionCleanupProperties extends PropertyFile
{

	private String REVISION_CLEANUP = "RevisionCleanup";
	private String CLEANUP_MAXIMUM = "CleanupMaximum";
	
	private List<String> foldersToClean = new ArrayList<String>();

	public RevisionCleanupProperties()
	{
		super();
		
		// Find all folders that are involved in automatic cleanup
		// Name patterns start with Folder_
		for (String propertyName : stringPropertyNames())
		{
			if (propertyName.startsWith("Folder_"))
			{
				// Also make sure the folder is enabled also
				if (!"enabled".equals(getProperty(propertyName)))
				{
					continue;		// Folder cleanup is not enabled
				}
				foldersToClean.add(propertyName.substring(7));
			}
		}
	}
	
	@Override
	public String getPropertyFileName()
	{
		return REVISION_CLEANUP_PROPERTIES;
	}

	public boolean isRevisionCleanupActive()
	{
		String active = getProperty(REVISION_CLEANUP);
		if (active == null || !("enabled".equals(active)))
		{
			return false;
		}
		return true;
	}

	public int getMaximumToCleanup()
	{
		String maximum = getProperty(CLEANUP_MAXIMUM);
		if (maximum == null)
		{
			return 0;
		}
		
		try
		{
			int max = Integer.parseInt(maximum);
			return max;
		} catch (NumberFormatException e)
		{
			return 0;
		}
	}

	public List<String> getFoldersToClean()
	{
		return foldersToClean;
	}
	
	public String getFolderDateRange(String folder)
	{
		String dateRange = getProperty(folder + "_DateRange");
		return dateRange;
	}
	
	/**
	 * Get passed in a Folder designation.  The folder might be a constant value as found
	 * in FolderGUIDS or could be a folder path defined by an _Path option in the
	 * property file.  This helper method makes that determination and returns the correct value.
	 */
	public String getFoldersForDateRange(String folder)
	{
		String folderPath = getProperty(folder + "_Path");
		if (folderPath == null)
		{
			return folder;		// No Path attribute found
		}
		return folderPath;
	}
}
