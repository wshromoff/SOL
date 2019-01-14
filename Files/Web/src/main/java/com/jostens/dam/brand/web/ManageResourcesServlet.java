package com.jostens.dam.brand.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.dam.shared.common.MediaBinConnectionPool;
import com.jostens.dam.shared.resource.ResourceState;

/**
 * Servlet object for supporting manage resources requests.
 */
@WebServlet("/ManageResources")
public class ManageResourcesServlet extends HttpServlet
{

	/**
	 * Return status automation's page
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		PrintWriter out = response.getWriter();
		printHTMLResponse(out);
		out.flush();
		out.close();
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// Look for all possible values from the manage resources form and update those accordingly and
		// then pass on to set any changes on the automation's.
		
		// Get the current ResourceStatus List of all known resources
		List<ResourceState> resourcesStatus = getApplicationResources();
		// Now for each defined resources, find the appropriate parameters
		for (ResourceState resource : resourcesStatus)
		{
			String groupName = resource.getGroup();
			int index = resource.getIndex();
			String startCheckbox = request.getParameter(groupName + "_" + index + "_Start");
			String stopCheckbox = request.getParameter(groupName + "_" + index + "_Stop");
			String forceCheckbox = request.getParameter(groupName + "_" + index + "_Force");
			if ("on".equals(startCheckbox))
			{
				resource.setStarted(true);
			}
			if ("on".equals(stopCheckbox))
			{
				resource.setStopped(true);
			}
			if ("on".equals(forceCheckbox))
			{
				resource.setForceStopped(true);
			}
			else
			{
				resource.setForceStopped(false);
			}
		}
		
		// Now the various resources need to be adjusted with the potential updates
		// Connection pool is always active, so create a resource entry for it
		MediaBinConnectionPool connectionPool = new MediaBinConnectionPool();
		connectionPool.setResourceState(resourcesStatus);

		// Do the same for automation's if started
		if (BrandStartup.getAutomationManager() != null)
		{	// Automation's have been started - Get all their resource status information
			BrandStartup.getAutomationManager().setResourceState(resourcesStatus);
		}
		
		// Do a sleep of 10 seconds to wait for resources to stop
		try
		{
			Thread.sleep(10000);
		} catch (InterruptedException e) {}
		
		PrintWriter out = response.getWriter();
		printHTMLResponse(out);
		out.flush();
		out.close();

	}
	
	/**
	 * With provided with the PrintWriter for the response, build the full manage resource HTML and print 
	 * it to the PrintWriter
	 * 
	 * The caller must flush and close PrintWriter
	 */
	private void printHTMLResponse(PrintWriter out)
	{
		// String Buffer to hold resource HTML
		StringBuffer sb = new StringBuffer();
		
		// Build a ResourceStatus List of all known resources
		List<ResourceState> resourcesStatus = getApplicationResources();
		
		// Now for each defined resources, build a manage HTML line
		for (ResourceState resource : resourcesStatus)
		{
			String resourceHTML = getHTMLforApplicationResource(resource);
			sb.append(resourceHTML);
		}

		// Get the HTML template and replace marker location with resource HTML
		String manageResources = HTMLHelper.getTemplateHTML("/com/jostens/dam/brand/web/ManageResources.html");
		manageResources = manageResources.replace("[RESOURCE_HTML]", sb.toString());
		out.println(manageResources);
	}

	/**
	 * Return a List of ResourceState objects
	 */
	public List<ResourceState> getApplicationResources()
	{
		// Build a ResourceStatus List of all known resources
		List<ResourceState> resourcesStatus = new ArrayList<ResourceState>();
		// Connection pool is always active, so create a resource entry for it
		MediaBinConnectionPool connectionPool = new MediaBinConnectionPool();
		resourcesStatus.addAll(connectionPool.getResourceStates());

		if (BrandStartup.getAutomationManager() != null)
		{	// Automation's have been started - Get all their resource status information
			resourcesStatus.addAll(BrandStartup.getAutomationManager().getResourceStates());
		}
		
		return resourcesStatus;
	}
	/**
	 * Method to convert ResourceState objects to HTML that displays a row of the manage resources web page
	 */
	public String getHTMLforApplicationResource(ResourceState resource)
	{
		String groupName = resource.getGroup();
		int index = resource.getIndex();
		// Determine any values that are checked
		String startChecked = "";
		String stopChecked = "";
		String forceChecked = "";
		String startType = "checkbox";
		String stopType = "checkbox";
		String forceType = "checkbox";
		if (resource.isStarted())
		{
			startChecked = "checked=\"checked\" disabled=\"disabled\"";
			forceType = "hidden";
		}
		if (resource.isStopped())
		{
			stopChecked = "checked=\"checked\" disabled=\"disabled\"";
		}
		if (resource.isForceStopped())
		{
			forceChecked = "checked=\"checked\"";
			startType = "hidden";
			stopType = "hidden";
		}
		
		// Get the resource template and replace marker location with resource HTML
		String template = HTMLHelper.getTemplateHTML("/com/jostens/dam/brand/web/ResourceTemplate.html");
		template = template.replace("[RESOURCE]", resource.getDisplayName());
		template = template.replace("[START_NAME]", groupName + "_" + index + "_Start");
		template = template.replace("[STOP_NAME]", groupName + "_" + index + "_Stop");
		template = template.replace("[FORCE_NAME]", groupName + "_" + index + "_Force");
		template = template.replace("[START_CHECKED]", startChecked);
		template = template.replace("[STOP_CHECKED]", stopChecked);
		template = template.replace("[FORCE_CHECKED]", forceChecked);
		template = template.replace("[START_TYPE]", startType);
		template = template.replace("[STOP_TYPE]", stopType);
		template = template.replace("[FORCE_TYPE]", forceType);

		return template;
	}
}
