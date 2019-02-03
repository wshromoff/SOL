package com.jostens.jemm2.helpers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.wassoftware.solr.ConnectToSolr;

public class CustomerHelperTest
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

	@Test
	public void persistCustomer() throws IOException, SQLException
	{
		CustomerHelper helper = new CustomerHelper();
		
		helper.persistAllCustomers(c);

	}

}
