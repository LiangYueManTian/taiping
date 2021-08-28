package com.taiping.utils.common.analyze.capacity;

import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * 模板工具类
 * @author hedongwei@wistronits.com
 * @date 2019/11/3 16:57
 */
public class TemplateUtil {

    /**
     * 替换后的值
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 17:01
     * @param template 模板
     * @param varNameList 替换名称集合
     * @param replaceMap 替换的值
     * @return 替换后的值
     */
    public static String parseTemplate(String template, List<String> varNameList, Map<String, String> replaceMap) {
        if (!ObjectUtils.isEmpty(template)) {
            if (!ObjectUtils.isEmpty(varNameList)) {
                for (String varName : varNameList) {
                    String replaceName = "";
                    if (!ObjectUtils.isEmpty(replaceMap)) {
                        if (!ObjectUtils.isEmpty(replaceMap.get(varName))) {
                            replaceName = replaceMap.get(varName);
                        }
                    }
                    template = template.replace("${" + varName + "}" , replaceName);
                }
            }
        }
        return template;
    }
}
