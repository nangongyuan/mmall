/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ProductServiceImpl
 * Author:   Administrator
 * Date:     2018/9/13 0013 16:51
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈〉
 *
 * @author Administrator
 * @create 2018/9/13 0013
 * @since 1.0.0
 */
@Service
public class ProductServiceImpl {

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private ICategoryService categoryService;

	public ServerResponse<String> insertOrUpdateProduct(Product product){
		if (product != null){
			if (StringUtils.isNotBlank(product.getSubImages())){
				String[] subImageArray = product.getSubImages().split(",");
				if (subImageArray.length>0){
					product.setMainImage(subImageArray[0]);
				}
			}
			if (product.getId() != null){
				int result = productMapper.updateByPrimaryKey(product);
				if (result>0){
					return ServerResponse.createBySuccessMessage("产品更新成功");
				}
				ServerResponse.createByErrorMessage("产品更新失败");
			}else{
				int result = productMapper.insert(product);
				if (result>0){
					return ServerResponse.createBySuccessMessage("新增产品成功");
				}
				ServerResponse.createByErrorMessage("新增产品失败");
			}
		}
		return ServerResponse.createByErrorMessage("新增或更新产品参数不正确");
	}

	public ServerResponse<String> updateProdectStatus(Integer productId,Integer productStatus){
		if (productId == null || productStatus==null){
			return ServerResponse.createByErrorMessage("参数错误");
		}
		Product product = new Product();
		product.setId(productId);
		product.setStatus(productStatus);
		int result = productMapper.updateByPrimaryKeySelective(product);
		if (result>0){
			return ServerResponse.createBySuccessMessage("修改产品状态成功");
		}
		return ServerResponse.createByErrorMessage("修改产品状态失败");
	}

	public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
		if (productId == null){
			return ServerResponse.createByErrorMessage("参数错误");
		}
		Product product = productMapper.selectByPrimaryKey(productId);
		if (product == null){
			return ServerResponse.createByErrorMessage("产品不存在");
		}
		ProductDetailVo detailVo = assembleProductDetailVo(product);;

		return ServerResponse.createBySuccessData(detailVo);

	}

	private ProductDetailVo assembleProductDetailVo(Product product){
		ProductDetailVo vo = new ProductDetailVo();
		vo.setId(product.getId());
		vo.setName(product.getName());
		vo.setSubtitle(product.getSubtitle());
		vo.setPrice(product.getPrice());
		vo.setMainImage(product.getMainImage());
		vo.setSubImages(product.getSubImages());
		vo.setCategoryId(product.getCategoryId());
		vo.setDetail(product.getDetail());
		vo.setName(product.getName());
		vo.setStatus(product.getStatus());
		vo.setStock(product.getStock());

		vo.setImageHost(PropertiesUtil.getProperty("server.http.prefix","http://localhost:8080/"));

		Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
		if (category == null){
			vo.setParentCategoryId(0);
		}else{
			vo.setParentCategoryId(category.getParentId());
		}
		vo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
		vo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
		return vo;
	}

	public ServerResponse<PageInfo> listProduct(int pageNum, int pageSize){
		PageHelper.startPage(pageNum,pageSize);
		List<Product> productList = productMapper.listProdect();

		List<ProductListVo> productListVoList = new ArrayList<ProductListVo>();
		for (Product productItem : productList){
			ProductListVo productListVo = assembleProductListVo(productItem);
			productListVoList.add(productListVo);
		}
		PageInfo pageInfo = new PageInfo(productList);
		pageInfo.setList(productListVoList);
		return ServerResponse.createBySuccessData(pageInfo);
	}

	private ProductListVo assembleProductListVo(Product product){
		ProductListVo vo = new ProductListVo();
		vo.setId(product.getId());
		vo.setName(product.getName());
		vo.setSubtitle(product.getSubtitle());
		vo.setCategoryId(product.getCategoryId());
		vo.setPrice(product.getPrice());
		vo.setMainImage(product.getMainImage());
		vo.setImageHost(PropertiesUtil.getProperty("server.http.prefix","http://localhost:8080/"));
		return vo;
	}

	public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize){
		PageHelper.startPage(pageNum, pageSize);
		if (StringUtils.isNotBlank(productName)){
			productName = new StringBuilder().append("%").append(productName).append("%").toString();
		}
		List<Product> productList = productMapper.searchProdect(productName,productId);
		List<ProductListVo> productListVoList = new ArrayList<ProductListVo>();
		for (Product productItem : productList){
			ProductListVo productListVo = assembleProductListVo(productItem);
			productListVoList.add(productListVo);
		}
		PageInfo pageInfo = new PageInfo(productList);
		pageInfo.setList(productListVoList);
		return ServerResponse.createBySuccessData(pageInfo);
	}

	public ServerResponse<ProductDetailVo> productDetail(Integer productId){
		if (productId == null){
			return ServerResponse.createByErrorMessage("参数错误");
		}
		Product product = productMapper.selectByPrimaryKey(productId);
		if (product == null){
			return ServerResponse.createByErrorMessage("产品不存在");
		}
		if (product.getStatus()!= Const.ProductStatusEnum.ON_SALE.getCode()){
			return ServerResponse.createByErrorMessage("产品已下线");
		}
		ProductDetailVo detailVo = assembleProductDetailVo(product);;
		return ServerResponse.createBySuccessData(detailVo);
	}

	public ServerResponse<PageInfo> searchProductByKeywordCategory(String keyword,Integer categoryId, Integer pageNum,Integer pageSize,String orderBy){
		if (StringUtils.isBlank(keyword) && categoryId==null){
			return ServerResponse.createByErrorMessage("参数错误");
		}
		List<Integer> categoryIdList = new ArrayList<Integer>();
		if (categoryId != null){
			Category category = categoryMapper.selectByPrimaryKey(categoryId);
			if (category == null && StringUtils.isBlank(keyword)){
				//没有改分类，并且没有关键字 返回空集
				PageHelper.startPage(pageNum,pageSize);
				List<ProductListVo> productListVoList = Lists.newArrayList();
				PageInfo pageInfo = new PageInfo(productListVoList);
				return ServerResponse.createBySuccessData(pageInfo);
			}
			categoryIdList = categoryService.listCategoryAndChildrenById(categoryId).getData();
		}
		if (StringUtils.isNotBlank(keyword)){
			keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
		}
		PageHelper.startPage(pageNum,pageSize);
		if (StringUtils.isNotBlank(orderBy)){
			if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
				String[] orderByArray = orderBy.split("_");
				PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
			}
		}
		List<Product> productList = productMapper.listProductByNameAndCategoryIds(StringUtils.isBlank(keyword)? null : keyword,
				categoryIdList.size()==0 ? null : categoryIdList);

		List<ProductListVo> productListVoList = Lists.newArrayList();
		for (Product item : productList){
			ProductListVo productListVo = assembleProductListVo(item);
			productListVoList.add(productListVo);
		}
		PageInfo pageInfo = new PageInfo(productList);
		pageInfo.setList(productListVoList);
		return ServerResponse.createBySuccessData(pageInfo);
	}
}