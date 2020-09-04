package com.yoga.utility.quartz;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yoga.core.utils.StringUtil;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class QuartzConfig {

    @Value("${org.quartz.datasource.driver-class-name:}")
    private String driverClassName;
    @Value("${org.quartz.datasource.url:}")
    private String url;
    @Value("${org.quartz.datasource.username:}")
    private String username;
    @Value("${org.quartz.datasource.password:}")
    private String password;

    @Autowired
    private QuartzJobFactory quartzJobFactory;
    @Autowired
    private DataSource dataSource;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException, PropertyVetoException {
        final SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(quartzJobFactory);
        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        schedulerFactoryBean.setDataSource(createDataSource());
        return schedulerFactoryBean;
    }

    @Bean
    public Scheduler scheduler() throws IOException, PropertyVetoException {
        return schedulerFactoryBean().getScheduler();
    }

    private Properties quartzProperties() throws IOException {
        final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
        Properties properties = null;
        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return properties;
    }

    private DataSource createDataSource() throws PropertyVetoException {
        if (StringUtil.hasBlank(driverClassName, url, username)) {
            return dataSource;
        } else {
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(driverClassName);
            dataSource.setJdbcUrl(url);
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setTestConnectionOnCheckin(true);
            dataSource.setTestConnectionOnCheckout(true);
            dataSource.setAutoCommitOnClose(true);
            return dataSource;
        }
    }
}