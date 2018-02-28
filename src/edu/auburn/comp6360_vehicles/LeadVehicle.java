package edu.auburn.comp6360_vehicles;

import edu.auburn.com6360_utility.ConfigFileHandler;
import edu.auburn.com6360_utility.NetworkHandler;
import edu.auburn.com6360_utility.PacketHeader;
import edu.auburn.com6360_utility.VehicleParaHandler;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Random;

/**
 *
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class LeadVehicle extends Vehicle {
  
  public LeadVehicle() {
    super();
  }
  
  public LeadVehicle(byte[] addr,
                 Gps initGps,
                 double initVel,
                 double initAcc,
                 VSize initSize,
                 int initNode) {
    super(addr, initGps, initVel, initAcc, initSize, initNode);
  }
  
  @Override
  public void run() {
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
        System.err.println("Failed to write to config file.");
        e.printStackTrace();
      }
      
      if (!ret) {
        System.out.println("Failed to write to config file");
      } else {
        this.startServer();
      }
    } else {
      System.out.println(filename + " found!");
      //configHandler.removeConfigFile();
      try {
        ret = configHandler.writeAll(this);
      } catch (IOException e) {
        System.err.println("Failed to write to config file.");
        e.printStackTrace();
      }
      
//      if (!ret) {
//        System.out.println("Failed to write to config file");
//      } else {
        this.startServer();
      //}
    }
  }
  
  @Override
  public void start() {
    System.out.println("Start lead vehicle server.");
    String threadName = "Lead";

    Thread t = new Thread(this, threadName);
    t.start();
  }
  
  /**
   * Obtain the acceleration for the Leading Vehicle (every 10 ms)
   * 
   * @param
   * @return a random number between -1 and 1
   */
  public void setAcc() {
    this.acceleration = Math.random() * 2 - 1;
  }
  
  
  private void startServer() {
    try {
      this.socketServer();
    } catch (Exception ex) {
      System.err.println(ex);
    }
  }
  
  @Override
  protected void socketServer() throws Exception {
    DatagramSocket server = new DatagramSocket(10121);
    int serverSn = new Random().nextInt(100);
    int clientSn = 0;

    byte[] dataRx = new byte[1024];
    byte[] dataTx = new byte[1024];
    
    
    DatagramPacket sendPacket = null;
    InetAddress IPAddress = null;
    int port = 0;
    while(true) {
      DatagramPacket receivePacket = new DatagramPacket(dataRx, dataRx.length);
      System.out.println("No following vehicles detected.\nBeacon broadcasting");
      
      server.setSoTimeout(timeout);
      
      try {
        server.receive(receivePacket);
        byte[] data = receivePacket.getData();
        PacketHeader clientHeader = new NetworkHandler().headerDessembler(data);
        clientSn = clientHeader.getSn();
//        System.out.println("sn: " + clientHeader.getSn());
//        System.out.println("ip: " + clientHeader.getIp());
//        System.out.println("type: " + clientHeader.getType());
        
        FollowingVehicle fv = (FollowingVehicle)new NetworkHandler().payloadDessembler(data);
//        System.out.println("out of payloadDessembler");
//        System.out.println("fv.nodeNum: " + fv.getNodeNum());
//        System.out.println("fv.addr " + fv.getAddr());
//        System.out.println("fv.length " + fv.getLength());
        
      } catch (SocketTimeoutException e) {
        System.out.println("timeout.");
        this.updateTimeout();
        continue;
      } catch (Exception e) {
        System.err.println(e);
      }
//      String sentence = new String(receivePacket.getData());
//      System.out.println("RECEIVED: " + sentence);
      
      IPAddress = receivePacket.getAddress();
      port = receivePacket.getPort();
      
      PacketHeader serverHeader = new PacketHeader(serverSn, this.getAddr(), NetworkHandler.NORMAL);
      dataTx = new NetworkHandler().packetAssembler(serverHeader, this);
      sendPacket = new DatagramPacket(dataTx, dataTx.length, 
            IPAddress, port);
      
      server.send(sendPacket);
      ++serverSn;
      System.out.println("client SN:" + clientSn);
      System.out.println("server SN:" + serverSn);
      System.out.println();
    }
  }
}
