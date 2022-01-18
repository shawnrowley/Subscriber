package com.iec.ics.emi.subscription.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Poster {
	
	DateTimeFormatter timeFormatter = 
			DateTimeFormatter.ofPattern("HH:mm:ss");
	
   	public void post(String postUrl) {

   		try {

   			URL url = new URL(postUrl);
   			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
   			conn.setDoOutput(true);
   			conn.setRequestMethod("POST");
   			conn.setRequestProperty("Content-Type", "application/json");

   			String input = "{\"Time\":\"" + LocalTime.now().format(timeFormatter) + "\",\"Code\":500,\"status\":\"successful\"}";

   			OutputStream os = conn.getOutputStream();
   			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
				+ conn.getResponseCode());
			}
		
			System.out.println("Sent: " + input);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			System.out.println("Server Output: ");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			conn.disconnect();

   		} catch (MalformedURLException e) {
   			e.printStackTrace();

   		} catch (IOException e) {
   			e.printStackTrace();
   		}

	}
	

}
