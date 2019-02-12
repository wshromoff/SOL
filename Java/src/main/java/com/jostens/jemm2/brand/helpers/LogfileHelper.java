package com.jostens.jemm2.brand.helpers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.input.ReversedLinesFileReader;

import com.jostens.jemm2.JEMM2Constants;

/**
 * Helps with the display of the end portion of log files
 * used by the automations.
 */
public class LogfileHelper
{
	private int MAX_LINES = 30;
	private String AUTOMATION_LOG = "AutomationLog.txt";
	private String EXCEPTION_LOG = "ExceptionLog.txt";
	
	/**
	 * Return up MAX_LINES of text from the automation log from the bottom of the file upwards.
	 * Effectively trying to do a tail of the file.
	 * @throws IOException 
	 */
	public List<String> tailAutomationLog() throws IOException
	{
		List<String> lines = readFromFile(JEMM2Constants.LOG_PATH + AUTOMATION_LOG);
		
		return lines;
	}

	/**
	 * Return up MAX_LINES of text from the exception log from the bottom of the file upwards.
	 * Effectively trying to do a tail of the file.
	 * @throws IOException 
	 */
	public List<String> tailExceptionLog() throws IOException
	{
		List<String> lines = readFromFile(JEMM2Constants.LOG_PATH + EXCEPTION_LOG);
		
		return lines;
	}

	/**
	 * Read the file
	 * @throws IOException 
	 */
	private List<String> readFromFile(String filePath) throws IOException
	{
		List<String> fileLines = new ArrayList<String>();

		ReversedLinesFileReader fr = new ReversedLinesFileReader(new File(filePath), Charset.defaultCharset());
		String current;
        while ((current = fr.readLine()) != null && fileLines.size() < MAX_LINES)
        {
        	fileLines.add(0, current);		// Reading from bottom up but each line found is placed at top so acts like a tail
        }
        fr.close();

		return fileLines;
	}
	
}
