package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;

public class ActionsDatabaseHelperTest
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

	//
	// All the below methods work against the ACTIONS table, to test for a specific type
	// of action select appropriate static constant on ActionsDatabaseHelper
	//
	
//	@Test
	public void testGetActionCount() throws SQLException
	{
		ActionsDatabaseHelper helper = new ActionsDatabaseHelper();
		int count = helper.getActionCount(c, ActionsDatabaseHelper.BOOKMARK);
		System.out.println("Action Count = " + count);
	}

//	@Test
	public void testAddAction() throws SQLException
	{
		ActionsDatabaseHelper helper = new ActionsDatabaseHelper();
		helper.addAction(c, "CP_000003", ActionsDatabaseHelper.BOOKMARK);
	}

//	@Test
	public void testIsDocumentForAction() throws SQLException
	{
		ActionsDatabaseHelper helper = new ActionsDatabaseHelper();
		boolean bookmarked = helper.isDocumentForAction(c, "CP_000003", ActionsDatabaseHelper.BOOKMARK);
		System.out.println("Bookmarked = " + bookmarked);
	}

//	@Test
	public void testDeleteAction() throws SQLException
	{
		ActionsDatabaseHelper helper = new ActionsDatabaseHelper();
		helper.deleteAction(c, "CP_000003", ActionsDatabaseHelper.BOOKMARK);
	}

	@Test
	public void testGetAllActions() throws SQLException
	{
		ActionsDatabaseHelper helper = new ActionsDatabaseHelper();
		List<String> documentIDs = helper.getAllActions(c, ActionsDatabaseHelper.BOOKMARK);
		System.out.println("Bookmarks = " + documentIDs);
	}

}
