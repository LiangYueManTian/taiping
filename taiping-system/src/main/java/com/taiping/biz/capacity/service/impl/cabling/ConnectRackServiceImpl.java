package com.taiping.biz.capacity.service.impl.cabling;

import com.baomidou.mybatisplus.plugins.Page;
import com.taiping.bean.capacity.cabling.dto.ConnectRackDto;
import com.taiping.biz.capacity.dao.cabling.ConnectRackDao;
import com.taiping.biz.capacity.service.cabling.ConnectRackService;
import com.taiping.constant.capacity.CapacityResultCode;
import com.taiping.constant.capacity.CapacityResultMsg;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.cabling.ConnectRack;
import com.taiping.enums.cabling.ConnectRackSheetEnum;
import com.taiping.exception.BizException;
import com.taiping.read.cabling.ConnectRackExcelImportRead;
import com.taiping.utils.MpQueryHelper;
import com.taiping.utils.ResultUtils;
import com.taiping.utils.common.CheckConditionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 配线架逻辑实现类
 * @author hedongwei@wistronits.com
 * @date 2019/10/12 9:35
 */
@Service
@Slf4j
public class ConnectRackServiceImpl implements ConnectRackService {

    /**
     * 自动注入配线架导入数据
     */
    @Autowired
    private ConnectRackExcelImportRead connectRackExcelImportRead;

    /**
     * 自动注入配线架持久层
     */
    @Autowired
    private ConnectRackDao connectRackDao;


    /**
     * 查询配线架列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 14:53
     * @param queryCondition 查询条件
     * @return 查询配线架列表数据
     */
    @Override
    public List<ConnectRackDto> queryConnectRackList(QueryCondition queryCondition) {
        return connectRackDao.queryConnectRackList(queryCondition);
    }


    /**
     * 查询配线架列表个数
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 14:53
     * @param queryCondition 查询条件
     * @return 配线架列表个数
     */
    @Override
    public int queryConnectRackCount(QueryCondition queryCondition) {
        return connectRackDao.queryConnectRackCount(queryCondition);
    }

    /**
     * 查询配线架列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 13:54
     * @param condition 条件
     * @return 返回机柜基础数据列表的信息
     */
    @Override
    public Result connectRackList(QueryCondition condition) {
        //校验参数是否正确
        if (null != CheckConditionUtil.checkQueryConditionParam(condition)) {
            return CheckConditionUtil.checkQueryConditionParam(condition);
        }
        //设置默认条件
        condition = CheckConditionUtil.filterQueryConditionByAsc(condition);

        //设置分页beginNum
        Integer beginNum = (condition.getPageCondition().getPageNum() - 1) * condition.getPageCondition().getPageSize();
        condition.getPageCondition().setBeginNum(beginNum);
        // 构造分页条件
        Page page = MpQueryHelper.myBatiesBuildPage(condition);
        //查询数据结果
        List<ConnectRackDto> baseConnectRackDtoList = this.queryConnectRackList(condition);
        //查询数据个数
        Integer count = this.queryConnectRackCount(condition);
        // 构造返回结果
        PageBean pageBean = MpQueryHelper.myBatiesBuildPageBean(page, count, baseConnectRackDtoList);
        // 返回数据
        return ResultUtils.pageSuccess(pageBean);
    }

    /**
     * 导入配线架数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/12 9:17
     * @param file 导入的文件
     * @return 返回导入的结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result importConnectRack(MultipartFile file) {
        Map<String, List<ExcelReadBean>> stringListMap = null;
        List<ConnectRack> connectRackList = new ArrayList<>();
        try {
            //获取配线架读取数据map
            stringListMap = connectRackExcelImportRead.readExcel(file);
            //获取配线架数据
            connectRackList = (List) stringListMap.get(ConnectRackSheetEnum.SHEET_ONE.getSheetName());
            for (ConnectRack connectRackOne : connectRackList) {
                System.out.println(connectRackOne.toString());
            }

            //删除配线架数据
            connectRackDao.deleteAllConnectRack();

            if (!ObjectUtils.isEmpty(connectRackList)) {
                //批量新增配线架数据
                connectRackDao.insertConnectRackBatch(connectRackList);
            }
        } catch (Exception e) {
            log.error("file read exception", e);
            throw new BizException(CapacityResultCode.IMPORT_CONNECT_RACK_ERROR, CapacityResultMsg.IMPORT_CONNECT_RACK_ERROR);
        }
        return ResultUtils.success();
    }
}
