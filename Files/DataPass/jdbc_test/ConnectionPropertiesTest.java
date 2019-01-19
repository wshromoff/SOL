package com.jostens.dam.shared.jdbc;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.dam.shared.testing.SharedBaseTest;

public class ConnectionPropertiesTest extends SharedBaseTest
{
	// If the desire it send an email for the production property file, un-comment all the following lines
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
//		setProductionEnvironment(true);
//		setIntegrationEnvironment(true);
		connectToMB();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		disconnectFromMB();
	}


	@Test
	public void testConnectionProperties()
	{
//		FolderPath.setUnitTest(true);		// Set that unit test paths should be used to find property file
		
		ConnectionProperties properties = new ConnectionProperties();
		System.out.println("props=" + properties.toString());
	}

}
