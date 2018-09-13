/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UserServiceImpl
 * Author:   Administrator
 * Date:     2018/9/13 0013 9:31
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.util.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.StringHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 〈〉
 *
 * @author Administrator
 * @create 2018/9/13 0013
 * @since 1.0.0
 */
@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public ServerResponse<User> login(String username, String password) {
		int result = userMapper.checkUserName(username);
		if (result==0){
			return ServerResponse.createByErrorMessage("用户名不存在");
		}
		password = MD5Util.MD5EncodeUtf8(password);
		User user = userMapper.selectLogin(username,password);
		if (user == null){
			return ServerResponse.createByErrorMessage("密码错误");
		}

		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createBySuccess("登陆成功",user);
	}

	@Override
	public ServerResponse<String> register(User user){
		ServerResponse validResponse = checkValid(user.getUsername(),Const.USERNAME);
		if (!validResponse.isSuccess()){
			return validResponse;
		}
		validResponse = checkValid(user.getEmail(),Const.EMAIL);
		if (!validResponse.isSuccess()){
			return validResponse;
		}
		user.setRole(Const.Role.ROLE_CUSTOMER);
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

		int result = userMapper.insert(user);
		if (result==0){
			return ServerResponse.createByErrorMessage("注册失败");
		}
		return ServerResponse.createBySuccessMessage("注册成功");
	}

	@Override
	public ServerResponse<String> checkValid(String str, String type) {
		if (Const.USERNAME.equals(type) || Const.EMAIL.equals(type)){
			if (Const.USERNAME.equals(type)){
				int result = userMapper.checkUserName(str);
				if (result>0){
					return ServerResponse.createByErrorMessage("用户名已存在");
				}
			}
			if (Const.EMAIL.equals(type)){
				int result = userMapper.checkEmail(str);
				if (result>0){
					return ServerResponse.createByErrorMessage("email已存在");
				}
			}
		}else{
			return ServerResponse.createByErrorMessage("参数错误");
		}
		return ServerResponse.createBySuccessMessage("校验成功");
	}

	@Override
	public ServerResponse<String> selectQuestion(String username) {
		ServerResponse<String> response = checkValid(username,Const.USERNAME);
		if (response.isSuccess()){
			return ServerResponse.createByErrorMessage("用户名不存在");
		}
		String question = userMapper.selectQuestionByUsername(username);
		if (StringUtils.isNotBlank(question)){
			return ServerResponse.createBySuccessData(question);
		}
		return ServerResponse.createByErrorMessage("找回密码的问题是空的");
	}

	@Override
	public ServerResponse<String> checkAnswer(String username, String question, String answer) {
		int result = userMapper.checkAnswer(username,question,answer);
		if (result>0){
			String forgetToken = UUID.randomUUID().toString();
			TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
			return ServerResponse.createBySuccessData(forgetToken);
		}
		return ServerResponse.createByErrorMessage("问题的答案错误");
	}

	@Override
	public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {
		if (StringUtils.isBlank(forgetToken)){
			return ServerResponse.createByErrorMessage("参数错误，需要传递token");
		}
		String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
		if (StringUtils.isBlank(token)){
			return ServerResponse.createByErrorMessage("token无效或者已过期");
		}
		if (StringUtils.equals(token,forgetToken)){
			String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
			int resutl = userMapper.updatePasswordByUsername(username,md5Password);
			if (resutl>0){
				return ServerResponse.createBySuccessMessage("密码更新成功");
			}
		}else{
			return ServerResponse.createByErrorMessage("token错误,请重新获取token");
		}
		return ServerResponse.createByErrorMessage("修改密码失败");
	}

	@Override
	public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
		int result = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
		if (result==0){
			return ServerResponse.createByErrorMessage("旧密码错误");
		}
		user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
		result = userMapper.updateByPrimaryKeySelective(user);
		if (result>0){
			return ServerResponse.createBySuccessMessage("密码更新成功");
		}
		return ServerResponse.createByErrorMessage("密码更新失败");
	}

	@Override
	public ServerResponse<User> updateInformation(User user) {
		int result = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
		if (result>0){
			return ServerResponse.createByErrorMessage("email已存在");
		}
		User updateUser = new User();
		updateUser.setId(user.getId());
		updateUser.setEmail(user.getEmail());
		updateUser.setPhone(user.getPhone());
		updateUser.setQuestion(user.getQuestion());
		updateUser.setAnswer(user.getAnswer());
		result = userMapper.updateByPrimaryKeySelective(updateUser);
		if (result>0){
			return ServerResponse.createBySuccess("更新个人信息成功",updateUser);
		}
		return ServerResponse.createByErrorMessage("更新个人信息失败");
	}

	@Override
	public ServerResponse<User> getInformation(Integer userId) {
		User user = userMapper.selectByPrimaryKey(userId);
		if (user == null){
			return ServerResponse.createByErrorMessage("找不到当前用户");
		}
		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createBySuccessData(user);
	}

	@Override
	public ServerResponse checkAdminRole(User user) {
		if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
			return ServerResponse.createBySuccess();
		}
		return ServerResponse.createByError();
	}
}