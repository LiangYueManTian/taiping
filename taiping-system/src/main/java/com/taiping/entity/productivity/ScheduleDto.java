package com.taiping.entity.productivity;

import com.taiping.entity.ExcelReadBean;
import lombok.Data;

import java.util.List;

/**
 * 倒班排班Excel解析实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@Data
public class ScheduleDto extends ExcelReadBean {
    /**
     * 倒班排班
     */
    private List<Schedule> scheduleList;
    /**
     * 日期
     */
    private Long scheduleDate;
    /**
     * 类型
     */
    private String scheduleType;
}
