package edu.auburn.comp6360_vehicles;

import edu.auburn.com6360_utility.ConfigFileHandler;
import edu.auburn.com6360_utility.NetworkHandler;
import static edu.auburn.com6360_utility.NetworkHandler.NORMAL;
import edu.auburn.com6360_utility.PacketHeader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class FollowingVehicle extends Vehicle {

  public FollowingVehicle() {
    super();
  }
  
  public FollowingVehicle(byte[] addr,
          Gps initGps,
          double initVel,
          double initAcc,
          VSize initSize,
          int initNode) {
    super(addr, initGps, initVel, initAcc, initSize, initNode);
  }
  
  /**
   * Send the request for forming the road train
   * 
   * @return 
   */
  public int formRequest() {
    return 0;
  }
  
  /**
   * Send the request for leaving the road train
   * 
   * @return 
   */
  public int leaveRequest() {
    return 0;
  }
  
  /**
   * When receives a message from leading vehicle allowing it to form the road train, and is far away from the road train
   * Tries to accelerate and catch up the road train to the appropriate location
   * 
   */
  public void catchup() {
	  this.setVel(33.3);
	  this.setAcc(0);
  }
  
  /** Evaluate the eligibility to join the road train while catching up
   * @param targetVehicle: the vehicle that this one tries to be right behind
   * @param targetDistance: the distance by which this one tries to be behind the target vehicle
   * 
   * @return whether it is the time to start into the follow mode
   * 
   */
  public boolean canJoin(Vehicle targetVehicle, double targetDistance) {
	  double distance = this.computeDistance(targetVehicle);
	  if (distance <= targetDistance)
		  return true;
	  return false;
  }
  
  
  /**
   * While in the road train, controlled by the leading vehicle, follow the velocity and acceleration according to the leading vehicle
   * 
   * @param leadVel: the velocity information of the leading vehicle received from the packet
   * @param leadAcc: the acceleration information of the leading vehicle received from the packet
   */
  public void follow(double leadVel, double leadAcc) {
	  this.setVel(leadVel);
	  this.setAcc(leadAcc);
  }
  
  @Override
  public void run() {
    
    ConfigFileHandler configHandler = new ConfigFileHandler(filename);
    boolean ret = false;
    
    ret = configHandler.isFileExist(filename);
    if (!ret) {
      System.out.println("Config file not exists.\n No lead vehicle.");
      System.out.println("Programe exits.");
      System.exit(0);
    } else {
      System.out.println(filename + " found!");
      try {
        ret = configHandler.writeAll(this);
      } catch (IOException e) {
        System.err.println("Failed to write to config file.");
        e.printStackTrace();
      }
    }
    
    this.startClient();
  }
  
  @Override
  public void start() {
    System.out.println("Start following vehicle client.");
    String threadName = "Follow";

    Thread t = new Thread(this, threadName);
    t.start();
  }
  
  @Override
  protected void socketClient() throws Exception {
    System.out.println("in socket client");
    int clientSn = new Random().nextInt(100);
    int serverSn = 0;

    while (true) {
      DatagramSocket client = new DatagramSocket();

      InetAddress IPAddress = InetAddress.getByName("localhost");
      byte[] dataRx = new byte[1024];
      byte[] dataTx = new byte[1024];
      
      PacketHeader clientHeader = new PacketHeader(clientSn, this.getAddr(), NetworkHandler.NORMAL);
      dataTx = new NetworkHandler().packetAssembler(clientHeader, this);

      DatagramPacket sendPacket = new DatagramPacket(dataTx, dataTx.length, IPAddress, 10121);
      client.send(sendPacket);
      //System.out.println(sendPacket.getData());

      DatagramPacket receivePacket = new DatagramPacket(dataRx, dataRx.length);
      client.receive(receivePacket);
      
      dataRx = receivePacket.getData();
      
      PacketHeader serverHeader = new NetworkHandler().headerDessembler(dataRx);
      LeadVehicle lv = (LeadVehicle)new NetworkHandler().payloadDessembler(dataRx);
      
      serverSn = serverHeader.getSn();

      //System.out.println("Client: " + new String(receivePacket.getData()));
      client.close();
      ++clientSn;
      System.out.println("client SN:" + clientSn);
      System.out.println("server SN:" + serverSn);
      System.out.println();
      Thread.sleep(timeInterval);
    }
  }
  
  private void startClient() {
    try {
      this.socketClient();
    } catch (Exception ex) {
      System.err.println(ex);
      Logger.getLogger(LeadVehicle.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
