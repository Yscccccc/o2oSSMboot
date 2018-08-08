package com.ysc.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "shopadmin", method = {RequestMethod.GET})
public class ShopAdminController {
	
	@RequestMapping(value = "/shopoperation")
	public String shopOperation() {
		return "shop/shopoperation";
	}
	
	@RequestMapping(value = "/shoplist")
	public String shopList() {
		return "shop/shoplist";
	}
	
	@RequestMapping(value = "/shopmanagement")
	public String shopManagement() {
		return "shop/shopmanagement";
	}
	
	@RequestMapping(value = "/productcategorymanagement", method = RequestMethod.GET)
	public String productCategoryManagement() {
		return "shop/productcategorymanagement";
	}
	
	@RequestMapping(value = "/productoperation")
	public String productOperation() {
		return "shop/productoperation";
	}
	
	@RequestMapping(value = "/productmanagement", method = RequestMethod.GET)
	public String productManagement() {
		return "shop/productmanagement";
	}
	
	@RequestMapping(value = "/awardmanagement", method = RequestMethod.GET)
	public String awardManagement() {
		return "shop/awardmanagement";
	}
	
	@RequestMapping(value = "/awardoperation", method = RequestMethod.GET)
	public String awardOperation() {
		return "shop/awardoperation";
	}
	
	@RequestMapping(value = "/productbuycheck", method = RequestMethod.GET)
	public String productbuyCheck() {
		return "shop/productbuycheck";
	}
	
	@RequestMapping(value = "/awarddelivercheck", method = RequestMethod.GET)
	public String awardDeliverCheck() {
		return "shop/awarddelivercheck";
	}
	
	@RequestMapping(value = "/usershopcheck", method = RequestMethod.GET)
	public String usershopCheck() {
		return "shop/usershopcheck";
	}
	
	@RequestMapping(value = "/shopauthmanagement", method = RequestMethod.GET)
	public String shopAuthManagement() {
		return "shop/shopauthmanagement";
	}
	
	@RequestMapping(value = "/shopauthedit", method = RequestMethod.GET)
	public String shopAuthEdit() {
		return "shop/shopauthedit";
	}
	
	@RequestMapping(value = "/operationsuccess", method = RequestMethod.GET)
	public String operationSuccess() {
		return "shop/operationsuccess";
	}
	
	@RequestMapping(value = "/operationfail", method = RequestMethod.GET)
	public String operationFail() {
		return "shop/operationfail";
	}
}
