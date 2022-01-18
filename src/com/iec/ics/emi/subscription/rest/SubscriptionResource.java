package com.iec.ics.emi.subscription.rest;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import com.iec.ics.emi.subscription.http.*;
import com.iec.ics.emi.subscription.model.*;
import com.iec.ics.emi.subscription.service.SubscriptionService;
//import com.lmco.iec.ecs.logClient.ClientLog;

//import java.sql.Connection;
//import com.iec.ics.emi.subscription.data.*;

@Path("/")
public class SubscriptionResource {

    private static List<Subscription> subscriptions = new ArrayList<Subscription>();
	static ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
	static Poster poster = new Poster();   
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	@Inject
	SubscriptionService subscriber;
		
	/**
     * isAlive
     * 
     * @return String
     */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path( "isAlive" )
	public String isAlive( ) {
        return "Alive";
	}

	/**
     * subscriptions
     * 
     * @return List<Subscription>
     */
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("subscriptions")
    public Subscription[] listAllSubscribers() {
		return subscriber.getSubscriptions();
    }
	
	/**
     * subscriptions demo
     * 
     * @return List<Subscription>
     */
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("subscriptions/demo")
    public List<Subscription>  listAllSubscribersDemo() {
    	return subscriptions;
    }
	
	 /**
     *  getSubscriptionById 
     *  
     * @param id
     * @return Subscription
     */
    @GET
    @Path("getSubscriptionById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Subscription getSubscriptionById(@PathParam("id") long id) {
        
    	Subscription subscription = subscriber.getSubscriptionById(id);
		if (subscription == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
        return subscription;
    }
    
    /**
     *  getSubscriptionByClientNum 
     *  
     * @param clientNum
     * @return Subscription
     */
    @GET
    @Path("getSubscription/{clientNum}")
    @Produces(MediaType.APPLICATION_JSON)
    public Subscription getSubscriptionByClientNum(@PathParam("clientNum") int clientNum) {
        
    	Subscription subscription = subscriber.getSubscriptionByClientNum(clientNum);
		if (subscription == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
        return subscription;
    }
    
    /**
     * Update subscription
     * 
     * @param subscription
     * @return Response 
     */
    @PUT
    @Path("renew")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response renew(Subscription subscription) {
    	 Response.ResponseBuilder builder = null;
         try {
             validateSubscription(subscription);
             subscriber.renew(subscription);
             builder = Response.ok();
          } catch (ConstraintViolationException ce) {
        	  builder = createViolationResponse(ce.getConstraintViolations());
          } catch (ValidationException e) {
             Map<String, String> responseObj = new HashMap<>();
             responseObj.put("error", e.getMessage());
             builder = Response.status(Response.Status.CONFLICT).entity(responseObj);  
          } catch (Exception e) {
             Map<String, String> responseObj = new HashMap<>();
             responseObj.put("error", e.getMessage());
             builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
          }
          return builder.build();
    }
    
    /**
     * subscribe 
     * 
     * @param registration
     * @return Response
     */
    @POST
    @Path("subscribe/demo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response subscribeDemo(Subscription subscription) {
        Response.ResponseBuilder builder = null;
        try {
           	subscriptions.add(subscription);
        	
        	if (subscriptions.size() == 1) {
        		timer.scheduleAtFixedRate(
        				  () ->sendStatusToAll(subscription),0,1,TimeUnit.SECONDS); 	
        	}
            builder = Response.ok();
            
        } catch (ConstraintViolationException ce) {
      	  	//builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }
        return builder.build();
    }
    
 	/**
     * subscribe 
     * 
     * @param subscription
     * @return Response
     */
    @POST
    @Path("subscribe")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response subscribe(Subscription subscription) {
    	
    	Response.ResponseBuilder builder = null;
        try {
        	openSubscription(subscription);
            validateSubscription(subscription);
            subscriber.subscribe(subscription);
            builder = Response.ok();
         } catch (ConstraintViolationException ce) {
       	  builder = createViolationResponse(ce.getConstraintViolations());
         } catch (ValidationException ve) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", ve.getMessage());
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
         } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
         }
         return builder.build();
    }
 
    /**
     *  Delete subscription by subscription id
     * 
     * @param id
     * @return Response
     */
    @DELETE  
    @Path("unsubscribeById/{id}")  
    @Produces(MediaType.APPLICATION_JSON)  
    public Response unsubscribe(@PathParam("id") long id)  
    {  
    	Response.ResponseBuilder builder = null;
    	try {
    		subscriber.unsubscribe(id);
            builder = Response.ok();
          } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }
    	return builder.build();
    }  
    
    /**
     *  Delete subscription by client number
     * 
     * @param clientNum
     * @return Response
     */
    @DELETE  
    @Path("unsubscribe/{clientNum}")  
    @Produces(MediaType.APPLICATION_JSON)  
    public Response unsubscribe(@PathParam("clientNum") int clientNum)  
    {  
    	Response.ResponseBuilder builder = null;
    	try {
    		subscriber.unsubscribe(clientNum);
            builder = Response.ok();
       
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }
    	return builder.build();
    }  
    
     /**
     * Validate Subscription based persistent constraints
     * 
     * @param Subscription
     * @throws ConstraintViolationException
     * @throws ValidationException
     */
    private void validateSubscription(Subscription subscription) throws ConstraintViolationException, 
                                                                        ValidationException {
        Set<ConstraintViolation<Subscription>> violations = validator.validate(subscription);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }
    
    /**
     *  Subscription existent
     * 
     * @param Subscription
     * @throws ConstraintViolationException
     * @throws ValidationException
     */
    private void openSubscription(Subscription subscription) throws  ValidationException {
    	Subscription sub = subscriber.getSubscriptionByClientNum(subscription.getClientNum());
 		if (sub != null) {
 			throw new ValidationException("Client Num " + subscription.getClientNum() + " is already subscribed.");
 		}
    }
    
    /**
     * Generate constraint violation response
     * 
     * @param violations
     * @return Response.ResponseBuilder
     */
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }
   
    /**
     * Send post to serve demo
     * 
     * @param subscription
     */
    private void sendStatusToAll(Subscription subscription){
  	   for (Subscription sub : subscriptions){
           try {  
 			 poster.post(sub.getUrl());	
           } catch (Exception ioe) {
        	   System.out.println(ioe.getMessage());
     	   }
 	   }
    }
  
 
    

} 
