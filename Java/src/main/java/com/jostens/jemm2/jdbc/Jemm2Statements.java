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

	// List all the names of Part SQL statements as static constants
	public static String DELETE_PART = "DELETE_PART";
	public static String GET_PART_ID = "GET_PART_ID";
	public static String INSERT_PART = "INSERT_PART";
	public static String GET_PART = "GET_PART";
	
	/**
	 * Method to return List of statement files to load.
	 */
	protected List<String> getStatementFilePaths()
	{
		List<String> filePaths = new ArrayList<String>();
		filePaths.add("/com/jostens/jemm2/jdbc/resources/DesignStatements.sql");
		filePaths.add("/com/jostens/jemm2/jdbc/resources/PartStatements.sql");
		return filePaths;
	}

}
