/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: Const
 * Author:   Administrator
 * Date:     2018/9/13 0013 10:16
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 〈〉
 *
 * @author Administrator
 * @create 2018/9/13 0013
 * @since 1.0.0
 */
public class Const {
	public static final String CURRENT_USER = "currentUser";

	public static final String EMAIL = "email";
	public static final String USERNAME = "username";

	public interface ProductListOrderBy{
		Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
	}
	public interface Role{
		int ROLE_CUSTOMER = 0; //普通用户
		int ROLE_ADMIN = 1; //管理员
	}

	public enum ProductStatusEnum{
		ON_SALE(1,"在线");
		private String value;
		private int code;

		ProductStatusEnum(int code, String value){
			this.code = code;
			this.value =value;
		}

		public int getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}
	}
}