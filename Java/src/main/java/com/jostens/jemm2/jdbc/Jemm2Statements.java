package com.jostens.jemm2.jdbc;

import java.util.ArrayList;
import java.util.List;

public class Jemm2Statements extends Statements
{
	// List all the names of SQL statements as static constants
	public static String DESIGN_COUNT = "DESIGN_COUNT";
	public static String DELETED_ITEMS_GUID = "DELETED_ITEMS_GUID";
	public static String FIND_COMPLETE_FOLDER_TREE = "FIND_COMPLETE_FOLDER_TREE";
	public static String FIND_ALL_ASSETS_FOR_FOLDER_TREE = "FIND_ALL_ASSETS_FOR_FOLDER_TREE";
	public static String FIND_CONTAINERTREE_COUNT = "FIND_CONTAINERTREE_COUNT";
	public static String FIND_PARENT_FOLDER_GUID = "FIND_PARENT_FOLDER_GUID";
	public static String FIND_ASSETS_MISSING_METADATA_FIELD = "FIND_ASSETS_MISSING_METADATA_FIELD";
	public static String FIND_ASSETS_MISSING_METADATA_FIELD_ORIGINAL = "FIND_ASSETS_MISSING_METADATA_FIELD_ORIGINAL";	
	public static String FIND_PARENT_FOLDERS = "FIND_PARENT_FOLDERS";
	
	/**
	 * Method to return List of statement files to load.
	 */
	protected List<String> getStatementFilePaths()
	{
		List<String> filePaths = new ArrayList<String>();
		filePaths.add("/com/jostens/jemm2/jdbc/resources/DesignStatements.sql");
		return filePaths;
	}

}
