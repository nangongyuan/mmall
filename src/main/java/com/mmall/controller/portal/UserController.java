/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UserController
 * Author:   Administrator
 * Date:     2018/9/13 0013 9:23
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 〈〉
 *
 * @author Administrator
 * @create 2018/9/13 0013
 * @since 1.0.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	IUserService userService;

	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<User> login(String username, String password, HttpSession session){
		ServerResponse<User> response = userService.login(username,password);
		if (response.isSuccess()){
			session.setAttribute(Const.CURRENT_USER,response.getData());
		}
		return response;
	}

	@RequestMapping(value = "/logout.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<User> login(HttpSession session){
		session.removeAttribute(Const.CURRENT_USER);
		return ServerResponse.createBySuccess();
	}

	@RequestMapping(value = "/register.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> register(User user){
		return userService.register(user);
	}

	@RequestMapping(value = "/checkValid.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> checkValid(String str,String type){
		return userService.checkValid(str,type);
	}

	@RequestMapping(value = "/getUserInfo.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<User> getUserInfo(HttpSession session){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user != null){
			return ServerResponse.createBySuccessData(user);
		}
		return ServerResponse.createByErrorMessage("用户未登陆");
	}

	@RequestMapping(value = "/forgetGetQuestion.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> forgetGetQuestion(String username){
		return userService.selectQuestion(username);
	}

	@RequestMapping(value = "/forgetCheckAnswer.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> forgetCheckAnswer(String username,String question, String answer){
		return userService.checkAnswer(username,question,answer);
	}

	@RequestMapping(value = "/forgetRestPassword.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetRestPassword(String username,String passwordNew, String forgetToken){
		return userService.forgetRestPassword(username,passwordNew,forgetToken);
	}

	@RequestMapping(value = "/restPassword.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> restPassword(HttpSession session, String passwordOld, String passwordNew){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null){
			return ServerResponse.createByErrorMessage("用户未登陆");
		}
		return userService.resetPassword(passwordOld,passwordNew,user);
	}

	@RequestMapping(value = "/updateInformation.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> updateInformation(HttpSession session,User user){
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		if (currentUser == null){
			return ServerResponse.createByErrorMessage("用户未登陆");
		}
		user.setId(currentUser.getId());
		user.setUsername(currentUser.getUsername());
		ServerResponse<User> response = userService.updateInformation(user);
		if (response.isSuccess()){
			response.getData().setUsername(currentUser.getUsername());
			session.setAttribute(Const.CURRENT_USER,response.getData());
		}
		return response;
	}

	@RequestMapping(value = "/getInformation.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<User> getInformation(HttpSession session){
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		if (currentUser == null){
			return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"未登陆需要强制登陆");
		}
		return userService.getInformation(currentUser.getId());
	}
}