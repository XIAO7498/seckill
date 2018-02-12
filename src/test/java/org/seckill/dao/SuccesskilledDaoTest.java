package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author xiaofan
 * @date 2018/2/5 上午11:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccesskilledDaoTest {

    @Resource
    private SuccesskilledDao successkilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        int successCount = successkilledDao.insertSuccessKilled(1001L,15700183132L);
        System.out.println("successCount=" + successCount);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        SuccessKilled successKilled = successkilledDao.queryByIdWithSeckill(1001L,15700183132L);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }

}