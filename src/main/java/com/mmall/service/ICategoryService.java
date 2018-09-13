/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ICategoryService
 * Author:   Administrator
 * Date:     2018/9/13 0013 14:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * 〈〉
 *
 * @author Administrator
 * @create 2018/9/13 0013
 * @since 1.0.0
 */
public interface ICategoryService {
	ServerResponse<String> insertCategory(String categoryName, Integer parentId);

	ServerResponse<String> updateCategory(Integer categoryId, String categoryName);

	ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

	ServerResponse<List<Integer>> listCategoryAndChildrenById(Integer categoryId);
}
