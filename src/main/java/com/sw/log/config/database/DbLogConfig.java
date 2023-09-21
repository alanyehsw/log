package com.sw.log.config.database;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import jakarta.persistence.SharedCacheMode;

@Configuration
@EnableJpaRepositories(
    basePackages = {"com.sw.log.dao"},
    entityManagerFactoryRef = "entityManagerFactoryBean"
)
public class DbLogConfig {

    @Value("${db-log.datasource.driver-class-name}")
    String driverClassName;

    @Value("${db-log.datasource.url}")
    String url;

    @Value("${db-log.datasource.username}")
    String username;

    @Value("${db-log.datasource.password}")
    String password;

    @Value("${db-log.datasource.databasePlatform}")
    String databasePlatform;

    @Value("${db-log.datasource.hibernate.hbm2ddl.auto}")
    String hibernateHbm2ddl;

    @Value("${db-log.datasource.hibernate.naming.physical-strategy}")
    String hibernateNaming;
    
    @Bean
    public DataSource dataSource () {
        return DataSourceBuilder.create()
            .driverClassName(driverClassName)
            .url(url)
            .username(username)
            .password(password)
            .build();
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter () {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL);
        adapter.setShowSql(false);
        adapter.setGenerateDdl(false);
        adapter.setDatabasePlatform(databasePlatform);

        return adapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean () {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("com.sw.log.entity");
        entityManagerFactoryBean.setJpaPropertyMap(getJpaProperties());
        entityManagerFactoryBean.setSharedCacheMode(SharedCacheMode.NONE);

        return entityManagerFactoryBean;
    }

    private Map<String, Object> getJpaProperties () {
        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddl);
        jpaProperties.put("hibernate.naming.physical-strategy", hibernateNaming);

        return jpaProperties;
    }
}
