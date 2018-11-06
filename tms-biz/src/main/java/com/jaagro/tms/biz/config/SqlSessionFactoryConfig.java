/*
package com.jaagro.tms.biz.config;

import com.jaagro.tms.biz.cat.CatMybatisPlugin;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

*/
/**
 * @author tony
 *//*

@Configuration
public class SqlSessionFactoryConfig {

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        //dataSource
        sqlSessionFactoryBean.setDataSource(dataSource);
        //mybatisPlugin
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{new CatMybatisPlugin()});
        //mapperLocation
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:/mapper/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(resolver.getResources("classpath*:/mybatis/mybatis_config.xml")[0]);
        return sqlSessionFactoryBean;
    }
}*/
