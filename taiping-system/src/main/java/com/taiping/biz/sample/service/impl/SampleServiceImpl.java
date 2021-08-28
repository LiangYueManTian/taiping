package com.taiping.biz.sample.service.impl;

import com.taiping.biz.sample.dao.SampleaDao;
import com.taiping.entity.User;
import com.taiping.biz.sample.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liyj
 * @date 2019/9/8
 */
@Service
public class SampleServiceImpl implements SampleService {

    @Autowired
    private SampleaDao sampleaDao;


    @Override
    public List<User> getUserInfo() {
        return sampleaDao.getUserInfo();
    }


    public static void main(String[] args) {

    }
}
