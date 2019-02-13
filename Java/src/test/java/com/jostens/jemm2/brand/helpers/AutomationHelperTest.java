package com.jostens.jemm2.brand.helpers;

import java.sql.Connection;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.brand.pojo.IncomingPackage;
import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;

public class AutomationHelperTest
{
	private static Connection c = null;
	private static AutomationHelper helper = null;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		Jemm2Statements statements = new Jemm2Statements();
		statements.initializeStatements();
		c = ConnectionHelper.getJEMM2Connection();
		
		helper = new AutomationHelper(c);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		ConnectionHelper.closeConnection(c);
	}


	@Test
	public void testGetIncomingPackagesFromUpload()
	{
		List<IncomingPackage> packages = helper.getIncomingPackagesFromUpload();
		for (IncomingPackage aPackage : packages)
		{
			System.out.println(aPackage.toString());
		}
	}

}
