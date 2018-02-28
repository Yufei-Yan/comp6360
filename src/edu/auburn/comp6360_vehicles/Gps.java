package edu.auburn.comp6360_vehicles;

// I think a timestamp could be recorded in GPS info, and might be useful for compute the location later

import java.io.Serializable;

// You may delete them if you think they are unnecessary

/**
 *
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class Gps implements Serializable {
  private double lattitude;
  private double longitude;
  private long timestamp;
  
  public double getLat() {
    return lattitude;
  }
  
  public double getLon() {
    return longitude;
  }
  
  public void setLat(double lat) {
    this.lattitude = lat;
  }
  
  public void setLon(double lon) {
    this.longitude = lon;
  }
  
  public void setTimestamp(long time) {
	  this.timestamp = time;
  }
  
  public long getTimestamp() {
	  return timestamp;
  }
  
}
