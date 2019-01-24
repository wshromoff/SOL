package com.jostens.jemm2.helpers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.jdbc.helpers.PartDatabaseHelper;
import com.jostens.jemm2.pojo.Part;

public class PartHelperTest
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
	public void persistParts() throws IOException, SQLException
	{
		PartHelper helper = new PartHelper();
		PartDatabaseHelper dbHelper = new PartDatabaseHelper();
		
//		List<Part> parts = helper.getPartObjects("Parts_01.txt");
//		List<Part> parts = helper.getPartObjects("Parts_02.txt");
		List<Part> parts = helper.getPartObjects("Parts_03.txt");

		int i = 0;
		for (Part part : parts)
		{
			i++;
			System.out.println("" + i);
			dbHelper.persistPart(c, part);
		}
	}

}
