package com.taiping.entity.user;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * @author zhangliangyu
 * @since 2019/9/25
 * 菜单信息实体
 */
@Data
@TableName("t_menuInfo")
public class MenuInfo {

    /**
     *菜单id
     */
    private String menuId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 菜单指向（菜单请求路径）
     */
    private String menuHref;
    /**
     * 父级菜单编码
     */
    private String parentMenuId;
    /**
     * 菜单级别
     */
    private Integer menuLevel;
    /**
     * 菜单排序
     */
    private Integer menuSort;

    /**
     * 菜单code码
     */
    private String menuCode;

}
