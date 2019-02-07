package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.pojo.Asset;
import com.jostens.jemm2.pojo.AssetPackage;
import com.jostens.jemm2.pojo.CustomerPackage;

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
//		else
//		{
//			// Need to insert a keyword and keep the ID
//			customerID = getNextPackageSequence(c);
//		}

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
	 * Persist the supplied Package
	 * @throws SQLException 
	 */
	public AssetPackage persistPackage(Connection c, AssetPackage aPackage) throws SQLException
	{
		boolean persistCustomerPackage = true;
		if (aPackage.getFirstCustomerID() == null)
		{
//			System.out.println("NULL FOUND");
			persistCustomerPackage = false;
			
		}
		// Get the Part ID for the Part on this packages name
		PartDatabaseHelper partHelper = new PartDatabaseHelper();
		int partID = partHelper.getPartID(c, aPackage.getPartName());
		aPackage.setPartID(partID);

		// Get a CustomerPackage ready to be saved
		CustomerPackage customerPackage = aPackage.getCustomerPackage();
		if (persistCustomerPackage)
		{
			
			CustomerDatabaseHelper cdHelper = new CustomerDatabaseHelper();
			int customerID = cdHelper.getCustomerID(c, aPackage.getFirstCustomerID());
			customerPackage.setCustomerID(customerID);
		}
		
//		System.out.println("IDENT=" + aPackage.getIdentifier());
		// Attempt to find Package by identifier. If found, no persisting is needed.
		int packageID = getPackageIDByIdentifier(c, aPackage.getIdentifier());
		customerPackage.setPackageID(packageID);
		if (packageID > 0)
		{
			if (persistCustomerPackage)
			{
				// Now persist customer package
				persistCustomerPackage(c, customerPackage);
			}

//			System.out.println("Found by Itentifier");
			aPackage.setID(packageID);
			return aPackage;
		}
			
		// Get this package ID and add to supplied package object
		packageID = getPackageIDByName(c, aPackage.getName());
		aPackage.setID(packageID);
		customerPackage.setPackageID(packageID);

		if (persistCustomerPackage)
		{
			// Now persist customer package
			persistCustomerPackage(c, customerPackage);
		}
		
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

		// Persist the assets of the package
		persistAssets(c, packageID, aPackage.getAssets());
		
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

		// Get the assets for this package
		aPackage.getAssets().clear();
		selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_ASSETS_FOR_PACKAGE);
		preparedSelectStatment = c.prepareStatement(selectStmt);
		// Populate the columns
		preparedSelectStatment.setInt(1, aPackage.getID());
		rs = preparedSelectStatment.executeQuery();
		while (rs.next())
		{
			Asset asset = new Asset();
			asset.setPackageID(rs.getInt(1));
			asset.setName(rs.getString(2));
			asset.setFolderPath(rs.getString(3));
			asset.setIsBlack(rs.getInt(4));
			asset.setIsGold(rs.getInt(5));
			asset.setIsSilver(rs.getInt(6));
			asset.setIsBestAvailable(rs.getInt(7));
			
			aPackage.getAssets().add(asset);
		}
		rs.close();
		preparedSelectStatment.close();

	}

	/**
	 * Persist Assets for this Package
	 * @throws SQLException 
	 */
	public void persistAssets(Connection c, int packageID, List<Asset> assets) throws SQLException
	{
		// Delete all assets for this packageID first so know everything is a simple add, don't need to check first
		String deleteStmt = Jemm2Statements.getStatement(Jemm2Statements.DELETE_ASSET_FOR_PACKAGE);

		PreparedStatement preparedDeleteStatment = c.prepareStatement(deleteStmt);
		// Populate the columns
		preparedDeleteStatment.setInt(1, packageID);
		preparedDeleteStatment.executeUpdate();
		preparedDeleteStatment.close();
		
		String insertStmt = Jemm2Statements.getStatement(Jemm2Statements.INSERT_ASSET_FOR_PACKAGE);

		// Need to add all assets
		for (Asset asset : assets)
		{
			PreparedStatement preparedInsertStatment = c.prepareStatement(insertStmt);
			// Populate the columns
			preparedInsertStatment.setInt(1, packageID);
			preparedInsertStatment.setString(2, asset.getName());
			preparedInsertStatment.setString(3, asset.getFolderPath());
			preparedInsertStatment.setInt(4, asset.getIsBlack());
			preparedInsertStatment.setInt(5, asset.getIsGold());
			preparedInsertStatment.setInt(6, asset.getIsSilver());
			preparedInsertStatment.setInt(7, asset.getIsBestAvailable());
			preparedInsertStatment.executeUpdate();
			preparedInsertStatment.close();
			
		}
		c.commit();

	}
	
	/**
	 * Persist a customer package
	 * @throws SQLException 
	 */
	public void persistCustomerPackage(Connection c, CustomerPackage customerPackage) throws SQLException
	{
		// Delete customer package record first so the data can be simpler added instead of updated
		String deleteStmt = Jemm2Statements.getStatement(Jemm2Statements.DELETE_CUSTOMER_PACKAGE);

		PreparedStatement preparedDeleteStatment = c.prepareStatement(deleteStmt);
		// Populate the columns
		preparedDeleteStatment.setInt(1, customerPackage.getPackageID());
		preparedDeleteStatment.setInt(2, customerPackage.getCustomerID());
		preparedDeleteStatment.executeUpdate();
		preparedDeleteStatment.close();
		
		String insertStmt = Jemm2Statements.getStatement(Jemm2Statements.INSERT_CUSTOMER_PACKAGE);

		PreparedStatement preparedInsertStatment = c.prepareStatement(insertStmt);
		// Populate the columns
		preparedInsertStatment.setInt(1, customerPackage.getPackageID());
		preparedInsertStatment.setInt(2, customerPackage.getCustomerID());
		preparedInsertStatment.setString(3, customerPackage.getAffiliationByUse());
		preparedInsertStatment.setString(4, customerPackage.getHistoricUseColor());
		preparedInsertStatment.setString(5, customerPackage.getHistoricUseDesign());
		preparedInsertStatment.setString(6, customerPackage.getStatusLifeCycle());
		preparedInsertStatment.setString(7, customerPackage.getStatusCataloging());
		preparedInsertStatment.setString(8, customerPackage.getStatusAutomation());
		preparedInsertStatment.setString(9, customerPackage.getStatusAvailability());
		preparedInsertStatment.setString(10, customerPackage.getBusinessDefaultUse());
		preparedInsertStatment.executeUpdate();
		preparedInsertStatment.close();
			
		c.commit();

	}

}
