package com.fluke.data.intraday;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class IntradayDetails {

@Expose
private Meta meta;
@Expose
private List<Series> series = new ArrayList<Series>();

/**
* 
* @return
* The meta
*/
public Meta getMeta() {
return meta;
}

/**
* 
* @param meta
* The meta
*/
public void setMeta(Meta meta) {
this.meta = meta;
}

/**
* 
* @return
* The series
*/
public List<Series> getSeries() {
return series;
}

/**
* 
* @param series
* The series
*/
public void setSeries(List<Series> series) {
this.series = series;
}

}