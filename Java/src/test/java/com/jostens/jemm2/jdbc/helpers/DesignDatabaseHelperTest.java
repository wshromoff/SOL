package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.pojo.Design;

public class DesignDatabaseHelperTest
{
	private static Connection c = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		Jemm2Statements statements = new Jemm2Statements();
		statements.initializeStatements();
		c = ConnectionHelper.getJEMM2Connection();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		ConnectionHelper.closeConnection(c);
	}

//	@Test
	public void testGetDesignCount()
	{
		DesignDatabaseHelper helper = new DesignDatabaseHelper();
		int designCount = helper.getDesignCount(c);
		System.out.println("Design Count = " + designCount);
	}

//	@Test
	public void testGetNextDesignSequence()
	{
		DesignDatabaseHelper helper = new DesignDatabaseHelper();
		int sequence = helper.getNextDesignSequence(c);
		System.out.println("Sequence Value = " + sequence);
		sequence = helper.getNextDesignSequence(c);
		System.out.println("Sequence Value = " + sequence);
	}
//	@Test
	public void testGetNextKeywordSequence()
	{
		DesignDatabaseHelper helper = new DesignDatabaseHelper();
		int sequence = helper.getNextKeywordSequence(c);
		System.out.println("Sequence Value = " + sequence);
		sequence = helper.getNextKeywordSequence(c);
		System.out.println("Sequence Value = " + sequence);
	}

//	@Test
	public void testGetKeywordID() throws SQLException
	{
		DesignDatabaseHelper helper = new DesignDatabaseHelper();
		int keywordId = helper.getKeywordID(c, "Wowwy");
		System.out.println("Keyword Value = " + keywordId);
		keywordId = helper.getKeywordID(c, "Dog");
		System.out.println("Keyword Value = " + keywordId);
	}

//	@Test
	public void testPersistKeywords() throws SQLException
	{
		DesignDatabaseHelper helper = new DesignDatabaseHelper();
		List<String> keywords = new ArrayList<String>();
		keywords.add("A");
		keywords.add("B");
		keywords.add("C");
		helper.persistKeywords(c, 134, keywords);
		
	}

//	@Test
	public void testGetDesignID() throws SQLException
	{
		DesignDatabaseHelper helper = new DesignDatabaseHelper();
		int designId = helper.getDesignID(c, "Wowwy");
		System.out.println("Design Value = " + designId);
		designId = helper.getDesignID(c, "Dog");
		System.out.println("Design Value = " + designId);
	}

	@Test
	public void testPersistDesign() throws SQLException
	{
		DesignDatabaseHelper helper = new DesignDatabaseHelper();
		
		Design design = new Design();
		design.setName("design1");
		List<String> keywords = new ArrayList<String>();
		keywords.add("D");
		keywords.add("E");
		keywords.add("F");
		design.setKeywords(keywords);
		helper.persistDesign(c, design);
		
	}

}
