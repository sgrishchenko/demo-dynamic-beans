package com.example.demo;

import com.example.demo.context.ContextContainer;
import com.example.demo.lookup.LookupContainer;
import com.example.demo.script.ScriptContainer;
import com.example.demo.swap.SwapContainer;
import com.example.demo.swap.SwapExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericGroovyApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Session;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "spring.jmx.enabled=true")
public class DemoApplicationTests {
    @Autowired
    private HotSwappableTargetSource hotSwappable;
    @Autowired
    private SwapContainer swapContainer;
    @Autowired
    @Qualifier("firstImplementation")
    private SwapExample first;
    @Autowired
    @Qualifier("secondImplementation")
    private SwapExample second;

    @Autowired
    private ScriptContainer scriptContainer;
    @Value("classpath:GroovyImplementation.groovy")
    private Path path;

    @Autowired
    private LookupContainer lookupContainer;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ContextContainer contextContainer;
    @Autowired
    private ConfigurableApplicationContext context;

    @Test
    public void contextLoads() {
    }

    @Test
    public void swap() throws Exception {
        swapContainer.doSomething();
        hotSwappable.swap(second);
        swapContainer.doSomething();
        hotSwappable.swap(first);
        swapContainer.doSomething();
    }

    @Test
    public void script() throws Exception {
        scriptContainer.doSomething();
        Files.write(path, Files.readAllLines(path)
                .stream()
                .collect(Collectors.joining("\n"))
                .replace("!", "!!")
                .getBytes());
        TimeUnit.SECONDS.sleep(2);
        scriptContainer.doSomething();
    }

    @Test
    public void lookup() throws Exception {
        lookupContainer.doSomething();
        lookupContainer.doSomething();

        jmsTemplate.sendAndReceive("invalidateLookupProxies", Session::createMessage);

        lookupContainer.doSomething();
        lookupContainer.doSomething();

        Map<String, String> request = Collections.unmodifiableMap(Stream.of(
                new SimpleEntry<>("type", "exec"),
                new SimpleEntry<>("mbean", "com.example.demo.lookup:name=lookupFactory,type=LookupFactory"),
                new SimpleEntry<>("operation", "invalidateLookupProxies()")
        ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));
        restTemplate.postForObject("/jolokia", request, String.class);

        lookupContainer.doSomething();
        lookupContainer.doSomething();
    }

    @Test
    public void context() throws Exception {
        contextContainer.doSomething();

        GenericGroovyApplicationContext dynamicContext
                = new GenericGroovyApplicationContext("classpath:dynamicContext.groovy");
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) context.getBeanFactory();

        Stream.of(dynamicContext.getBeanDefinitionNames()).forEach(name ->
                factory.registerBeanDefinition(name, dynamicContext.getBeanDefinition(name)));

        AutowiredAnnotationBeanPostProcessor postProcessor = new AutowiredAnnotationBeanPostProcessor();
        postProcessor.setBeanFactory(context.getAutowireCapableBeanFactory());
        postProcessor.processInjection(contextContainer);

        contextContainer.doSomething();
    }

}
