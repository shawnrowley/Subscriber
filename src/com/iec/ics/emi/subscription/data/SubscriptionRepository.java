package com.iec.ics.emi.subscription.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.iec.ics.emi.subscription.data.DBConnection;
import com.iec.ics.emi.subscription.model.Subscription;

public class SubscriptionRepository {
	
	private Connection conn = null;

    public SubscriptionRepository() {
        conn = DBConnection.getInstance().getConnection();
    }
    
    
    /**
     *  get current timestamp
     * 
     */
    private static java.sql.Timestamp getCurrentTimeStamp() {
     	java.util.Date today = new java.util.Date();
    	return new java.sql.Timestamp(today.getTime());
    }
    
    /**
     *  save subscription
     * 
     * @param subscription
     */
    public JsonMessage save(Subscription subscription) {
        String insertTableSQL = "INSERT INTO SUBSCRIPTION "
                + "(SUBSCRIBE_ID, SUBSCRIBE_TYPE, URL, CLIENT_NUM, TIMESTAMP, SERVER_KEY, HOSTNAME, PORT) "
                + "VALUES(?,?,?,?,?,?,?,?)";

        try (PreparedStatement preparedStatement = this.conn
                .prepareStatement(insertTableSQL)) {

            preparedStatement.setLong(1, getNextID());
            preparedStatement.setString(2, subscription.getSubType());
            preparedStatement.setString(3, subscription.getUrl());
            preparedStatement.setInt(4, subscription.getClientNum());
            preparedStatement.setTimestamp(5, getCurrentTimeStamp());
            preparedStatement.setInt(6, subscription.getServerKey());
            preparedStatement.setString(7, subscription.getHostName());
            preparedStatement.setInt(8, subscription.getPort());
   
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            return new JsonMessage("Error", "Subscription add failed: "
                    + e.getMessage());
        } catch (Exception e) {
            return new JsonMessage("Error", "Subscription add failed: "
                    + e.getMessage());
        }
        return new JsonMessage("Success", "Subscription add succeeded...");
    }

    /**
     *  update subscription
     * 
     * @param subscription
     */
    public JsonMessage update(Subscription subscription) {
        String updateTableSQL = "UPDATE SUBSCRIPTION SET "
        		              + "SUBSCRIBE_TYPE= ?, URL= ?, TIMESTAMP= ?, SERVER_KEY= ?, HOSTNAME= ?, PORT= ?  "
        		              + "WHERE CLIENT_NUM=?";
        try (PreparedStatement preparedStatement = this.conn
                .prepareStatement(updateTableSQL);) {

             preparedStatement.setString(1, subscription.getSubType());
             preparedStatement.setString(2, subscription.getUrl());
             preparedStatement.setTimestamp(3, getCurrentTimeStamp());
             preparedStatement.setInt(4, subscription.getServerKey());
             preparedStatement.setString(5, subscription.getHostName());
             preparedStatement.setInt(6, subscription.getPort());
             preparedStatement.setInt(7, subscription.getClientNum());
    
             preparedStatement.executeUpdate();
        } catch (SQLException e) {
                    e.printStackTrace();
            return new JsonMessage("Error", "Subscription update failed: "
                    + e.getMessage());
        } catch (Exception e) {
                    e.printStackTrace();
            return new JsonMessage("Error", "Subscription update failed: "
                    + e.getMessage());
        }
        return new JsonMessage("Success", "Subscription add succeeded...");
    }

    /**
     *  delete subscription by id
     * 
     * @param id
     */
    public JsonMessage delete(long id) {
        String deleteRowSQL = "DELETE FROM SUBSCRIPTION WHERE SUBSCRIBE_ID=?";
        try (PreparedStatement preparedStatement = this.conn
                .prepareStatement(deleteRowSQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            return new JsonMessage("Error", "Subscription delete failed: "
                    + e.getMessage());
        } catch (Exception e) {
            return new JsonMessage("Error", "Subscription delete failed: "
                    + e.getMessage());
        }
        return new JsonMessage("Success", "Subscription delete succeeded...");
    }
    
    /**
     *  delete subscription by client number
     * 
     * @param clientNum
     */
    public JsonMessage delete(int clientNum) {
        String deleteRowSQL = "DELETE FROM SUBSCRIPTION WHERE CLIENT_NUM=?";
        try (PreparedStatement preparedStatement = this.conn
                .prepareStatement(deleteRowSQL)) {
            preparedStatement.setLong(1, clientNum);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            return new JsonMessage("Error", "Subscription delete failed: "
                    + e.getMessage());
        } catch (Exception e) {
            return new JsonMessage("Error", "Subscription delete failed: "
                    + e.getMessage());
        }
        return new JsonMessage("Success", "Subscription delete succeeded...");
    }

    /**
     *  get all subscriptions
     * 
     */
    public Subscription[] getAll() {
        String queryStr = "SELECT * FROM SUBSCRIPTION";
        return this.query(queryStr);
    }
    
    /**
     *  find subscription by Id
     * 
     * @param id
     */
    public Subscription findbyId(long id) {
        String queryStr = "SELECT * FROM SUBSCRIPTION WHERE SUBSCRIBE_ID=" + id;
        Subscription subscription = null;
        Subscription subscriptions[] = this.query(queryStr);
        if (subscriptions != null && subscriptions.length > 0) {
        	subscription = subscriptions[0];
        }
        return subscription;
    }
    
    
    /**
     *  find subscription by clientNum
     * 
     * @param id
     */
    public Subscription findbyClientNum(int clientNum) {
        String queryStr = "SELECT * FROM SUBSCRIPTION WHERE CLIENT_NUM=" + clientNum;
        Subscription subscription = null;
        Subscription subscriptions[] = this.query(queryStr);
        if (subscriptions != null && subscriptions.length > 0) {
        	subscription = subscriptions[0];
        }
        return subscription;
    }
    
    /**
     *  get Next available subscribeID
     *  
     *  temporary should be replace with generator 
     * 
     */
    public int  getNextID() {
        String queryStr = "SELECT MAX(SUBSCRIBE_ID) MAXID FROM SUBSCRIPTION";
        int maxID = 0;
        try (PreparedStatement stmt = conn.prepareStatement(queryStr)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
            	maxID = rs.getInt("MAXID");
            }
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
        return ++maxID;
    }

    /**
     *  queries subscription
     *  
     * @param sqlQueryStr
     */
    public Subscription[] query(String sqlQueryStr) {
    	ArrayList<Subscription> cList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sqlQueryStr)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
               cList.add(new Subscription(rs.getLong("SUBSCRIBE_ID"), 
            		                      rs.getString("SUBSCRIBE_TYPE"), 
            		                      rs.getString("URL"), 
            		                      rs.getInt("CLIENT_NUM"),
            		                      rs.getTimestamp("TIMESTAMP"),
            		                      rs.getInt("SERVER_KEY"),
            		                      rs.getString("HOSTNAME"), 
            		                      rs.getInt("PORT") ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cList.toArray(new Subscription[0]);
    }
    
    

}
