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
import com.jostens.jemm2.jdbc.helpers.DesignDatabaseHelper;
import com.jostens.jemm2.pojo.Design;

public class DesignHelperTest
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
	public void testGetDesignObjects() throws IOException
	{
		DesignHelper helper = new DesignHelper();
		
		List<Design> designs = helper.getDesignObjects("design_01.txt");
		
		System.out.println("Design count = " + designs.size());
	}

//	@Test
	public void testGetDesign() throws IOException
	{
		DesignHelper helper = new DesignHelper();
		
		Design design = helper.getDesign("|20150626-aecac9d49554|Knight,Baron,Crusader,Lancer|");
		
		System.out.println("Keyword count = " + design.getKeywords().size());
	}

	@Test
	public void persistDesigns() throws IOException, SQLException
	{
		DesignHelper helper = new DesignHelper();
		DesignDatabaseHelper dbHelper = new DesignDatabaseHelper();
		
//		List<Design> designs = helper.getDesignObjects("design_01.txt");
//		List<Design> designs = helper.getDesignObjects("design_02.txt");
		List<Design> designs = helper.getDesignObjects("design_03.txt");

		int i = 0;
		for (Design design : designs)
		{
			i++;
			System.out.println("" + i);
			dbHelper.persistDesign(c, design);
		}
	}

}
