package com.jostens.dam.brand.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.jostens.dam.brand.assetclass.interfaces.CustomerProfileAsset;
import com.jostens.dam.shared.assetclass.BaseAsset;
import com.jostens.dam.shared.assetclass.MetadataGUIDS;
import com.jostens.dam.shared.common.CalendarAdjust;
import com.jostens.dam.shared.common.ExceptionHelper;
import com.jostens.dam.shared.helpers.MetadataHelper;

/**
 * Methods related to interacting with the Driver table in the customer profile. 
 * 
 * Caller must specify a valid Connection.
 */
public class CustomerProfileDatabase
{
	// By default this object will perform updates, which means insert rows to the database.
	// However, that functionality can be disabled by calling the setter to change performUpdate to false
	private boolean performUpdate = true;
	
	// Mapping of meta data stored in the customer profile as the key to the column name in the MB_METADATA table (value)
	private static Map<String, String> METADATA = new LinkedHashMap<String, String>()
	{
		{
			put(MetadataGUIDS.AFFILIATION_BY_DEPICTION, "AFFILIATION_BY_DEPICTION");
			put(MetadataGUIDS.AFFILIATION_BY_USE, "AFFILIATION_BY_USE");
			put(MetadataGUIDS.ASSET_CLASS, "ASSET_CLASS");
			put(MetadataGUIDS.BASE_COLOR_TONES_RENDERED, "BASE_COLOR_TONES_RENDERED");
			put(MetadataGUIDS.BRAND_ASSET_TYPE, "BRAND_ASSET_TYPE");
			put(MetadataGUIDS.BRAND_VALIDATION, "BRAND_VALIDATION");
			put(MetadataGUIDS.BUSINESS_DEFAULT_USE, "BUSINESS_DEFAULT_USE");
			put(MetadataGUIDS.COLOR_SCHEME, "COLOR_SCHEME");
			put(MetadataGUIDS.COLORIZATION_LEVEL, "COLORIZATION_LEVEL");
			put(MetadataGUIDS.COLORIZATION_TECHNIQUE, "COLORIZATION_TECHNIQUE");
			put(MetadataGUIDS.CREATIVE_INTENT_COLOR, "CREATIVE_INTENT_COLOR");
			put(MetadataGUIDS.CREATIVE_INTENT_DESIGN, "CREATIVE_INTENT_DESIGN");
			put(MetadataGUIDS.CUSTOMER_HISTORIC_USE_COLOR, "CUSTOMER_HISTORIC_USE_COLOR");
			put(MetadataGUIDS.CUSTOMER_HISTORIC_USE_DESIGN, "CUSTOMER_HISTORIC_USE_DESIGN");
			put(MetadataGUIDS.DETAIL_ENHANCEMENT_COLOR, "DETAIL_ENHANCEMENT_COLOR");
			put(MetadataGUIDS.DIMENSIONS_PIXELS, "DIMENSIONS_PIXELS");
			put(MetadataGUIDS.DISPLAYED_YEARDATE, "DISPLAYED_YEARDATE");
			put(MetadataGUIDS.EXTENT_OF_USABILITY, "EXTENT_OF_USABILITY");
			put(MetadataGUIDS.JEM_FACET, "JEM_FACET");
			put(MetadataGUIDS.KEYWORD_MAIN_SUBJECT, "KEYWORD_MAIN_SUBJECT");
			put(MetadataGUIDS.PART_ID, "PART_ID");
			put(MetadataGUIDS.PART_ID_DERIVATIVE, "PART_ID_DERIVATIVE");
			put(MetadataGUIDS.RESOLUTION_PPI, "RESOLUTION_PPI");
			put(MetadataGUIDS.STATUS_AVAILABILITY, "STATUS_AVAILABILITY");
			put(MetadataGUIDS.STATUS_LIFE_CYCLE, "STATUS_LIFE_CYCLE");
			put(MetadataGUIDS.WORKFLOW_PROJECT, "WORKFLOW_PROJECT");
			put(MetadataGUIDS.RIGHTS_MANAGEMENT, "RIGHTS_MANAGEMENT");
			put(MetadataGUIDS.RIGHTS_MANAGEMENT_STATUS, "RIGHTS_MANAGEMENT_STATUS");
		}
	};
	
	// Define a List of all known meta data GUIDS based on the defined Names as an Array
	private static String[] METADATA_GUIDS_ARRAY = null;
	private static String[] METADATA_TO_POPULATE = null;
	
	// String of all column names
	private static String allColumnNames = null;
	// String of question marks for all column names
	private static String allColumnValues = null;
	
	public CustomerProfileDatabase()
	{
		if (allColumnNames != null)
		{
			return;		// Already built the allColumn... values
		}
		
		// Setup meta data guids array after all runtime properties may have been added
		METADATA_GUIDS_ARRAY = (String[])METADATA.keySet().toArray(new String[0]);
		
		// Build the List of all column names for reuse when inserting a new asset into the MB_METADATA table
		// The columns in this table can change and is controlled by the METADATA Map.  Also build the same
		// number of '?,' to match the number of fields found.
		StringBuffer sbNames = new StringBuffer();
		StringBuffer sbValues = new StringBuffer();
		for (String columnName : METADATA.values())
		{
			sbNames.append(columnName + ", ");
			sbValues.append("?,");
		}
		
		// Convert the temporary StringBuffers to Strings that will be reused
		allColumnNames = sbNames.toString();
		allColumnValues = sbValues.toString();
		
		// Remove the last comma from both strings
		int i = allColumnNames.lastIndexOf(",");
		allColumnNames = allColumnNames.substring(0, i);
		i = allColumnValues.lastIndexOf(",");
		allColumnValues = allColumnValues.substring(0, i);
	}
	
	/**
	 * Return the GUIDS needed to be populated prior to inserting an asset into the customer profile tables
	 */
	public static String[] getMetadataToPopulate()
	{
		// Does the METADATA_GUIDS_ARRAY need to be built?
		if (METADATA_TO_POPULATE == null)
		{
			// Start with the metadata to be populated
			List<String> guids = new ArrayList<String>(METADATA.keySet());
			// Add fields that need to be populated
			guids.add(MetadataGUIDS.CUSTOMER_ID_ORACLE);
			guids.add(MetadataGUIDS.ASSET_ID_JOSTENS);
			guids.add(MetadataGUIDS.ASSET_ID_COLOR_SCHEME);
//			guids.add(MetadataGUIDS.NAME);
			guids.add(MetadataGUIDS.MODIFICATION_TIME);
			METADATA_TO_POPULATE = guids.toArray(new String[0]);
		}
		return METADATA_TO_POPULATE;
	}

	/**
	 * The supplied asset should be deleted from the customer profile.  Drive an insert
	 * into driver table found in the customer profile.
	 */
	public void insertDeleteAction(Connection c, BaseAsset ba) throws SQLException
	{
		performActionInsert(c, CustomerProfileAction.DELETE_ACTION, ba, null);
	}
	
	public void insertAddAction(Connection c, BaseAsset ba) throws SQLException
	{
		performActionInsert(c, CustomerProfileAction.ADD_ACTION, ba, "ALL");
		
	}

	public void insertMetadataAction(Connection c, BaseAsset ba, String metadata) throws SQLException
	{
		performActionInsert(c, CustomerProfileAction.METADATA_ACTION, ba, metadata);
		
	}

	private void performActionInsert(Connection c, String action, BaseAsset ba, String metadata) throws SQLException
	{
		
		if (!performUpdate)
		{
			return;			// Updates have been disabled
		}
		

		String insertStmt = "insert into MB_ACTIONS (ID, ACTION, ASSET_NAME, CUSTOMER_ID, ASSET_ID_JOSTENS, MB_GUID, METADATA, INSERTED, COMPLETED, MESSAGE) " + 
				"values (?,?,?,?,?,?,?,?,?,?)";

		PreparedStatement preparedInsertStatment = null;
		
		try
		{
			preparedInsertStatment = c.prepareStatement(insertStmt.toString());
			// Populate the columns
			// Get the next sequence number to use as the primary key for both tables
			int ID = getNextMBActionsSequence(c);
			preparedInsertStatment.setInt(1, ID);
			preparedInsertStatment.setString(2, action);
			preparedInsertStatment.setString(3, ba.getAssetName());
			preparedInsertStatment.setString(4, ba.getMetaDataValue(MetadataGUIDS.CUSTOMER_ID_ORACLE));
			preparedInsertStatment.setString(5, ba.getMetaDataValue(MetadataGUIDS.ASSET_ID_JOSTENS));
// PRIORITY column doesn't have a real purpose now, so don't include.
//			preparedInsertStatment.setString(6, ba.getMetaDataValue(MetadataGUIDS.JOB_SCHEDULE_PRIORITY));
			preparedInsertStatment.setString(6, ba.getAssetId());
			preparedInsertStatment.setString(7, metadata);
			
			Timestamp now = new Timestamp(System.currentTimeMillis());
			preparedInsertStatment.setTimestamp(8, now);
			preparedInsertStatment.setDate(9, null);
			preparedInsertStatment.setString(10, null);
			
			preparedInsertStatment.executeUpdate();
			
			c.commit();		

		}
		finally
		{
			if (preparedInsertStatment != null)
			{
				preparedInsertStatment.close();
			}
		}
	}

	/**
	 * Return the next sequence # from MB_ASSETS_SEQUENCE
	 */
	public int getNextMBAssetsSequence(Connection c)
	{
		int value = 0;
		try
		{
			Statement statement = c.createStatement();
			ResultSet rs = statement.executeQuery("SELECT MB_ASSETS_SEQUENCE.NEXTVAL FROM DUAL");
			rs.next();
			value = rs.getInt(1);
			rs.close();
			statement.close();
		} 
		catch (Exception e)
		{
			ExceptionHelper.logExceptionToFile("getNextMBAssetsSequence (CustomerProfileDatabase)", e);
			//System.out.println("Exception in retrieving next MB Assets Sequence value: " + ExceptionHelper.getStackTraceAsString(e));
		}
		return value;
	}

	/**
	 * Return the next sequence # from MB_ACTIONS_SEQUENCE
	 */
	public int getNextMBActionsSequence(Connection c)
	{
		int value = 0;
		try
		{
			Statement statement = c.createStatement();
			ResultSet rs = statement.executeQuery("SELECT MB_ACTIONS_SEQUENCE.NEXTVAL FROM DUAL");
			rs.next();
			value = rs.getInt(1);
			rs.close();
			statement.close();
		} 
		catch (Exception e)
		{
			ExceptionHelper.logExceptionToFile("getNextMBActionsSequence (CustomerProfileDatabase)", e);
			//System.out.println("Exception in retrieving next MB Actions Sequence value: " + ExceptionHelper.getStackTraceAsString(e));
		}
		return value;
	}

	/**
	 * Insert a row into the MB_ASSETS table.  Must be provided a Connection, primary key (ID)
	 * and a BaseAsset which contains all other values needed to populate.
	 * 
	 * This method shouldn't be called directly.  addNewAsset wrapper method should be entry point.
	 */
	public void insertMBAssets(Connection c, int ID, BaseAsset asset, String damPath) throws SQLException
	{
		String insertStmt = "insert into MB_ASSETS (ID, CUSTOMER_ID, ASSET_NAME, ASSET_ID_JOSTENS, COLOR, PREFIX, SUFFIX, EXTENSION, PATH_TO_FILE, PATH_TO_BESTCOLOR, ADDED, UPDATED, MODIFICATION_TIME) " + 
					"values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

		PreparedStatement preparedInsertStatment = null;
		
		try
		{
			preparedInsertStatment = c.prepareStatement(insertStmt.toString());
			// Populate the columns
			preparedInsertStatment.setInt(1, ID);
			preparedInsertStatment.setString(2, asset.getMetaDataValue(MetadataGUIDS.CUSTOMER_ID_ORACLE));
			preparedInsertStatment.setString(3, asset.getAssetName());
			preparedInsertStatment.setString(4, asset.getMetaDataValue(MetadataGUIDS.ASSET_ID_JOSTENS));
			
			// Populate CustomerProfileAsset related columns if an appropriate asset
			CustomerProfileAsset profileAsset = null;
			if (asset instanceof CustomerProfileAsset)
			{
				profileAsset = (CustomerProfileAsset)asset;
				preparedInsertStatment.setString(5, profileAsset.getCPColor());
				preparedInsertStatment.setString(6, profileAsset.getCPPrefix());
				preparedInsertStatment.setString(7, profileAsset.getCPSuffix());
				preparedInsertStatment.setString(8, profileAsset.getCPExtension());
			}
			else
			{
				preparedInsertStatment.setString(5, null);
				preparedInsertStatment.setString(6, null);
				preparedInsertStatment.setString(7, null);
				preparedInsertStatment.setString(8, null);				
			}

			// Build final path where the asset can be found
			// Build the final production path
			int i = damPath.lastIndexOf("DAM");
			String pathToFile = damPath.substring(i) + "/" + asset.getAssetName();
			preparedInsertStatment.setString(9, pathToFile);
			if (profileAsset != null)
			{
				String pathToBestColor = damPath.substring(i) + "/" + profileAsset.getCPBestColorName();
				preparedInsertStatment.setString(10, pathToBestColor);
			}
			else
			{
				preparedInsertStatment.setString(10, null);
			}

			Timestamp now = new Timestamp(System.currentTimeMillis());
			preparedInsertStatment.setTimestamp(11, now);
			preparedInsertStatment.setTimestamp(12, now);
			// Modification time is GMT and needs to be adjust to CST, use AssetRevisionsHelper method
			CalendarAdjust.adjustModificationTimeFromGMT(asset);
			Timestamp modified = new Timestamp(Long.parseLong(asset.getMetaDataValue(MetadataGUIDS.MODIFICATION_TIME)));
			preparedInsertStatment.setTimestamp(13, modified);
			
			preparedInsertStatment.executeUpdate();
		}
		finally
		{
			if (preparedInsertStatment != null)
			{
				preparedInsertStatment.close();
			}
		}
	}
	
	/**
	 * Insert a row into the MB_METADATA table.  Must be provided a Connection, primary key (ID)
	 * and a BaseAsset which contains all other values needed to populate.
	 * 
	 * The method is applicable when a new asset is being placed in the customer profile, so ALL metadata is inserted. 
	 * 
	 * This method shouldn't be called directly.  addNewAsset wrapper method should be entry point.
	 */
	public void insertMBMetadata(Connection c, int ID, BaseAsset asset) throws SQLException
	{
		// Create the full SQL Insert statement
		StringBuffer insertStmt = new StringBuffer("INSERT INTO MB_METADATA ");
		insertStmt.append("(ID, " + allColumnNames + ") ");
		insertStmt.append("VALUES (?, " + allColumnValues + ") ");
		
		PreparedStatement preparedInsertStatment = null;
		
		try
		{
			preparedInsertStatment = c.prepareStatement(insertStmt.toString());
			// Populate the columns
			preparedInsertStatment.setInt(1, ID);
			int i = 2;
			for (String guid : METADATA_GUIDS_ARRAY)
			{
				preparedInsertStatment.setString(i, asset.getMetaDataValue(guid));
				i++;
			}
			preparedInsertStatment.executeUpdate();
		}
		finally
		{
			if (preparedInsertStatment != null)
			{
				preparedInsertStatment.close();
			}
		}
	}
	
	/**
	 * Wrapper method to add a new asset into the customer profile database tables.  This includes:
	 * 		1. Get next sequence number
	 * 		2. Insert row into MB_ASSETS table
	 * 		3. Insert row into MB_METADATA table
	 * 		4. Commit the transaction
	 * 
	 * Return false in event of exception and sending to the logger, the exception stack trace.
	 * 
	 * Must provide a connection, the asset with metadata populated and the path to where the asset file has been previously placed
	 */
	public boolean addNewAsset(Connection c, BaseAsset ba, String damPath) throws SQLException
	{
		if (!performUpdate)
		{
			return true;			// Updates have been disabled
		}

		try
		{
			// Get the next sequence number to use as the primary key for both tables
			int id = getNextMBAssetsSequence(c);
			
			// Add to MB_Assets table
			insertMBAssets(c, id, ba, damPath);
			
			// Add to MB_Metadata table
			insertMBMetadata(c, id, ba);
			
			c.commit();
			
		} 
		catch (SQLException e)
		{
			c.rollback();
			ExceptionHelper.logExceptionToFile("addNewAsset (CustomerProfileDatabase) " + ba.getAssetName(), e);
			//System.out.println("Exception adding asset to customer profile: " + ExceptionHelper.getStackTraceAsString(e));
			return false;
		}
		
		return true;
	}

	/**
	 * Return a populated BaseAsset for the specified asset name.
	 */
	public BaseAsset getAssetForName(Connection c, String assetName) throws SQLException
	{

		// Build the select statement to retrieve needed data for specified ID
		String stmt = "select ma.id, customer_id, asset_name, asset_id_jostens, color, modification_time, " +
				allColumnNames + " " +
				"from MB_ASSETS ma " +
				"inner join MB_METADATA mm on mm.id = ma.id " +
				"where ma.asset_name = ?";
		
//		String fullStmt = stmt.replace("?", "'" + assetName + "'");
//		System.out.println("-->\n" + fullStmt);

		PreparedStatement preparedStatement = c.prepareStatement(stmt);
		preparedStatement.setString(1, assetName);	// Search by supplied asset name
		
		preparedStatement.execute();
		ResultSet resultSet = preparedStatement.getResultSet();		
		boolean rowFound = resultSet.next();

//		System.out.println("rowFound=" + rowFound);
		if (!rowFound)
		{		// Asset not found by that name
			resultSet.close();
			preparedStatement.close();
			return null;		// A null return indicates no asset found
		}

		BaseAsset returnAsset = new BaseAsset();		// Populate this asset and return to caller
		ResultSetMetaData rsMetadata = resultSet.getMetaData();
		int columnCount = rsMetadata.getColumnCount();

		// Row was found for the supplied ID, populate the return asset with values from the database
		// Get non metadata values from result set
		int ID = resultSet.getInt(1);
		String customerID = resultSet.getString(2);
		String name = resultSet.getString(3);
		String assetIDJostens = resultSet.getString(4);
		String color = resultSet.getString(5);
		Timestamp modificationTime = resultSet.getTimestamp(6);
		// Set values onto the return asset
		returnAsset.setAssetName(name);
		returnAsset.setAssetId("" + ID);
//		returnAsset.addAssetMetadata(MetadataGUIDS.NAME, MetadataHelper.getSingleValueParameter(MetadataGUIDS.NAME, name));
		if (customerID != null)
		{
			returnAsset.addAssetMetadata(MetadataGUIDS.CUSTOMER_ID_ORACLE, MetadataHelper.getSingleValueParameter(MetadataGUIDS.CUSTOMER_ID_ORACLE, customerID));
		}
		returnAsset.addAssetMetadata(MetadataGUIDS.ASSET_ID_JOSTENS, MetadataHelper.getSingleValueParameter(MetadataGUIDS.ASSET_ID_JOSTENS, assetIDJostens));
		returnAsset.addAssetMetadata(MetadataGUIDS.MODIFICATION_TIME, MetadataHelper.getSingleValueParameter(MetadataGUIDS.MODIFICATION_TIME, "" + modificationTime.getTime()));
		StringTokenizer st = new StringTokenizer(color, "_");		// Find the asset ID color scheme
		// Skip over first 3 tokens
		st.nextToken();
		st.nextToken();
		st.nextToken();
		returnAsset.addAssetMetadata(MetadataGUIDS.ASSET_ID_COLOR_SCHEME, MetadataHelper.getSingleValueParameter(MetadataGUIDS.ASSET_ID_COLOR_SCHEME, st.nextToken()));
		
		// Now need to grab the meta data values
		for (int i = 7; i <= columnCount; i++)
		{
			String value = resultSet.getString(i);
//				System.out.println(i + " = " + value);
			if (value == null)
			{
				continue;		// No value for this column
			}
			String guidValue = METADATA_GUIDS_ARRAY[i - 7];
			returnAsset.addAssetMetadata(guidValue, MetadataHelper.getSingleValueParameter(guidValue, value));				
		}

		resultSet.close();
		preparedStatement.close();
		return returnAsset;
	}

	/**
	 * Get passed a start and end date in a Strin format DD/MM/YY.  A query will be issued against MB_ASSETS for Modification_Time
	 * dates between that range.
	 * 
	 * The return will be a sorted List of asset names found for that range
	 */
	public List<String> getAssetNamesDateRange(Connection c, String startDate, String endDate) throws SQLException
	{
		return getAssetNamesTimeDateRange(c, "00:00:00 " + startDate, "00:00:00 " + endDate);
	}
	
	/**
	 * Get passed a start and end date in a String format HH:MM:SS DD/MM/YY.  A query will be issued against MB_ASSETS for Modification_Time
	 * dates between that range.
	 * 
	 * The return will be a sorted List of asset names found for that range
	 */
	public List<String> getAssetNamesTimeDateRange(Connection c, String startDate, String endDate) throws SQLException
	{
		String stmt = "select asset_name from mb_assets " +
				"where modification_time >= TO_DATE('STARTDATE', 'HH24:MI:SS DD/MM/YYYY') " + 
				"and modification_time < TO_DATE('ENDDATE', 'HH24:MI:SS DD/MM/YYYY') " + 
				"order by asset_name";
		
		stmt = stmt.replace("STARTDATE", startDate);
		stmt = stmt.replace("ENDDATE", endDate);

//		System.out.println("STMT=\n" + stmt);
		Statement statement = c.createStatement();
		ResultSet resultSet = statement.executeQuery(stmt);
		List<String> assetNames = new ArrayList<String>();
		while (resultSet.next())
		{
			String name = resultSet.getString(1);
			assetNames.add(name);
		}

		return assetNames;
	}

	/**
	 * Update the modified date on a asset in the MB_ASSETS table.
	 * 
	 * Parameters include the primary key ID and the new time in Epoch time (time in ms).  It's assumed
	 * the epoch date is from MB and has been adjusted to go from UTC time to adjusted CST time.
	 */
	public void updateModifiedTime(Connection c, String ID, String timeInMS) throws SQLException
	{
		if (!performUpdate)
		{
			return;			// Updates have been disabled
		}

		String updateTableSQL = "UPDATE MB_ASSETS SET modification_time = ? WHERE id = ?";
		PreparedStatement preparedStatement = c.prepareStatement(updateTableSQL);
		
		// Setup parameters in prepared statement
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.parseLong(timeInMS));
		Timestamp modified = new Timestamp(cal.getTimeInMillis());
		preparedStatement.setTimestamp(1, modified);
		preparedStatement.setInt(2, Integer.parseInt(ID));

		// execute insert SQL stetement
		preparedStatement.executeUpdate();
		preparedStatement.close();
		
		c.commit();		
	}

	/**
	 * Update the updated date on a asset in the MB_ASSETS table to the current time.
	 * 
	 * Parameters include the primary key ID.
	 */
	public void updateUpdatedTime(Connection c, String ID) throws SQLException
	{
		if (!performUpdate)
		{
			return;			// Updates have been disabled
		}

		String updateTableSQL = "UPDATE MB_ASSETS SET updated = ? WHERE id = ?";
		PreparedStatement preparedStatement = c.prepareStatement(updateTableSQL);
		
		// Setup parameters in prepared statement
		Timestamp modified = new Timestamp(System.currentTimeMillis());
		preparedStatement.setTimestamp(1, modified);
		preparedStatement.setInt(2, Integer.parseInt(ID));

		// execute insert SQL stetement
		preparedStatement.executeUpdate();
		preparedStatement.close();
		
		c.commit();		
	}

	/**
	 * Update the metadata found in the MB_METADATA table.
	 * 
	 * Parameters include the primary key ID, a List of guids being updated (so can determine if a value should be nulled out 
	 * and a BaseAsset to get the metadata value from
	 */
	public void updateMetadata(Connection c, String ID, List<String> guidsToUpdate, BaseAsset ba) throws SQLException
	{
		if (!performUpdate)
		{
			return;			// Updates have been disabled
		}

		StringBuffer sql = new StringBuffer("UPDATE MB_METADATA SET ");
		// Add all parameters to being built string
		int cnt = 1;
		for (String guid : guidsToUpdate)
		{
			sql.append(METADATA.get(guid) + " = ?");
			if (cnt < guidsToUpdate.size())
			{
				sql.append(", ");
			}
			cnt++;
		}
		sql.append(" WHERE id = ?");
		
//		System.out.println("SQL = " + sql.toString());
				
		// Now a prepared statement needs to be built and have parameters set		
		PreparedStatement preparedStatement = c.prepareStatement(sql.toString());
		
		int paramNumber = 1;
		for (String guid : guidsToUpdate)
		{
			String metadata = ba.getMetaDataValue(guid);
//			System.out.println("METADATA=" + metadata);
			if (metadata != null)
			{
//				System.out.println("NORMAL");
				preparedStatement.setString(paramNumber, metadata);
			}
			else
			{
//				System.out.println("null");
				preparedStatement.setNull(paramNumber, Types.VARCHAR);
			}
			paramNumber++;
		}
		// And finally the ID
		preparedStatement.setInt(paramNumber, Integer.parseInt(ID));

		// execute insert SQL statement
		preparedStatement .executeUpdate();
		preparedStatement.close();
		
		c.commit();	
		
		// Finally update the updated column on the MB_ASSETS table
		updateUpdatedTime(c, ID);
	}

	/**
	 * Return the MB_ACTION objects that need processing.  This is defined as having the
	 * COMPLETED column set to a null value.  These IDs will then be used to get the full
	 * record and perform the processing.
	 */
	public List<CustomerProfileAction> getActionsToProcess(Connection c) throws SQLException
	{
		List<CustomerProfileAction> actions = new ArrayList<CustomerProfileAction>();

		String stmt = "select ID, action, asset_name, mb_guid, metadata, customer_id from mb_actions where completed is null order by ID";
		
		Statement statement = c.createStatement();
		ResultSet resultSet = statement.executeQuery(stmt);
		while (resultSet.next())
		{
			CustomerProfileAction cpAction = new CustomerProfileAction();
			String ID = resultSet.getString(1);
			String action = resultSet.getString(2);
			String assetName = resultSet.getString(3);
			String mbGUID = resultSet.getString(4);
			String metadata = resultSet.getString(5);
			String customerID = resultSet.getString(6);
			cpAction.setID(ID);
			cpAction.setAction(action);
			cpAction.setAssetName(assetName);
			cpAction.setMbGUID(mbGUID);
			cpAction.setMetadata(metadata);
			cpAction.setCustomerID(customerID);
			actions.add(cpAction);
		}

		return actions;
	}
	
	/**
	 * Mark a database action row complete being provided the Message which details any errors or OK for success
	 */
	public void markActionComplete(Connection c, CustomerProfileAction action, String message) throws SQLException
	{
		if (!performUpdate)
		{
			return;			// Updates have been disabled
		}

		String msg = message;
		if (message.length() > 200)
		{
			msg = message.substring(0, 200);
		}

		// Create the full SQL Insert statement
		StringBuffer updateStmt = new StringBuffer("Update MB_ACTIONS ");
		updateStmt.append("set completed = ?, message = ? ");
		updateStmt.append("where ID = ?");
		
		PreparedStatement preparedUpdateStatment = null;
		
		try
		{
			preparedUpdateStatment = c.prepareStatement(updateStmt.toString());
			// Populate the parameters
			Timestamp now = new Timestamp(System.currentTimeMillis());
			preparedUpdateStatment.setTimestamp(1, now);
			preparedUpdateStatment.setString(2, msg);
			preparedUpdateStatment.setString(3, action.getID());
			preparedUpdateStatment.executeUpdate();
			
			c.commit();
		}
		finally
		{
			if (preparedUpdateStatment != null)
			{
				preparedUpdateStatment.close();
			}
		}
	}
	
	/**
	 * Remove an asset out of the MB_ASSETS and MB_METADATA tables.  Referential integrity requires only
	 * issueing the delete to the MB_ASSETS table
	 * @return
	 */
	public void removeAsset(Connection c, BaseAsset ba) throws SQLException
	{
		if (!performUpdate)
		{
			return;			// Updates have been disabled
		}

		// Create the full SQL Insert statement
		StringBuffer deleteStmt = new StringBuffer("delete from MB_ASSETS ");
		deleteStmt.append("where asset_name = ?");
		
		PreparedStatement preparedDeleteStatment = null;
		
		try
		{
			preparedDeleteStatment = c.prepareStatement(deleteStmt.toString());
			// Populate the parameters
			preparedDeleteStatment.setString(1, ba.getAssetName());
			preparedDeleteStatment.executeUpdate();

			c.commit();

		}
		finally
		{
			if (preparedDeleteStatment != null)
			{
				preparedDeleteStatment.close();
			}
		}
	}
	
	/**
	 * Return a List of asset names for the specified customer ID as found in the MBASSET table.  The SQL
	 * statement is in the form of a like clause so portions of a customer ID can be provided.  Just remember
	 * that the wild card needs to be included.
	 */
	public List<String> getAssetNamesForCustomerID(Connection c, String customerID) throws SQLException
	{
		StringBuffer stmt = new StringBuffer("select asset_name from mb_assets ");
		stmt.append("where asset_name like '%\\_" + customerID + "\\_%' escape '\\' "); 
		if ("%000".equals(customerID))
		{	// Find not defined customer IDs also
			stmt.append("or customer_id is null "); 			
		}
		stmt.append("order by asset_name");
		
//		System.out.println("STMT=" + stmt.toString());
		
		Statement statement = c.createStatement();
		ResultSet resultSet = statement.executeQuery(stmt.toString());
		List<String> assetNames = new ArrayList<String>();
		while (resultSet.next())
		{
			String name = resultSet.getString(1);
			assetNames.add(name);
		}

		return assetNames;
	}

	
	public String getAllColumnNames()
	{
		return allColumnNames;
	}
	public String getAllColumnValues()
	{
		return allColumnValues;
	}

	public void setPerformUpdate(boolean performUpdate)
	{
		this.performUpdate = performUpdate;
	}
}
