package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jostens.jemm2.jdbc.Jemm2Statements;
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
		int deisgnID = 0;
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_PART_ID);
		selectStmt = selectStmt.replace("[NAME]", partName);
		
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(selectStmt);
		boolean rowFound = rs.next();
		if (rowFound)
		{
			deisgnID = rs.getInt(1);
		}
		else
		{
			// Need to insert a keyword and keep the ID
			deisgnID = getNextPartSequence(c);
		}

		rs.close();
		statement.close();
		
		return deisgnID;
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

}
