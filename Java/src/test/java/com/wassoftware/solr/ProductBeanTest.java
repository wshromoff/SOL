package com.wassoftware.solr;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
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

//	@Test
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
//		solr.addBean(sb1);
//		solr.addBean( sb2);
		solr.addBean(prd1);
		solr.commit();

		
//		DocumentObjectBinder binder = new DocumentObjectBinder();
//		binder.
		SolrQuery query = new SolrQuery("*:*");
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
		query = new SolrQuery("*:*");
//		query.set("q", "cost:23");
		query.addFilterQuery("cost:78");
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

//	@Test
	public void testLoad() throws SolrServerException, IOException
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
//		prd1.addSubBean(sb1);
//		prd1.addSubBean(sb2);
//		solr.addBean(sb1);
//		solr.addBean( sb2);
//		solr.addBean(prd1);
//		solr.commit();

		
		DocumentObjectBinder binder = new DocumentObjectBinder();
		SolrInputDocument doc1 = binder.toSolrInputDocument(prd1);
		SolrInputDocument sub1 = binder.toSolrInputDocument(sb1);
		SolrInputDocument sub2 = binder.toSolrInputDocument(sb2);
		
		doc1.addChildDocument(sub1);
		doc1.addChildDocument(sub2);
		
		solr.add(doc1);
		solr.commit();

		ProductBean prd2 = new ProductBean("2", "product 2", "price2");
		prd1.setPrice("price2");
		prd1.addKeyword("word4");
		prd1.addKeyword("word5");

		SolrInputDocument doc2 = binder.toSolrInputDocument(prd2);
		doc2.addChildDocument(sub2);
		
		solr.add(doc2);
		solr.commit();

		solr.close();
		
	}
	
	@Test
	public void testQuery() throws SolrServerException, IOException
	{
		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();

	    SolrQuery q = new SolrQuery( "*:*" );
 //       q.addFilterQuery( "contentType:product" );
 //       q.addFilterQuery( "contentType:subBean" );
	    q.addFilterQuery( "{!parent which=\"contentType:product\"}" + "(contentType:subBean AND cost:23)");
 
 //       q.addField( "*" );

		QueryResponse response = solr.query(q);
		
		SolrDocumentList docList = response.getResults();
		assertEquals(docList.getNumFound(), 1);

//		DocumentObjectBinder binder = new DocumentObjectBinder();
//		ProductBean b1 = binder.getBean(ProductBean.class, docList.get(0));
//		
//		System.out.println("ID=" + b1.getId());
//		System.out.println("Name=" + b1.getName());
//		System.out.println("Price=" + b1.getPrice());
//		System.out.println("Keywords=" + b1.getKeywords());

        List<SolrDocument> childDocs = docList.get(0).getChildDocuments();
		System.out.println("Children Count=" + childDocs.size());
        

		solr.close();

	}
}
