package com.example.demo.swap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sigri on 09.08.2017.
 */
@Component
public class SwapContainer implements SwapExample {
    private SwapExample swapExample;

    @Autowired
    public SwapContainer(SwapExample swapExample) {
        this.swapExample = swapExample;
    }

    @Override
    public void doSomething() {
        swapExample.doSomething();
    }
}
