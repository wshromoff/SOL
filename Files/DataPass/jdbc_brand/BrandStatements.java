package com.jostens.dam.brand.jdbc;

import java.util.ArrayList;
import java.util.List;

import com.jostens.dam.shared.jdbc.Statements;

public class BrandStatements extends Statements
{
	// List all the names of SQL statements as static constants which are specific to the Brand project
	public static String FIND_REVISION_CLEANUP = "FIND_REVISION_CLEANUP";

	/**
	 * Method to return List of statement files to load.
	 */
	protected List<String> getStatementFilePaths()
	{
		List<String> filePaths = new ArrayList<String>();
		filePaths.add("/com/jostens/dam/brand/jdbc/BrandStatements.sql");
		return filePaths;
	}
}
