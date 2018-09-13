/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ProductController
 * Author:   Administrator
 * Date:     2018/9/13 0013 20:21
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 〈〉
 *
 * @author Administrator
 * @create 2018/9/13 0013
 * @since 1.0.0
 */
@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private IProductService productService;

	@RequestMapping(value = "/detail.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<ProductDetailVo> getDetail(Integer productId){
		return productService.productDetail(productId);
	}

	public ServerResponse<PageInfo> listProduct(@RequestParam(value = "keyword", required = false) String keyword,
												@RequestParam(value = "categoryId", required = false)Integer categoryId,
												@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
												@RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize,
												@RequestParam(value = "orderBy", defaultValue = "")String orderBy){
		return productService.searchProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
	}
}