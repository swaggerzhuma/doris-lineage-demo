/*
package com.cnhis.dorislineage.demo.configuration;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Neo4jConfig {
    @Value("${spring.neo4j.uri}")
    String neo4jUrl;

    @Value("${spring.neo4j.authentication.username}")
    String neo4jUsername;

    @Value("${spring.neo4j.uri}")
    String neo4jPassword;

    @Bean
    public Driver neo4jDriver() {
        return GraphDatabase.driver(neo4jUrl, AuthTokens.basic(neo4jUsername, neo4jPassword));
    }
}
*/
