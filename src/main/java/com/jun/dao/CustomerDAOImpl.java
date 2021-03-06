package com.jun.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jun.exceptions.InvalidBalanceException;
import com.jun.exceptions.UserNotFoundException;
import com.jun.model.Customer;

public class CustomerDAOImpl implements CustomerDAO {

	@Override
	public Customer getCustomerAccountsById(int userId, Connection con) throws SQLException {
		Customer cust = null;
		String sql = "SELECT * FROM bank.account WHERE login_id = ?";
		String custSql = "SELECT cash FROM bank.customer WHERE login_id = ?";

		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1, userId);

		ResultSet rs = pstmt.executeQuery();
		List<String> cards = new ArrayList<>();

		while (rs.next()) {
			cards.add(rs.getString("account_no"));
		}
		if (cards.size() > 0) {
			PreparedStatement balPS = con.prepareStatement(custSql);
			balPS.setInt(1, userId);
			ResultSet balRS = balPS.executeQuery();
			double custBal = 0;
			if (balRS.next()) {
				custBal = balRS.getDouble("cash");
			}
			cust = new Customer(cards, userId, custBal);
		}
		return cust;
	}

	@Override
	public Customer getCustomer(int userId, Connection con) throws SQLException {
		Customer cust = null;
		double custBal = 0;
		String custSql = "SELECT * FROM bank.customer WHERE login_id = ?";

		PreparedStatement custPS = con.prepareStatement(custSql);
		custPS.setInt(1, userId);
		ResultSet custRS = custPS.executeQuery();
		
		if (custRS.next()) {
			custBal = custRS.getDouble("cash");
			cust = new Customer(userId, custBal);
		}

		return cust;
	}

	@Override
	public boolean checkValidUnsername(String username, Connection con) throws SQLException {
		String sql = "SELECT * FROM bank.login WHERE username = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean updateCustomerBalance(int userId, double amount, double currBal, Connection con) throws UserNotFoundException, InvalidBalanceException, SQLException {
		String sql = "UPDATE bank.customer SET cash = ? WHERE login_id = ?";
		double newBalance = currBal - amount;
		if (newBalance < 0) {
			throw new InvalidBalanceException("You do not have enough cash to make this transaction");
		}
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setDouble(1, newBalance);
		ps.setInt(2, userId);
		ps.executeUpdate();
		return true;
	}

}
