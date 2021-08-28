package com.taiping.biz.personwork.controller;

import com.taiping.biz.personwork.service.PersonWorkService;
import com.taiping.constant.SystemResultCode;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.personwork.PersonWork;
import com.taiping.exception.BizException;
import com.taiping.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 个人工作台 控制层接口
 *
 * @author liyj
 * @date 2019/11/11
 */
@RestController
@Slf4j
@RequestMapping("/taiping/person")
public class PersonWorkController {

    /**
     * service
     */
    @Autowired
    private PersonWorkService service;

    /**
     * 获取个人工作台信息
     *
     * @param
     * @return
     */
    @PostMapping("/queryPersonWork")
    public Result queryPersonWork(@RequestBody QueryCondition<PersonWork> queryCondition) {
        PersonWork personWork = queryCondition.getBizCondition();
        if (personWork == null || personWork.getUserId() == null || personWork.getType() == null) {
            log.error("queryPersonWorkByType type is null");
            throw new BizException(SystemResultCode.PERSON_TYPE_IS_NULL, "获取个人工作台参数错误!");
        }

        return service.queryPersonWork(queryCondition);
    }

    /**
     * 获取个人工作台总数
     *
     * @return
     */
    @GetMapping("/queryPersonWorkTotal/{userId}")
    public Result queryPersonWorkTotal(@PathVariable("userId") String userId) {
        if (StringUtils.isEmpty(userId)) {
            log.error("queryPersonWorkTotal type is null");
            throw new BizException(SystemResultCode.PERSON_USER_ID_IS_NULL, "获取个人工作台参数错误!");
        }
        return ResultUtils.success(service.queryPersonWorkTotal(userId));
    }


}
