package com.taiping.entity;

import lombok.Data;

/**
 * 综合布线实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-09-05
 */
@Data
public class SampleIntegratedWiring extends ExcelReadBean {

    private String odfName;

    private String portName;

    private String odfCode;

    private String odfPlace;

    private String baseWiring;

    private String endOdfName;

    private String endOdfPort;

    private String endOdfPlace;

    private String status;
}
