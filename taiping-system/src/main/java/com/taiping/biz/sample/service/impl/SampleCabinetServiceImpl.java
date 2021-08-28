package com.taiping.biz.sample.service.impl;

import com.taiping.entity.SampleCabinet;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.SampleIntegratedWiring;
import com.taiping.entity.Result;
import com.taiping.enums.SampleCabinetSheetEnum;
import com.taiping.biz.sample.service.SampleCabinetService;
import com.taiping.read.sample.SampleCabinetExcelImportRead;
import com.taiping.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 导入机柜Excel服务实现层
 *
 * @author chaofang@wistronits.com
 * @since 2019-09-09
 */
@Service
@Slf4j
public class SampleCabinetServiceImpl implements SampleCabinetService {

    @Autowired
    private SampleCabinetExcelImportRead sampleCabinetExcelImportRead;

    @Override
    public Result test(MultipartFile file) {
        Map<String, List<ExcelReadBean>> stringListMap = null;
        try {
            stringListMap = sampleCabinetExcelImportRead.readExcel(file);
            List<SampleCabinet> sampleCabinets = (List) stringListMap.get(SampleCabinetSheetEnum.SHEET_ONE.getSheetName());
            List<SampleIntegratedWiring> sampleIntegratedWiringList = (List) stringListMap.get(SampleCabinetSheetEnum.SHEET_TWO.getSheetName());
            for (SampleCabinet sampleCabinet : sampleCabinets) {
                System.out.println(sampleCabinet.toString());
            }
            for (SampleIntegratedWiring sampleIntegratedWiring : sampleIntegratedWiringList) {
                System.out.println(sampleIntegratedWiring.toString());
            }
        } catch (IOException | InvalidFormatException e) {
            log.error("file read exception", e);
        }
        return ResultUtils.success();
    }
}
