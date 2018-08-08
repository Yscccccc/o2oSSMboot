package com.ysc.o2o;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * 配置spring和junit整合,junit启动时加载springIO容器
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//spring配置文件的位置
@ContextConfiguration({"classpath:spring/applicationContext-dao.xml","classpath:spring/applicationContext-service.xml","classpath:spring/applicationContext-redis.xml"})
public class BaseTest {

}
