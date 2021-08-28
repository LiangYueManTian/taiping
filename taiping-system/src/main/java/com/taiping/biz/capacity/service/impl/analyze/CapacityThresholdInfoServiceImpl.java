package com.taiping.biz.capacity.service.impl.analyze;

import com.taiping.biz.capacity.dao.analyze.CapacityThresholdInfoDao;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdInfoService;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 容量逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 17:03
 */
@Service
@Slf4j
public class CapacityThresholdInfoServiceImpl implements CapacityThresholdInfoService {

    /**
     * 自动注入容量阈值关联信息持久层
     */
    @Autowired
    private CapacityThresholdInfoDao capacityThresholdInfoDao;

    /**
     * 查询阈值信息集合
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:16
     * @param capacityThresholdInfo 参数信息
     * @return 返回阈值信息集合
     */
    @Override
    public List<CapacityThresholdInfo> queryThresholdInfoList(CapacityThresholdInfo capacityThresholdInfo) {
        return capacityThresholdInfoDao.queryThresholdInfoList(capacityThresholdInfo);
    }

    /**
     * 查询最新的容量阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 14:05
     * @return 返回最新的空间容量阈值数据
     */
    @Override
    public CapacityThresholdInfo queryTopDataByTime() {
        return capacityThresholdInfoDao.queryTopDataByTime();
    }

    /**
     * 批量新增容量阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:43
     * @param list 阈值信息集合
     * @return 返回阈值信息
     */
    @Override
    public int insertThresholdInfoBatch(List<CapacityThresholdInfo> list) {
        return capacityThresholdInfoDao.insertThresholdInfoBatch(list);
    }

    /**
     * 新增容量阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:47
     * @param info 阈值信息
     * @return 返回容量阈值信息
     */
    @Override
    public int insertThresholdInfo(CapacityThresholdInfo info) {
        return capacityThresholdInfoDao.insertThresholdInfo(info);
    }


    /**
     * 批量修改运维管理活动类型和处理说明
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:39
     * @param capacityThresholdInfoList 修改的对象
     * @return 批量修改运维管理活动类型和处理说明
     */
    @Override
    public int updateAdviceInfoListForActivity(List<CapacityThresholdInfo> capacityThresholdInfoList) {
        return capacityThresholdInfoDao.updateAdviceInfoListForActivity(capacityThresholdInfoList);
    }
}
