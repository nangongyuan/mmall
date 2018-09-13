/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: IProductService
 * Author:   Administrator
 * Date:     2018/9/13 0013 16:51
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * 〈〉
 *
 * @author Administrator
 * @create 2018/9/13 0013
 * @since 1.0.0
 */
public interface IProductService {
	ServerResponse<String> insertOrUpdateProduct(Product product);

	ServerResponse<String> updateProdectStatus(Integer productId,Integer productStatus);

	ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

	ServerResponse<PageInfo> listProduct(int pageNum, int pageSize);

	ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize);

	ServerResponse<ProductDetailVo> productDetail(Integer productId);

	ServerResponse<PageInfo> searchProductByKeywordCategory(String keyword,Integer categoryId, Integer pageNum,Integer pageSize,String orderBy);
}
