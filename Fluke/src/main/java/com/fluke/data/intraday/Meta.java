/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.data.intraday;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Meta {

@Expose
private String uri;
@Expose
private String ticker;
@SerializedName("Company-Name")
@Expose
private String CompanyName;
@SerializedName("Exchange-Name")
@Expose
private String ExchangeName;
@Expose
private String unit;
@Expose
private String timezone;
@Expose
private String currency;
@Expose
private Integer gmtoffset;
@SerializedName("previous_close")
@Expose
private Float previousClose;

/**
* 
* @return
* The uri
*/
public String getUri() {
return uri;
}
public String getId(){
    return this.ticker.split("\\.")[0];
}
/**
* 
* @param uri
* The uri
*/
public void setUri(String uri) {
this.uri = uri;
}

/**
* 
* @return
* The ticker
*/
public String getTicker() {
return ticker;
}

/**
* 
* @param ticker
* The ticker
*/
public void setTicker(String ticker) {
this.ticker = ticker;
}

/**
* 
* @return
* The CompanyName
*/
public String getCompanyName() {
return CompanyName;
}

/**
* 
* @param CompanyName
* The Company-Name
*/
public void setCompanyName(String CompanyName) {
this.CompanyName = CompanyName;
}

/**
* 
* @return
* The ExchangeName
*/
public String getExchangeName() {
return ExchangeName;
}

/**
* 
* @param ExchangeName
* The Exchange-Name
*/
public void setExchangeName(String ExchangeName) {
this.ExchangeName = ExchangeName;
}

/**
* 
* @return
* The unit
*/
public String getUnit() {
return unit;
}

/**
* 
* @param unit
* The unit
*/
public void setUnit(String unit) {
this.unit = unit;
}

/**
* 
* @return
* The timezone
*/
public String getTimezone() {
return timezone;
}

/**
* 
* @param timezone
* The timezone
*/
public void setTimezone(String timezone) {
this.timezone = timezone;
}

/**
* 
* @return
* The currency
*/
public String getCurrency() {
return currency;
}

/**
* 
* @param currency
* The currency
*/
public void setCurrency(String currency) {
this.currency = currency;
}

/**
* 
* @return
* The gmtoffset
*/
public Integer getGmtoffset() {
return gmtoffset;
}

/**
* 
* @param gmtoffset
* The gmtoffset
*/
public void setGmtoffset(Integer gmtoffset) {
this.gmtoffset = gmtoffset;
}

/**
* 
* @return
* The previousClose
*/
public Float getPreviousClose() {
return previousClose;
}

/**
* 
* @param previousClose
* The previous_close
*/
public void setPreviousClose(Float previousClose) {
this.previousClose = previousClose;
}

}