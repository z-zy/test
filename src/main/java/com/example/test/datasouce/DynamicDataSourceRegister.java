package com.example.test.datasouce;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);
    /**
     * ConversionService
     * A service interface for type conversion.
     * This is the entry point into the convert system.
     * Call convert(Object, Class) to perform a thread-safe type conversion using this system.
    * */
    private ConversionService conversionService = new DefaultConversionService();

    //配置文件未指定数据源类型，使用该默认值
    private static  final Object DATASOURCCE_TYPE_DEFAULT = "org.apache.tomcat.jdbc.pool.DataSource";
    //springboot默认不支持
    private static final Object DATASOURCCE_TYPE_DRUID = "com.alibaba.druid.pool.DruidDataSource";
    //目前性能最好的数据源
    private static final Object DATASOURCCE_TYPE_HIKARI= "com.zaxxer.hikari.HikariDataSource";

    private DataSource defaultDataSource;

    private Map<String,DataSource> customDataSources = new HashMap<String, DataSource>();

    public DataSource buildDataSource(Map<String,Object> dsMap){
        try {
            Object type = dsMap.get("type");
            if (null == type) {
                type = DATASOURCCE_TYPE_DEFAULT;
            }
            Class<? extends DataSource> dataSourceType;
            dataSourceType = (Class<? extends DataSource>)Class.forName((String)type);

            String driverClassName = dsMap.get("driver-class-name").toString();
            String url = dsMap.get("url").toString();
            String username = dsMap.get("username").toString();
            String password = dsMap.get("password").toString();

            //DruidDataSource
            HikariDataSource hikariDataSource = new HikariDataSource();
            hikariDataSource.setDriverClassName(driverClassName);
            hikariDataSource.setJdbcUrl(url);
            hikariDataSource.setUsername(username);
            hikariDataSource.setPassword(password);
            hikariDataSource.setMinimumIdle(5);
            hikariDataSource.setMaximumPoolSize(10);

            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setDriverClassName(driverClassName);
            basicDataSource.setUrl(url);
            basicDataSource.setUsername(username);
            basicDataSource.setPassword(password);
            basicDataSource.setPoolPreparedStatements(true);
            basicDataSource.setMaxOpenPreparedStatements(100);


            /*DataSourceBuilder factory = DataSourceBuilder.create()
                    .driverClassName(driverClassName)
                    .url(url)
                    .username(username)
                    .password(password)
                    .type(dataSourceType);
            System.out.println(factory.build());
            return factory.build();*/
            return  basicDataSource;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void initDefaultDataSource(Environment env){
        Map<String,Object>  dsMap = new HashMap<>();
        dsMap.put("driver-class-name",env.getProperty("spring.datasource.driver-class-name"));
        dsMap.put("url",env.getProperty("spring.datasource.url"));
        dsMap.put("username",env.getProperty("spring.datasource.username"));
        dsMap.put("password",env.getProperty("spring.datasource.password"));
        defaultDataSource = buildDataSource(dsMap);
    }
    public void initCustomDataSources(Environment env){
        String dsPrefixs = env.getProperty("spring.datasource.other");
        for (String dsPrefix : dsPrefixs.split(",")) {
            Map<String,Object>  dsMap = new HashMap<>();
            dsMap.put("driver-class-name",env.getProperty("spring.datasource."+ dsPrefix +".driver-class-name"));
            dsMap.put("url",env.getProperty("spring.datasource."+ dsPrefix +".url"));
            dsMap.put("username",env.getProperty("spring.datasource."+ dsPrefix +".username"));
            dsMap.put("password",env.getProperty("spring.datasource."+ dsPrefix +".password"));
            DataSource ds  = buildDataSource(dsMap);
            customDataSources.put(dsPrefix,ds);
        }
    }

    /**
     *  实现此方法可以注入bean
     * */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Map<Object,Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put("dataSource",defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");
        targetDataSources.putAll(customDataSources);
        for(String key : customDataSources.keySet()){
            DynamicDataSourceContextHolder.dataSourceIds.add(key);
        }
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource",defaultDataSource);
        mpv.addPropertyValue("targetDataSources",targetDataSources);

        beanDefinitionRegistry.registerBeanDefinition("dataSource",beanDefinition);
        logger.info("Dynamic DataSource Registy");
    }
    /**
     * 实现此方法可以获取application.properties配置
     * */
    @Override
    public void setEnvironment(Environment environment) {
        initDefaultDataSource(environment);
        initCustomDataSources(environment);
    }
}
