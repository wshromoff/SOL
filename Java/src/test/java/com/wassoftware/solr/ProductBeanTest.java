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

//	@Test
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

//	@Test
	public void testKeywords() throws SolrServerException, IOException
	{
		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();
		
		ProductBean prd1 = new ProductBean("1", "product 1", "price1");
		prd1.setPrice("price3");
		prd1.addKeyword("word1");
		prd1.addKeyword("word2");
		prd1.addKeyword("word3");
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
			System.out.println("Keywords=" + doc.getFieldValues("keywords"));
		}
		
		// Now try to do a query by a keyword and see if the prd1 bean is returned
		query.set("q", "keywords:word3");
		response = solr.query(query);
		
		docList = response.getResults();
		assertEquals(docList.getNumFound(), 1);
		 
		for (SolrDocument doc : docList)
		{
			System.out.println("ID=" + doc.getFieldValue("id"));
			System.out.println("Name=" + doc.getFieldValue("name"));
			System.out.println("Price=" + doc.getFieldValue("price"));
			System.out.println("Keywords=" + doc.getFieldValues("keywords"));
		}

		solr.deleteById("1");
		solr.commit();
		response = solr.query(query);
		docList = response.getResults();
		assertEquals(docList.getNumFound(), 0);
	}

	@Test
	public void testSubBean() throws SolrServerException, IOException
	{
		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();
		
		ProductBean prd1 = new ProductBean("1", "product 1", "price1");
		prd1.setPrice("price3");
		prd1.addKeyword("word1");
		prd1.addKeyword("word2");
		prd1.addKeyword("word3");
		SubBean sb1 = new SubBean("8", "sb1", 23);
		SubBean sb2 = new SubBean("9", "sb2", 78);
		prd1.addSubBean(sb1);
		prd1.addSubBean(sb2);
		solr.addBean(sb1);
		solr.addBean( sb2);
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
			System.out.println("Keywords=" + doc.getFieldValues("keywords"));
			System.out.println("Sub Bean count=" + doc.getFieldValues("subBeans").size());
		}
		
		// Now try to do a query by a keyword and see if the prd1 bean is returned
		query.set("q", "cost:23");
		response = solr.query(query);
		
		docList = response.getResults();
		assertEquals(docList.getNumFound(), 1);
		 
		for (SolrDocument doc : docList)
		{
			System.out.println("ID=" + doc.getFieldValue("id"));
			System.out.println("Name=" + doc.getFieldValue("name"));
			System.out.println("Price=" + doc.getFieldValue("price"));
			System.out.println("Keywords=" + doc.getFieldValues("keywords"));
			System.out.println("Sub Bean count=" + doc.getFieldValues("subBeans").size());
		}

		solr.deleteById("1");
		solr.commit();
		response = solr.query(query);
		docList = response.getResults();
		assertEquals(docList.getNumFound(), 0);
	}

}
