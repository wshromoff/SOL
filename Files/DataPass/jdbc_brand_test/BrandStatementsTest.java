package com.jostens.dam.brand.jdbc;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.dam.brand.testing.BrandBaseTest;

public class BrandStatementsTest extends BrandBaseTest
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
//		setProductionEnvironment(true);
//		setIntegrationEnvironment(true);
		useLiveFolders();
		connectToMB();		// Do a bogus connect to initialize FolderGUIDS correctly for test execution
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		disconnectFromMB();
	}

	@Test
	public void testGetStatement() throws Exception
	{
	}

//	private void outputStatement(String statementName)
//	{
//		String stmt = Statements.getStatement(statementName);
//		System.out.println(statementName + " = " + stmt);
//	}

}
