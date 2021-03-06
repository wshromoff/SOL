package com.jostens.jemm2.jdbc;

import java.util.ArrayList;
import java.util.List;

public class Jemm2Statements extends Statements
{
	// List all the names of Design SQL statements as static constants
	public static String DESIGN_COUNT = "DESIGN_COUNT";
	public static String INSERT_DESIGN_KEYWORD = "INSERT_DESIGN_KEYWORD";
	public static String GET_KEYWORD_ID = "GET_KEYWORD_ID";
	public static String INSERT_KEYWORD = "INSERT_KEYWORD";
	public static String DELETE_DESIGN_KEYWORDS = "DELETE_DESIGN_KEYWORDS";
	public static String GET_DESIGN_ID = "GET_DESIGN_ID";
	public static String DELETE_DESIGN = "DELETE_DESIGN";
	public static String INSERT_DESIGN = "INSERT_DESIGN";	
	public static String GET_DESIGN = "GET_DESIGN";
	public static String GET_DESIGN_KEYWORDS = "GET_DESIGN_KEYWORDS";
	public static String GET_ALL_DESIGNIDS = "GET_ALL_DESIGNIDS";

	// List all the names of Part SQL statements as static constants
	public static String DELETE_PART = "DELETE_PART";
	public static String GET_PART_ID = "GET_PART_ID";
	public static String INSERT_PART = "INSERT_PART";
	public static String GET_PART = "GET_PART";
	public static String GET_ALL_PARTIDS = "GET_ALL_PARTIDS";

	// List all the names of CUSTOMER SQL statements as static constants
	public static String GET_CUSTOMER_ID = "GET_CUSTOMER_ID";
	public static String DELETE_CUSTOMER = "DELETE_CUSTOMER";
	public static String INSERT_CUSTOMER = "INSERT_CUSTOMER";
	public static String GET_CUSTOMER = "GET_CUSTOMER";
	public static String GET_ALL_CUSTOMERIDS = "GET_ALL_CUSTOMERIDS";

	// List all the names of PACKAGE SQL statements as static constants
	public static String GET_PACKAGE_ID_IDENTIFIER = "GET_PACKAGE_ID_IDENTIFIER";
	public static String GET_PACKAGE_ID_NAME = "GET_PACKAGE_ID_NAME";
	public static String DELETE_PACKAGE = "DELETE_PACKAGE";
	public static String INSERT_PACKAGE = "INSERT_PACKAGE";
	public static String GET_PACKAGE = "GET_PACKAGE";
	public static String DELETE_ASSET_FOR_PACKAGE = "DELETE_ASSET_FOR_PACKAGE";
	public static String INSERT_ASSET_FOR_PACKAGE = "INSERT_ASSET_FOR_PACKAGE";
	public static String GET_ASSETS_FOR_PACKAGE = "GET_ASSETS_FOR_PACKAGE";
	public static String DELETE_CUSTOMER_PACKAGE = "DELETE_CUSTOMER_PACKAGE";
	public static String INSERT_CUSTOMER_PACKAGE = "INSERT_CUSTOMER_PACKAGE";
	public static String GET_CUSTOMER_PACKAGE_ID = "GET_CUSTOMER_PACKAGE_ID";
	public static String GET_CUSTOMER_PACKAGE = "GET_CUSTOMER_PACKAGE";
	public static String GET_ALL_PACKAGEIDS = "GET_ALL_PACKAGEIDS";
	public static String GET_ALL_CUSTOMER_PACKAGEIDS = "GET_ALL_CUSTOMER_PACKAGEIDS";

	// List all the names of Image statements SQL statements as static constants
	public static String GET_PART_FOLDER_PATH = "GET_PART_FOLDER_PATH";
	public static String GET_CUSTOMER_FOLDER_PATH = "GET_CUSTOMER_FOLDER_PATH";
	public static String GET_PACKAGE_FOLDER_PATH = "GET_PACKAGE_FOLDER_PATH";
	public static String GET_CUSTOMER_PACKAGE_FOLDER_PATH = "GET_CUSTOMER_PACKAGE_FOLDER_PATH";

	// List all the names of Search statements SQL as static constants
	public static String GET_CUSTOMER_FROM_PART = "GET_CUSTOMER_FROM_PART";

	// List all statements related to bookmarks
	public static String GET_ACTION_COUNT = "GET_ACTION_COUNT";
	public static String INSERT_ACTION = "INSERT_ACTION";
	public static String IS_DOCUMENT_ACTION = "IS_DOCUMENT_ACTION";
	public static String DELETE_ACTION = "DELETE_ACTION";
	public static String GET_ALL_ACTIONS = "GET_ALL_ACTIONS";

	// List all statements related to automations
	public static String GET_INCOMING_PACKAGE_ID_NAME = "GET_INCOMING_PACKAGE_ID_NAME";
	public static String INSERT_INCOMING_PACKAGE = "INSERT_INCOMING_PACKAGE";
	public static String DELETE_INCOMING_PACKAGE = "DELETE_INCOMING_PACKAGE";
	public static String GET_CURRENT_REVISION = "GET_CURRENT_REVISION";

	/**
	 * Method to return List of statement files to load.
	 */
	protected List<String> getStatementFilePaths()
	{
		List<String> filePaths = new ArrayList<String>();
		filePaths.add("/com/jostens/jemm2/jdbc/resources/DesignStatements.sql");
		filePaths.add("/com/jostens/jemm2/jdbc/resources/PartStatements.sql");
		filePaths.add("/com/jostens/jemm2/jdbc/resources/CustomerStatements.sql");
		filePaths.add("/com/jostens/jemm2/jdbc/resources/PackageStatements.sql");
		filePaths.add("/com/jostens/jemm2/jdbc/resources/ImageStatements.sql");
		filePaths.add("/com/jostens/jemm2/jdbc/resources/SearchStatements.sql");
		filePaths.add("/com/jostens/jemm2/jdbc/resources/ActionStatements.sql");
		filePaths.add("/com/jostens/jemm2/jdbc/resources/AutomationStatements.sql");
		return filePaths;
	}

}
