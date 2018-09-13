/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ServerResponse
 * Author:   Administrator
 * Date:     2018/9/13 0013 9:32
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mmall.common;


import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * 〈〉
 *
 * @author Administrator
 * @create 2018/9/13 0013
 * @since 1.0.0
 */
//为null时不转json
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {
	private Integer status;
	private String msg;
	private T data;

	private ServerResponse(Integer status){
		this.status = status;
	}

	private ServerResponse(Integer status, T data){
		this.status = status;
		this.data = data;
	}

	private ServerResponse(Integer status, String msg){
		this.status = status;
		this.msg = msg;
	}

	private ServerResponse(Integer status, String msg, T data){
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	//转json时忽略该方法
	@JsonIgnore
	public boolean isSuccess(){
		return status == ResponseCode.SUCCESS.getCode();
	}

	public Integer getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}

	public T getData() {
		return data;
	}

	public static <T> ServerResponse<T> createBySuccess(){
		return new ServerResponse<>(ResponseCode.SUCCESS.getCode());
	}

	public static <T> ServerResponse<T> createBySuccessMessage(String msg){
		return new ServerResponse<>(ResponseCode.SUCCESS.getCode(),msg);
	}

	public static <T> ServerResponse<T> createBySuccessData(T data){
		return new ServerResponse<>(ResponseCode.SUCCESS.getCode(),data);
	}

	public static <T> ServerResponse<T> createBySuccess(String msg,T data){
		return new ServerResponse<>(ResponseCode.SUCCESS.getCode(),msg,data);
	}

	public static <T> ServerResponse<T> createByError(){
		return new ServerResponse<>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
	}

	public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
		return new ServerResponse<>(ResponseCode.SUCCESS.getCode(),errorMessage);
	}

	public static <T> ServerResponse<T> createByError(int code,String errorMessage){
		return new ServerResponse<>(code,errorMessage);
	}
}