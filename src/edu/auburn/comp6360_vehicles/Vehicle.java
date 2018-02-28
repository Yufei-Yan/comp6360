package edu.auburn.comp6360_vehicles;

import edu.auburn.com6360_utility.ConfigFileHandler;
import edu.auburn.com6360_utility.VehicleParaHandler;
import java.io.Serializable;

/**
 *
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class Vehicle implements Runnable, Serializable {
  
  public final int timeout = 1000;
  public final int timeInterval = 750;
  
  protected byte[] address;
  protected Gps gps;
  protected double velocity;
  protected double acceleration;
  protected int brake;
  protected double gas;
  protected VSize vSize;
  protected int nodeNum;
  protected String filename;
  
  public Vehicle() {
    gps = new Gps();
    vSize = new VSize();
    this.brake = 1;
    this.gas = 100.0;
  }
  
  public Vehicle(byte[] addr,
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
  
  public void setAddr(byte[] localAddr) {
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
  
  public void setFilename(String newFile) {
    this.filename = newFile;
  }
  
  public int getNodeNum() {
    return this.nodeNum;
  }
  
  public byte[] getAddr() {
    return this.address;
  }
  
  public Gps getGps() {
    return this.gps;
  }
  
  public double getLength() {
	  return this.vSize.getLen();
  }
  
  public String getFilename() {
    return this.filename;
  }

  public double getVel() {
    return this.velocity;
  }
  
  public double getAcc() {
    return this.acceleration;
  }
  
  public double computeDistance(Vehicle otherVehicle) {
	  double thisX = this.getGps().getLon();
	  double otherX = otherVehicle.getGps().getLon();
	  if (thisX < otherX)
		  return otherX - otherVehicle.getLength() - thisX;
	  else
		  return thisX - this.getLength() - otherX;
  }
  
  @Override
  public void run() {
  
  }
  
  public void start() {
  
  }
  
  protected void socketServer() throws Exception {
  
  }
  
  protected void socketClient() throws Exception {
  
  }
  
  protected void update(int timeInterval) {
    boolean ret = false;
    VehicleParaHandler newGps = new VehicleParaHandler();
    this.setGps(newGps.gpsCal(this.getGps(),
            this.getVel(), this.getAcc(), timeInterval / 1000.0));
    
    ConfigFileHandler update = new ConfigFileHandler(filename);
    int lineNum = update.isNodeExist(this.getNodeNum());
    System.out.println("lineNum: " + lineNum);
    ret = update.updateLine(lineNum, this);
    if (!ret) {
      System.out.println("Fail to update config file.");
    }
  }
}
