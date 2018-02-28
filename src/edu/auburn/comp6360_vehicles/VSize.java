package edu.auburn.comp6360_vehicles;

import java.io.Serializable;

/**
 *
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class VSize implements Serializable {
  private double length;
  private double width;
  
  public VSize() {
    length = 10.0;
    width = 5.0;
  }
  
  public VSize(double len, double wid) {
    this.length = len;
    this.width = wid;
  }
  
  public double getLen() {
    return length;
  }
  
  public double getWid() {
    return width;
  }
  
  public void setLen(double len) {
    this.length = len;
  }
  
  public void setWid(double wid) {
    this.width = wid;
  }
}
