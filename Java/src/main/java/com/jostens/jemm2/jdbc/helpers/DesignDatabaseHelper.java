package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
	 * get the next sequence # and return
	 * @throws SQLException 
	 */
	public int getDesignID(Connection c, String designName) throws SQLException
	{
		int deisgnID = 0;
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_DESIGN_ID);
		selectStmt = selectStmt.replace("[NAME]", designName);
		
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
	public Design persistDesign(Connection c, Design design) throws SQLException
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
		preparedInsertStatment.setString(3, design.getAffiliationByDepiction());
		preparedInsertStatment.setString(4, design.getDesignID());
		preparedInsertStatment.setString(5, design.getJostensID());
		preparedInsertStatment.setString(6, design.getBrandAssetType());
		preparedInsertStatment.setString(7, design.getCreativeIntentDesign());
		preparedInsertStatment.setString(8, design.getDisplayedInitials());
		preparedInsertStatment.setString(9, design.getDisplayedName());
		preparedInsertStatment.setString(10, design.getDisplayedMascot());
		preparedInsertStatment.setString(11, design.getDisplayedMotto());
		preparedInsertStatment.setString(12, design.getDisplayedInscription());
		preparedInsertStatment.setString(13, design.getDisplayedYearDate());
		preparedInsertStatment.setString(14, design.getExtentOfUsability());
		preparedInsertStatment.setString(15, design.getFunctionalIntent());
		preparedInsertStatment.setString(16, design.getMainSubject());
		preparedInsertStatment.setString(17, design.getMultipleMainSubject());
		preparedInsertStatment.setString(18, design.getPortionMainSubject());
		preparedInsertStatment.setString(19, design.getViewMainSubject());
		preparedInsertStatment.executeUpdate();
		preparedInsertStatment.close();
		
		// Persist the keywords also
		persistKeywords(c, deisgnID, design.getKeywords());
		
		c.commit();
		
		return design;
	}

	/**
	 * Get the supplied Design by ID
	 * @throws SQLException 
	 */
	public void getDesign(Connection c, Design design) throws SQLException
	{
		// Get this designs ID and add to supplied design object
//		int deisgnID = getDesignID(c, design.getName());
//		design.setID(deisgnID);

		// Try to delete the ID from the design table
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_DESIGN);

		PreparedStatement preparedDeleteStatment = c.prepareStatement(selectStmt);
		// Populate the columns
		preparedDeleteStatment.setInt(1, design.getID());
		ResultSet rs = preparedDeleteStatment.executeQuery();
		rs.next();
		int id = rs.getInt(1);
		design.setName(rs.getString(2));
		design.setAffiliationByDepiction(rs.getString(3));
		design.setDesignID(rs.getString(4));
		design.setJostensID(rs.getString(5));
		design.setBrandAssetType(rs.getString(6));
		design.setCreativeIntentDesign(rs.getString(7));
		design.setDisplayedInitials(rs.getString(8));
		design.setDisplayedName(rs.getString(9));
		design.setDisplayedMascot(rs.getString(10));
		design.setDisplayedMotto(rs.getString(11));
		design.setDisplayedInscription(rs.getString(12));
		design.setDisplayedYearDate(rs.getString(13));
		design.setExtentOfUsability(rs.getString(14));
		design.setFunctionalIntent(rs.getString(15));
		design.setMainSubject(rs.getString(16));
		design.setMultipleMainSubject(rs.getString(17));
		design.setPortionMainSubject(rs.getString(18));
		design.setViewMainSubject(rs.getString(19));
		rs.close();
		preparedDeleteStatment.close();
		
		// Get the keywords
		selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_DESIGN_KEYWORDS);
		preparedDeleteStatment = c.prepareStatement(selectStmt);
		// Populate the columns
		preparedDeleteStatment.setInt(1, design.getID());
		rs = preparedDeleteStatment.executeQuery();
		while (rs.next())
		{
			String keyword = rs.getString(1);
			design.addKeyword(keyword);
		}
		rs.close();
		preparedDeleteStatment.close();
	}

	/**
	 * Get all design IDs from the design table and return in List
	 * @throws SQLException 
	 */
	public List<Integer> getAllDesignIDs(Connection c) throws SQLException
	{
		List<Integer> designs = new ArrayList<Integer>();
		
		int designID = 0;
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_ALL_DESIGNIDS);
		
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(selectStmt);
		while (rs.next())
		{
			designID = rs.getInt(1);
			designs.add(new Integer(designID));
		}

		rs.close();
		statement.close();
		
		return designs;

	}
}