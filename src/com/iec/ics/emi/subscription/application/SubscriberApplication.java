package com.iec.ics.emi.subscription.application;

/*
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Application;
*/

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

//import com.iec.ics.emi.subscription.rest.SubscriptionResource;

@ApplicationPath("/")
//public class SubscriptionMgrApplication extends Application {
public class SubscriberApplication extends ResourceConfig {
	
	public SubscriberApplication() {
        packages("com.iec.ics.emi.subscription.rest",
        		 "com.iec.ics.emi.subscription.service");
        register(new SubscriberBinder());
    }

	// javax.ws.rs.core.Application implementation
	/*
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> set = new HashSet<>();
		set.add(SubscriptionResource.class);
		return set;
	}

	@Override
	public Set<Object> getSingletons() {
		Set<Object> set = new HashSet<>();
	     // set.add(new MySingletonResource());
		return set;
	}
	
	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> map = new HashMap<>();
	    map.put("KEY_MAX_ORDER_LIMIT", "100");
	    return map;
	}
	*/

}
