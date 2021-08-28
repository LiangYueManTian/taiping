package com.taiping.biz.change.controller;

import com.taiping.biz.change.service.ChangeAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 变更分析控制层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@RestController
@RequestMapping("/taiping/change")
public class ChangeAnalysisController {

    @Autowired
    private ChangeAnalysisService changeAnalysisService;
}
