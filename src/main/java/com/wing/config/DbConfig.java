package com.wing.config;


import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import com.wing.dao.MongoDb;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by qingshan.wqs on 2016/9/13.
 */
@Configuration
@MapperScan("com.wing.dao")  //ibatis 配置扫描
public class DbConfig {

//    @Value("${mongodb.uri}")
//    String mongoUrl;
//
//    @Bean(name = "mongodb")
//    public MongoDb mongo() {
//        return new MongoDb(mongoUrl);
//    }


    @Bean(name = "logFilter")
    public Filter filter() {
        Slf4jLogFilter filter = new Slf4jLogFilter();
        filter.setStatementExecutableSqlLogEnable(true);
        filter.setStatementCreateAfterLogEnabled(false);
        filter.setStatementPrepareAfterLogEnabled(false);
        filter.setStatementParameterSetLogEnabled(false);
        filter.setStatementCloseAfterLogEnabled(false);
        return filter;
    }

    /* main */
    @Bean(name = "mainDataSource", destroyMethod = "close")
    @ConfigurationProperties(prefix = "datasource.main")
    public DataSource mainDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setProxyFilters(Lists.newArrayList(filter()));
        return dataSource;
    }


    @Bean(name = "mainJdbcTemplate")
    public JdbcTemplate mainJdbcTemplate(){
        return new JdbcTemplate( mainDataSource());
    }



//    ibatis 配置数据源
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        //数据源指向@Primary标记的Mysql源
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        return sessionFactory.getObject();
    }


}
