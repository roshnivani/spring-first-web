package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bean.CustomerBean;
import com.bean.ProductBean;
import com.bean.ResponseBean;
import com.dao.CustomerDao;
import com.util.TokenGenerator;

@RestController
public class CustomerController {
	@Autowired
	CustomerDao customerDao;

	@Autowired
	TokenGenerator tokenGenerator;

	@PostMapping("/customers")
	public ResponseBean<CustomerBean> addCustomer(@RequestBody CustomerBean customer) {

		ResponseBean<CustomerBean> res = new ResponseBean<>();
		customerDao.addCustomer(customer);

		res.setData(customer);
		res.setMessage("customer save");
		res.setStatus(200);

		return res;
	}
	@GetMapping("/customers")
	public ResponseBean<List<CustomerBean>> getAllProducts() {
		ResponseBean<List<CustomerBean>> res = new ResponseBean<>();
		List<CustomerBean> products = customerDao.getAllCustomers();
		//
		res.setStatus(200);
		res.setMessage("Products Retrieved");
		res.setData(products);
		
		
		return res;
	}
	
	@PostMapping("/authenticate")
	public ResponseBean<CustomerBean> authenticate(@RequestBody CustomerBean customer) {
		ResponseBean<CustomerBean> res = new ResponseBean<>();

		customer = customerDao.authenticate(customer.getEmail(), customer.getPassword());
		if (customer == null) {
			res.setStatus(-1);
			res.setData(customer);
			res.setMessage("Invalid Credentials");
		} else {
			String token = tokenGenerator.generateToken();
			customer.setToken(token);
			customerDao.updateToken(customer.getCustomerId(), token);
			res.setData(customer);
			res.setStatus(200);
			res.setMessage("authentication done");
		}

		return res;
	}

	@GetMapping("/getcustomerbytoke/{token}")
	public ResponseBean<CustomerBean> getCustomerByToken(@PathVariable("token") String token) {

		ResponseBean<CustomerBean> res = new ResponseBean<>();

		CustomerBean customer = customerDao.getCustomerByToken(token);

		if (customer == null) {
			res.setStatus(-1);
			res.setData(customer);
			res.setMessage("Invalid token");
		} else {
			res.setData(customer);
			res.setStatus(200);
			res.setMessage("customer retrieved...");
		}
		return res;
	}
	@DeleteMapping("/customers/{customerId}")
	public ResponseBean<CustomerBean> deleteCustomerById(@PathVariable("customerId") int productId){

		ResponseBean<CustomerBean> res = new ResponseBean<>();
		
		CustomerBean product = customerDao.deleteCustomerById(productId);
		
		if(product == null) {
			res.setData(null);
			res.setMessage("Invalid customer Id");
			res.setStatus(-1);
		}else {
			res.setData(product);
			res.setMessage("customer removed");
			res.setStatus(200);
		}
		return res;
	}
	@GetMapping("/customers/{customerId}")
	public ResponseBean<CustomerBean> getUserById(@PathVariable("customerId") int customerId) {
		ResponseBean<CustomerBean> res = new ResponseBean<>();
		CustomerBean user = customerDao.update(customerId);
		

		if (user == null) {
			res.setStatus(-1);//
			res.setMessage("Invalid UserId");
		} else {	
			res.setStatus(200);
			res.setData(user);
			res.setMessage("User Retrived");
		}
		return res;
	}
	/*
	 * @PutMapping("/customers") public ResponseBean<CustomerBean>
	 * udpateUser(CustomerBean user) { ResponseBean<CustomerBean> res = new
	 * ResponseBean<>(); user = customerDao.update(user);
	 * 
	 * if (user == null) { res.setStatus(-1); res.setMessage("invalid userId"); }
	 * else { res.setStatus(200); res.setMessage("user updated...");
	 * res.setData(user); }
	 * 
	 * return res;// }
	 */
}