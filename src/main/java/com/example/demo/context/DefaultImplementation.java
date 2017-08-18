package com.example.demo.context;

import org.springframework.stereotype.Component;

/**
 * Created by sigri on 17.08.2017.
 */
@Component
public class DefaultImplementation implements ContextExample {
    @Override
    public void doSomething() {
        System.out.println(this.getClass());
    }
}
