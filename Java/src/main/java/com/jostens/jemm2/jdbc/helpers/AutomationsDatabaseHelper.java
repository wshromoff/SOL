package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

}
