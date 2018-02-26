package edu.auburn.comp6360_vehicles;

import edu.auburn.com6360_utility.ConfigFileHandler;
import java.io.IOException;

/**
 *
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class LeadVehicle extends Vehicle {
  
  public LeadVehicle() {
    super();
  }
  
  public LeadVehicle(String addr,
                 Gps initGps,
                 double initVel,
                 double initAcc,
                 VSize initSize,
                 int initNode) {
    super(addr, initGps, initVel, initAcc, initSize, initNode);
  }
  
  public void runLead(String filename) {
    System.out.println(this.address);
    System.out.println(this.gps.getLat());
    
    ConfigFileHandler configHandler = new ConfigFileHandler(filename);
    boolean ret = false;
    ret = configHandler.isFileExist(filename);
    if (!ret) {
      System.out.println(filename + " not exits");
      ret = configHandler.createConfigFile();
      if (!ret) {
        System.err.println("config file not created!\nProgram exits");
        System.exit(0);
      }
      System.out.println("New file created...");
      
      try {
        ret = configHandler.writeAll(this);
      } catch (IOException e) {
        System.err.println("Fialed to write to config file.");
        e.printStackTrace();
      }
      
      if (!ret) {
        System.out.println("Failed to write to config file");
      } else {
        
      }
      
    } else {
      System.out.println(filename + " found!");
      //configHandler.removeConfigFile();
      try {
        ret = configHandler.writeAll(this);
      } catch (IOException e) {
        System.err.println("Fialed to write to config file.");
        e.printStackTrace();
      }
      System.out.println("Old file removed...");
    }
  }
}
