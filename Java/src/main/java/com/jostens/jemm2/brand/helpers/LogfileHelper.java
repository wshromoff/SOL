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
	
	// Which log is selected
	private boolean automationLog = true;
	private boolean exceptionLog = false;

	// This static instance holds the users selected information for controlling log display
	private static LogfileHelper activeLogHelper = null;

	// Return the log file helper with currently set log file
	public static LogfileHelper getActiveLogHelper()
	{
		if (activeLogHelper == null)
		{
			activeLogHelper = new LogfileHelper();
		}
		return activeLogHelper;
	}
	
	/**
	 * Return up MAX_LINES of text from the currently selected log from the bottom of the file upwards.
	 * Effectively trying to do a tail of the file.
	 * @throws IOException 
	 */
	public List<String> tailSelectedLog() throws IOException
	{
		if (automationLog)
		{
			return tailAutomationLog();
		}
		else
		{
			return tailExceptionLog();
		}
	}
	
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

	public boolean isAutomationLog()
	{
		return automationLog;
	}
	public void setAutomationLog(boolean automationLog)
	{
		this.automationLog = automationLog;
		this.exceptionLog = !automationLog;
	}
	public boolean isExceptionLog()
	{
		return exceptionLog;
	}
	public void setExceptionLog(boolean exceptionLog)
	{
		this.exceptionLog = exceptionLog;
		this.automationLog = !exceptionLog;
	}

	
}
