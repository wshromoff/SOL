package com.jostens.jemm2.jdbc;

import java.util.ArrayList;
import java.util.List;

public class Jemm2Statements extends Statements
{
	// List all the names of SQL statements as static constants
	public static String DESIGN_COUNT = "DESIGN_COUNT";
	public static String INSERT_DESIGN_KEYWORD = "INSERT_DESIGN_KEYWORD";
	public static String GET_KEYWORD_ID = "GET_KEYWORD_ID";
	public static String INSERT_KEYWORD = "INSERT_KEYWORD";
	public static String DELETE_DESIGN_KEYWORDS = "DELETE_DESIGN_KEYWORDS";
	public static String GET_DESIGN_ID = "GET_DESIGN_ID";
	public static String DELETE_DESIGN = "DELETE_DESIGN";
	public static String INSERT_DESIGN = "INSERT_DESIGN";	
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
