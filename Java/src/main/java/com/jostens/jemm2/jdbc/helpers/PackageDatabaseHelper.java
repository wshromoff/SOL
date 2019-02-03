package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.pojo.AssetPackage;
import com.jostens.jemm2.pojo.Customer;

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
		
		// Try to delete the ID from the customer table
		String deleteStmt = Jemm2Statements.getStatement(Jemm2Statements.DELETE_PACKAGE);

		PreparedStatement preparedDeleteStatment = c.prepareStatement(deleteStmt);
		// Populate the columns
		preparedDeleteStatment.setInt(1, aPackage.getID());
		preparedDeleteStatment.executeUpdate();
		preparedDeleteStatment.close();
		
		// Update the design and keywords for the design
		String insertStmt = Jemm2Statements.getStatement(Jemm2Statements.INSERT_PACKAGE);

		PreparedStatement preparedInsertStatment = c.prepareStatement(insertStmt.toString());
		// Populate the columns
		preparedInsertStatment.setInt(1, packageID);
		preparedInsertStatment.setString(2, aPackage.getName());
		preparedInsertStatment.setString(3, aPackage.getIdentifier());
		preparedInsertStatment.setInt(4, aPackage.getPartID());
		preparedInsertStatment.setString(5, aPackage.getFirstCustomerID());
		preparedInsertStatment.setString(6, aPackage.getBrandAssetType());
		preparedInsertStatment.setString(7, aPackage.getBaseColorTones());
		preparedInsertStatment.setString(8, aPackage.getEnhancementColor());
		preparedInsertStatment.setString(9, aPackage.getColor1());
		preparedInsertStatment.setString(10, aPackage.getColor2());
		preparedInsertStatment.setString(11, aPackage.getColor3());
		preparedInsertStatment.setString(12, aPackage.getColor4());
		preparedInsertStatment.setString(13, aPackage.getColor5());
		preparedInsertStatment.setString(14, aPackage.getColor6());
		preparedInsertStatment.setString(15, aPackage.getColor7());
		preparedInsertStatment.setString(16, aPackage.getColor8());
		preparedInsertStatment.setString(17, aPackage.getColor9());
		preparedInsertStatment.setString(18, aPackage.getColor10());
		preparedInsertStatment.setString(19, aPackage.getBaseColor());
		preparedInsertStatment.setString(20, aPackage.getColorScheme());
		preparedInsertStatment.executeUpdate();
		preparedInsertStatment.close();
		
		c.commit();
		
		return aPackage;
	}

	/**
	 * Get the supplied Package by ID
	 * @throws SQLException 
	 */
	public void getPackage(Connection c, AssetPackage aPackage) throws SQLException
	{

		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_PACKAGE);

		PreparedStatement preparedSelectStatment = c.prepareStatement(selectStmt);
		// Populate the columns
		preparedSelectStatment.setInt(1, aPackage.getID());
		ResultSet rs = preparedSelectStatment.executeQuery();
		rs.next();
		rs.getInt(1);
		aPackage.setName(rs.getString(2));
		aPackage.setIdentifier(rs.getString(3));
		aPackage.setPartID(rs.getInt(4));
		aPackage.setFirstCustomerID(rs.getString(5));
		aPackage.setBrandAssetType(rs.getString(6));
		aPackage.setBaseColorTones(rs.getString(7));
		aPackage.setEnhancementColor(rs.getString(8));
		aPackage.setColor1(rs.getString(9));
		aPackage.setColor2(rs.getString(10));
		aPackage.setColor3(rs.getString(11));
		aPackage.setColor4(rs.getString(12));
		aPackage.setColor5(rs.getString(13));
		aPackage.setColor6(rs.getString(14));
		aPackage.setColor7(rs.getString(15));
		aPackage.setColor8(rs.getString(16));
		aPackage.setColor9(rs.getString(17));
		aPackage.setColor10(rs.getString(18));
		aPackage.setBaseColor(rs.getString(19));
		aPackage.setColorScheme(rs.getString(20));
		rs.close();
		preparedSelectStatment.close();
		
	}

}
