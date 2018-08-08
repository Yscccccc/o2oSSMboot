package com.ysc.o2o.web.wechat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ysc.o2o.dto.WechatAuthExecution;
import com.ysc.o2o.entity.PersonInfo;
import com.ysc.o2o.entity.WechatAuth;
import com.ysc.o2o.enums.WechatAuthStateEnum;
import com.ysc.o2o.service.PersonInfoService;
import com.ysc.o2o.service.WechatAuthService;
import com.ysc.o2o.util.wechat.WechatUser;
import com.ysc.o2o.util.wechat.WechatUserUtil;
import com.ysc.o2o.util.wechat.message.pojo.UserAccessToken;

/**
 * 从微信菜单点击后调用的接口，可以在url里增加参数（role_type）来表明是从商家还是从玩家按钮进来的，依次区分登陆后跳转不同的页面
 * 玩家会跳转到index.html页面
 * 商家如果没有注册，会跳转到注册页面，否则跳转到任务管理页面
 * 如果是商家的授权用户登陆，会跳到授权店铺的任务管理页面
 * 获取关注公众号之后的微信用户信息的接口，如果在微信浏览器里访问 
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=您的appId&redirect_uri=http://o2o.yitiaojieinfo.com/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect 
 * 则这里将会获取到code,之后再可以通过code获取到access_token 进而获取到用户信息
 * @author chenshuying
 *
 */
@Controller
@RequestMapping("wechatlogin")
public class WechatLoginController {
	
	private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);
	private static String FRONTEND = "1";
	private static String SHOPEND = "2";
	
	@Autowired
	private PersonInfoService personInfoService;
	
	@Autowired
	private WechatAuthService wechatAuthService;
	
	
	@RequestMapping(value = "/logincheck", method = {RequestMethod.GET})
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("wechat login get....");
		//获取微信公众号传输过来的code,通过code可获取access_token，进而获取用户信息
		String code = request.getParameter("code");
		//这个state可以用来传我们自定义的信息，方便程序调用
		String roleType = request.getParameter("state");
		log.debug("wechat login code: " + code);
		WechatUser user = null;
		String openId = null;
		WechatAuth auth = null;
		if (null != code) {
			UserAccessToken token;
			try {
				//通过code获取access_token
				token = WechatUserUtil.getUserAccessToken(code);
				log.debug("wechat login token: " + token.toString());
				//通过token获取accessToken
				String accessToken = token.getAccessToken();
				//通过token获取openId
				openId = token.getOpenId();
				//通过access_token和openId获取用户昵称等信息
				user = WechatUserUtil.getUserInfo(accessToken, openId);
				log.debug("wechat login user: " + user.toString());
				request.getSession().setAttribute("openId", openId);
				auth = wechatAuthService.getWechatAuthByOpenId(openId);
			} catch (IOException e) {
				log.error("error in getUserAccessToken or get userInfo or findByOpenId: " + e.toString());
				e.printStackTrace();
			}
		}

		// 获取到openId后，可以通过它去数据库判断该微信帐号是否在我们网站里有对应的帐号了，
		// 没有的话这里可以自动创建上，直接实现微信与咱们网站的无缝对接。 
		if (auth == null) {
			PersonInfo personInfo = WechatUserUtil.getPersonInfoFromRequest(user);
			auth = new WechatAuth();
			auth.setOpenId(openId);
			if (FRONTEND.equals(roleType)) {
				personInfo.setUserType(1);
			} else {
				personInfo.setUserType(2);
			}
			auth.setPersonInfo(personInfo);
			WechatAuthExecution we = wechatAuthService.register(auth);
			if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
				return null;
			} else {
				personInfo = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
				request.getSession().setAttribute("user", personInfo);
			}
		}
		//若用户点击的是前端展示系统按钮则进入前端展示系统
		if (FRONTEND.equals(roleType)) {
			return "frontend/index";
		} else {
			return "shopadmin/shoplist";
		}
	}
}
