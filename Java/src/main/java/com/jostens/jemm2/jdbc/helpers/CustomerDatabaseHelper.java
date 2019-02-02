package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jostens.jemm2.jdbc.Jemm2Statements;

public class CustomerDatabaseHelper
{

	/**
	 * For the provided Customer Number search the customer table and return its ID if found or
	 * get the next sequence # and return
	 * @throws SQLException 
	 */
	public int getCustomerID(Connection c, String customerNumber) throws SQLException
	{
		int customerID = 0;
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_CUSTOMER_ID);
		selectStmt = selectStmt.replace("[CUSTOMER_ID]", customerNumber);
		
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(selectStmt);
		boolean rowFound = rs.next();
		if (rowFound)
		{
			customerID = rs.getInt(1);
		}
		else
		{
			// Need to insert a keyword and keep the ID
			customerID = getNextCustomerSequence(c);
		}

		rs.close();
		statement.close();
		
		return customerID;
	}

	/**
	 * Return the next sequence # from CUSTOMER_SEQUENCE
	 */
	public int getNextCustomerSequence(Connection c)
	{
		int value = 0;
		try
		{
			Statement statement = c.createStatement();
			ResultSet rs = statement.executeQuery("SELECT CUSTOMER_SEQUENCE.NEXTVAL FROM DUAL");
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

}
