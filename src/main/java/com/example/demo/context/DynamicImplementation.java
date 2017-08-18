package com.example.demo.context;

/**
 * Created by sigri on 17.08.2017.
 */
public class DynamicImplementation implements ContextExample {
    @Override
    public void doSomething() {
        System.out.println(this.getClass());
    }
}
