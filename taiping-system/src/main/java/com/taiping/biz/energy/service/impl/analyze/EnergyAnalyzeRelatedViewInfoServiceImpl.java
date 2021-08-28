package com.taiping.biz.energy.service.impl.analyze;

import com.taiping.bean.energy.parameter.analyze.EnergyAnalyzeDeleteViewParameter;
import com.taiping.bean.energy.parameter.analyze.EnergyAnalyzeRelatedViewListParameter;
import com.taiping.biz.energy.dao.analyze.EnergyAnalyzeRelatedViewInfoDao;
import com.taiping.biz.energy.service.analyze.EnergyAnalyzeRelatedViewInfoService;
import com.taiping.entity.analyze.energy.EnergyAnalyzeRelatedViewInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 能耗分析查询逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/11/19 17:54
 */
@Service
public class EnergyAnalyzeRelatedViewInfoServiceImpl implements EnergyAnalyzeRelatedViewInfoService {

    /**
     * 自动注入查询关联信息持久层
     */
    @Autowired
    private EnergyAnalyzeRelatedViewInfoDao viewInfoDao;

    /**
     * 阈值关联显示信息集合
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:25
     * @param parameter 参数
     * @return 返回阈值关联显示信息集合
     */
    @Override
    public List<EnergyAnalyzeRelatedViewInfo> queryViewInfoList(EnergyAnalyzeRelatedViewListParameter parameter) {
        return viewInfoDao.queryViewInfoList(parameter);
    }


    /**
     * 查询最新的能耗关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 14:05
     * @return 返回最新的能耗阈值关联数据
     */
    @Override
    public EnergyAnalyzeRelatedViewInfo queryTopDataByTime() {
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
    public int insertThresholdRelatedViewInfoBatch(List<EnergyAnalyzeRelatedViewInfo> list) {
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
    public int insertThresholdRelatedViewInfo(EnergyAnalyzeRelatedViewInfo info) {
        return viewInfoDao.insertThresholdRelatedViewInfo(info);
    }


    /**
     * 批量删除分析关联信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/18 14:11
     * @param parameter 删除参数
     * @return 返回值
     */
    @Override
    public int deleteAnalyzeRelatedInfoBatch(EnergyAnalyzeDeleteViewParameter parameter) {
        return viewInfoDao.deleteAnalyzeRelatedInfoBatch(parameter);
    }
}
