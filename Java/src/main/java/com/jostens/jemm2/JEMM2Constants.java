package com.jostens.jemm2;

/**
 * Define constants that help control functionality of application.  Candidates for this file include
 * file system paths and connection information.
 */
public class JEMM2Constants
{

	// Root path of DAM folder structure
	public static String ROOT_DAM_PATH = "/Users/wadeshromoff/assets/";
	
	// Should application use hard coded path - Used for development testing
	public static boolean useHardCoded = true;
	
	// Database connection information
	public static String DB_URL = "jdbc:oracle:thin:@devapps:1521/orcl";
	public static String DB_USER = "jemm2";
	public static String DB_PASSWORD = "Jostens01.";
	
	// SOLR connection information
	public static String SOLR_URL = "http://localhost:8983/solr/jemm2";
}
