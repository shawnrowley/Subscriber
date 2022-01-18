package com.iec.ics.emi.subscription.model;

import java.io.Serializable;
import java.util.Date; 

import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
public class Subscription implements Serializable {
	
	public Subscription()
	{
		
	}
	
	public Subscription(long id, String subType, String url, int clientNum,
			            Date subDate,  int serverKey, String hostName, int port) {
        super();
        this.id =id;
        this.subType = subType;
        this.url = url;
        this.clientNum = clientNum;
        this.subDate = subDate;
        this.serverKey = serverKey;
        this.hostName = hostName;
        this.port = port;
    }
	
	private long   id;
	private String subType;
	
	@NotNull
	@Size(min = 1, max = 2083)
	private String url;
	
	@NotNull
	private int    clientNum;
	
	private Date   subDate;
	private int    serverKey;
	private String hostName;
	private int    port;

	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getClientNum() {
		return clientNum;
	}
	public void setClientNum(int clientNum) {
		this.clientNum = clientNum;
	}
	
	public Date getSubDate() {
		return subDate;
	}
	public void setSubDate(Date subDate) {
		this.subDate = subDate;
	}
 
	public int getServerKey() {
		return serverKey;
	}
	public void setServerKey(int serverKey) {
		this.serverKey = serverKey;
	}
	
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}


