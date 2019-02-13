package com.jostens.jemm2.jdbc.helpers;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.pojo.Customer;

public class CustomerDatabaseHelperTest
{
	private static Connection c = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		Jemm2Statements statements = new Jemm2Statements();
		statements.initializeStatements();
		c = ConnectionHelper.getJEMM2Connection();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		ConnectionHelper.closeConnection(c);
	}

//	@Test
	public void testGetNextCustomerSequence()
	{
		CustomerDatabaseHelper helper = new CustomerDatabaseHelper();
		int sequence = helper.getNextCustomerSequence(c);
		System.out.println("Sequence Value = " + sequence);
		sequence = helper.getNextCustomerSequence(c);
		System.out.println("Sequence Value = " + sequence);
	}

//	@Test
	public void testGetCustomerID() throws SQLException
	{
		CustomerDatabaseHelper helper = new CustomerDatabaseHelper();
		int customerId = helper.getCustomerID(c, "23456");
		System.out.println("Customer ID Value = " + customerId);
		customerId = helper.getCustomerID(c, "23332");
		System.out.println("Customer ID Value = " + customerId);
	}

//	@Test
	public void testPersistCustomer() throws SQLException
	{
		CustomerDatabaseHelper helper = new CustomerDatabaseHelper();
		
		Customer customer = new Customer();
		customer.setCustomerID("432145");
		customer.setName("Osceola High School");
		helper.persistCustomer(c, customer);		
	}

	@Test
	public void testImportCustomer() throws SQLException
	{
		Customer customer = new Customer("Harmony High School (Farmington, IA)|1071790|Farmington|Iowa|US|Red|White|NA|Rockets|");
		CustomerDatabaseHelper helper = new CustomerDatabaseHelper();
		helper.persistCustomer(c, customer);
		
		Customer customer2 = new Customer();
		customer2.setID(customer.getID());
		
		// Populate customer2 from database
		helper.getCustomer(c, customer2);
		
		assertTrue(customer.equals(customer2));
		
		
		System.out.println("Customer ID = " + customer.getID());
	}

}
