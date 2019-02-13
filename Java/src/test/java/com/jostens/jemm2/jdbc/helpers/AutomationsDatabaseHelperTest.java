package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;

public class AutomationsDatabaseHelperTest
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

	@Test
	public void testGetNextIncomingPackageSequence()
	{
		AutomationsDatabaseHelper helper = new AutomationsDatabaseHelper();
		int sequence = helper.getNextIncomingPackageSequence(c);
		System.out.println("Sequence Value = " + sequence);
		sequence = helper.getNextIncomingPackageSequence(c);
		System.out.println("Sequence Value = " + sequence);
	}

}
