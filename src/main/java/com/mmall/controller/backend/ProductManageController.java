/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ProductManageController
 * Author:   Administrator
 * Date:     2018/9/13 0013 16:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 〈〉
 *
 * @author Administrator
 * @create 2018/9/13 0013
 * @since 1.0.0
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IProductService productService;

	@Autowired
	private IFileService fileService;

	@RequestMapping(value = "/insert.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse insertProduct(HttpSession session, Product product){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null){
			return ServerResponse.createByErrorMessage("未登录");
		}
		if (userService.checkAdminRole(user).isSuccess()){
			return productService.insertOrUpdateProduct(product);
		}else{
			return ServerResponse.createByErrorMessage("需要管理员权限");
		}
	}

	@RequestMapping(value = "/update_product_status.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse updateProdectStatus(HttpSession session, Integer productId, Integer productStatus){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null){
			return ServerResponse.createByErrorMessage("未登录");
		}
		if (userService.checkAdminRole(user).isSuccess()){
			return productService.updateProdectStatus(productId,productStatus);
		}else{
			return ServerResponse.createByErrorMessage("需要管理员权限");
		}
	}

	@RequestMapping(value = "/detail.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse getProductDetail(HttpSession session, Integer productId){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null){
			return ServerResponse.createByErrorMessage("未登录");
		}
		if (userService.checkAdminRole(user).isSuccess()){
			return productService.manageProductDetail(productId);
		}else{
			return ServerResponse.createByErrorMessage("需要管理员权限");
		}
	}

	@RequestMapping(value = "/list.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse listProdect(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
									  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null){
			return ServerResponse.createByErrorMessage("未登录");
		}
		if (userService.checkAdminRole(user).isSuccess()){
			return productService.listProduct(pageNum,pageSize);
		}else{
			return ServerResponse.createByErrorMessage("需要管理员权限");
		}
	}

	@RequestMapping(value = "/search.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse searchProduct(HttpSession session,Integer productId, String productName,
										@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
										@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null){
			return ServerResponse.createByErrorMessage("未登录");
		}
		if (userService.checkAdminRole(user).isSuccess()){
			return productService.searchProduct(productName,productId,pageNum,pageSize);
		}else{
			return ServerResponse.createByErrorMessage("需要管理员权限");
		}
	}

	@RequestMapping(value = "/upload.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile multipartFile, HttpServletRequest request){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null){
			return ServerResponse.createByErrorMessage("未登录");
		}
		if (userService.checkAdminRole(user).isSuccess()){
			String path = request.getSession().getServletContext().getRealPath("upload");
			String targetFileName = fileService.upload(multipartFile,path);
			String url = PropertiesUtil.getProperty("server.http.prefix","http://localhost:8080/");
			Map<String,String> fileMap = Maps.newHashMap();
			fileMap.put("uri",targetFileName);
			fileMap.put("url",url);
			return ServerResponse.createBySuccessData(fileMap);
		}else{
			return ServerResponse.createByErrorMessage("需要管理员权限");
		}

	}

	@RequestMapping(value = "/richtext_img_upload.do",method = RequestMethod.POST)
	@ResponseBody
	public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile multipartFile,
								 HttpServletRequest request, HttpServletResponse response){
		Map resultMap = Maps.newHashMap();
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null){
			resultMap.put("success",false);
			resultMap.put("msg","未登录");
			return resultMap;
		}
		if (userService.checkAdminRole(user).isSuccess()){
			//富文本对返回有要求
			String path = request.getSession().getServletContext().getRealPath("upload");
			String targetFileName = fileService.upload(multipartFile,path);
			String url = PropertiesUtil.getProperty("server.http.prefix","http://localhost:8080/");
			if (StringUtils.isBlank(targetFileName)){
				resultMap.put("success",false);
				resultMap.put("msg","上传失败");
				return resultMap;
			}
			resultMap.put("success",true);
			resultMap.put("msg","上传成功");
			resultMap.put("file_path",url);

			response.addHeader("Access-Control-Allow-Headers","X-File-Name");
			return resultMap;
		}else{
			resultMap.put("success",false);
			resultMap.put("msg","权限不足");
			return resultMap;
		}
	}
}