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
  protected int nodeNum;
  
  public Vehicle() {
    gps = new Gps();
    vSize = new VSize();
    this.brake = 1;
    this.gas = 100.0;
  }
  
  public Vehicle(String addr,
                 Gps initGps,
                 double initVel,
                 double initAcc,
                 VSize initSize,
                 int initNode) {
    this.address = addr;
    this.gps = initGps;
    this.velocity = initVel;
    this.acceleration = initAcc;
    this.vSize = initSize;
    this.nodeNum = initNode;
    
    this.brake = 1;
    this.gas = 100.0;
  }
  
  public void setAddr(String localAddr) {
    this.address = localAddr;
  }
  
  /**
   * Copy new GPS coordinates.
   * 
   * @param newGps 
   */
  public void setGps(Gps newGps) {
    this.gps.setLat(newGps.getLat());
    this.gps.setLon(newGps.getLon());
  }
  
  public void setVel(double newVel) {
    this.velocity = newVel;
  }
  
  public void setAcc(double newAcc) {
    this.acceleration = newAcc;
  }
  
  public void setBrake(int newBrake) {
    this.brake = newBrake;
  }
  
  public void setGas(double newGas) {
    this.gas = newGas;
  }
  
  public void setSize(VSize newSize) {
    this.vSize.setLen(newSize.getLen());
    this.vSize.setWid(newSize.getWid());
  }
  
  public void setNodeNum(int newNum) {
    this.nodeNum = newNum;
  }
  
  public int getNodeNum() {
    return this.nodeNum;
  }
  
  public String getAddr() {
    return this.address;
  }
  
  public Gps getGps() {
    return this.gps;
  }
}
