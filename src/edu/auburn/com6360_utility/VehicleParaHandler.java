package edu.auburn.com6360_utility;

import edu.auburn.comp6360_vehicles.FollowingVehicle;
import edu.auburn.comp6360_vehicles.Gps;
import edu.auburn.comp6360_vehicles.LeadVehicle;
import edu.auburn.comp6360_vehicles.Vehicle;
import edu.auburn.comp6360_vehicles.VSize;

import java.util.Random;

/**
 * Handle all parameters needed by vehicles
 * 
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class VehicleParaHandler {
  
  /**
   * Generate a vehicle object with initial state.
   * 
   * @param pos indicates if it is lead or following vehicle.
   * @return a Vehicle object
   */
  public Vehicle vehicleGenrator(String pos) {
    VehicleParaHandler u = new VehicleParaHandler();
    Vehicle veh;
    VSize vSize;
    if (pos.equals("lead")) {
      veh = new LeadVehicle();
      vSize = new VSize();
    } else {
      veh = new FollowingVehicle();
      vSize = new VSize(5.0, 5.0);
    }
    
    veh.setAddr(u.getAddr());
    veh.setGps(u.gpsGenerator(pos));
    veh.setVel(30);
    veh.setAcc(0);
    veh.setSize(vSize);
    veh.setNodeNum(new NetworkHandler().assignNodeNum());
    new NetworkHandler().updateNodeArr(veh.getNodeNum());
    
    return veh;
  }
  
  /**
   * Generate dummy GPS coordinates.
   * 
   * @param pos indicates if it is lead or following vehicle.
   * @return a GPS object
   */
  public Gps gpsGenerator(String pos) {
    System.out.println(pos);
    Gps newGps = new Gps();
    
    if (pos.equals("lead")) {
      newGps.setLat(0);
      newGps.setLon(0);
    } else {

    }
    
    System.out.println(newGps.getLat());
    return newGps;
  }
  
  /**
   * Calculate GPS coordinates based on velocity and acceleration
   * @param oldLoc: old location
   * @param velocity: initial velocity
   * @param acceleration
   * @param time interval
   * @return the new location
   */
  public Gps gpsCal(Gps oldLoc, double velocity, double acceleration, double timeInterval) {
	  Gps newLoc = new Gps();
	  newLoc.setLat(oldLoc.getLat());
	  double distance = velocity * timeInterval + .5 * acceleration * timeInterval * timeInterval;
	  newLoc.setLon(oldLoc.getLon() + distance);
	  return newLoc;
  }
  
  /**
   * Calculate velocity based on initial velocity, acceleration and 
   * time interval
   * 
   * @param initial velocity
   * @param acceleration
   * @param time interval
   * @return new velocity after timeInterval
   */
  public double velCal(double velocity, double acceleration, double timeInterval) {
	velocity += acceleration * timeInterval;
	return velocity;
  }
  
  /**
   * Calculate throughput
   * 
   * @param 
   * @return 
   */
  public void throughputCal() {
  
  }
  
  /**
   * Calculate packet loss rate
   * 
   * @param 
   * @return 
   */
  public void packetLossCal() {
  
  }
  
  /**
   * Calculate end-to-end latency
   * 
   * @param 
   * @return 
   */
  public void latencyCal() {
  
  }
  
  private String getAddr() {
    return "localhost";
  }
  
  /**
   * Obtain the acceleration for the Leading Vehicle (every 10 ms)
   * 
   * @param
   * @return a random number between -1 and 1
   */
  public double randomAcceleration() {
	  return Math.random() * 2 - 1;
  }
  
  
}
