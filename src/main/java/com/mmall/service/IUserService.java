/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: IUserService
 * Author:   Administrator
 * Date:     2018/9/13 0013 9:30
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * 〈〉
 *
 * @author Administrator
 * @create 2018/9/13 0013
 * @since 1.0.0
 */
public interface IUserService {

	ServerResponse<User> login(String username, String password);

	ServerResponse<String> register(User user);

	ServerResponse<String> checkValid(String str,String type);

	ServerResponse<String> selectQuestion(String username);

	ServerResponse<String> checkAnswer(String username,String question, String answer);

	ServerResponse<String> forgetRestPassword(String username,String passwordNew, String forgetToken);

	ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

	ServerResponse<User> updateInformation(User user);

	ServerResponse<User> getInformation(Integer userId);

	ServerResponse checkAdminRole(User user);
}
