package com.taiping.bean.maintenance;

/**
 * @author zhangliangyu
 * @since 2019/11/8
 * 表头对象
 */
public class TableHeader {
    /**
     * KEY
     */
    private Object key;
    /**
     * 表头显示月份字段
     */
    private String monthTitle;
    /**
     * 表头显示周数字段
     */
    private String weekTitle;
    /**
     * 表头宽度
     */
    private String width;

    /**
     * 月份合并列数
     */
    private Integer monthColSpan;


    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public String getMonthTitle() {
        return monthTitle;
    }

    public void setMonthTitle(String monthTitle) {
        this.monthTitle = monthTitle;
    }

    public String getWeekTitle() {
        return weekTitle;
    }

    public void setWeekTitle(String weekTitle) {
        this.weekTitle = weekTitle;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public Integer getMonthColSpan() {
        return monthColSpan;
    }

    public void setMonthColSpan(Integer monthColSpan) {
        this.monthColSpan = monthColSpan;
    }
}
