package com.example.demo;

import com.example.demo.lookup.LookupContainer;
import com.example.demo.script.ScriptContainer;
import com.example.demo.swap.SwapContainer;
import com.example.demo.swap.SwapExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Session;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
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
        jmsTemplate.send("invalidateLookupProxies", Session::createMessage);
        lookupContainer.doSomething();
        lookupContainer.doSomething();
    }
}
