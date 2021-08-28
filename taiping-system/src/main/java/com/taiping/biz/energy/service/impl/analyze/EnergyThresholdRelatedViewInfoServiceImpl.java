package com.taiping.biz.energy.service.impl.analyze;

import com.taiping.bean.energy.parameter.analyze.EnergyRelatedViewListParameter;
import com.taiping.biz.energy.dao.analyze.EnergyThresholdRelatedViewInfoDao;
import com.taiping.biz.energy.service.analyze.EnergyThresholdRelatedViewInfoService;
import com.taiping.entity.analyze.energy.EnergyThresholdRelatedViewInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 能耗关联信息逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/11/19 18:02
 */
@Service
public class EnergyThresholdRelatedViewInfoServiceImpl implements EnergyThresholdRelatedViewInfoService {

    /**
     * 自动注入能耗关联信息持久层
     */
    @Autowired
    private EnergyThresholdRelatedViewInfoDao viewInfoDao;

    /**
     * 阈值关联显示信息集合
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:25
     * @param parameter 参数
     * @return 返回阈值关联显示信息集合
     */
    @Override
    public List<EnergyThresholdRelatedViewInfo> queryViewInfoList(EnergyRelatedViewListParameter parameter) {
        return viewInfoDao.queryViewInfoList(parameter);
    }


    /**
     * 查询最新的能耗关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 14:05
     * @return 返回最新的能耗阈值关联数据
     */
    @Override
    public EnergyThresholdRelatedViewInfo queryTopDataByTime() {
        return viewInfoDao.queryTopDataByTime();
    }


    /**
     * 批量新增能耗关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:43
     * @param list 阈值信息集合
     * @return 返回能耗关联显示信息
     */
    @Override
    public int insertThresholdRelatedViewInfoBatch(List<EnergyThresholdRelatedViewInfo> list) {
        return viewInfoDao.insertThresholdRelatedViewInfoBatch(list);
    }


    /**
     * 新增能耗关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:47
     * @param info 阈值信息
     * @return 返回能耗关联显示信息
     */
    @Override
    public int insertThresholdRelatedViewInfo(EnergyThresholdRelatedViewInfo info) {
        return viewInfoDao.insertThresholdRelatedViewInfo(info);
    }
}
