package com.ric.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.tiles.request.servlet.ServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ric.persistence.model.ReferralJob;
import com.ric.persistence.model.User;
import com.ric.persistence.service.IUserService;
import com.ric.web.json.RegistrationResponse;
import com.ric.web.model.ReferralJobBO;
import com.ric.web.model.SecurityUser;
import com.ric.web.model.UserBO;

@Controller
public class LoginController {

	@Autowired
	private IUserService service;

	static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView getHomePage(Map<String, Object> model) {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		SecurityUser user = (SecurityUser) auth.getPrincipal();

		ModelAndView mv = new ModelAndView();
		// to get user credentials
		mv.addObject("user", user);

		ReferralJobBO bo = new ReferralJobBO();
		model.put("rfjob", bo);

		log.info("Home Page is going to lauch ");
		mv.setViewName("home");
		return mv;

	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String getLoginPage() {

		//ModelAndView mv = new ModelAndView();
		log.info("Login Page is going to lauch ");
		//mv.setViewName("landingpage");
		return "landingpage";

	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public ModelAndView getLogoutPage() {

		ModelAndView mv = new ModelAndView();
		log.info("Logout Page is going to lauch ");
		mv.setViewName("logout");
		return mv;

	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ModelAndView login(@RequestParam("username") String username,
			@RequestParam("password") String password) {

		ModelAndView mv = new ModelAndView();

		if (service.authenticate(username, password)) {
			log.info("login authenitcation succes home page is going to launch  ");
			mv.setViewName("home");
		} else
			mv.setViewName("login");

		return mv;

	}

	/*
	 * @RequestMapping(value = "signup", method = RequestMethod.GET) public
	 * ModelAndView signup(Map<String, Object> model) { ModelAndView mv = new
	 * ModelAndView(); UserBO userBO = new UserBO();
	 * 
	 * log.info("signup page launched "); model.put("userBO", userBO);
	 * 
	 * 
	 * mv.setViewName("signup"); return mv;
	 * 
	 * }
	 */

	@RequestMapping(value = "useridcheck", method = RequestMethod.POST)
	@ResponseBody
	public String userCheck(@RequestParam("username") String username) {

		String status = "notavailable";

		try {
			if (!service.searchByUserName(username)) {
				log.info("checking username is available or not ");
				status = "available";
			}
		} catch (Exception e) {
			log.error("Error cause:{} Error message:{}", e.getCause(),
					e.getMessage());
		}

		return status;

	}

	@RequestMapping(value = "signup", method = RequestMethod.POST)
	@ResponseBody
	public RegistrationResponse register(
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("firstName") String firstname,
			@RequestParam("lastName") String lastname,
			@RequestParam("peremail") String permail,
			@RequestParam("ogrEmail") String omail,Map<String, Object> model) {

		RegistrationResponse response = new RegistrationResponse();

		User user = new User();
		user.setUserName(username);
		user.setPassword(password);
		user.setFirstName(firstname);
		user.setLastName(lastname);
		user.setPerEmail(permail);
		user.setOgrEmail(omail);


		if (username == "" || password == "" || firstname == ""
				|| lastname == "" || permail == "" || omail == "") {

		} else {

			try {
				if (!service.searchByUserName(user.getUserName())) {
					log.info("registering the user ");
					service.create(user);
					response.setStatus(username+"    registered succesfull try to login....! ");

				} else {

					response.setStatus("not registered");
				}

			} catch (Exception e) {
				log.error("Error cause:{} Error message:{}", e.getCause(),
						e.getMessage());
			}
		}

		return response;

	}

	/*
	 * public User populate(UserBO bo) { User user = new User();
	 * user.setUserName(bo.getUserName()); user.setPassword(bo.getPassword());
	 * user.setFirstName(bo.getFirstName()); user.setLastName(bo.getLastName());
	 * user.setPerEmail(bo.getPerEmail()); user.setOgrEmail(bo.getOgrEmail());
	 * return user;
	 * 
	 * }
	 */

}
