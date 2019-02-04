package com.jostens.jemm2.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jostens.jemm2.jdbc.helpers.PackageDatabaseHelper;
import com.jostens.jemm2.pojo.AssetPackage;

public class PackageHelper
{

	/**
	 * Find all the part load files and call method to persist Part objects
	 * @throws IOException 
	 */
	public List<AssetPackage> getPackageObjects(String fileName) throws IOException
	{
		List<AssetPackage> packages = new ArrayList<AssetPackage>();

        InputStream stream = DesignHelper.class.getResourceAsStream("/com/jostens/jemm2/jdbc/resources/" + fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = null;
        while ((line = reader.readLine()) != null)
        {
//            System.out.println("Line entered : " + line);
            
        	packages.add(new AssetPackage(line));
        }
		
		return packages;
	}
	
	/**
	 * Load all customers into the database
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void persistAllPackages(Connection c) throws IOException, SQLException
	{
		List<AssetPackage> packages = new ArrayList<AssetPackage>();
		packages.addAll(getPackageObjects("Packages_01.txt"));
		packages.addAll(getPackageObjects("Packages_02.txt"));
		packages.addAll(getPackageObjects("Packages_03.txt"));

		PackageDatabaseHelper dbHelper = new PackageDatabaseHelper();

		int i = 0;
		for (AssetPackage aPackage : packages)
		{
			i++;
			System.out.println("" + i);
			dbHelper.persistPackage(c, aPackage);
			if (i >= 5)
			{
//				break;
			}
		}
	}


}
