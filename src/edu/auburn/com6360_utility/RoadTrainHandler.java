package edu.auburn.com6360_utility;

import com.sun.corba.se.impl.io.FVDCodeBaseImpl;
import edu.auburn.comp6360_vehicles.FollowingVehicle;
import edu.auburn.comp6360_vehicles.Gps;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoadTrainHandler implements Runnable {
  
  public static int roadTrainState = 0;
  
  public static final int NORMAL = 0;
  public static final int FORM = 1;
  public static final int LEAVE = 2;
  
  private FollowingVehicle roadTrainFV = null;
  private String filename;
  private double rtDistance; //the predefined road train distance 
  
  public RoadTrainHandler (FollowingVehicle fv, String file) {
    this.roadTrainFV = fv;
    this.filename = file;
  }

  @Override
  public void run() {
    roadTrainState = NORMAL;
    
    ConfigFileHandler configFile = new ConfigFileHandler(filename);
    VehicleParaHandler paraHandler = new VehicleParaHandler();
    
    Gps leadGps = this.getRtGps(configFile, 1);
    Gps followGps = this.getRtGps(configFile, roadTrainFV.getNodeNum());
    while (null == leadGps || null == followGps) {
      leadGps = this.getRtGps(configFile, 1);
      followGps = this.getRtGps(configFile, roadTrainFV.getNodeNum());
    }
    //Gps followGps = this.getRtGps(configFile, 2);
    //System.out.println("fgps:" + followGps.getLon());
    //System.out.println("lgps:" + leadGps.getLon());
    double distance = paraHandler.distanceCal(followGps, leadGps);
    System.out.println("Distance00: " + distance);
    
    if (distance > rtDistance) {
      roadTrainFV.catchup();
      while (distance > rtDistance) {
        try {
          Thread.sleep(roadTrainFV.timeInterval);
          leadGps = this.getRtGps(configFile, 1);
          followGps = this.getRtGps(configFile, roadTrainFV.getNodeNum());
          if (leadGps == null || followGps == null) {
            continue;
          }
          distance = paraHandler.distanceCal(followGps, leadGps);
          //System.out.println("fgps:" + followGps.getLon());
          //System.out.println("lgps:" + leadGps.getLon());
          System.out.println("Distance: " + distance);
        } catch (InterruptedException ex) {
          Logger.getLogger(RoadTrainHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    
    System.out.println("Following and lead vehicles are now within predefined distance.");
    System.out.println("Following vehicle brakes.");
    roadTrainFV.setVel(30);
    roadTrainState = FORM;
    try {
      Thread.sleep(250);
    } catch (InterruptedException ex) {
      Logger.getLogger(RoadTrainHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    System.out.println("Keeping the disntance.");
    
    while (true) {
      Scanner sc = new Scanner(System.in);
      String key = sc.next();

      if (FORM == roadTrainState) {
        if (key.equals("form")) {
          System.out.println("Form road train.");
          NetworkHandler.packetState = PacketHeader.FORM;
          roadTrainState = LEAVE;
        }
        //System.out.println("rt packet state:" + NetworkHandler.packetState);
      } else {
        if (key.equals("leave")) {
          System.out.println("Leave road train.");
          NetworkHandler.packetState = PacketHeader.LEAVE;
          roadTrainState = FORM;
        }
        //System.out.println("packet state:" + NetworkHandler.packetState);
      }
      
    }
  }

  public void start() {
    System.out.println("Roadtrain.");
    String thraedName = "Roadtrain";
    
    Thread t = new Thread(this, thraedName);
    t.start();
  }
  
  private Gps getRtGps(ConfigFileHandler configFile, int nodeNum) {
    boolean isFileExist = configFile.isFileExist(filename);
    int lineNum = 0;
    
    
    while(!isFileExist) {
      try {
        Thread.sleep(5);
        isFileExist = configFile.isFileExist(filename);
      } catch (InterruptedException ex) {
        Logger.getLogger(RoadTrainHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    while (0 != lineNum) {
      try {
        lineNum = configFile.isNodeExist(nodeNum);
        Thread.sleep(5);
      } catch (InterruptedException ex) {
        Logger.getLogger(RoadTrainHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    Gps gps = configFile.getGps(nodeNum);
    if (null != gps) {
      //System.out.println("rt gps:" + gps.getLat() + " " + gps.getLon());
    } else {
      System.out.println("No GPS data.");
    }
    return gps;
  }
  
  public void setRtDistance(double newRtDistance) {
    this.rtDistance = newRtDistance;
  }
}
