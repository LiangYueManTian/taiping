package com.taiping.biz.energy.service.impl.analyze;

import com.taiping.bean.energy.parameter.analyze.EnergyThresholdInfoListParameter;
import com.taiping.biz.energy.dao.analyze.EnergyThresholdInfoDao;
import com.taiping.biz.energy.service.analyze.EnergyThresholdInfoService;
import com.taiping.entity.analyze.energy.EnergyThresholdInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 能耗分析信息逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/11/19 17:58
 */
@Service
public class EnergyThresholdInfoServiceImpl implements EnergyThresholdInfoService {

    /**
     * 能耗持久层
     */
    @Autowired
    private EnergyThresholdInfoDao energyThresholdInfoDao;

    /**
     * 查询分析信息集合
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:16
     * @param parameter 参数信息
     * @return 返回分析信息集合
     */
    @Override
    public List<EnergyThresholdInfo> queryThresholdInfoList(EnergyThresholdInfoListParameter parameter) {
        return energyThresholdInfoDao.queryThresholdInfoList(parameter);
    }


    /**
     * 查询最新的能耗阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 14:05
     * @return 返回最新的空间容量阈值数据
     */
    @Override
    public EnergyThresholdInfo queryTopDataByTime() {
        return energyThresholdInfoDao.queryTopDataByTime();
    }


    /**
     * 批量新增能耗阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:43
     * @param list 阈值信息集合
     * @return 返回阈值信息
     */
    @Override
    public int insertThresholdInfoBatch(List<EnergyThresholdInfo> list) {
        return energyThresholdInfoDao.insertThresholdInfoBatch(list);
    }



    /**
     * 新增能耗阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:47
     * @param info 阈值信息
     * @return 返回能耗阈值信息
     */
    @Override
    public int insertThresholdInfo(EnergyThresholdInfo info) {
        return energyThresholdInfoDao.insertThresholdInfo(info);
    }


    /**
     * 批量修改运维管理活动类型和处理说明
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:39
     * @param energyThresholdInfoList 修改的对象
     * @return 批量修改运维管理活动类型和处理说明
     */
    @Override
    public int updateAdviceInfoListForActivity(List<EnergyThresholdInfo> energyThresholdInfoList) {
        return energyThresholdInfoDao.updateAdviceInfoListForActivity(energyThresholdInfoList);
    }
}
