package com.jostens.jemm2.jdbc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper class for the Statements.sql file.
 * 
 * This class provides a static cache of used SQL statements.  This was implemented to be able to store
 * SQL statement in a more friendly format in a text file compared to in-lining them in code separated by several quoted lines.
 * 
 * The code using these statements needs to remain coordinated because there are codes in the SQL where
 * values may need to be replaced prior to executing.  Also, for use in prepared statements, a '?' could be found.
 * 
 * Class is abstract because implementers need to provide the path to statement SQL files.
 */
public abstract class Statements
{
	// Cache of statements
	private static Map<String, String> statementCache = new HashMap<String, String>();

	/**
	 * Prior to requesting any statements, they must be initialized by calling this method.
	 * 
	 * @return true if initial load was completed without issues or they had previously been loaded.
	 */
	public boolean initializeStatements()
	{
		if (!Statements.statementCache.isEmpty())
		{
			return true;		// Already initialized - still considered a success
		}
		
		// Call abstract method to get List of statement file paths to load
		List<String> statementFilePaths = getStatementFilePaths();
		// Also include the folder path for SharedStatements
		Jemm2Statements statements = new Jemm2Statements();
		statementFilePaths.addAll(statements.getStatementFilePaths());

		for (String filePath : statementFilePaths)
		{
			boolean success = readStatements(filePath);
			if (!success)
			{
				// Statements failed, so clear them and output an error
				Statements.statementCache.clear();
				System.out.println("Could not read the statement file: " + filePath);
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Method to return List of statement files to load.  Extender's of this object need
	 * to specify valid paths.
	 */
	protected abstract List<String> getStatementFilePaths();
	
	public static String getStatement(String statementName)
	{
		if (Statements.statementCache.isEmpty())
		{
			throw new RuntimeException("SQL statements have not been initialized");
		}
		String foundStatement = Statements.statementCache.get(statementName);
		return foundStatement;
	}
	
	protected boolean readStatements(String statementsPackagePath)
	{
		try
		{
			InputStream istream = Statements.class.getResourceAsStream(statementsPackagePath);
			DataInputStream in = new DataInputStream(istream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			StringBuffer statement = new StringBuffer();
			String statementName = null;
			boolean inStatement = false;
			while ((strLine = br.readLine()) != null)
			{
				if (strLine == null || strLine.length() == 0 || strLine.startsWith("-"))
				{	// Ignore empty lines (length 0) and comment lines (start with dash)
					continue;
				}
//				System.out.println("> " + strLine);
				
				if (!inStatement)
				{	// Not in a statement, so this should be the statement name
					statementName = strLine;
					inStatement = true;
					continue;
				}
				
				if (inStatement && strLine.startsWith("{"))
				{	// Start a new statement
					statement.setLength(0);
					continue;
				}
				if (inStatement && strLine.startsWith("}"))
				{	// End of a statement
	//				System.out.println(statementName + ":" + statement.toString());
					Statements.statementCache.put(statementName, statement.toString());
					inStatement = false;
					statementName = null;
					continue;
				}
				if (inStatement)
				{	// Part of the statement
					statement.append(strLine.trim() + " ");
					continue;
				}
				
				System.out.println("INVALID format on line: " + strLine);
			}
			
			br.close();
			in.close();
			istream.close();
		} catch (Exception e)
		{
			Statements.statementCache.clear();
//			ExceptionHelper.logExceptionToFile("readStatements", e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
