package edu.auburn.com6360_utility;

import java.io.File;
import edu.auburn.comp6360_vehicles.Gps;
import edu.auburn.comp6360_vehicles.Vehicle;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Handle all functionalities for the configuration file
 * 
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class ConfigFileHandler {
  
  private String filename;
  
  public ConfigFileHandler(String name) {
    this.filename = name;
  }
  
  /**
   * Method to check if the config file exists.
   * 
   * @param filename configuration file name
   * @return 
   */
  public boolean isFileExist(String filename) {
    boolean ret = false;
    
    File configFile = new File(filename);
    if (configFile.exists()) {
      ret = true;
      return ret;
    }
    
    return ret;
  }
  
  /**
   * Method to create a new config file
   * 
   * @param filename
   * @return 
   */
  public boolean createConfigFile() {
    boolean ret = false;
    
    File configFile = new File(filename);
    try {
      ret = configFile.createNewFile();
    } catch (IOException ex) {
      System.err.println("Cannot create file: ");
      ex.printStackTrace();
      return ret;
    }
    
    return ret;
  }
  
  /**
   * Method to remove old config file
   * 
   * @param filename
   * @return 
   */
  public boolean removeConfigFile() {
    boolean ret = false;
  
    File configFile = new File(filename);
    ret = configFile.delete();
    
    return ret;
  }
  
  public boolean writeAll(Vehicle veh) throws IOException {
    boolean ret = false;
    
    ret = this.isNodeExist(veh.getNodeNum());
    if (ret) {
      System.out.println("Node already exists.");
      ret = false;
      return ret;
    } else {
      String all = veh.getNodeNum() + " "
                 + veh.getAddr() + " "
                 + "port" + " "
                 + veh.getGps().getLat() + " "
                 + veh.getGps().getLon() + " ";
      FileWriter fstream = new FileWriter(filename, true);
      BufferedWriter writer = new BufferedWriter(fstream);
      
      writer.write(all);
      writer.newLine();
      writer.close();
    }
    
    return ret;
  }
  
  /**
   * Method to write node number to config file.
   * 
   * @param nodeNum
   * @return 
   */
  public boolean writeNode(int nodeNum) {
    boolean ret = false;
    
//    ret = this.isNodeExist(nodeNum);
//    if (ret) {
//      System.out.println("Node already exists.");
//      ret = false;
//      return ret;
//    } else {
//      
//    }
    
    return ret;
  }
  
  public boolean writeAddr(String addr, int nodeNum) {
    boolean ret = false;
    
    return ret;
  }
  
  public boolean writePort(String port, int nodeNum) {
    boolean ret = false;
    
    return ret;
  }
  
  public boolean writeGps(Gps gps, int nodeNum) {
    boolean ret = false;
    
    return ret;
  }
  
  public boolean writeStatus(String linkName, int nodeNum) {
    boolean ret = false;
    
    return ret;
  }
  
  public boolean isNodeExist(int nodeNum) {
    boolean ret = false;
    
    Path path = Paths.get(filename);
    List<String> lines = null;
    try {
      lines = Files.readAllLines(path, StandardCharsets.UTF_8);
    } catch (IOException ex) {
      System.err.println("Exception caught.\n Program exits");
      ex.printStackTrace();
      System.exit(0);
    }
    String[] tokens;
    for (String line : lines) {
      tokens = line.split(" ");
      if (tokens[0].equals(Integer.toString(nodeNum))) {
        ret = true;
        return ret;
      }
    }
    
    return ret;
  }
}
