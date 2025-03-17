package com.cnhis.dorislineage.demo.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author liuqiang
 * @date 2024/7/16 19:40
 */
@Configuration
public class DatasourceConfiguration {
    @Value(value = "${spring.datasource.url}")
    public String datasourceUrl;

    @Value(value = "${spring.datasource.username}")
    public String datasourceUsername;

    @Value(value = "${spring.datasource.password}")
    public String datasourcePassword;

    @Value(value = "${spring.datasource.driver-class-name}")
    public String datasourceDriver;

    @Value(value = "${spring.datasource.maxActive:10}")
    public Integer datasourceMaxActive;

    @Value(value = "${spring.datasource.minIdle:1}")
    public Integer datasourceMinIdle;

    @Bean("DataSource")
    @Primary
    public HikariDataSource configDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(datasourceUrl);
        dataSource.setUsername(datasourceUsername);
        dataSource.setPassword(datasourcePassword);
        dataSource.setDriverClassName(datasourceDriver);
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(datasourceMaxActive);
        return dataSource;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("DataSource") DataSource dataSourceConfig) {
        return new JdbcTemplate(dataSourceConfig);
    }

}
