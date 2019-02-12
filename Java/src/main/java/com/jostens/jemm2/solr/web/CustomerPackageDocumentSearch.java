package com.jostens.jemm2.solr.web;

import org.apache.solr.client.solrj.SolrQuery;

/**
 * Perform Customer Package specific search.
 * 
 */
public class CustomerPackageDocumentSearch extends QueryBase
{

	@Override
	protected void performDocumentQuery() throws Exception
	{
		if (!parts.isEmpty())
		{
			// Part name searches just get added to those of a package
			packages.addAll(parts);
		}
		if (!customers.isEmpty())
		{
			for (int i = 0; i < customers.size(); i++)
			{
				String customer = customers.get(i);
				packages.add("*_" + customer + "_");
			}
		}

		if (!packages.isEmpty())
		{
			// Perform packages query off the name by taking whatever was provided and adding a wild card
			String packageName = addCustomerNumber(packages.get(0));
			StringBuffer sb = new StringBuffer(packageName + "*");
			for (int i = 1; i < packages.size(); i++)
			{
				packageName = addCustomerNumber(packages.get(i));
				sb.append(" " + packageName + "*");
			}
			String queryText = "name:(" + sb.toString() + ")";
			System.out.println("QT[4-1] " + queryText);
			SolrQuery query = new SolrQuery();
			query.set("q", queryText);
			query.setFilterQueries("contentType:CustomerPackage");
			
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
				System.out.println("QT[4-2] " + queryText);
	
				SolrQuery query = new SolrQuery();
				query.set("q", queryText);
				query.setFilterQueries("contentType:CustomerPackage");
				
				executeQuery(query);
			}
		}

	}

	/*
	 * Add customer number if supplied with a package name
	 */
	private String addCustomerNumber(String packageName)
	{
		String[] segments = packageName.split("_");	// Split name into segments
		if (segments.length < 2)
		{
			return packageName;	// Less than 2 segments couldn't need a client number
		}
		String customerID = segments[1];	// Look at 2nd item in name to determine if all numeric and if so it's a customer ID
		String regex = "\\d+";
		if (!customerID.matches(regex))
		{
			// Missing a customer number add a wild card as part of the name
			int i = packageName.indexOf("_");
			String partName = packageName.substring(0, i);
			return packageName.replace(partName, partName + "_*");
		}
		return packageName;
	}

}
