package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.pojo.AssetPackage;

public class PackageDatabaseHelper
{

	/**
	 * For the provided Package Identifier search the Package table and return its ID if found or
	 * get the next sequence # and return.  Identifier is used because a combination of fields make the package unique.
	 * @throws SQLException 
	 */
	public int getPackageIDByIdentifier(Connection c, String identifier) throws SQLException
	{
		int customerID = 0;
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_PACKAGE_ID_IDENTIFIER);
		selectStmt = selectStmt.replace("[IDENTIFIER]", identifier);
		
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
			customerID = getNextPackageSequence(c);
		}

		rs.close();
		statement.close();
		
		return customerID;
	}

	/**
	 * For the provided Package name search the Package table and return its ID if found or
	 * get the next sequence # and return.
	 * @throws SQLException 
	 */
	public int getPackageIDByName(Connection c, String name) throws SQLException
	{
		int customerID = 0;
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_PACKAGE_ID_NAME);
		selectStmt = selectStmt.replace("[NAME]", name);
		
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
			customerID = getNextPackageSequence(c);
		}

		rs.close();
		statement.close();
		
		return customerID;
	}

	/**
	 * Return the next sequence # from PACKAGE_SEQUENCE
	 */
	public int getNextPackageSequence(Connection c)
	{
		int value = 0;
		try
		{
			Statement statement = c.createStatement();
			ResultSet rs = statement.executeQuery("SELECT PACKAGE_SEQUENCE.NEXTVAL FROM DUAL");
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
	 * Persist the supplied Customer
	 * @throws SQLException 
	 */
	public AssetPackage persistPackage(Connection c, AssetPackage aPackage) throws SQLException
	{
		// Get this customer ID and add to supplied customer object
		int packageID = getPackageIDByName(c, aPackage.getName());
		aPackage.setID(packageID);
		
		System.out.println("THE ID=" + packageID);
		
		// Try to delete the ID from the customer table
		String deleteStmt = Jemm2Statements.getStatement(Jemm2Statements.DELETE_PACKAGE);

		PreparedStatement preparedDeleteStatment = c.prepareStatement(deleteStmt);
		// Populate the columns
		preparedDeleteStatment.setInt(1, aPackage.getID());
		preparedDeleteStatment.executeUpdate();
		preparedDeleteStatment.close();
		
//		// Update the design and keywords for the design
//		String insertStmt = Jemm2Statements.getStatement(Jemm2Statements.INSERT_PACKAGE);
//
//		PreparedStatement preparedInsertStatment = c.prepareStatement(insertStmt.toString());
//		// Populate the columns
//		preparedInsertStatment.setInt(1, customerID);
//		preparedInsertStatment.setString(2, aPackage.getName());
//		preparedInsertStatment.setString(3, aPackage.getCustomerID());
//		preparedInsertStatment.setString(4, aPackage.getCity());
//		preparedInsertStatment.setString(5, aPackage.getState());
//		preparedInsertStatment.setString(6, aPackage.getColor1());
//		preparedInsertStatment.setString(7, aPackage.getColor2());
//		preparedInsertStatment.setString(8, aPackage.getColor3());
//		preparedInsertStatment.setString(9, aPackage.getMascot());
//		preparedInsertStatment.executeUpdate();
//		preparedInsertStatment.close();
		
		c.commit();
		
		return aPackage;
	}

}
