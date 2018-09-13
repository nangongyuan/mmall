/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: IFileService
 * Author:   Administrator
 * Date:     2018/9/13 0013 19:45
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 〈〉
 *
 * @author Administrator
 * @create 2018/9/13 0013
 * @since 1.0.0
 */
public interface IFileService {
	String upload(MultipartFile file, String path);
}
