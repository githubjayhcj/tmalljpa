package com.taobao.tmalljpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement//开启事务管理
@ServletComponentScan // 加之下列方法继承的父类，为压缩 war 包 必须( pom 文件 配置pageckage = war ，内置tomcat为 provided )
@EnableCaching //启动缓存
//整合elasticsearch  不与jpa dao 发生冲突（jpa dao之前整合的 redis ）
@EnableJpaRepositories(basePackages = {"com.taobao.tmalljpa.entity","com.taobao.tmalljpa.dao"})
@EnableElasticsearchRepositories(basePackages = "com.taobao.tmalljpa.es")
public class TmalljpaApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return super.configure(builder);
    }

    public static void main(String[] args) {
        SpringApplication.run(TmalljpaApplication.class, args);
    }

}
