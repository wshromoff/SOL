package com.waspr.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Display details about an asset
 */
@WebServlet("/AssetDetails")
public class AssetDetailsServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String id = request.getParameter("ID");
		System.out.println("In Details Servlet " + id);
		// TODO Auto-generated method stub
		response.getWriter().append("ID = " + id);
	}


}
