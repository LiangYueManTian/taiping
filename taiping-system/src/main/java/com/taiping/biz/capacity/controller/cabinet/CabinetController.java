package com.taiping.biz.capacity.controller.cabinet;

import com.taiping.bean.capacity.cabinet.parameter.BaseCabinetListParameter;
import com.taiping.bean.capacity.cabinet.parameter.CabinetInfoListParameter;
import com.taiping.bean.capacity.cabinet.parameter.ItEnergyListParameter;
import com.taiping.biz.capacity.service.cabinet.BaseCabinetService;
import com.taiping.biz.capacity.service.cabinet.CabinetService;
import com.taiping.biz.capacity.service.cabinet.ItEnergyCurrentService;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.cabinet.BaseCabinet;
import com.taiping.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 机柜控制层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 16:23
 */
@RestController
@RequestMapping("/taiping/cabinetInfo")
public class CabinetController {

    /**
     * 机柜逻辑层
     */
    @Autowired
    private CabinetService cabinetService;

    /**
     * 基础机柜逻辑层
     */
    @Autowired
    private BaseCabinetService baseCabinetService;

    /**
     * it能耗逻辑层
     */
    @Autowired
    private ItEnergyCurrentService itEnergyCurrentService;

    /**
     * 导入机柜基础数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/10 16:28
     * @param file 导入的文件
     * @return 返回导入数据的结果
     */
    @PostMapping("/importBaseCabinet")
    public Result importBaseCabinet(@RequestBody MultipartFile file) {
        return baseCabinetService.importBaseCabinet(file);
    }


    /**
     * 导入机柜信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/10 16:29
     * @param file 导入的文件
     * @return 返回导入数据的结果
     */
    @PostMapping("/importCabinet")
    public Result importCabinet(@RequestBody MultipartFile file) {
        return cabinetService.importCabinet(file);
    }



    /**
     * 导入it能耗数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 14:33
     * @param file 文件流
     * @return 返回导入文件结果
     */
    @PostMapping("/importItEnergy")
    public Result importItEnergy(@RequestBody MultipartFile file) {
        return itEnergyCurrentService.importItEnergy(file);
    }

    /**
     * 机柜基础信息列表参数
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 13:50
     * @param condition 筛选列表条件
     * @return 返回机柜基础信息列表的数据
     */
    @PostMapping("/cabinetBaseList")
    public Result cabinetBaseList(@RequestBody QueryCondition<BaseCabinetListParameter> condition) {
        return baseCabinetService.cabinetBaseList(condition);
    }


    /**
     * 机柜信息列表参数
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 13:50
     * @param condition 筛选列表条件
     * @return 返回机柜信息列表的数据
     */
    @PostMapping("/cabinetList")
    public Result cabinetList(@RequestBody QueryCondition<CabinetInfoListParameter> condition) {
        return cabinetService.cabinetList(condition);
    }



    /**
     * it能耗列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 13:50
     * @param condition 筛选列表条件
     * @return 返回it能耗列表的数据
     */
    @PostMapping("/itEnergyList")
    public Result itEnergyList(@RequestBody ItEnergyListParameter condition) {
        return ResultUtils.success(itEnergyCurrentService.queryItEnergyListNotPage(condition));
    }

    /**
     * 根据机柜唯一标识修改机柜基础信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/11 10:11
     * @param baseCabinet 机柜父级信息表
     * @return 返回机柜基础信息修改结果
     */
    @PostMapping("/updateBaseCabinetByCabinetName")
    public Result updateBaseCabinetByCabinetName(@RequestBody BaseCabinet baseCabinet) {
        //修改机柜基础信息
        baseCabinetService.updateBaseCabinetByCabinetName(baseCabinet);
        return ResultUtils.success();
    }


}
