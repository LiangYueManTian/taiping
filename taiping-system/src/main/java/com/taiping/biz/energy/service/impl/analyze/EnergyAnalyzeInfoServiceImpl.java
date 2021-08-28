package com.taiping.biz.energy.service.impl.analyze;

import com.taiping.bean.energy.parameter.analyze.EnergyAnalyzeDeleteParameter;
import com.taiping.bean.energy.parameter.analyze.EnergyAnalyzeInfoListParameter;
import com.taiping.biz.energy.dao.analyze.EnergyAnalyzeInfoDao;
import com.taiping.biz.energy.service.analyze.EnergyAnalyzeInfoService;
import com.taiping.entity.analyze.energy.EnergyAnalyzeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 能耗分析数据逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/11/19 17:51
 */
@Service
public class EnergyAnalyzeInfoServiceImpl implements EnergyAnalyzeInfoService {

    /**
     * 自动注入能耗分析持久层
     */
    @Autowired
    private EnergyAnalyzeInfoDao energyAnalyzeInfoDao;

    /**
     * 查询能耗分析阈值信息集合
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:16
     * @param parameter 参数信息
     * @return 返回阈值信息集合
     */
    @Override
    public List<EnergyAnalyzeInfo> queryThresholdInfoList(EnergyAnalyzeInfoListParameter parameter) {
        return energyAnalyzeInfoDao.queryThresholdInfoList(parameter);
    }


    /**
     * 查询最新的能耗分析阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 14:05
     * @return 返回最新的能耗分析阈值数据
     */
    @Override
    public EnergyAnalyzeInfo queryTopDataByTime() {
        return energyAnalyzeInfoDao.queryTopDataByTime();
    }


    /**
     * 批量新增能耗分析阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:43
     * @param list 阈值信息集合
     * @return 返回阈值信息
     */
    @Override
    public int insertThresholdInfoBatch(List<EnergyAnalyzeInfo> list) {
        return energyAnalyzeInfoDao.insertThresholdInfoBatch(list);
    }


    /**
     * 新增能耗分析阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:47
     * @param info 阈值信息
     * @return 返回容量阈值信息
     */
    @Override
    public int insertThresholdInfo(EnergyAnalyzeInfo info) {
        return energyAnalyzeInfoDao.insertThresholdInfo(info);
    }


    /**
     * 批量修改运维管理活动类型和处理说明
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:39
     * @param energyThresholdInfoList 修改的对象
     * @return 批量修改运维管理活动类型和处理说明
     */
    @Override
    public int updateAdviceInfoListForActivity(List<EnergyAnalyzeInfo> energyThresholdInfoList) {
        return energyAnalyzeInfoDao.updateAdviceInfoListForActivity(energyThresholdInfoList);
    }

    /**
     * 批量删除分析信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/18 14:19
     * @param parameter 删除分析的条件
     * @return 返回修改的行数
     */
    @Override
    public int deleteAnalyzeBatch(EnergyAnalyzeDeleteParameter parameter) {
        return energyAnalyzeInfoDao.deleteAnalyzeBatch(parameter);
    }
}
