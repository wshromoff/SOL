package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jostens.jemm2.brand.pojo.IncomingPackage;
import com.jostens.jemm2.jdbc.Jemm2Statements;

public class AutomationsDatabaseHelper
{

	/**
	 * Return the next sequence # from INCOMINGPACKAGE_SEQUENCE
	 */
	public int getNextIncomingPackageSequence(Connection c)
	{
		int value = 0;
		try
		{
			Statement statement = c.createStatement();
			ResultSet rs = statement.executeQuery("SELECT INCOMING_PACKAGE_SEQUENCE.NEXTVAL FROM DUAL");
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
	 * For the provided IncomingPackage name search the Incoming_Package table and return its ID if found or
	 * a zero will be returned
	 * @throws SQLException 
	 */
	public int getIncomingPackageIDByName(Connection c, String name) throws SQLException
	{
		int incomingID = 0;
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_INCOMING_PACKAGE_ID_NAME);
		selectStmt = selectStmt.replace("[NAME]", name);
		
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(selectStmt);
		boolean rowFound = rs.next();
		if (rowFound)
		{
			incomingID = rs.getInt(1);
		}

		rs.close();
		statement.close();
		
		return incomingID;
	}

	/**
	 * Persist the supplied IncomingPackage
	 * @throws SQLException 
	 */
	public void persistIncomingPackage(Connection c, IncomingPackage aPackage) throws SQLException
	{
		// Get this package ID and add to supplied package object
		int packageID = getIncomingPackageIDByName(c, aPackage.getName());
		if (packageID == 0)
		{
			packageID = getNextIncomingPackageSequence(c);
		}
		aPackage.setID(packageID);
		
		// Try to delete the ID from the customer table
		String deleteStmt = Jemm2Statements.getStatement(Jemm2Statements.DELETE_INCOMING_PACKAGE);

		PreparedStatement preparedDeleteStatment = c.prepareStatement(deleteStmt);
		// Populate the columns
		preparedDeleteStatment.setInt(1, aPackage.getID());
		preparedDeleteStatment.executeUpdate();
		preparedDeleteStatment.close();
		
		// Update the design and keywords for the design
		String insertStmt = Jemm2Statements.getStatement(Jemm2Statements.INSERT_INCOMING_PACKAGE);

		PreparedStatement preparedInsertStatment = c.prepareStatement(insertStmt.toString());
		// Populate the columns
		preparedInsertStatment.setInt(1, packageID);
		preparedInsertStatment.setString(2, aPackage.getName());
		preparedInsertStatment.setInt(3, aPackage.getRevision());
		preparedInsertStatment.setString(4, aPackage.getStatusAutomation());
		preparedInsertStatment.setString(5, aPackage.getError());
		preparedInsertStatment.executeUpdate();
		preparedInsertStatment.close();
		
		c.commit();
		
	}

	/**
	 * For the provided IncomingPackage name search the Incoming_Package table and return its ID if found or
	 * a zero will be returned
	 * @throws SQLException 
	 */
	public int getCurrentRevision(Connection c, IncomingPackage aPackage) throws SQLException
	{
		int revision = 0;
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_CURRENT_REVISION);
		selectStmt = selectStmt.replace("[PACKAGEID]", aPackage.getID() + "");

		System.out.println("STMT=" + selectStmt);
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(selectStmt);
		boolean rowFound = rs.next();
		if (rowFound)
		{
			revision = rs.getInt(1);
		}

		rs.close();
		statement.close();
		
		return revision;
	}


}
