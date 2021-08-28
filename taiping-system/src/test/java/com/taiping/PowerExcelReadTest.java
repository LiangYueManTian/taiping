package com.taiping;

import com.alibaba.fastjson.JSON;
import com.taiping.biz.operation.service.OperationAnalysisService;
import com.taiping.entity.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

/**
 * 运行情况-物业分析-配电系统
 *
 * @author: liyj
 * @date: 2019/12/10 14:35
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class PowerExcelReadTest {


    @Autowired
    OperationAnalysisService excelImportRead;

    /**
     * readTest 测试 获取数据
     */
    @Test
    public void readTest() {
        String excelParh = "excelTest/test.xlsx";
        InputStream resource = getClass().getClassLoader().getResourceAsStream(excelParh);
        try {
            MultipartFile file = new MockMultipartFile("test", resource);
            Result result = excelImportRead.importPowerData(file);
            System.out.println("===========================获取的返回结果........");
            System.out.println(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void queryExcelData() {
        Result result = excelImportRead.queryOperationPower(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
        System.out.println("=========查询结果 ==================");
        System.out.println(JSON.toJSONString(result));
    }

}

