package com.jostens.dam.brand.web;

import com.jostens.dam.brand.folders.BrandFolderPath;
import com.jostens.dam.brand.jdbc.BrandStatements;
import com.jostens.dam.shared.common.MediaBinConnectionPool;
import com.jostens.dam.shared.control.BaseEnvironment;
import com.jostens.dam.shared.folders.FolderGUIDS;

public class BrandEnvironment extends BaseEnvironment
{

	@Override
	protected boolean performEnvironmentStartup()
	{
		System.out.println("   - Environment Selections - ");
		System.out.println("Development: " + isDevelopment());
		System.out.println("Integration: " + isIntegration());
		System.out.println("Production: " + isProduction());
		System.out.println("\n");

		FolderGUIDS.setEnvironmentValues(isDevelopment(), isIntegration(), isProduction());
//		FolderGUIDS.setEnvironmentValues(false, false, true);

    	// Initialize project-specific FolderPath object based on environment
    	BrandFolderPath.initialize(isDevelopment(), isIntegration(), isProduction());

    	// Initialize any BrandStatements
		BrandStatements statements = new BrandStatements();
		statements.initializeStatements();

		return true;
	}

	@Override
	protected boolean performEnvironmentStop()
	{
		// Disconnect all web service connections
		MediaBinConnectionPool.disconnectAllConnections();
		return true;
	}

}
