package com.jostens.dam.brand.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.jostens.dam.shared.control.BaseStartup;

public class BrandListener implements ServletContextListener
{
	
	// The application startup object used as this environment starts and stops
	private BaseStartup startup = null;

	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
		System.out.println("\n  *** WEB APPLICATION IS STOPPING ***   \n");
		
		startup.contextBeingDestroyed();
		
		System.out.println("\n  *** WEB APPLICATION IS FULLY STOPPED ***   \n");

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0)
	{
		System.out.println("\n  *** WEB APPLICATION IS STARTING ***   \n");

		// Select the environment object responsible for starting/stopping properly
		startup = new BrandStartup();
		startup.start();
	}

}
