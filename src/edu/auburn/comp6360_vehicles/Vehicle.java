package edu.auburn.comp6360_vehicles;

/**
 *
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class Vehicle {
  protected String address;
  protected Gps gps;
  protected double velocity;
  protected double acceleration;
  protected int brake;
  protected double gas;
  protected VSize vSize;
  
  public Vehicle(String addr,
                 Gps initGps,
                 double initVel,
                 double initAcc,
                 VSize initSize) {
    this.address = addr;
    this.gps = initGps;
    this.velocity = initVel;
    this.acceleration = initAcc;
    this.vSize = initSize;
    
    this.brake = 1;
    this.gas = 100.0;
  }
  
  /**
   * Copy new GPS coordinates.
   * 
   * @param newGps 
   */
  protected void setGps(Gps newGps) {
    this.gps.setLat(newGps.getLat());
    this.gps.setLon(newGps.getLon());
  }
  
  protected void setVel(double newVel) {
  
  }
  
  protected void setAcc(double newAcc) {
  
  }
  
  protected void setBrake(int newBrake) {
  
  }
  
  protected void setGas(double newGas) {
  
  }
}
