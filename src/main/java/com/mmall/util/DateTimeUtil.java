/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: DateTimeUtil
 * Author:   Administrator
 * Date:     2018/9/13 0013 17:48
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * 〈〉
 *
 * @author Administrator
 * @create 2018/9/13 0013
 * @since 1.0.0
 */
public class DateTimeUtil {

	public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static Date strToDate(String dateTimeStr, String formatStr){
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
		DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
		return dateTime.toDate();
	}

	public static String dateToStr(Date dateTime, String formatStr){
		if (dateTime == null){
			return StringUtils.EMPTY;
		}
		DateTime dateTime1 = new DateTime(dateTime);
		return dateTime1.toString(formatStr);
	}

	public static Date strToDate(String dateTimeStr){
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
		DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
		return dateTime.toDate();
	}

	public static String dateToStr(Date dateTime){
		if (dateTime == null){
			return StringUtils.EMPTY;
		}
		DateTime dateTime1 = new DateTime(dateTime);
		return dateTime1.toString(STANDARD_FORMAT);
	}

//	public static void main(String[] args) {
//		System.out.println(DateTimeUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
//		System.out.println(DateTimeUtil.strToDate("2000-1-1 20:22:22","yyyy-MM-dd HH:mm:ss"));
//	}
}