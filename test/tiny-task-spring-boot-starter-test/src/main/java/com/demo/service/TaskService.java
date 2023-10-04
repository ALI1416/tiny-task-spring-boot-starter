package com.demo.service;

import cn.z.tinytask.annotation.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <h1>TaskService</h1>
 *
 * <p>
 * createDate 2023/10/04 21:45:41
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@Slf4j
public class TaskService {

    @Task(fixedDelay = 10)
    public void test1() {
        log.info("测试1");
    }

    @Task(initialDelay = 1, fixedRateDuration = "PT10S")
    public void test2() {
        log.info("测试2");
    }

    @Task(cron = "*/10 * * * * *")
    public void test3() {
        log.info("测试3");
    }

}