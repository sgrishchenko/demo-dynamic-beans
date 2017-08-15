package com.example.demo.lookup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Created by sigri on 10.08.2017.
 */
@Component
public class LookupFactoryJmsWrapper {
    private LookupFactory lookupFactory;
    private JmsTemplate jmsTemplate;

    @Autowired
    protected LookupFactoryJmsWrapper(LookupFactory lookupFactory, JmsTemplate jmsTemplate) {
        this.lookupFactory = lookupFactory;
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = "invalidateLookupProxies")
    public void invalidateLookupProxies(Message message) throws JMSException {
        lookupFactory.invalidateLookupProxies();
        jmsTemplate.send(message.getJMSReplyTo(), Session::createMessage);
    }
}
