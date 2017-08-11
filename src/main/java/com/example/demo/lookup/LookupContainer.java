package com.example.demo.lookup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sigri on 10.08.2017.
 */
@Component
public class LookupContainer implements LookupExample {
    private LookupFactory lookupFactory;

    @Autowired
    public LookupContainer(LookupFactory lookupFactory) {
        this.lookupFactory = lookupFactory;
    }

    @Override
    public void doSomething() {
        lookupFactory.lookup().doSomething();
    }
}
