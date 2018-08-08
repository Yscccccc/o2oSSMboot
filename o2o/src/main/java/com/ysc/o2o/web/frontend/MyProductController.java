package com.ysc.o2o.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysc.o2o.dto.UserProductMapExecution;
import com.ysc.o2o.entity.PersonInfo;
import com.ysc.o2o.entity.Product;
import com.ysc.o2o.entity.Shop;
import com.ysc.o2o.entity.UserProductMap;
import com.ysc.o2o.service.UserProductMapService;
import com.ysc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class MyProductController {
	@Autowired
	private UserProductMapService userProductMapService;

	@RequestMapping(value = "/listuserproductmapsbycustomer", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserProductMapsByCustomer(
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		PersonInfo user = (PersonInfo) request.getSession()
				.getAttribute("user");
		if ((pageIndex > -1) && (pageSize > -1) && (user != null)
				&& (user.getUserId() != -1)) {
			UserProductMap userProductMapCondition = new UserProductMap();
			userProductMapCondition.setUser(user);
			long shopId = HttpServletRequestUtil.getLong(request, "shopId");
			if (shopId > -1) {
				//若传入店铺信息，则列出某个店铺下该顾客的消费历史
				Shop shop = new Shop();
				shop.setShopId(shopId);
				userProductMapCondition.setShop(shop);
			}
			String productName = HttpServletRequestUtil.getString(request,
					"productName");
			if (productName != null) {
				//若传入的商品名不为空，则按照商品名模糊查询
				Product product = new Product();
				product.setProductName(productName);
				userProductMapCondition.setProduct(product);
			}
			UserProductMapExecution ue = userProductMapService
					.listUserProductMap(userProductMapCondition, pageIndex,
							pageSize);
			modelMap.put("userProductMapList", ue.getUserProductMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
}
