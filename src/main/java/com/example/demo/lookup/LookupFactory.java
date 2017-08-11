package com.example.demo.lookup;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by sigri on 10.08.2017.
 */
@Component
public abstract class LookupFactory {
    @Lookup("prototypeImplementation")
    public abstract LookupExample lookupProxy();

    @Cacheable("lookupProxies")
    public LookupExample lookup() {
        return lookupProxy();
    }

    @JmsListener(destination = "invalidateLookupProxies")
    @CacheEvict("lookupProxies")
    public void invalidateLookupProxiesByJms() {
    }
}
