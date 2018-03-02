package edu.auburn.com6360_utility;

import edu.auburn.comp6360_vehicles.Vehicle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.instrument.Instrumentation;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class NetworkHandler {
  
  public static int packetState = PacketHeader.NORMAL;
  
  private int headerSize = 117;
          //(int)ObjectSizeFetcher.getObjectSize(new PacketHeader(0, new byte[4], 0));
  
  public NetworkHandler() {
    byte[] temp;
    ByteArrayOutputStream bos = null;
    ObjectOutputStream oos = null;
    
    bos = new ByteArrayOutputStream();
    try {
      oos = new ObjectOutputStream(bos);
      oos.writeObject((Object)new PacketHeader(0, new byte[4], 0));
      oos.flush();
    } catch (IOException ex) {
      Logger.getLogger(NetworkHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    temp = bos.toByteArray();
    this.headerSize = temp.length;
  }
  
//  public int assignNodeNum() {
//    int i = 0;
//    
//    System.out.println("global: " + Global.node[i]);
//    while(i < Global.node.length && true == Global.node[i]) {
//      System.out.println(Global.node[i]);
//      ++i;
//    }
//    System.out.println("assigned node number: " + (i + 1));
//    System.out.println("global: " + Global.node[i]);
//    return i + 1;
//  }
//  
//  public boolean updateNodeArr(int nodeNum) {
//    if (nodeNum >= Global.allNode) {
//      System.out.println("Illegal node number!");
//      return false;
//    }
//    
//    System.out.println("update global: " + Global.node[nodeNum - 1]);
//    Global.node[nodeNum - 1] = true;
//    System.out.println("afater update global: " + Global.node[nodeNum - 1]);
//    return true;
//  }
  
  public byte[] packetAssembler(PacketHeader header, Vehicle veh) throws Exception {
    byte[] data;
    byte[] tempHeader;
    byte[] tempVeh;
    
    ByteArrayOutputStream bos = null;
    ObjectOutputStream oos = null;

    try {
      bos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(bos);
      oos.writeObject((Object)header);
      oos.flush();
      tempHeader = bos.toByteArray();
      headerSize = tempHeader.length;
      //headerLength = temp.length;
      //System.out.println("header length:" + headerLength);
      
      
      bos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(bos);
      oos.writeObject((Object)veh);
      oos.flush();
      tempVeh = bos.toByteArray();
      
      data = new byte[tempHeader.length + tempVeh.length];
      //System.out.println(temp.length);
      System.arraycopy(tempHeader, 0, data, 0, headerSize);
      System.arraycopy(tempVeh, 0, data, headerSize, tempVeh.length);
      
    } finally {
      if (oos != null) {
        oos.close();
      }
      if (bos != null) {
        bos.close();
      }
    }

    return data;
  }
  
  
  public PacketHeader headerDessembler(byte[] data) throws Exception {
    byte[] temp = new byte[data.length];
    System.arraycopy(data, 0, temp, 0, headerSize);
    
    PacketHeader header = null;
    ByteArrayInputStream bis = null;
    ObjectInputStream ois = null;
    
    try {
      bis = new ByteArrayInputStream(temp);
      ois = new ObjectInputStream(bis);
      header = (PacketHeader)ois.readObject();
    } finally {
      if (bis != null) {
        bis.close();
      }
      if (ois != null) {
        ois.close();
      }
    }
    
    return header;
  }
  
  public Vehicle payloadDessembler(byte[] data) throws Exception {
    byte[] temp = new byte[data.length];
    System.arraycopy(data, headerSize, temp, 0, data.length - headerSize);
    
    Vehicle obj = null;
    ByteArrayInputStream bis = null;
    ObjectInputStream ois = null;
    
    try {
      bis = new ByteArrayInputStream(temp);
      ois = new ObjectInputStream(bis);
      obj = (Vehicle)ois.readObject();
    } finally {
      if (bis != null) {
        bis.close();
      }
      if (ois != null) {
        ois.close();
      }
    }
    return obj;
  }
  
  public String rawIpToString(byte[] rawIp) {
    if ( rawIp == null || rawIp.length != 4)
      return null;
      
    //  Convert the raw IP address to a string
    
    StringBuffer str = new StringBuffer();
    
    str.append((int) ( rawIp[0] & 0xFF));
    str.append(".");
    str.append((int) ( rawIp[1] & 0xFF));
    str.append(".");
    str.append((int) ( rawIp[2] & 0xFF));
    str.append(".");
    str.append((int) ( rawIp[3] & 0xFF));

    //  Return the address string

    return str.toString();    
  }
}

/**
 * This is class for calculating the size of an object.
 * Refer to: https://www.quora.com/How-do-we-calculate-the-size-of-the-object-in-Java
 * 
 * @author Unknown
 */
class ObjectSizeFetcher {
    private static Instrumentation instrumentation;
 
    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }
 
    public static long getObjectSize(Object o) {
        return instrumentation.getObjectSize(o);
    }
}