package com.jostens.jemm2.brand.helpers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.jostens.jemm2.JEMM2Constants;

public class LogfileHelperTest
{

//	@Test
	public void readAutomationLog() throws IOException
	{
		LogfileHelper helper = new LogfileHelper();
		List<String> logLines = helper.tailAutomationLog();
		
		for (String line : logLines)
		{
			System.out.println(line);
		}
	}

//	@Test
	public void readExceptionLog() throws IOException
	{
		LogfileHelper helper = new LogfileHelper();
		List<String> logLines = helper.tailExceptionLog();
		
		for (String line : logLines)
		{
			System.out.println(line);
		}
	}

	@Test
	public void readAutomationLogContinuous() throws IOException
	{
		// Continuously read the automation log file with a 5 second sleep between
		// Look for a stop file to halt process
		while (true)
		{
			// Check for stop file
			File file = new File(JEMM2Constants.LOG_PATH + "STOP");
			if (file.exists())
			{
				break;
			}
			LogfileHelper helper = new LogfileHelper();
			List<String> logLines = helper.tailExceptionLog();

			System.out.println("  ------------ ");
			for (String line : logLines)
			{
				System.out.println(line);
			}
			
			try
			{
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
		}
	}

}
