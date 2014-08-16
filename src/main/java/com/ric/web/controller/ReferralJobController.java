package com.ric.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ric.persistence.model.ReferralJob;
import com.ric.persistence.service.IReferralJobService;
import com.ric.web.json.PostResponse;
import com.ric.web.model.ReferralJobBO;

@RestController
public class ReferralJobController {
	static Logger log = LoggerFactory.getLogger(ReferralJobController.class);

	@Autowired
	private IReferralJobService service;

	@RequestMapping(value = "postajob", method = RequestMethod.POST)
	public PostResponse post(final ReferralJobBO request) {

		log.info("request payload: skill={},position={},expto={}",
				request.getExpire_date());

		PostResponse response = new PostResponse();
		ReferralJob rj = new ReferralJob();
		rj.setExpFrom(request.getExp_from());
		rj.setExpTo(request.getExp_to());
		rj.setExpireDate(request.getExpire_date());
		rj.setLocation(request.getLocation());
		rj.setPosition(request.getOpen_position());
		rj.setSkill(request.getSkill());
		rj.setCompany(request.getCompany());
		rj.setPostedBy(request.getPosted_by());

		rj.setPostedDate(new Date());

		try {
			service.create(rj);
			response.setStatus("succesfully posted");
			response.setStatusMessage("Job got posted successfully");
		} catch (Exception e) {
			log.error("Error cause:{} Error message:{}", e.getCause(),
					e.getMessage());
		}

		return response;
	}

	@RequestMapping(value = "post", method = RequestMethod.GET)
	public ModelAndView getPostpage(Map<String, Object> model) {
		ModelAndView mv = new ModelAndView();
		log.info("post page is going to launch ");
		ReferralJobBO bo = new ReferralJobBO();
		model.put("rfjob", bo);
		mv.setViewName("post");
		return mv;
	}

	@RequestMapping(value = "displayreferraljobs", method = RequestMethod.GET)
	public List<ReferralJobBO> getReferralJobs(Map<String, Object> model) {

		log.info("referral jobs  going to display");

		List<ReferralJob> rfjobs = service.findAll();
		List<ReferralJobBO> rfjobBo = new ArrayList<ReferralJobBO>();
		int size=rfjobs.size();
		//to show latest 4 jobs only 
		for(int i=1;i<5;i++)
		{
			ReferralJobBO bo = new ReferralJobBO();
			bo.setCompany(rfjobs.get(size-i).getCompany());
			bo.setSkill(rfjobs.get(size-i).getSkill());
			bo.setOpen_position(rfjobs.get(size-i).getPosition());
			bo.setPosted_by(rfjobs.get(size-i).getPostedBy());
			
			bo.setPosted_date(rfjobs.get(size-i).getPostedDate());
			bo.setExpire_date(rfjobs.get(size-i).getExpireDate());
			rfjobBo.add(bo);
		}
	
		
		/*for (ReferralJob k : rfjobs) {
			ReferralJobBO bo = new ReferralJobBO();
			bo.setCompany(k.getCompany());
			bo.setSkill(k.getSkill());
			bo.setOpen_position(k.getPosition());
			rfjobBo.add(bo);

		}*/
		
	return rfjobBo;

	}

}
