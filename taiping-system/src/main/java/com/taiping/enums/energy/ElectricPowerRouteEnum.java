package com.taiping.enums.energy;

/**
 * 动力能耗数据枚举类
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum ElectricPowerRouteEnum {

    /**
     *  A路
     */
    A_ROUTE("A路", "1"),


    /**
     *  B路
     */
    B_ROUTE("B路", "2");


    private String routeName;

    private String route;


    /**
     * 根据数据类型获取code
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param name 数据名称
     * @return 类型code
     */
    public static String getRouteByName(String name) {
        for (ElectricPowerRouteEnum routeEnum : ElectricPowerRouteEnum.values()) {
            if (routeEnum.getRouteName().equals(name)) {
                return routeEnum.getRoute();
            }
        }
        return "";
    }


    /**
     * 根据数据类型获取名称
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param code 数据code
     * @return 类型名称
     */
    public static String getRouteNameByCode(String code) {
        for (ElectricPowerRouteEnum routeEnum : ElectricPowerRouteEnum.values()) {
            if (routeEnum.getRouteName().equals(code)) {
                return routeEnum.getRouteName();
            }
        }
        return "";
    }

    ElectricPowerRouteEnum(String routeName, String route) {
        this.routeName = routeName;
        this.route = route;
    }

    public String getRouteName() {
        return routeName;
    }

    public String getRoute() {
        return route;
    }
}
