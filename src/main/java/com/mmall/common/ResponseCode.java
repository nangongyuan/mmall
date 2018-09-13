/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ResponseCode
 * Author:   Administrator
 * Date:     2018/9/13 0013 9:39
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mmall.common;

/**
 * 〈〉
 *
 * @author Administrator
 * @create 2018/9/13 0013
 * @since 1.0.0
 */
public enum ResponseCode {
	SUCCESS(0,"SUCCESS"),
	ERROR(1,"ERROR"),
	NEED_LOGIN(10,"NEED_LOGIN"),
	ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

	private final int code;
	private final String desc;

	ResponseCode(int code,String desc){
		this.code = code;
		this.desc = desc;
	}

	public int getCode(){
		return code;
	}

	public String getDesc() {
		return desc;
	}
}