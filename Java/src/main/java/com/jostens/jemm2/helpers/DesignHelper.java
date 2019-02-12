package com.jostens.jemm2.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import com.jostens.jemm2.jdbc.helpers.DesignDatabaseHelper;
import com.jostens.jemm2.pojo.Design;
import com.jostens.jemm2.solr.DesignDocument;

public class DesignHelper
{
	public Design getDesign(String definition)
	{
		Design design = new Design();
		
		StringTokenizer st = new StringTokenizer(definition, "|");
		design.setName(st.nextToken());
		design.setKeywords(st.nextToken());
		
		System.out.println("name=" + design.getName());
		
		return design;
	}

	/**
	 * Find all the design load files and call method to persist Design objects
	 * @throws IOException 
	 */
	public List<Design> getDesignObjects(String fileName) throws IOException
	{
		List<Design> designs = new ArrayList<Design>();

        InputStream stream = DesignHelper.class.getResourceAsStream("/com/jostens/jemm2/jdbc/resources/" + fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = null;
        while ((line = reader.readLine()) != null)
        {
//            System.out.println("Line entered : " + line);
            
            designs.add(new Design(line));
        }
		
		return designs;
	}
	
	/**
	 * Load all designs into the database
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void persistAllDesigns(Connection c) throws IOException, SQLException
	{
		List<Design> designs = new ArrayList<Design>();
		designs.addAll(getDesignObjects("design_01.txt"));
		designs.addAll(getDesignObjects("design_02.txt"));
		designs.addAll(getDesignObjects("design_03.txt"));

		DesignDatabaseHelper dbHelper = new DesignDatabaseHelper();

		int i = 0;
		for (Design design : designs)
		{
			i++;
			System.out.println("" + i);
			dbHelper.persistDesign(c, design);
		}
	}
	
	/**
	 * Get all designs from Oracle and persist into SOLR
	 * @throws SQLException 
	 * @throws SolrServerException 
	 * @throws IOException 
	 */
	public void persistAllDesignDocuments(Connection c, HttpSolrClient solr) throws SQLException, IOException, SolrServerException
	{
		DesignDatabaseHelper dbHelper = new DesignDatabaseHelper();
		
		List<Integer> designs = dbHelper.getAllDesignIDs(c);

		System.out.println("Designs found = " + designs.size());
		
		// Add each design to SOLR
		
		int i = 0;
		for (Integer designID : designs)
		{
			i++;
			System.out.println("" + i);
			Design design = new Design();
			design.setID(designID.intValue());
			dbHelper.getDesign(c, design);
			
			// Now persist the design into SOLR
			DesignDocument dd = design.getDesignDocument();
			solr.addBean(dd);
			solr.commit();

//			if (i > 5)
//			{
//				break;
//			}
		}
	}

}
