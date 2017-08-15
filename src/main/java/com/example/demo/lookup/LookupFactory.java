package com.example.demo.lookup;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

/**
 * Created by sigri on 10.08.2017.
 */
@Component
@ManagedResource
public abstract class LookupFactory {
    @Lookup("prototypeImplementation")
    public abstract LookupExample lookupProxy();

    @Cacheable("lookupProxies")
    public LookupExample lookup() {
        return lookupProxy();
    }

    @ManagedOperation
    @CacheEvict("lookupProxies")
    public void invalidateLookupProxies() {
    }
}
