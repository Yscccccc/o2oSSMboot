package com.ysc.config.service;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
/**
 * 对标spring-service里面的transactionManager
 * 实现TransactionManagementConfigurer 是因为开启annotation-driven
 * @author chenshuying
 *
 */
@Configuration
//首先使用注解@EnableTransactionManagement开启事务支持后
//在Service方法上添加注解@Transcational便可
@EnableTransactionManagement
public class TransactionManagementConfiguration implements TransactionManagementConfigurer{
	
	//注入DataSourceConfiguraion里边的dataSource，通过createDataSource()获取
	@Autowired
	private DataSource dataSource;

	@Override
	/**
	 * 关于事务管理，需要返回PlatformTransactionManager的实现
	 */
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		
		return new DataSourceTransactionManager(dataSource);
	}
	
	
}
