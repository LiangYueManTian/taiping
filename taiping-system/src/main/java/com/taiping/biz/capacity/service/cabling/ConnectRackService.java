package com.taiping.biz.capacity.service.cabling;

import com.taiping.bean.capacity.cabling.dto.ConnectRackDto;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 配线架逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/10/12 9:22
 */
public interface ConnectRackService {


    /**
     * 查询配线架列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 14:53
     * @param queryCondition 查询条件
     * @return 查询配线架列表数据
     */
    List<ConnectRackDto> queryConnectRackList(QueryCondition queryCondition);


    /**
     * 查询配线架列表个数
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 14:53
     * @param queryCondition 查询条件
     * @return 配线架列表个数
     */
    int queryConnectRackCount(QueryCondition queryCondition);


    /**
     * 查询配线架列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 13:54
     * @param condition 条件
     * @return 返回机柜基础数据列表的信息
     */
    Result connectRackList(QueryCondition condition);

    /**
     * 导入配线架数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/12 9:17
     * @param file 导入的文件
     * @return 返回导入的结果
     */
    Result importConnectRack(@RequestBody MultipartFile file);

}
