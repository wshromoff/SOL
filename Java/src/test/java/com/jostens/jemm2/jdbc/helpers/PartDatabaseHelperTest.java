package com.jostens.jemm2.jdbc.helpers;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.pojo.Design;
import com.jostens.jemm2.pojo.Part;

public class PartDatabaseHelperTest
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
	public void testGetNextPartSequence()
	{
		PartDatabaseHelper helper = new PartDatabaseHelper();
		int sequence = helper.getNextPartSequence(c);
		System.out.println("Sequence Value = " + sequence);
		sequence = helper.getNextPartSequence(c);
		System.out.println("Sequence Value = " + sequence);
	}

//	@Test
	public void testGetPartID() throws SQLException
	{
		PartDatabaseHelper helper = new PartDatabaseHelper();
		int designId = helper.getPartID(c, "Wowwy");
		System.out.println("Part Value = " + designId);
		designId = helper.getPartID(c, "Dog");
		System.out.println("Part Value = " + designId);
	}
	
//	@Test
	public void testPersistPart() throws SQLException
	{
		PartDatabaseHelper helper = new PartDatabaseHelper();
		
		Part part = new Part();
		part.setName("part1");
		helper.persistPart(c, part);
		
	}

	@Test
	public void testImportPart() throws SQLException
	{
		Part part = new Part("CE602260|20180817-48c3dfa2babe|20180817-66b84ae756c2|CE602260||Asset Matches Part (Master)|");
		PartDatabaseHelper helper = new PartDatabaseHelper();
		helper.persistPart(c, part);
		
//		Design design2 = new Design();
//		design2.setID(design.getID());
//		
//		// Populate design2 from database
//		helper.getDesign(c, design2);
//		
//		assertTrue(design.equals(design2));
		
		
		System.out.println("PART ID = " + part.getID());
	}

}
