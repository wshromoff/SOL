package com.jostens.jemm2.solr.web;

import org.apache.solr.client.solrj.SolrQuery;

/**
 * Perform Package specific search.
 * 
 * Customers IDs are ignored as part of this search
 */
public class PackageDocumentSearch extends QueryBase
{

	@Override
	protected void performDocumentQuery() throws Exception
	{

		if (!parts.isEmpty())
		{
			// Part name searches just get added to those of a package
			packages.addAll(parts);
		}

		if (!packages.isEmpty())
		{
			// Perform packages query off the name by taking whatever was provided and adding a wild card
			String packageName = removeCustomerNumber(packages.get(0));
			StringBuffer sb = new StringBuffer(packageName + "*");
			for (int i = 1; i < packages.size(); i++)
			{
				packageName = removeCustomerNumber(packages.get(i));
				sb.append(" " + packageName + "*");
			}
			String queryText = "name:(" + sb.toString() + ")";
			System.out.println("QT[3-1] " + queryText);
			SolrQuery query = new SolrQuery();
			query.set("q", queryText);
			query.setFilterQueries("contentType:Package");
			
			executeQuery(query);
		}
		if (!keywords.isEmpty())
		{
			// Under 3 character keywords not included in search
			StringBuffer sb = null;
			for (String keyword : keywords)
			{
				if (keyword.length() < 3)
				{
					continue;
				}
				if (sb == null)
				{
					sb = new StringBuffer(keyword + "*");
					continue;
				}
				sb.append(" AND " + keyword + "*");
			}
			if (sb != null)
			{
				String queryText = "{!join from=id to=designID}keywords:(" + sb.toString() + ")";
				System.out.println("QT[1-3] " + queryText);
	
				SolrQuery query = new SolrQuery();
				query.set("q", queryText);
				query.setFilterQueries("contentType:Package");
				
				executeQuery(query);
			}
		}

	}

	/*
	 * Remove customer number if supplied with a customer package name
	 */
	private String removeCustomerNumber(String packageName)
	{
		String[] segments = packageName.split("_");	// Split name into segments
		if (segments.length < 2)
		{
			return packageName;	// Less than 2 segments couldn't be a client number
		}
		String customerID = segments[1];	// Look at 2nd item in name to determine if all numeric and if so it's a customer ID
		String regex = "\\d+";
		if (customerID.matches(regex))
		{
			// Found a customer number remove this from name and return
			String toRemove = "_" + customerID;
			return packageName.replace(toRemove, "");
		}
		return packageName;
	}
}
