package com.taiping.biz.capacity.service.impl.analyze;

import com.taiping.bean.capacity.analyze.parameter.CapacityRelatedViewListParameter;
import com.taiping.biz.capacity.dao.analyze.CapacityThresholdRelatedViewInfoDao;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdRelatedViewInfoService;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedViewInfo;
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
public class CapacityThresholdRelatedViewInfoServiceImpl implements CapacityThresholdRelatedViewInfoService {

    /**
     * 自动注入阈值关联信息持久层
     */
    @Autowired
    private CapacityThresholdRelatedViewInfoDao capacityThresholdRelatedViewInfoDao;



    /**
     * 阈值关联显示信息集合
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:25
     * @param parameter 参数
     * @return 返回阈值关联显示信息集合
     */
    @Override
    public List<CapacityThresholdRelatedViewInfo> queryViewInfoList(CapacityRelatedViewListParameter parameter) {
        return capacityThresholdRelatedViewInfoDao.queryViewInfoList(parameter);
    }

    /**
     * 查询最新的容量关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 14:05
     * @return 返回最新的容量阈值关联数据
     */
    @Override
    public CapacityThresholdRelatedViewInfo queryTopDataByTime() {
        return capacityThresholdRelatedViewInfoDao.queryTopDataByTime();
    }

    /**
     * 批量新增容量关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:43
     * @param list 阈值信息集合
     * @return 返回容量关联显示信息
     */
    @Override
    public int insertThresholdRelatedViewInfoBatch(List<CapacityThresholdRelatedViewInfo> list) {
        return capacityThresholdRelatedViewInfoDao.insertThresholdRelatedViewInfoBatch(list);
    }

    /**
     * 新增容量关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:47
     * @param info 阈值信息
     * @return 返回容量关联显示信息
     */
    @Override
    public int insertThresholdRelatedViewInfo(CapacityThresholdRelatedViewInfo info) {
        return capacityThresholdRelatedViewInfoDao.insertThresholdRelatedViewInfo(info);
    }
}
