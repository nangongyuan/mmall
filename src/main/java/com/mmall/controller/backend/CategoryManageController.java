/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: CategoryManageController
 * Author:   Administrator
 * Date:     2018/9/13 0013 14:35
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 〈〉
 *
 * @author Administrator
 * @create 2018/9/13 0013
 * @since 1.0.0
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

	@Autowired
	private IUserService userService;

	@Autowired
	private ICategoryService categoryService;

	@RequestMapping(value = "insert_category.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse addCategory(HttpSession session,String categoryName, @RequestParam(value = "parentId" ,defaultValue = "0") Integer parentId){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null){
			return ServerResponse.createByErrorMessage("未登录");
		}
		if (userService.checkAdminRole(user).isSuccess()){
			return categoryService.insertCategory(categoryName,parentId);
		}else{
			return ServerResponse.createByErrorMessage("需要管理员权限");
		}
	}

	@RequestMapping(value = "update_category.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse setCategoryName(HttpSession session,Integer categoryId, String categoryName){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null){
			return ServerResponse.createByErrorMessage("未登录");
		}
		if (userService.checkAdminRole(user).isSuccess()){
			return categoryService.updateCategory(categoryId,categoryName);
		}else{
			return ServerResponse.createByErrorMessage("需要管理员权限");
		}
	}

	@RequestMapping(value = "list_children_category.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<List<Category>> getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null){
			return ServerResponse.createByErrorMessage("未登录");
		}
		if (userService.checkAdminRole(user).isSuccess()){
			return categoryService.getChildrenParallelCategory(categoryId);
		}else{
			return ServerResponse.createByErrorMessage("需要管理员权限");
		}
	}

	@RequestMapping(value = "list_all_children_category.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<List<Integer>> listCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null){
			return ServerResponse.createByErrorMessage("未登录");
		}
		if (userService.checkAdminRole(user).isSuccess()){
			return categoryService.listCategoryAndChildrenById(categoryId);
		}else{
			return ServerResponse.createByErrorMessage("需要管理员权限");
		}
	}
}