package com.jostens.jemm2.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import com.jostens.jemm2.jdbc.helpers.CustomerDatabaseHelper;
import com.jostens.jemm2.pojo.Customer;
import com.jostens.jemm2.solr.CustomerDocument;

public class CustomerHelper
{

	/**
	 * Find all the part load files and call method to persist Part objects
	 * @throws IOException 
	 */
	public List<Customer> getCustomerObjects(String fileName) throws IOException
	{
		List<Customer> customers = new ArrayList<Customer>();

        InputStream stream = DesignHelper.class.getResourceAsStream("/com/jostens/jemm2/jdbc/resources/" + fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = null;
        while ((line = reader.readLine()) != null)
        {
//            System.out.println("Line entered : " + line);
            
        	customers.add(new Customer(line));
        }
		
		return customers;
	}
	
	/**
	 * Load all customers into the database
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void persistAllCustomers(Connection c) throws IOException, SQLException
	{
		List<Customer> customers = new ArrayList<Customer>();
		customers.addAll(getCustomerObjects("Customers_01.txt"));
		customers.addAll(getCustomerObjects("Customers_02.txt"));
		customers.addAll(getCustomerObjects("Customers_03.txt"));

		CustomerDatabaseHelper dbHelper = new CustomerDatabaseHelper();

		int i = 0;
		for (Customer customer : customers)
		{
			i++;
			System.out.println("" + i);
			dbHelper.persistCustomer(c, customer);
		}
	}

	/**
	 * Get all Customers from Oracle and persist into SOLR
	 * @throws SQLException 
	 * @throws SolrServerException 
	 * @throws IOException 
	 */
	public void persistAllCustomerDocuments(Connection c, HttpSolrClient solr) throws SQLException, IOException, SolrServerException
	{
		CustomerDatabaseHelper dbHelper = new CustomerDatabaseHelper();
		
		List<Integer> customers = dbHelper.getAllCustomerIDs(c);

		System.out.println("Customers found = " + customers.size());
		
		// Add each part to SOLR
		
		int i = 0;
		for (Integer customerID : customers)
		{
			i++;
			System.out.println("" + i);
			Customer customer = new Customer();
			customer.setID(customerID.intValue());
			dbHelper.getCustomer(c, customer);
			
			// Now persist the design into SOLR
			CustomerDocument pd = customer.getCustomerDocument();
			solr.addBean(pd);
			solr.commit();

//			if (i > 5)
//			{
//				break;
//			}
		}
	}

}
