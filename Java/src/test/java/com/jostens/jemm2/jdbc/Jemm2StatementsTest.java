package com.jostens.jemm2.jdbc;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class Jemm2StatementsTest
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		
    	// Initialize any BrandStatements
		Jemm2Statements statements = new Jemm2Statements();
		statements.initializeStatements();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Test
	public void testGetStatement() throws Exception
	{
		outputStatement(Jemm2Statements.DESIGN_COUNT);
	}
	
	private void outputStatement(String statementName)
	{
		String stmt = Statements.getStatement(statementName);
		System.out.println(statementName + " = " + stmt);
	}
}
