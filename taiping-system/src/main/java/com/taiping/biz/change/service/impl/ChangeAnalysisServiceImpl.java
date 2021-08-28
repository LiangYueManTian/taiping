package com.taiping.biz.change.service.impl;

import com.taiping.biz.change.dao.ChangeAnalysisDao;
import com.taiping.biz.change.service.ChangeAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 变更分析服务实现层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@Service
public class ChangeAnalysisServiceImpl implements ChangeAnalysisService {

    @Autowired
    private ChangeAnalysisDao changeAnalysisDao;
}
