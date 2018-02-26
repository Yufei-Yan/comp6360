package edu.auburn.com6360_utility;

import edu.auburn.comp6360_vehicles.FollowingVehicle;
import edu.auburn.comp6360_vehicles.Gps;
import edu.auburn.comp6360_vehicles.LeadVehicle;
import edu.auburn.comp6360_vehicles.Vehicle;
import edu.auburn.comp6360_vehicles.VSize;


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
   * 
   * @param 
   * @return 
   */
  public void gpsCal() {
  
  }
  
  /**
   * Calculate velocity based on initial velocity, acceleration and 
   * time interval
   * 
   * @param 
   * @return 
   */
  public void velCal() {
  
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
}