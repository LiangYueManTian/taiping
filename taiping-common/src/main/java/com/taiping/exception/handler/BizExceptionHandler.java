package com.taiping.exception.handler;

import com.taiping.entity.Result;
import com.taiping.exception.BizException;
import com.taiping.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常捕获
 *
 * @author liyj
 * @date 2019/10/16
 */
@RestControllerAdvice
@Slf4j
public class BizExceptionHandler {
    /**
     * 捕获业务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result handlerBizException(BizException ex) {
        log.info(ex.getMessage());
        log.info(ex.getStackTrace().toString());
//        ex.printStackTrace();
        //这里后续国际化
        return ResultUtils.warn(ex.getCode(), ex.getMsg());
    }

}
