package com.waspr.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.jdbc.helpers.PartDatabaseHelper;
import com.jostens.jemm2.pojo.Part;
import com.jostens.jemm2.web.HTMLHelper;

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
		Jemm2Statements statements = new Jemm2Statements();
		statements.initializeStatements();
		Connection c = ConnectionHelper.getJEMM2Connection();
				
		String id = request.getParameter("ID");
		int databaseID = new Integer(id.substring(3)).intValue();
		System.out.println("In Details Servlet " + id);

		String responseHTML = null;
		if (id.startsWith("PR_"))
		{
			// Displaying details about a part
			// Get the HTML template and replace marker location with resource HTML
			responseHTML = HTMLHelper.getTemplateHTML("/PartDetails.html");

			// Get Part
			PartDatabaseHelper helper = new PartDatabaseHelper();
			Part part = new Part();
			part.setID(databaseID);
			
			// Populate design2 from database
			try
			{
				helper.getPart(c, part);
				responseHTML = responseHTML.replace("[ID]", databaseID + "");
				responseHTML = responseHTML.replace("[PART_NAME]", part.getName());
				responseHTML = responseHTML.replace("[KEYWORDS]", part.getDesign().getKeywordsForDisplay());
				responseHTML = responseHTML.replace("[ASSET_TYPE]", part.getDesign().getBrandAssetType());
				responseHTML = responseHTML.replace("[INTENT]", part.getDesign().getCreativeIntentDesign());
				responseHTML = responseHTML.replace("[AFFILIATION]", part.getDesign().getAffiliationByDepiction());
				responseHTML = responseHTML.replace("[FUNC_INTENT]", part.getDesign().getFunctionalIntent());
				responseHTML = responseHTML.replace("[USABILITY]", part.getDesign().getExtentOfUsability());
				responseHTML = responseHTML.replace("[DISP_NAME]", part.getDesign().getDisplayedName());
				responseHTML = responseHTML.replace("[MASCOT]", part.getDesign().getDisplayedMascot());
				responseHTML = responseHTML.replace("[MOTO]", part.getDesign().getDisplayedMotto());
				responseHTML = responseHTML.replace("[INITIALS]", part.getDesign().getDisplayedInitials());
				responseHTML = responseHTML.replace("[YEAR_DATE]", part.getDesign().getDisplayedYearDate());
				responseHTML = responseHTML.replace("[INSCRIPTION]", part.getDesign().getDisplayedInscription());
				responseHTML = responseHTML.replace("[SUBJECT]", part.getDesign().getMainSubject());
				responseHTML = responseHTML.replace("[MULTIPLE]", part.getDesign().getMultipleMainSubject());
				responseHTML = responseHTML.replace("[PORTION]", part.getDesign().getPortionMainSubject());
				responseHTML = responseHTML.replace("[VIEW]", part.getDesign().getViewMainSubject());
			} catch (SQLException e)
			{
				e.printStackTrace();
			}

		}
		
		ConnectionHelper.closeConnection(c);

		// TODO Auto-generated method stub
		response.getWriter().append(responseHTML);
	}


}
