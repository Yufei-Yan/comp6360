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
import java.util.logging.Level;
import java.util.logging.Logger;

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
   * @param 
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
    System.out.println("in writeAll");
    boolean ret = false;
    int lineNum = 0;
    
    lineNum = this.isNodeExist(veh.getNodeNum());
    if (0 != lineNum) {
      System.out.println("Node already exists.");
      ret = false;
      return ret;
    } else {
      String all = veh.getNodeNum() + " "
                 + new NetworkHandler().rawIpToString(veh.getAddr()) + " "
                 + "10121" + " "
                 + veh.getGps().getLat() + " "
                 + veh.getGps().getLon() + " "
                 + veh.getLink();
      FileWriter fstream = new FileWriter(filename, true);
      BufferedWriter writer = new BufferedWriter(fstream);
      
      writer.write(all);
      writer.newLine();
      writer.close();
      ret = true;
    }
    
    return ret;
  }
  
  public boolean updateAll(Vehicle veh) throws Exception {
    System.out.println("in updateAll");
    boolean ret = false;
    int lineNum = 0;
    
    lineNum = this.isNodeExist(veh.getNodeNum());
    if (0 == lineNum) {
      return ret;
    }
    return this.writeAll(veh);
  }
  
  /**
   * Method to write node number to config file.
   * 
   * @param nodeNum
   * @return 
   */
  public boolean updateNode(int nodeNum) {
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
  
  public boolean updateStatus(String linkName, int nodeNum) {
    boolean ret = false;
    
    return ret;
  }
  
  public int isNodeExist(int nodeNum) {
    int lineNum = 0;
    
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
    int count = 0;
    for (String line : lines) {
      ++count;
      tokens = line.split(" ");
      if (tokens[0].equals(Integer.toString(nodeNum))) {
        lineNum = count;
      }
    }
    
    return lineNum;
  }
  
  public int getBiggestNode() {
    
    Path path = Paths.get(filename);
    List<String> lines = null;
    
    try {
      lines = Files.readAllLines(path, StandardCharsets.UTF_8);
    } catch (IOException ex) {
      System.err.println("Exception caught.\n Program exits");
      ex.printStackTrace();
      System.exit(0);
    }
    
    String line = lines.get(lines.size() - 1);
    String[] tokens;
    tokens = line.split(" ");
    
    return new Integer(tokens[0]);
  }
  
  public boolean updateLine(int lineNum, Vehicle veh) {
    //System.out.println("in updateLine");
    boolean ret = false;
    
    Path path = Paths.get(filename);
    List<String> lines = null;
    
    try {
      lines = Files.readAllLines(path, StandardCharsets.UTF_8);
      ret = true;
    } catch (IOException ex) {
      System.err.println("Exception caught.\n Program exits");
      ex.printStackTrace();
      System.exit(0);
    }
    
    String all = veh.getNodeNum() + " "
                 + new NetworkHandler().rawIpToString(veh.getAddr()) + " "
                 + "10121" + " "
                 + veh.getGps().getLat() + " "
                 + veh.getGps().getLon() + " "
                 + veh.getLink(); 

    if (lineNum != 0 && !lines.isEmpty()) {
      lines.set(lineNum - 1, all);
    } else {
      return false;
    }
    
    try {
      Files.write(path, lines);
      ret = true;
    } catch (IOException ex) {
      System.err.println(ex);
      ex.printStackTrace();
    }
    
    return ret;
  }
  
  public Gps getGps(int nodeNum) {
    //System.out.println("getGps: " + nodeNum);
    if (0 == nodeNum) {
      return null;
    }
    
    Gps vGps = new Gps();
    
    Path path = Paths.get(filename);
    List<String> lines = null;
    
    try {
      lines = Files.readAllLines(path, StandardCharsets.UTF_8);
    } catch (IOException ex) {
      System.err.println("Exception caught.\n Program exits");
      ex.printStackTrace();
      System.exit(0);
    }
    
    int lineNum = this.isNodeExist(nodeNum);
    
    String line;
    if (0 != lineNum && 0 != lines.size()) {
      line = lines.get(lineNum - 1);
      String[] tokens;
      tokens = line.split(" ");

      vGps.setLat(Double.parseDouble(tokens[3]));
      vGps.setLon(Double.parseDouble(tokens[4]));
      return vGps;
    }
    
    return null;
  }
}
