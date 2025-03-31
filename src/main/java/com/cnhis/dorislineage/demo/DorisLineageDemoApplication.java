package com.cnhis.dorislineage.demo;

import com.cnhis.dorislineage.demo.neo4j.dao.SimpleJpaRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication(scanBasePackages = {"com.cnhis.dorislineage.demo.*"}, exclude = {DataSourceAutoConfiguration.class})
@EnableNeo4jRepositories(repositoryBaseClass = SimpleJpaRepositoryImpl.class, basePackages = "com.cnhis.dorislineage.demo.neo4j.dao")
@EntityScan(basePackages = "com.cnhis.dorislineage.demo.neo4j.domain")
public class DorisLineageDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DorisLineageDemoApplication.class, args);
    }

}
