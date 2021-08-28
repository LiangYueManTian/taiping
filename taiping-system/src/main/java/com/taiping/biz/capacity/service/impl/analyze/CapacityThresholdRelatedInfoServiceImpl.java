package com.taiping.biz.capacity.service.impl.analyze;

import com.taiping.biz.capacity.dao.analyze.CapacityThresholdRelatedInfoDao;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdRelatedInfoService;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 容量逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 17:03
 */
@Service
@Slf4j
public class CapacityThresholdRelatedInfoServiceImpl implements CapacityThresholdRelatedInfoService {


    /**
     * 自动注入阈值关联信息持久层
     */
    @Autowired
    private CapacityThresholdRelatedInfoDao capacityThresholdRelatedInfoDao;

    /**
     * 查询最新的容量阈值关联信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 14:05
     * @return 返回最新的容量阈值关联数据
     */
    @Override
    public CapacityThresholdRelatedInfo queryTopDataByTime() {
        return capacityThresholdRelatedInfoDao.queryTopDataByTime();
    }

    /**
     * 批量新增容量阈值关联信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:43
     * @param list 阈值信息集合
     * @return 返回阈值信息
     */
    @Override
    public int insertThresholdRelatedBatch(List<CapacityThresholdRelatedInfo> list) {
        return capacityThresholdRelatedInfoDao.insertThresholdRelatedBatch(list);
    }

    /**
     * 新增容量阈值关联信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:47
     * @param info 阈值信息
     * @return 返回容量阈值关联信息
     */
    @Override
    public int insertThresholdRelated(CapacityThresholdRelatedInfo info) {
        return capacityThresholdRelatedInfoDao.insertThresholdRelated(info);
    }
}
