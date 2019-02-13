package com.jostens.jemm2.brand.helpers;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jostens.jemm2.JEMM2Constants;
import com.jostens.jemm2.brand.pojo.IncomingPackage;
import com.jostens.jemm2.jdbc.helpers.AutomationsDatabaseHelper;

/**
 * Helper methods for Automations to call as an interface to the incomingAutomations tables.
 * 
 * This object will use AutomationDatabaseHelper to perform the actual persisting in the database.
 * 
 * User is responsible for providing a database connection for use and then closing when done with object
 */
public class AutomationHelper
{
	private AutomationsDatabaseHelper dbHelper = new AutomationsDatabaseHelper();
	private Connection c = null;
	
	public void setConnection(Connection c)
	{
		this.c = c;
	}

	/**
	 * Find .cdr files in the upload folder.  A .cdr file represents an incoming package.  The .cdr
	 * files are returned in the form of a IncomingPackage object.  All rendition assets related to those packages are
	 * included as IncomingAsset objects.
	 */
	public List<IncomingPackage> getIncomingPackagesFromUpload()
	{
		final List<IncomingPackage> packages = new ArrayList<IncomingPackage>();

		// Find each .cdr then look for all renditions related to it
		File uploadDir = new File(JEMM2Constants.UPLOAD_PATH);
		File[] cdrFiles = uploadDir.listFiles(new FilenameFilter() {
	        public boolean accept(File dir, String fileName) {
	            if (fileName.endsWith(".cdr"))
	            {
	            	IncomingPackage aPackage = new IncomingPackage();
	            	aPackage.setName(fileName);
	            	packages.add(aPackage);
	            	return true;
	            }
	            return false;
	        }
	    });
		
//		System.out.println(".cdr found = " + packages.size());

		// Get .png files found these will match up with .cdr files
		File[] pngFiles = uploadDir.listFiles(new FilenameFilter() {
	        public boolean accept(File dir, String fileName) {
	            if (fileName.endsWith(".png"))
	            {
	            	return true;
	            }
	            return false;
	        }
	    });
//		System.out.println(".png found = " + pngFiles.length);

		// Match the .png files with packages
		for (File pngFile : pngFiles)
		{
			String pngName = pngFile.getName();
			for (IncomingPackage aPackage : packages)
			{
				boolean matchFound = aPackage.addIncomingName(pngName);
				if (matchFound)
				{
					break;
				}
			}
		}
		
		return packages;
	}
	
	/**
	 * Get passed a package name, check the incoming_package table for if this package has been previously processed.
	 * Return the ID of the table entry or 0 if not persisted
	 * @throws SQLException 
	 */
	public int getIncomingIDForName(String incomingName) throws SQLException
	{
		int id = dbHelper.getIncomingPackageIDByName(c, incomingName);
		return id;
	}
	
	/**
	 * Pesist the provided package
	 * @throws SQLException 
	 */
	public void persistIncomingPackage(IncomingPackage aPackage) throws SQLException
	{
		dbHelper.persistIncomingPackage(c, aPackage);
	}
	
}
