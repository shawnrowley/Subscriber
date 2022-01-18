package com.iec.ics.emi.subscription.application;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import com.iec.ics.emi.subscription.service.SubscriptionService;
import com.iec.ics.emi.subscription.data.SubscriptionRepository;

public class SubscriberBinder extends AbstractBinder {
	
	@Override
    protected void configure() {
        bind(SubscriptionService.class).to(SubscriptionService.class);
        bind(SubscriptionRepository.class).to(SubscriptionRepository.class);
    }

}
