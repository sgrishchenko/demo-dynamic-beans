package com.example.demo.lookup;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by sigri on 10.08.2017.
 */
@Component
@Scope("prototype")
public class PrototypeImplementation implements LookupExample {
    @Override
    public void doSomething() {
        System.out.println(this);
    }
}
