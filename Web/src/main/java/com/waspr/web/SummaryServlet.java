package com.waspr.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.jemm2.solr.web.DocumentCounts;
import com.jostens.jemm2.web.HTMLHelper;

/**
 * Servlet implementation for summary information
 */
@WebServlet("/summary")
public class SummaryServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public Summary() {
//        super();
//        // TODO Auto-generated constructor stub
//    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("Inside Summary Servlet");
		
		// Get the HTML template and replace marker location with resource HTML
		String dashboardHTML = HTMLHelper.getTemplateHTML("/Dashboard.html");
//		System.out.println("HTML=" + manageResources);
		DocumentCounts helper = new DocumentCounts();
		helper.generateCounts();
//		response.getWriter().append("SUMMARY " + helper.getDesignCount());
		
		dashboardHTML = dashboardHTML.replace("[DESIGN_COUNT]", helper.getDesignCount() + "");
		dashboardHTML = dashboardHTML.replace("[DESIGN_DELTA]", helper.getDeltaDesignCount() + "");
		dashboardHTML = dashboardHTML.replace("[PART_COUNT]", helper.getPartCount() + "");
		dashboardHTML = dashboardHTML.replace("[PART_DELTA]", helper.getDeltaPartCount() + "");
		dashboardHTML = dashboardHTML.replace("[CUSTOMER_COUNT]", helper.getCustomerCount() + "");
		dashboardHTML = dashboardHTML.replace("[CUSTOMER_DELTA]", helper.getDeltaCustomerCount() + "");
		
		long delta = helper.getDeltaCount();
		response.getWriter().append(delta + "," + dashboardHTML);

	}

//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		doGet(request, response);
//	}

}
