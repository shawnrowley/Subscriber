package com.iec.ics.emi.subscription.service;

/**
 * @author Shawn
 *
 */
import com.iec.ics.emi.subscription.model.Subscription;
import javax.inject.Inject;
import com.iec.ics.emi.subscription.data.SubscriptionRepository; 


public class SubscriptionService {
	
	@Inject
	SubscriptionRepository repository;
	
	
	public Subscription getSubscriptionById(long id)  {
		return repository.findbyId(id);
	}
	
	public Subscription getSubscriptionByClientNum(int clientNum)  {
		return repository.findbyClientNum(clientNum);
	}
	
	
	public Subscription[] getSubscriptions() {
		return repository.getAll();
	}
	
	
	public void subscribe(Subscription subscription) throws Exception {
      //  log.info("Subscribing " + subscription.getName());
		
		repository.save(subscription); 

    }
	
	public void unsubscribe(long id) throws Exception {
	    //  log.info("Un-subscribing " + subscription.getName());
		
		
    	try {
    		repository.delete(id);
		} catch (Exception e) {
			//log.fine("Validation completed. violations found: " + e.getMessage());
		}  
    }
	
	public void unsubscribe(int clientNum) throws Exception {
	    //  log.info("Un-subscribing " + subscription.getName());
		
		
    	try {
    		repository.delete(clientNum);
		} catch (Exception e) {
			//log.fine("Validation completed. violations found: " + e.getMessage());
		}  
    }
	
	public void renew(Subscription subscription) throws Exception {
	      //  log.info("Subscribing " + subscription.getName());
			repository.update(subscription); 

	}
	
	
	public boolean validate(Subscription subscription)  {
		// TODO Validate subscription
		return true;
	}
	

}
