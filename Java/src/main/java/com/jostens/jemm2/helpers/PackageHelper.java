package com.jostens.jemm2.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import com.jostens.jemm2.jdbc.helpers.PackageDatabaseHelper;
import com.jostens.jemm2.pojo.AssetPackage;
import com.jostens.jemm2.pojo.CustomerPackage;
import com.jostens.jemm2.solr.CustomerPackageDocument;
import com.jostens.jemm2.solr.PackageDocument;

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
        	// If first character is lower case it's a derivative package which are being skipped now due to
        	// derivative parts not in extract for some reason
        	char[] firstChar = line.substring(0, 1).toCharArray();
        	if (Character.isLowerCase(firstChar[0]))
        	{
        		continue;		// Skip derivative package
        	}
             
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

	/**
	 * Get all Packages from Oracle and persist into SOLR
	 * @throws SQLException 
	 * @throws SolrServerException 
	 * @throws IOException 
	 */
	public void persistAllPackageDocuments(Connection c, HttpSolrClient solr) throws SQLException, IOException, SolrServerException
	{
		PackageDatabaseHelper dbHelper = new PackageDatabaseHelper();
		
		List<Integer> packages = dbHelper.getAllPackageIDs(c);

		System.out.println("Packages found = " + packages.size());
		
		// Add each part to SOLR
		
		int i = 0;
		for (Integer packageID : packages)
		{
			i++;
			System.out.println("" + i + " : " + packageID);
			AssetPackage aPackage = new AssetPackage();
			aPackage.setID(packageID.intValue());
			dbHelper.getPackage(c, aPackage);
			
			// Now persist the design into SOLR
			PackageDocument pd = aPackage.getPackageDocument();
			solr.addBean(pd);
			solr.commit();

//			if (i >= 5)
//			{
//				break;
//			}
		}
	}

	/**
	 * Get all Customer Packages from Oracle and persist into SOLR
	 * @throws SQLException 
	 * @throws SolrServerException 
	 * @throws IOException 
	 */
	public void persistAllCustomerPackageDocuments(Connection c, HttpSolrClient solr) throws SQLException, IOException, SolrServerException
	{
		PackageDatabaseHelper dbHelper = new PackageDatabaseHelper();
		
		List<Integer> customerPackages = dbHelper.getAllCustomerPackageIDs(c);

		System.out.println("Customer Packages found = " + customerPackages.size());
		
		// Add each customer project document to SOLR
		
		int i = 0;
		for (Integer customerPackageID : customerPackages)
		{
			i++;
			System.out.println("" + i + " : " + customerPackageID);
			CustomerPackage aPackage = new CustomerPackage();
			aPackage.setID(customerPackageID.intValue());
			dbHelper.getCustomerPackage(c, aPackage);
			
			// Now persist the design into SOLR
			CustomerPackageDocument pd = aPackage.getCustomerPackageDocument();
			solr.addBean(pd);
			solr.commit();

//			if (i >= 5)
//			{
//				break;
//			}
		}
	}

}
