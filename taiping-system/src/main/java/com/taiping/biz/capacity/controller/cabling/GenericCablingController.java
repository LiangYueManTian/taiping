package com.taiping.biz.capacity.controller.cabling;

import com.taiping.bean.capacity.cabling.parameter.ConnectRackListParameter;
import com.taiping.bean.capacity.cabling.parameter.GenericCablingListParameter;
import com.taiping.biz.capacity.service.cabling.ConnectRackService;
import com.taiping.biz.capacity.service.cabling.GenericCablingService;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 综合布线控制层
 * @author hedongwei@wistronits.com
 * @date 2019/10/12 9:08
 */
@RestController
@RequestMapping("/taiping/genericCabling")
public class GenericCablingController {

    /**
     * 自动注入配线架逻辑层
     */
    @Autowired
    private ConnectRackService connectRackService;

    /**
     * 自动注入配线架逻辑层
     */
    @Autowired
    private GenericCablingService genericCablingService;

    /**
     * 导入综合布线数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/12 9:17
     * @param file 导入的文件
     * @return 返回导入的结果
     */
    @PostMapping("/importGenericCabling")
    public Result importGenericCabling(@RequestBody MultipartFile file) {
        return genericCablingService.importGenericCabling(file);
    }

    /**
     * 导入配线架数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/12 9:17
     * @param file 导入的文件
     * @return 返回导入的结果
     */
    @PostMapping("/importConnectRack")
    public Result importConnectRack(@RequestBody MultipartFile file) {
        return connectRackService.importConnectRack(file);
    }

    /**
     * 配线架列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/28 11:40
     * @param condition 查询条件
     * @return 查询配线架列表数据
     */
    @PostMapping("/connectRackList")
    public Result connectRackList(@RequestBody QueryCondition<ConnectRackListParameter> condition) {
        return connectRackService.connectRackList(condition);
    }


    /**
     * 综合布线列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/28 14:20
     * @param condition 查询条件
     * @return 返回综合布线列表数据
     */
    @PostMapping("/genericCablingList")
    public Result genericCablingList(@RequestBody QueryCondition<GenericCablingListParameter> condition) {
        return genericCablingService.genericCablingList(condition);
    }

}
