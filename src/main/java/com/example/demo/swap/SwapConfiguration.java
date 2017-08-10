package com.example.demo.swap;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Created by sigri on 09.08.2017.
 */
@Configuration
public class SwapConfiguration {

    @Bean
    public HotSwappableTargetSource hotSwappable(@Qualifier("firstImplementation") SwapExample swapExample) {
        return new HotSwappableTargetSource(swapExample);
    }

    @Bean
    @Primary
    public SwapExample swapProxy(HotSwappableTargetSource hotSwappable) {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTargetSource(hotSwappable);
        return (SwapExample) proxyFactoryBean.getObject();
    }
}
