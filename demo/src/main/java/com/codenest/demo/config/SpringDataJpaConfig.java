package com.codenest.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages="com.codenest.demo.data.interfaces")
@EnableTransactionManagement
public class SpringDataJpaConfig {  }