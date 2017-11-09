package com.jaf.rds.mysql.pool;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.beans.PropertyVetoException;
import java.sql.SQLException;

@Configuration
public class DruidDatabaseConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public DruidDataSource dataSource() throws PropertyVetoException {
        String url = environment.getProperty("spring.datasource.url");
        String username = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");
        String driverClassName = environment.getProperty("spring.datasource.driver-class-name");

        if (url == null || "".equals(url)) {
            return null;
        }

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);

        druidDataSource.setMaxActive(30);
        druidDataSource.setMinIdle(5);
        druidDataSource.setInitialSize(5);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(true);
        druidDataSource.setValidationQuery("select 1");
        druidDataSource.setValidationQueryTimeout(3600);

        try {
            // druidDataSource.setFilters("stat, wall");
            druidDataSource.setFilters("stat");
        } catch (SQLException e) {
//            log.warn("设置Druid的Filters错误。", e);
        }
        return druidDataSource;
    }

}
