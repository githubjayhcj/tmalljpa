package com.taobao.tmalljpa.springMvcConfigurer;

import com.taobao.tmalljpa.interceptor.GlobalInterceptor;
import com.taobao.tmalljpa.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@EnableWebMvc      // 会将请求放入 mapping 句柄匹配controller    //@EnableWebMvc注解来实现完全自己控制的MVC配置。
public class SpringMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Bean
    public GlobalInterceptor globalInterceptor(){
        return new GlobalInterceptor();
    }

    //静态资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("js/**/").addResourceLocations("file:E:/my/");   //指定外部的非拦截目录
    }

    //拦截器注册方法
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/**/image/**","/**/js/**","/**/css/**");
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**").excludePathPatterns("/**/image/**","/**/js/**","/**/css/**");// 需要全(绝对)路径匹配
    }
}
