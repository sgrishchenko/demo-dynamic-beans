package com.example.demo.script;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sigri on 10.08.2017.
 */
@Component
public class ScriptContainer implements ScriptExample {
    private ScriptExample scriptExample;

    @Autowired
    public ScriptContainer(ScriptExample scriptExample) {
        this.scriptExample = scriptExample;
    }

    @Override
    public void doSomething() {
        scriptExample.doSomething();
    }
}
