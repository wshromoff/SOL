package com.jostens.jemm2.brand.helpers;

import java.sql.Connection;
import java.sql.SQLException;
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

		helper = new AutomationHelper();
		helper.setConnection(c);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		ConnectionHelper.closeConnection(c);
	}


//	@Test
	public void testGetIncomingPackagesFromUpload()
	{
		List<IncomingPackage> packages = helper.getIncomingPackagesFromUpload();
		for (IncomingPackage aPackage : packages)
		{
			System.out.println(aPackage.toString());
		}
	}

//	@Test
	public void testersistIncomingPackage() throws SQLException
	{
		IncomingPackage aPackage = new IncomingPackage();
		aPackage.setName("name");
		aPackage.setRevision(3);
		aPackage.setStatusAutomation("FS_COMPLETE MD_COMPLETE ");
		
		helper.persistIncomingPackage(aPackage);
		System.out.println("New Package ID = " + aPackage.getID());
	}

	@Test
	public void testGetCurrentRevision() throws SQLException
	{
		IncomingPackage aPackage = new IncomingPackage();
		aPackage.setID(23);
		
		int revision = helper.getCurrentRevision(aPackage);
		System.out.println("New Package ID = " + revision);
	}

}
