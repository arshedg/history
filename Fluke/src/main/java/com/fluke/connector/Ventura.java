/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.connector;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author arsh
 */
public class Ventura {

    static String template = "{\n"
            + "\"scrip_code\":\"%s\",\n"
            + "\"quantity\": %s,\n"
            + "\"rate\": %s,\n"
            + "\"trigger_rate\": %s,\n"
            + "\"is_stoploss\": %s,\n"
            + "\"place_at_market\": false,\n"
            + "\"order_type\": \"%s\"\n"
            + "}";
     static String template2 = "{\n"
            + "\"scrip_code\":\"%s\",\n"
            + "\"quantity\": %s,\n"
            + "\"rate\": %s,\n"
            + "\"trigger_rate\": %s,\n"
            + "\"is_stoploss\": %s,\n"
            + "\"place_at_market\": true,\n"
            + "\"order_type\": \"%s\"\n"
            + "}";
    static String order = "http://localhost/orders.json";
    static String cancel = "http://localhost/orders/%s/cancell";
    static String details = "http://localhost/orders/%s.json";

    public int placeBuy(String equity, float price, float trigger) {
        boolean isStopLoss = trigger > 0;
        String command = String.format(template, equity, getQuantity(price), price, trigger, isStopLoss, "buy");
        String response = execute(command, order).trim();
        return getId(response);
    }

    private int getId(String response) {
        JSONObject json = new JSONObject(response);
        return json.getInt("id");
    }

    public int placeSell(String equity, float price, float trigger) {
        boolean isStopLoss = trigger > 0;
        String command = String.format(template2, equity, getQuantity(price), price, trigger, isStopLoss, "sell");
        String response = execute(command, order).trim();
        return getId(response);

    }

    public boolean isExecuted(int id) {
        String url = String.format(details, id);
        try {

            String response = executeGet(url);
           // System.out.println(response);
            String status = new JSONObject(response).getString("status");
            if (status.equals("completed")) {
                return true;
            }
            if (status.equals("pending") || status.contains("place")) {
                return false;
            }
            System.out.println("UNKNOWN status for id:" + id + " " + status);
            return false;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public boolean cancelTrade(int id) {
        String url = String.format(cancel, id);
        execute("", url);
        return true;
    }

    private int getQuantity(float price) {
        int quantity = (int) (30000 / price);
        return quantity == 0 ? 1 : quantity;
    }

    private String executeGet(String api) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpGet request = new HttpGet(api);
            HttpResponse response = httpClient.execute(request);
            return IOUtils.toString(response.getEntity().getContent());
            // handle response here...
        } catch (Exception ex) {
            throw new RuntimeException("Failed to pace order", ex);
        }
    }

    private String execute(String command, String api) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(api);
            StringEntity params = new StringEntity(command);
            request.setEntity(params);
            request.addHeader("content-type", "application/json");
            request.addHeader("Accept", "application/json");
            HttpResponse response = httpClient.execute(request);
            return IOUtils.toString(response.getEntity().getContent());
            // handle response here...
        } catch (Exception ex) {
            throw new RuntimeException("Failed to pace order", ex);
        }
    }

    public static void main(String[] args) {
        new Ventura().isExecuted(1);
    }
}
