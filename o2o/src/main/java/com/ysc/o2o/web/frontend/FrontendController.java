package com.ysc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	private String index() {
		return "frontend/index";
	}
	
	/**
	 * 店铺详情页路由
	 * @return
	 */
	@RequestMapping(value = "/shopdetail", method = RequestMethod.GET)
	private String showShopDetail() {
		return "frontend/shopdetail";
	}
	
	/**
	 * 商品详情页路由
	 * @return
	 */
	@RequestMapping(value = "/productdetail", method = RequestMethod.GET)
	private String showProductDetail() {
		return "frontend/productdetail";
	}
	
	@RequestMapping(value = "/shoplist", method = RequestMethod.GET)
	private String showShopList() {
		return "frontend/shoplist";
	}
	
	@RequestMapping(value = "/awardlist", method = RequestMethod.GET)
	private String showAwardList() {
		return "frontend/awardlist";
	}
	
	@RequestMapping(value = "/pointrecord", method = RequestMethod.GET)
	private String showPointRecord() {
		return "frontend/pointrecord";
	}
	
	@RequestMapping(value = "/myawarddetail", method = RequestMethod.GET)
	private String showmyawarddetail() {
		return "frontend/myawarddetail";
	}
	
	@RequestMapping(value = "/myrecord", method = RequestMethod.GET)
	private String showmyrecord() {
		return "frontend/myrecord";
	}
	
	@RequestMapping(value = "/mypoint", method = RequestMethod.GET)
	private String showmypoint() {
		return "frontend/mypoint";
	}
}
