package edu.auburn.com6360_utility;

import edu.auburn.comp6360_vehicles.FollowingVehicle;
import edu.auburn.comp6360_vehicles.Gps;
import edu.auburn.comp6360_vehicles.LeadVehicle;
import edu.auburn.comp6360_vehicles.Vehicle;
import edu.auburn.comp6360_vehicles.VSize;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

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
  public Vehicle vehicleGenrator(String pos, String filename, String serverAddr) {
    VehicleParaHandler u = new VehicleParaHandler();
    Vehicle veh;
    VSize vSize;
    if (pos.equals("lead")) {
      veh = new LeadVehicle();
      vSize = new VSize();
      veh.setNodeNum(1);
    } else {
      veh = new FollowingVehicle();
      vSize = new VSize(5.0, 5.0);
      veh.setNodeNum(new ConfigFileHandler(filename).getBiggestNode() + 1);
      System.out.println("node number:" + veh.getNodeNum());
    }
    
    veh.setAddr(u.getAddr());
    veh.setGps(u.gpsGenerator(pos));
    veh.setVel(30);
    veh.setAcc(0);
    veh.setSize(vSize);
    veh.setFilename(filename);
    veh.setServerAddr(serverAddr);
    
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
      newGps.setLat(5);
      newGps.setLon(0);
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
   * Calculate the horizontal distance based on GPS coordinates.
   * 
   * @param fGps GPS GPS on following vehicle
   * @param lGps GPS on lead vehicle
   * @return the horizontal distance
   */
  public double distanceCal(Gps fGps, Gps lGps) {
    return lGps.getLon() - fGps.getLon();
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
  public double throughputCal(int bytes, double time) {
    return bytes / time;
  
  }
  
  /**
   * Calculate packet loss rate
   * 
   * @param lead GPS on lead vehicle
   * @param follow GPS on following vehicle
   * @return the packet loss possibility
   */
  public double packetLossCal(Gps lead, Gps follow) {
    double rate = 0;
    
    double jitter = (95 + Math.random() * 10) / 100;
    double distance = lead.getLon()- follow.getLon();
    double possibility = 90.158730 - (0.00873 * distance * distance) + (0.571428 * distance);
    System.out.println("poss:" + possibility);
    
    rate = jitter * possibility / 100;

    return rate;
  }
  
  /**
   * Determine if the packet is lost
   * 
   * @param lead GPS on lead vehicle
   * @param follow GPS on following vehicle
   * @return true if the packet should be lost;
   *         false if the packet should not be lost.
   */
  public boolean isPacketLoss(Gps lead, Gps follow) {
    double rate = this.packetLossCal(lead, follow);
    double toss = rate * ((50 + Math.random() * 50)/100);
    System.out.println("toss: " + toss);
    
    if (toss < 0.5) {
      return true;
    }
    
    return false;
  }
  
  /**
   * Calculate packet deliver ratio
   * 
   * @param totalPacket all packets sent by server/client
   * @param receivePacket all received packets sent by server/client
   * @return packet deliver ratio
   */
  public double deliverRatio(int totalPacket, int receivePacket) {
    return ((double)receivePacket) / totalPacket;
  }
  
  /**
   * Calculate end-to-end latency
   * 
   * @param lead GPS on lead vehicle
   * @param follow GPS on following vehicle
   * @return latency in nano second.
   */
  public int latencyCal(Gps lead, Gps follow) {
    int latency = 0;
    //assume processing time and transmission delay is 10000 nano seconds.
    int pt = 10000;
    
    double distance = lead.getLon()- follow.getLon();
    latency = (int) (pt + (distance / 300000000) * Math.pow(10, 9));
    
    return latency;
  }
  
  private byte[] getAddr() {
    InetAddress ipAddr = null;
    
    String interfaceName = "enp1s0";
    String ip = "localhost";
    NetworkInterface networkInterface;
    try {
      networkInterface = NetworkInterface.getByName(interfaceName);
      Enumeration<InetAddress> inetAddress = networkInterface.getInetAddresses();
      InetAddress currentAddress;
      currentAddress = inetAddress.nextElement();
      while (inetAddress.hasMoreElements()) {
        currentAddress = inetAddress.nextElement();
        if (currentAddress instanceof Inet4Address && !currentAddress.isLoopbackAddress()) {
          ip = currentAddress.toString();
          ip = ip.substring(1);
          System.out.println(ip);
          break;
        }
      }
    } catch (SocketException ex) {
      Logger.getLogger(VehicleParaHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    try {
      ipAddr = InetAddress.getByName(ip);
    } catch (UnknownHostException ex) {
      System.out.println("Cannot get localhost IP address.");
      ex.printStackTrace();
      //return "127.0.0.1";
    }
    return ipAddr.getAddress();
  }
  
  public int objectSizeCal(Object obj) {
    byte[] temp;
    ByteArrayOutputStream bos = null;
    ObjectOutputStream oos = null;
    
    bos = new ByteArrayOutputStream();
    try {
      oos = new ObjectOutputStream(bos);
      oos.writeObject(obj);
      oos.flush();
    } catch (IOException ex) {
      Logger.getLogger(NetworkHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    temp = bos.toByteArray();
    return temp.length;
  }

}
