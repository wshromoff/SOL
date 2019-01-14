package com.jostens.dam.brand.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.dam.brand.workdisplay.CurrentAssets;

/**
 * Servlet implementation class CurrentWorkServlet
 */
@WebServlet("/CurrentAssetsServlet")
public class CurrentAssetsServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		CurrentAssets.addServletHit();
		
		String currentAssetHTML = HTMLHelper.getCachedHTML(CurrentAssets.CURRENT_ASSETS_HTML);
		response.getWriter().write(currentAssetHTML);

	}
}
