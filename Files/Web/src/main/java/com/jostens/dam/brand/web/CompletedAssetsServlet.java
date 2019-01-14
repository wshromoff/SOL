package com.jostens.dam.brand.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.dam.brand.workdisplay.CompletedAssets;

/**
 * Servlet implementation class ProcessedAssetsServlet
 */
@WebServlet("/CompletedAssetsServlet")
public class CompletedAssetsServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

//		System.out.println("INSIDE PROCESSED ASSET SERVLET ");

		String currentAssetHTML = HTMLHelper.getCachedHTML(CompletedAssets.COMPLETED_ASSETS_HTML);
		response.getWriter().write(currentAssetHTML);

	}
}
