package com.jostens.jemm2.solr.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;

import com.jostens.jemm2.jdbc.Jemm2Statements;

/**
 * Perform Customer specific search.
 * 
 */
public class CustomerDocumentSearch extends QueryBase
{

	@Override
	protected void performDocumentQuery() throws Exception
	{
		if (!parts.isEmpty())
		{
			for (String part : parts)
			{
				List<String> customerIDs = getCustomersUsingPart(part);
				customers.addAll(customerIDs);				
			}
		}
		if (!packages.isEmpty())
		{
			// Examine the packages to determine if they can lead to a customer number
			// which is added to the customers List for by customer number search
			for (String aPackage : packages)
			{
				String[] segments = aPackage.split("_");	// Split name into segments
				if (segments.length < 2)
				{
					continue;	// Too small of string so ignore
				}
				String customerID = segments[1];	// Look at 2nd item in name to determine if all numeric and if so it's a customer ID
				String regex = "\\d+";
				if (customerID.matches(regex))
				{
					// Found a customer number add this to customers List
					customers.add(customerID);
					continue;
				}
				// Now there is a package (i.e. no customer in name) - Look at part in name and find all customers having a customer package with that name
				String partID = segments[0];
				List<String> customerIDs = getCustomersUsingPart(partID);
				customers.addAll(customerIDs);
			}
		}
		if (!customers.isEmpty())
		{
			// Perform customerID query
			String customerID = customers.get(0);
			StringBuffer sb = new StringBuffer(customerID);
			for (int i = 1; i < customers.size(); i++)
			{
				customerID = customers.get(i);
				sb.append(" OR " + customerID);
			}
			String queryText = "customerID:(" + sb.toString() + ")";
			System.out.println("QT[2-1] " + queryText);
			SolrQuery query = new SolrQuery();
			query.set("q", queryText);
			
			executeQuery(query);
		}

	}

	private List<String> getCustomersUsingPart(String partName) throws SQLException
	{
		List<String> customerNumbers = new ArrayList<String>();
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_CUSTOMER_FROM_PART);
		selectStmt = selectStmt.replace("[PART_NAME]", partName);
		
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(selectStmt);
		boolean rowFound = rs.next();
		if (rowFound)
		{
			String customerID = rs.getString(1);
			customerNumbers.add(customerID);
		}
		return customerNumbers;

	}
}
