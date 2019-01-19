package com.jostens.dam.shared.jdbc;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.dam.shared.testing.SharedBaseTest;

public class SharedStatementsTest extends SharedBaseTest
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
//		setProductionEnvironment(true);
//		setIntegrationEnvironment(true);
		connectToMB();		// Do a bogus connect to initialize FolderGUIDS correctly for test execution
		
    	// Initialize any BrandStatements
		SharedStatements statements = new SharedStatements();
		statements.initializeStatements();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		disconnectFromMB();
	}

	@Test
	public void testGetStatement() throws Exception
	{
		outputStatement(SharedStatements.MEDIA_DATABASE_GUID);
		outputStatement(SharedStatements.DELETED_ITEMS_GUID);
		outputStatement(SharedStatements.FIND_COMPLETE_FOLDER_TREE);
		outputStatement(SharedStatements.FIND_ALL_ASSETS_FOR_FOLDER_TREE);
		outputStatement(SharedStatements.FIND_CONTAINERTREE_COUNT);
		outputStatement(SharedStatements.FIND_PARENT_FOLDER_GUID);
		outputStatement(SharedStatements.FIND_ASSETS_MISSING_METADATA_FIELD);
		outputStatement(SharedStatements.FIND_PARENT_FOLDERS);
	}
	
	private void outputStatement(String statementName)
	{
		String stmt = Statements.getStatement(statementName);
		System.out.println(statementName + " = " + stmt);
	}

}
