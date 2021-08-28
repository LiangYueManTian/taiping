package com.taiping.biz.sample.controller;

import com.taiping.entity.Result;
import com.taiping.biz.sample.service.SampleCabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 导入机柜Excel控制层
 *
 * @author chaofang@wistronits.com
 * @since 2019-09-09
 */
@RestController
@RequestMapping("/cabinet")
public class SampleCabinetController {

    @Autowired
    private SampleCabinetService sampleCabinetService;

    @PostMapping("/test")
    public Result test(@RequestBody MultipartFile file) {
        return sampleCabinetService.test(file);
    }
}
