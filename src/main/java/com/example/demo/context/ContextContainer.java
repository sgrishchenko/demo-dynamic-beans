package com.example.demo.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by sigri on 17.08.2017.
 */
@Component
public class ContextContainer implements ContextExample {
    @Autowired
    @Qualifier("defaultImplementation")
    private ContextExample contextExample;

    @Override
    public void doSomething() {
        contextExample.doSomething();
    }
}
