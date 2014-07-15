package com.ric.web.controller;

import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ric.persistence.model.User;
import com.ric.persistence.service.IUserService;
import com.ric.web.model.SecurityUser;
import com.ric.web.model.UserBO;

@Controller
public class LoginController {

	@Autowired
	private IUserService service;

	static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = "/loginSucces", method = RequestMethod.GET)
	public ModelAndView getHomePage() {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		SecurityUser user = (SecurityUser) auth.getPrincipal();
	
		ModelAndView mv = new ModelAndView();
		mv.addObject("user", user);
		log.info("Home Page is going to lauch ");
		mv.setViewName("home");
		return mv;

	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public ModelAndView getLoginPage() {

		ModelAndView mv = new ModelAndView();
		log.info("Login Page is going to lauch ");
		mv.setViewName("start");
		return mv;

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

	@RequestMapping(value = "signup", method = RequestMethod.GET)
	public ModelAndView signup(Map<String, Object> model) {
		ModelAndView mv = new ModelAndView();
		UserBO userBO = new UserBO();

		log.info("signup page launched ");
		model.put("userBO", userBO);
		mv.setViewName("signup");
		return mv;

	}

	@RequestMapping(value = "useridcheck", method = RequestMethod.POST)
	@ResponseBody
	public String userCheck(UserBO bo) {
		String status = "notavailable";

		try {
			if (!service.searchByUserName(bo.getUserName())) {
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
	public ModelAndView register(@Valid UserBO userBO, BindingResult result) {

		ModelAndView mv = new ModelAndView();

		User user = new User();

		if (result.hasErrors()) {

			mv.setViewName("signup");
		} else {
			user = populate(userBO);

			try {
				if (!service.searchByUserName(user.getUserName())) {
					log.info("registering the user ");
					service.create(user);
					mv.setViewName("home");
				} else {
					mv.setViewName("signup");
				}

			} catch (Exception e) {
				log.error("Error cause:{} Error message:{}", e.getCause(),
						e.getMessage());
			}
		}

		return mv;

	}

	public User populate(UserBO bo) {
		User user = new User();
		user.setUserName(bo.getUserName());
		user.setPassword(bo.getPassword());
		user.setFirstName(bo.getFirstName());
		user.setLastName(bo.getLastName());
		user.setPerEmail(bo.getPerEmail());
		user.setOgrEmail(bo.getOgrEmail());
		return user;

	}

}
