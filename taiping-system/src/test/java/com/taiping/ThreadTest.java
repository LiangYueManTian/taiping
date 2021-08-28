package com.taiping;

import com.taiping.biz.sample.controller.SampleController;
import com.taiping.biz.sample.service.impl.SampleServiceImpl;
import com.taiping.biz.system.service.SystemService;
import com.taiping.entity.system.BudgetTemp;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.Target;
import java.util.List;

/**
 * 多线程配置
 *
 * @author liyj
 * @date 2019/10/22
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ThreadTest {

    @Autowired
    private SampleController sampleController;

    @Test
    public void threadTest() {
        SampleServiceImpl s = new SampleServiceImpl();
        for (int i = 0; i < 5; i++) {
            sampleController.executeAsyncTask(i);
        }
    }


    @Autowired
    SystemService service;

    @Test
    public void saveBudgetTemp() {

        List<String> list = Lists.newArrayList();
        list.add("采购项目启动");
        list.add("金科签报");
        list.add("太寿会签（将依据太寿进度更新本阶段进度）");
        list.add("需求单领导邮件审批");
        list.add("采购平台需求单审批（将依据流程规划部进度更新本阶段进度）");
        list.add("流程规划部或集采办立项（将依据流程规划部进度更新本阶段进度）");
        list.add("编写邀标文件");
        list.add("招标或者询价采购（将依据流程规划部进度更新本阶段进度）");
        list.add("厂商回标或者询价答复（将依据流程规划部进度更新本阶段进度）");
        list.add("开标（将依据流程规划部进度更新本阶段进度）");
        list.add("中标结果通知（将依据流程规划部进度更新本阶段进度）");
        list.add("合同法务审核（将依据法务进度更新本阶段进度）");
        list.add("合同行政审批（将依据行政进度更新本阶段进度）");
        list.add("合同签订（将依据太寿或者流程规划部进度更新本阶段进度）");
        list.add("设备到货");
        list.add("验收");
        list.add("初验付款/第一次付款");
        list.add("验收");
        list.add("终验付款/第二次付款");
        list.add("验收");
        list.add("第三次次付款");

        for (int i = 0; i < list.size(); i++) {
            BudgetTemp temp = new BudgetTemp();
            temp.setCheckFlag(true);
            temp.setIsOrder(i + 1);
            temp.setName(list.get(i));
            service.saveBudgetTemp(temp);
        }


    }


}
