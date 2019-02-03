package com.jostens.jemm2.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jostens.jemm2.jdbc.helpers.CustomerDatabaseHelper;
import com.jostens.jemm2.pojo.Customer;

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

}
