package edu.auburn.comp6360_vehicles;

/**
 *
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class Gps {
  private double lattitude;
  private double longitude;
  
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
}
