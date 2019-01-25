package com.jostens.jemm2.helpers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.pojo.Design;
import com.wassoftware.solr.ConnectToSolr;

public class DesignHelperTest
{
	private static Connection c = null;
	private static HttpSolrClient solr = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		Jemm2Statements statements = new Jemm2Statements();
		statements.initializeStatements();
		c = ConnectionHelper.getJEMM2Connection();
		
		ConnectToSolr connect = new ConnectToSolr();
		solr = connect.makeConnection();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		ConnectionHelper.closeConnection(c);
		solr.close();
	}

//	@Test
	public void testGetDesignObjects() throws IOException
	{
		DesignHelper helper = new DesignHelper();
		
		List<Design> designs = helper.getDesignObjects("design_01.txt");
		
		System.out.println("Design count = " + designs.size());
	}

//	@Test
	public void testGetDesign() throws IOException
	{
		DesignHelper helper = new DesignHelper();
		
		Design design = helper.getDesign("|20150626-aecac9d49554|Knight,Baron,Crusader,Lancer|");
		
		System.out.println("Keyword count = " + design.getKeywords().size());
	}

//	@Test
	public void persistDesigns() throws IOException, SQLException
	{
		DesignHelper helper = new DesignHelper();
		
		helper.persistAllDesigns(c);
	}

	@Test
	public void persistDesignDocuments() throws SQLException, IOException, SolrServerException
	{
		DesignHelper helper = new DesignHelper();
		
		helper.persistAllDesignDocuments(c, solr);
	}

}
