package com.wassoftware.solr;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

public class ProductBeanTest
{

	@Test
	public void testAddBeanQueryAndDelete() throws SolrServerException, IOException
	{
		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();
		
		ProductBean prd1 = new ProductBean("1", "product 1", "price1");
		solr.addBean(prd1);
		solr.commit();
		
		SolrQuery query = new SolrQuery();
		query.set("q", "id:1");
		QueryResponse response = solr.query(query);
		
		SolrDocumentList docList = response.getResults();
		assertEquals(docList.getNumFound(), 1);
		 
		for (SolrDocument doc : docList)
		{
			System.out.println("ID=" + doc.getFieldValue("id"));
			System.out.println("Name=" + doc.getFieldValue("name"));
			System.out.println("Price=" + doc.getFieldValue("price"));
		}

		solr.deleteById("1");
		solr.commit();
		response = solr.query(query);
		docList = response.getResults();
		assertEquals(docList.getNumFound(), 0);
	}

}
