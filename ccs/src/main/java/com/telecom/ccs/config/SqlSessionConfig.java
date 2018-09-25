package com.telecom.ccs.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
public class SqlSessionConfig {

    private final static String DATASOURCE_PREIFX = "spring.datasource";

    @Autowired
    private PropertiesConfig properties;

    @Bean
    @ConfigurationProperties(prefix = DATASOURCE_PREIFX)
    public DataSource dataSource() {


        System.out.println("***************************"+ properties.getSystem_ftp_username());
        //test 自定义变量可以拿到

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(properties.getSystem_datasource_url());
        dataSource.setDriverClassName(properties.getSystem_datasource_driver_class_name());
        dataSource.setUsername(properties.getSystem_datasource_username());
        dataSource.setPassword(properties.getSystem_datasource_password());
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(5);
        dataSource.setMaxActive(100);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
       // dataSource.setFilters("stat,wall");
        return dataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {

        SqlSessionFactoryBean ssb = new SqlSessionFactoryBean();
        ssb.setDataSource(dataSource());
        ssb.setTypeAliasesPackage("com.telecom.ccs.config");
        ssb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/*.xml"));

     /*   PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mapper/*.xml"));
*/
        return ssb.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {

        return new DataSourceTransactionManager(dataSource());
    }
}
