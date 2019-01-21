package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.jdbc.StatementProcessor;
import com.jostens.jemm2.pojo.Design;

/**
 * Methods for interacting with DESIGN database table so all this complex code is isolated
 */
public class DesignDatabaseHelper
{

	/**
	 * Return the count of design rows
	 */
	public int getDesignCount(Connection c)
	{
		int rowCount = 0;
		String stmt = Jemm2Statements.getStatement(Jemm2Statements.DESIGN_COUNT);

		try
		{
			StatementProcessor processor = new StatementProcessor(c, stmt);
			ResultSet rs = processor.getResultSet();
			while (rs.next())
			{
				rowCount = rs.getInt(1);
			}
			rs.close();
			processor.closeStatement();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return rowCount;
	}

	/**
	 * Return the next sequence # from DESIGN_SEQUENCE
	 */
	public int getNextDesignSequence(Connection c)
	{
		int value = 0;
		try
		{
			Statement statement = c.createStatement();
			ResultSet rs = statement.executeQuery("SELECT DESIGN_SEQUENCE.NEXTVAL FROM DUAL");
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
	 * Return the next sequence # from KEYWORD_SEQUENCE
	 */
	public int getNextKeywordSequence(Connection c)
	{
		int value = 0;
		try
		{
			Statement statement = c.createStatement();
			ResultSet rs = statement.executeQuery("SELECT KEYWORD_SEQUENCE.NEXTVAL FROM DUAL");
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
	 * Persist keywords for a design
	 * @throws SQLException 
	 */
	public void persistKeywords(Connection c, int design, List<String> keywords) throws SQLException
	{
		// Delete all keywords for this design first so know everything is a simple add, don't need to check first
		String deleteStmt = Jemm2Statements.getStatement(Jemm2Statements.DELETE_DESIGN_KEYWORDS);

		PreparedStatement preparedDeleteStatment = c.prepareStatement(deleteStmt);
		// Populate the columns
		preparedDeleteStatment.setInt(1, design);
		preparedDeleteStatment.executeUpdate();
		preparedDeleteStatment.close();
		
		String insertStmt = Jemm2Statements.getStatement(Jemm2Statements.INSERT_DESIGN_KEYWORD);

		int sequence = 1;
		// Need to add all design / keyword pairs
		for (String keyword : keywords)
		{
			// Get keyword ID, use existing or new will be created
			int ID = getKeywordID(c, keyword);

			PreparedStatement preparedInsertStatment = c.prepareStatement(insertStmt);
			// Populate the columns
			preparedInsertStatment.setInt(1, design);
			preparedInsertStatment.setInt(2, ID);
			preparedInsertStatment.setInt(3, sequence++);
			preparedInsertStatment.executeUpdate();
			preparedInsertStatment.close();
			
		}
		c.commit();
	}
	
	/**
	 * For the provided keyword search the keyword table and return its ID if found or
	 * insert the keyword and return the newly created ID
	 * @throws SQLException 
	 */
	public int getKeywordID(Connection c, String keyword) throws SQLException
	{
		int keywordID = 0;
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_KEYWORD_ID);
		selectStmt = selectStmt.replace("[KEYWORD]", keyword);
		
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(selectStmt);
		boolean rowFound = rs.next();
		if (rowFound)
		{
			keywordID = rs.getInt(1);
		}
		else
		{
			// Need to insert a keyword and keep the ID
			keywordID = getNextKeywordSequence(c);
			String insertStmt = Jemm2Statements.getStatement(Jemm2Statements.INSERT_KEYWORD);

			PreparedStatement preparedInsertStatment = c.prepareStatement(insertStmt.toString());
			// Populate the columns
			preparedInsertStatment.setInt(1, keywordID);
			preparedInsertStatment.setString(2, keyword);
			preparedInsertStatment.executeUpdate();
			preparedInsertStatment.close();
		}

		rs.close();
		statement.close();
		
		return keywordID;
	}
	
	/**
	 * For the provided design Name search the design table and return its ID if found or
	 * get the next sequence # and resturn
	 * @throws SQLException 
	 */
	public int getDesignID(Connection c, String deisginName) throws SQLException
	{
		int deisgnID = 0;
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_DESIGN_ID);
		selectStmt = selectStmt.replace("[NAME]", deisginName);
		
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
			deisgnID = getNextDesignSequence(c);
		}

		rs.close();
		statement.close();
		
		return deisgnID;
	}

	/**
	 * Persist the supplied Design
	 * @throws SQLException 
	 */
	public void persistDesign(Connection c, Design design) throws SQLException
	{
		// Get this designs ID and add to supplied design object
		int deisgnID = getDesignID(c, design.getName());
		design.setID(deisgnID);

		// Try to delete the ID from the design table
		String deleteStmt = Jemm2Statements.getStatement(Jemm2Statements.DELETE_DESIGN);

		PreparedStatement preparedDeleteStatment = c.prepareStatement(deleteStmt);
		// Populate the columns
		preparedDeleteStatment.setInt(1, design.getID());
		preparedDeleteStatment.executeUpdate();
		preparedDeleteStatment.close();
		
		// Update the design and keywords for the design
		String insertStmt = Jemm2Statements.getStatement(Jemm2Statements.INSERT_DESIGN);

		PreparedStatement preparedInsertStatment = c.prepareStatement(insertStmt.toString());
		// Populate the columns
		preparedInsertStatment.setInt(1, deisgnID);
		preparedInsertStatment.setString(2, design.getName());
		preparedInsertStatment.executeUpdate();
		preparedInsertStatment.close();
		
		// Persist the keywords also
		persistKeywords(c, deisgnID, design.getKeywords());
		
		c.commit();
	}

}