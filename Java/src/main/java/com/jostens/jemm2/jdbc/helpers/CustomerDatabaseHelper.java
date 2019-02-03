package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.pojo.Customer;

public class CustomerDatabaseHelper
{

	/**
	 * For the provided Customer Number search the customer table and return its ID if found or
	 * get the next sequence # and return
	 * @throws SQLException 
	 */
	public int getCustomerID(Connection c, String customerNumber) throws SQLException
	{
		int customerID = 0;
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_CUSTOMER_ID);
		selectStmt = selectStmt.replace("[CUSTOMER_ID]", customerNumber);
		
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(selectStmt);
		boolean rowFound = rs.next();
		if (rowFound)
		{
			customerID = rs.getInt(1);
		}
		else
		{
			// Need to insert a keyword and keep the ID
			customerID = getNextCustomerSequence(c);
		}

		rs.close();
		statement.close();
		
		return customerID;
	}

	/**
	 * Return the next sequence # from CUSTOMER_SEQUENCE
	 */
	public int getNextCustomerSequence(Connection c)
	{
		int value = 0;
		try
		{
			Statement statement = c.createStatement();
			ResultSet rs = statement.executeQuery("SELECT CUSTOMER_SEQUENCE.NEXTVAL FROM DUAL");
			rs.next();
			value = rs.getInt(1);
			rs.close();
			statement.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
//			ExceptionHelper.logExceptionToFile("getNextMBAssetsSequence (CustomerProfileDatabase)", e);
			//System.out.println("Exception in retrieving next MB Assets Sequence value: " + ExceptionHelper.getStackTraceAsString(e));
		}
		return value;
	}

	/**
	 * Persist the supplied Customer
	 * @throws SQLException 
	 */
	public Customer persistCustomer(Connection c, Customer customer) throws SQLException
	{
		// Get this customer ID and add to supplied customer object
		int customerID = getCustomerID(c, customer.getCustomerID());
		customer.setID(customerID);
		
		// Try to delete the ID from the customer table
		String deleteStmt = Jemm2Statements.getStatement(Jemm2Statements.DELETE_CUSTOMER);

		PreparedStatement preparedDeleteStatment = c.prepareStatement(deleteStmt);
		// Populate the columns
		preparedDeleteStatment.setInt(1, customer.getID());
		preparedDeleteStatment.executeUpdate();
		preparedDeleteStatment.close();
		
		// Update the design and keywords for the design
		String insertStmt = Jemm2Statements.getStatement(Jemm2Statements.INSERT_CUSTOMER);

		PreparedStatement preparedInsertStatment = c.prepareStatement(insertStmt.toString());
		// Populate the columns
		preparedInsertStatment.setInt(1, customerID);
		preparedInsertStatment.setString(2, customer.getName());
		preparedInsertStatment.setString(3, customer.getCustomerID());
		preparedInsertStatment.setString(4, customer.getCity());
		preparedInsertStatment.setString(5, customer.getState());
		preparedInsertStatment.setString(6, customer.getColor1());
		preparedInsertStatment.setString(7, customer.getColor2());
		preparedInsertStatment.setString(8, customer.getColor3());
		preparedInsertStatment.setString(9, customer.getMascot());
		preparedInsertStatment.executeUpdate();
		preparedInsertStatment.close();
		
		c.commit();
		
		return customer;
	}

}
