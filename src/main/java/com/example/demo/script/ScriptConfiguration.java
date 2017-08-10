package com.example.demo.script;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.scripting.groovy.GroovyScriptFactory;
import org.springframework.scripting.support.RefreshableScriptTargetSource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * Created by sigri on 10.08.2017.
 */
@Configuration
public class ScriptConfiguration {
    @Value("GroovyImplementation.groovy")
    private Resource resource;

    @Bean
    public RefreshableScriptTargetSource refreshableScript(BeanFactory beanFactory) {
        GroovyScriptFactory factory = new GroovyScriptFactory("classpath");
        ResourceScriptSource script = new ResourceScriptSource(resource);

        RefreshableScriptTargetSource refreshableScript = new RefreshableScriptTargetSource(beanFactory, "groovyImplementation", factory, script, false) {
            @Override
            protected Object obtainFreshBean(BeanFactory beanFactory, String beanName) {
                try {
                    return factory.getScriptedObject(script);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        refreshableScript.setRefreshCheckDelay(1000);

        return refreshableScript;
    }

    @Bean
    @Primary
    public ScriptExample scriptProxy(RefreshableScriptTargetSource refreshableScript) {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTargetSource(refreshableScript);
        return (ScriptExample) proxyFactoryBean.getObject();
    }
}
