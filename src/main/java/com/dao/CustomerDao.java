package com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bean.CustomerBean;
import com.bean.LoginBean;
import com.bean.ProductBean;

@Repository
public class CustomerDao {

	@Autowired
	JdbcTemplate stmt;

	public void addCustomer(CustomerBean customer) {
		stmt.update("insert into customer (firstName,email,password) values (?,?,?)", customer.getFirstName(),
				customer.getEmail(), customer.getPassword());
	}

	public List<CustomerBean> getAllCustomers() {

		List<CustomerBean> products = stmt.query("select * from customer",
				new BeanPropertyRowMapper<CustomerBean>(CustomerBean.class));

		return products;
	}

	public CustomerBean authenticate(String email, String password) {

		CustomerBean customer = null;

		try {
			customer = stmt.queryForObject("select * from customer where email like ? and password like ?",
					new BeanPropertyRowMapper<CustomerBean>(CustomerBean.class), email, password);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return customer;
	}

	public void updateToken(int customerId, String token) {
		stmt.update("update customer set token = ? where customerId = ?", token, customerId);
	}

	public CustomerBean getCustomerByToken(String token) {
		CustomerBean customer = null;

		try {
			customer = stmt.queryForObject("select * from customer where token like ? ",
					new BeanPropertyRowMapper<CustomerBean>(CustomerBean.class), token);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return customer;
	}

	public CustomerBean deleteCustomerById(int customerId) {
		// TODO Auto-generated method stub
		CustomerBean productBean = getCustomerById(customerId);
		if (productBean != null) {
			stmt.update("delete from customer where customerId = ?", customerId);
		}

		return productBean;
	}
	/*
	 * public CustomerBean editCustomerById(int customerId) { // TODO Auto-generated
	 * method stub CustomerBean productBean = getCustomerById(customerId);
	 * 
	 * if(productBean != null) { stmt.
	 * update("update customer set firstName,email,password where customerId = ?"
	 * ,customerId); }
	 * 
	 * 
	 * return productBean; }
	 */

	public CustomerBean getCustomerById(int productId) {

		CustomerBean product = null;

		try {
			product = stmt.queryForObject("select * from customer where customerId = ?",
					new BeanPropertyRowMapper<CustomerBean>(CustomerBean.class), productId);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return product;

	}

	public CustomerBean update(int id) {
		CustomerBean bean = getCustomerById(id);
		stmt.update("update customer set firstName'"+bean.getFirstName()+"'email'"+bean.getEmail()+"'password'"+bean.getPassword()+"'where customerId=?",id);
		return bean;
	}

}