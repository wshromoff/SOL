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
			String packageName = packages.get(0);
			StringBuffer sb = new StringBuffer(packageName + "*");
			for (int i = 1; i < packages.size(); i++)
			{
				packageName = packages.get(i);
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
			StringBuffer sb = new StringBuffer(keywords.get(0) + "*");
			for (int i = 1; i < keywords.size(); i++)
			{
				sb.append(" AND " + keywords.get(i) + "*");
			}

			String queryText = "{!join from=id to=designID}keywords:(" + sb.toString() + ")";
			System.out.println("QT[1-3] " + queryText);

			SolrQuery query = new SolrQuery();
			query.set("q", queryText);
			query.setFilterQueries("contentType:Package");
			
			executeQuery(query);
		}

	}

}
