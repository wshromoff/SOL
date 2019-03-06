package com.jostens.jemm2;

/**
 * Define constants that help control functionality of application.  Candidates for this file include
 * file system paths and connection information.
 */
public class JEMM2Constants
{
		//			-- Development --
//	// Root path of DAM folder structure
//	public static String ROOT_DAM_PATH = "/Users/wadeshromoff/assets/";	
//	// Should application use hard coded path - Used for development testing
//	public static boolean useHardCoded = true;
//	// Database connection information
//	public static String DB_URL = "jdbc:oracle:thin:@devapps:1521/orcl";
//	public static String DB_USER = "jemm2";
//	public static String DB_PASSWORD = "Jostens01.";
//	// SOLR connection information
//	public static String SOLR_URL = "http://localhost:8983/solr/jemm2";
//	// Location for uploading new assets
//	public static String UPLOAD_PATH = "/Users/wadeshromoff/assets/Upload";
//	// Log file reading of automation log & exception log
//	public static String LOG_PATH = "/Users/wadeshromoff/assets/Upload/";	
//	public static String COMPARE_PATH = "/Users/wadeshromoff/assets/Upload/";	
	
	
	public static String ROOT_DAM_PATH = "//owbsajpvf23/es_tst_mediabin01/Integration/";
	public static boolean useHardCoded = false;	
	public static String DB_URL = "jdbc:oracle:thin:@db_tstt.jostens.com:1565/tstt";
	public static String DB_USER = "xt11781";
	public static String DB_PASSWORD = "Jostens02.";
	public static String SOLR_URL = "http://owbswjddam05:8983/solr/jemm2";	
	public static String UPLOAD_PATH = "//owbsajpvf26/dam_prd_hotfolder01/Java/16_2/WadeTest/Upload";
	public static String LOG_PATH = "//owbsajpvf26/dam_prd_hotfolder01/Java/16_2/WadeTest/logs/";	
	public static String COMPARE_PATH = "//egnsw2kfp1/public/shromow/compare/";	
	
}
