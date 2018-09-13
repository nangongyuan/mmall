/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: CategoryServiceImpl
 * Author:   Administrator
 * Date:     2018/9/13 0013 14:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * 〈〉
 *
 * @author Administrator
 * @create 2018/9/13 0013
 * @since 1.0.0
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

	private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public ServerResponse<String> insertCategory(String categoryName, Integer parentId) {
		if (parentId == null || StringUtils.isBlank(categoryName)){
			return ServerResponse.createByErrorMessage("添加品类参数错误");
		}
		Category category = new Category();
		category.setName(categoryName);
		category.setParentId(parentId);
		category.setStatus(true);

		int result = categoryMapper.insert(category);
		if (result>0){
			return ServerResponse.createBySuccessMessage("添加品类成功");
		}
		return ServerResponse.createByErrorMessage("添加品类失败");
	}

	@Override
	public ServerResponse<String> updateCategory(Integer categoryId, String categoryName) {
		if (categoryId == null || StringUtils.isBlank(categoryName)){
			return ServerResponse.createByErrorMessage("添加品类参数错误");
		}
		Category category  = new Category();
		category.setId(categoryId);
		category.setName(categoryName);

		int result = categoryMapper.updateByPrimaryKeySelective(category);
		if (result>0){
			return ServerResponse.createBySuccessMessage("更新品类成功");
		}
		return ServerResponse.createByErrorMessage("更新品类失败");
	}

	@Override
	public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
		List<Category> categoryList = categoryMapper.listCategoryChildrenByParentId(categoryId);
		if (CollectionUtils.isEmpty(categoryList)){
			logger.info("未找到当前分类的子分类");
		}
		return ServerResponse.createBySuccessData(categoryList);
	}

	@Override
	public ServerResponse<List<Integer>> listCategoryAndChildrenById(Integer categoryId) {
		Set<Category> categorySet = Sets.newHashSet();
		findChildCategory(categorySet,categoryId);
		List<Integer> categoryIdList = Lists.newArrayList();
		if (categoryId != null){
			for(Category categoryItem : categorySet){
				categoryIdList.add(categoryItem.getId());
			}
		}
		return ServerResponse.createBySuccessData(categoryIdList);
	}

	private Set<Category> findChildCategory(Set<Category> categorySet,Integer categoryId){
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category != null){
			categorySet.add(category);
		}
		List<Category> categoryList = categoryMapper.listCategoryChildrenByParentId(categoryId);
		for (Category c : categoryList) {
			findChildCategory(categorySet,c.getId());
		}
		return categorySet;
	}
}