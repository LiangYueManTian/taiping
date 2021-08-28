package com.taiping.biz.sample.service;

import com.taiping.entity.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * 导入机柜Excel服务层
 *
 * @author chaofang@wistronits.com
 * @since 2019-09-09
 */
public interface SampleCabinetService {

    Result test(MultipartFile file);

}
