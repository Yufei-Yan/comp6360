package edu.auburn.comp6360_vehicles;

import edu.auburn.com6360_utility.ConfigFileHandler;
import edu.auburn.com6360_utility.NetworkHandler;
import edu.auburn.com6360_utility.PacketHeader;
import edu.auburn.com6360_utility.RoadTrainHandler;
import edu.auburn.com6360_utility.VehicleParaHandler;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
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
          int initNode,
          String initServerAddr) {
    super(addr, initGps, initVel, initAcc, initSize, initNode, initServerAddr);
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
	  this.setVel(35);
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
    boolean ret;
    
    ret = configHandler.isFileExist(filename);
    if (!ret) {
      System.out.println("Config file not exists.\n No lead vehicle.");
      System.out.println("Programe exits.");
      System.exit(0);
    } else {
      System.out.println(filename + " found!");
      try {
        configHandler.writeAll(this);
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
    VehicleParaHandler vehPara = new VehicleParaHandler();
    long start = System.nanoTime();
    long end = 0;
    int serverSn = 0;
    int totalPacket = 0;
    int receivedPacket = 0;
    boolean isLoss = false;

    while (true) {
      DatagramSocket client = new DatagramSocket();
      client.setSoTimeout(timeout);
      
      InetAddress IPAddress = InetAddress.getByName(this.serverAddr);
      byte[] dataRx = new byte[1024];
      byte[] dataTx = new byte[1024];
      
      try {
        PacketHeader clientHeader = new PacketHeader(clientSn, this.getAddr(), PacketHeader.NORMAL);
        //System.out.println("fv packet state:" + NetworkHandler.packetState);
        if (PacketHeader.FORM == NetworkHandler.packetState) {
          clientHeader = new PacketHeader(clientSn, this.getAddr(), PacketHeader.FORM);
          NetworkHandler.packetState = PacketHeader.NORMAL;
        } else if (PacketHeader.LEAVE == NetworkHandler.packetState) {
          clientHeader = new PacketHeader(clientSn, this.getAddr(), PacketHeader.LEAVE);
          NetworkHandler.packetState = PacketHeader.NORMAL;
        }
        
        //System.out.println("fv client packet type: " + clientHeader.getType());
        dataTx = new NetworkHandler().packetAssembler(clientHeader, this);

        DatagramPacket sendPacket = new DatagramPacket(dataTx, dataTx.length, IPAddress, 10121);
        client.send(sendPacket);
      } catch (SocketTimeoutException e) {
        System.out.println("Update timeout.");
        this.update(timeout);
        ++clientSn;
        continue;
      } catch (Exception e) {
        System.err.println(e);
      }
      //System.out.println(sendPacket.getData());

      LeadVehicle lv = null;
      PacketHeader serverHeader = null;
      try {
        DatagramPacket receivePacket = new DatagramPacket(dataRx, dataRx.length);
        client.receive(receivePacket);
        //System.out.println("receivePacket size:" + receivePacket.getData().length);

        dataRx = receivePacket.getData();

        serverHeader = new NetworkHandler().headerDessembler(dataRx);
        lv = (LeadVehicle) new NetworkHandler().payloadDessembler(dataRx);

        serverSn = serverHeader.getSn();
        //System.out.println("fv server packet type: " + serverHeader.getType());
        if (PacketHeader.ACCEPT == serverHeader.getType()) {
          if (RoadTrainHandler.roadTrainState == RoadTrainHandler.FORM) {
            System.out.println("Disable link.");
            System.out.println("Lead vehicle accepts LEAVE request.");
            this.setLink(0);
          } else {
            System.out.println("Set up link.");
            System.out.println("Lead vehicle accepts JOIN request.");
            this.setLink(lv.getNodeNum());
          }
          System.out.println("Lead vehicle accepts JOIN/LEAVE request.");
          PacketHeader clientHeader = new PacketHeader(clientSn, this.getAddr(), PacketHeader.ACK);
          dataTx = new NetworkHandler().packetAssembler(clientHeader, this);
          DatagramPacket sendPacket = new DatagramPacket(dataTx, dataTx.length, IPAddress, 10121);
          client.send(sendPacket);
          System.out.println("Acknowledgement sent.");
        }

        isLoss = vehPara.isPacketLoss(lv.getGps(), this.getGps());

      } catch (SocketTimeoutException e) {
        client.close();
        continue;
      } catch (Exception e) {
        System.err.println(e);
      }
      //System.out.println("Client: " + new String(receivePacket.getData()));
      client.close();
      ++clientSn;
      int latency = vehPara.latencyCal(lv.getGps(), this.getGps());
      System.out.println("Client latency: " + latency);
      Thread.sleep(0, latency);
      
      this.update(timeInterval);
      System.out.println("client SN:" + clientSn);
      
      if (!isLoss) {
        System.out.println("server SN:" + serverSn);
        ++receivedPacket;
      } else {
        System.out.println("No server packet Received.");
      }
      ++totalPacket;
      System.out.println("Packet deliver ratio: " + 
                         vehPara.deliverRatio(totalPacket, receivedPacket) * 100 + 
                         "%");
      
      end = System.nanoTime();
      double timeDifference = (end - start) / 1000000000.0;
      int packetSize = vehPara.objectSizeCal(serverHeader) + vehPara.objectSizeCal(lv);
      System.out.println("Throughput: " + 
                         vehPara.throughputCal(packetSize, timeDifference) * 8 + 
                         " bps");
      
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
