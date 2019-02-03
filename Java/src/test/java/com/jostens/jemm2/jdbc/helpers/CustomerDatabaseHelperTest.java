package com.jostens.jemm2.jdbc.helpers;

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
	public void testGetPartID() throws SQLException
	{
		CustomerDatabaseHelper helper = new CustomerDatabaseHelper();
		int customerId = helper.getCustomerID(c, "23456");
		System.out.println("Part Value = " + customerId);
		customerId = helper.getCustomerID(c, "23332");
		System.out.println("Part Value = " + customerId);
	}

	@Test
	public void testPersistCustomer() throws SQLException
	{
		CustomerDatabaseHelper helper = new CustomerDatabaseHelper();
		
		Customer customer = new Customer();
		customer.setCustomerID("432145");
		customer.setName("Osceola High School");
		helper.persistCustomer(c, customer);
		
	}

}
