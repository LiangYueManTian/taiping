package com.taiping.biz.sample.controller;

import com.alibaba.fastjson.JSON;
import com.taiping.bean.TaskProcess;
import com.taiping.biz.operation.dao.OperationDao;
import com.taiping.biz.sample.service.SampleService;
import com.taiping.biz.sample.service.impl.WordUtils;
import com.taiping.entity.Result;
import com.taiping.entity.User;
import com.taiping.exception.BizException;
import com.taiping.job.JobSample;
import com.taiping.job.JobService;
import com.taiping.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liyj
 * @date 2019/9/8
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class SampleController {

    @Autowired
    private SampleService service;

    @Autowired
    private JobService jobService;

    @Value("${spring.datasource.url}")
    String url;

    @GetMapping("/getHeart")
    public String getHeart() {
//        List<User> userInfo = service.getUserInfo();
//        if (!ObjectUtils.isEmpty(userInfo)) {
//            return JSON.toJSONString(userInfo);
//        }
//     int i=4;
//     if(i>2){
//         log.info("测试报错!");
//         throw new BizException(1,"200");
//     }


        return url;
    }

    @Autowired
    private OperationDao operationDao;

    @GetMapping("/deletePowerData")
    public Result deletePower() {
        Integer month = LocalDate.now().getMonthValue();
        Integer year = LocalDate.now().getYear();
//
//        operationDao.deleteDistHighByCondition(month, year);
//        operationDao.deleteDistLowByCondition(month, year);
//        operationDao.deleteTransByCondition(month, year);
//        operationDao.deleteChaiFaByCondition(month, year);

        operationDao.deleteImport(0, month, year);


        return ResultUtils.success();

    }

    @GetMapping("/getJobList")
    public Result testJobList() {
        List<TaskProcess> TaskProcesss = jobService.queryTasklist();
        return ResultUtils.success(TaskProcesss);
    }

    @PostMapping("/addjob")
    public Result addjob(@RequestBody TaskProcess info) throws SchedulerException {
        info.setTClass(JobSample.class);
        jobService.addJob(info);
        return ResultUtils.success();
    }

    @PostMapping("/updatejob")
    public Result updateJob(@RequestBody TaskProcess info) {
        info.setTClass(JobSample.class);

        jobService.updateJob(info);
        return ResultUtils.success();
    }

    @PostMapping("/deleteJob")
    public Result djob(@RequestBody TaskProcess info) {
        info.setTClass(JobSample.class);

        jobService.deleteJob(info.getJobName(), info.getJobGroupEnum());
        return ResultUtils.success();
    }

    @PostMapping("/pauseJob")
    public Result pjob(@RequestBody TaskProcess info) {
        jobService.pauseJob(info.getJobName(), info.getJobGroupEnum());
        return ResultUtils.success();
    }

    @PostMapping("/resumeJob")
    public Result resumeJob(@RequestBody TaskProcess info) {
        info.setTClass(JobSample.class);

        jobService.resumeJob(info.getJobName(), info.getJobGroupEnum());
        return ResultUtils.success();
    }

    @Async
    public void executeAsyncTask(int i) {
        System.out.println("Thread" + Thread.currentThread().getName() + " run：" + i);
    }


    @GetMapping("/testExport")
    public void exportWordData(HttpServletRequest request, HttpServletResponse response) {
        WordUtils wordUtil = new WordUtils();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("${powerReport}", "电力容量测试报表");
        params.put("${heared1}", "测试heared");


        try {
            Map<String, Object> header = new HashMap<String, Object>();
            header.put("width", 600);
            header.put("height", 300);
            header.put("type", "png");
            InputStream stream = getClass().getClassLoader().getResourceAsStream("exportTemple/test/powerModule.png");

//            header.put("content", WordUtils.inputStream2ByteArray(new FileInputStream("D:/005TaiPing/test/powerModule.png"), true));
            header.put("content", WordUtils.inputStream2ByteArray(stream, true));
            params.put("${images1}", header);


            String path = "exportTemple/test/powerModuleTemp.docx";  //模板文件位置
            String fileName = new String("testWord.docx".getBytes("UTF-8"), "iso-8859-1");    //生成word文件的文件名
            wordUtil.getWord(path, params, null, fileName, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
