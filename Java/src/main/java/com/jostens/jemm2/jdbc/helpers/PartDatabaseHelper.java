package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.pojo.Design;
import com.jostens.jemm2.pojo.Part;

public class PartDatabaseHelper
{

	/**
	 * For the provided Part Name search the part table and return its ID if found or
	 * get the next sequence # and return
	 * @throws SQLException 
	 */
	public int getPartID(Connection c, String partName) throws SQLException
	{
		int partID = 0;
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_PART_ID);
		selectStmt = selectStmt.replace("[NAME]", partName);
		
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(selectStmt);
		boolean rowFound = rs.next();
		if (rowFound)
		{
			partID = rs.getInt(1);
		}
		else
		{
			// Need to insert a keyword and keep the ID
			partID = getNextPartSequence(c);
		}

		rs.close();
		statement.close();
		
		return partID;
	}

	/**
	 * Return the next sequence # from PART_SEQUENCE
	 */
	public int getNextPartSequence(Connection c)
	{
		int value = 0;
		try
		{
			Statement statement = c.createStatement();
			ResultSet rs = statement.executeQuery("SELECT PART_SEQUENCE.NEXTVAL FROM DUAL");
			rs.next();
			value = rs.getInt(1);
			rs.close();
			statement.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
//			ExceptionHelper.logExceptionToFile("getNextMBAssetsSequence (CustomerProfileDatabase)", e);
			//System.out.println("Exception in retrieving next MB Assets Sequence value: " + ExceptionHelper.getStackTraceAsString(e));
		}
		return value;
	}

	/**
	 * Persist the supplied Part
	 * @throws SQLException 
	 */
	public Part persistPart(Connection c, Part part) throws SQLException
	{
		// Get this designs ID and add to supplied design object
		int partID = getPartID(c, part.getName());
		part.setID(partID);
		
		// Get the ID of the design this part implements
		DesignDatabaseHelper designHelper = new DesignDatabaseHelper();
		int designID = designHelper.getDesignID(c, part.getDesignIDString());
		part.setDesignID(designID);
		
		// Try to delete the ID from the design table
		String deleteStmt = Jemm2Statements.getStatement(Jemm2Statements.DELETE_PART);

		PreparedStatement preparedDeleteStatment = c.prepareStatement(deleteStmt);
		// Populate the columns
		preparedDeleteStatment.setInt(1, part.getID());
		preparedDeleteStatment.executeUpdate();
		preparedDeleteStatment.close();
		
		// Update the design and keywords for the design
		String insertStmt = Jemm2Statements.getStatement(Jemm2Statements.INSERT_PART);

		PreparedStatement preparedInsertStatment = c.prepareStatement(insertStmt.toString());
		// Populate the columns
		preparedInsertStatment.setInt(1, partID);
		preparedInsertStatment.setString(2, part.getName());
		preparedInsertStatment.setString(3, part.getDesignIDString());
		preparedInsertStatment.setString(4, part.getJostensIDString());
		preparedInsertStatment.setString(5, part.getPartID());
		preparedInsertStatment.setString(6, part.getPartIDDerivative());
		preparedInsertStatment.setString(7, part.getPartValidation());
		preparedInsertStatment.setInt(8, part.getDesignID());
		preparedInsertStatment.executeUpdate();
		preparedInsertStatment.close();
		
		c.commit();
		
		return part;
	}

	/**
	 * Get the supplied Part by ID
	 * @throws SQLException 
	 */
	public void getPart(Connection c, Part part) throws SQLException
	{
		// Get this designs ID and add to supplied design object
//		int deisgnID = getDesignID(c, design.getName());
//		design.setID(deisgnID);

		// Try to delete the ID from the design table
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_PART);

		PreparedStatement preparedDeleteStatment = c.prepareStatement(selectStmt);
		// Populate the columns
		preparedDeleteStatment.setInt(1, part.getID());
		ResultSet rs = preparedDeleteStatment.executeQuery();
		rs.next();
		rs.getInt(1);
		part.setName(rs.getString(2));
		part.setDesignIDString(rs.getString(3));
		part.setJostensIDString(rs.getString(4));
		part.setPartID(rs.getString(5));
		part.setPartIDDerivative(rs.getString(6));
		part.setPartValidation(rs.getString(7));
		part.setDesignID(rs.getInt(8));
		rs.close();
		preparedDeleteStatment.close();
		
		if (part.getDesignID() == 0)
		{
			part.setDesign(new Design());
			return;
		}
		
		// If the design ID is != 0, go and get the design also
		DesignDatabaseHelper designHelper = new DesignDatabaseHelper();
		Design design = new Design();
		design.setID(part.getDesignID());
		designHelper.getDesign(c, design);
		part.setDesign(design);
		
	}

	/**
	 * Get all part IDs from the part table and return in List
	 * @throws SQLException 
	 */
	public List<Integer> getAllPartIDs(Connection c) throws SQLException
	{
		List<Integer> parts = new ArrayList<Integer>();
		
		int partID = 0;
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_ALL_PARTIDS);
		
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(selectStmt);
		while (rs.next())
		{
			partID = rs.getInt(1);
			parts.add(new Integer(partID));
		}

		rs.close();
		statement.close();
		
		return parts;

	}

}
