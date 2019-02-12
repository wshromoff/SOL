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

import com.jostens.jemm2.jdbc.helpers.PartDatabaseHelper;
import com.jostens.jemm2.pojo.Part;
import com.jostens.jemm2.solr.PartDocument;

public class PartHelper
{

	/**
	 * Find all the part load files and call method to persist Part objects
	 * @throws IOException 
	 */
	public List<Part> getPartObjects(String fileName) throws IOException
	{
		List<Part> parts = new ArrayList<Part>();

        InputStream stream = DesignHelper.class.getResourceAsStream("/com/jostens/jemm2/jdbc/resources/" + fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = null;
        while ((line = reader.readLine()) != null)
        {
//            System.out.println("Line entered : " + line);
            
        	parts.add(new Part(line));
        }
		
		return parts;
	}
	
	/**
	 * Load all parts into the database
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void persistAllParts(Connection c) throws IOException, SQLException
	{
		List<Part> parts = new ArrayList<Part>();
		parts.addAll(getPartObjects("Parts_01.txt"));
		parts.addAll(getPartObjects("Parts_02.txt"));
		parts.addAll(getPartObjects("Parts_03.txt"));

		PartDatabaseHelper dbHelper = new PartDatabaseHelper();

		int i = 0;
		for (Part part : parts)
		{
			i++;
			System.out.println("" + i);
			dbHelper.persistPart(c, part);
		}
	}


	/**
	 * Get all parts from Oracle and persist into SOLR
	 * @throws SQLException 
	 * @throws SolrServerException 
	 * @throws IOException 
	 */
	public void persistAllPartDocuments(Connection c, HttpSolrClient solr) throws SQLException, IOException, SolrServerException
	{
		PartDatabaseHelper dbHelper = new PartDatabaseHelper();
		
		List<Integer> parts = dbHelper.getAllPartIDs(c);

		System.out.println("Parts found = " + parts.size());
		
		// Add each part to SOLR
		
		int i = 0;
		for (Integer partID : parts)
		{
			i++;
			System.out.println("" + i);
			Part part = new Part();
			part.setID(partID.intValue());
			dbHelper.getPart(c, part);
			
			// Now persist the design into SOLR
			PartDocument pd = part.getPartDocument();
			solr.addBean(pd);
			solr.commit();

//			if (i > 5)
//			{
//				break;
//			}
		}
	}

}
