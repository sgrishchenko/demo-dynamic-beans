package com.example.demo.swap;

import org.springframework.stereotype.Component;

/**
 * Created by sigri on 09.08.2017.
 */
@Component
public class FirstImplementation implements SwapExample {
    @Override
    public void doSomething() {
        System.out.println(this.getClass());
    }
}
