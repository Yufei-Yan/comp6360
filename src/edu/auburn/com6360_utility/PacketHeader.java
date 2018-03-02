package edu.auburn.com6360_utility;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class PacketHeader implements Serializable {
  public static final int NORMAL = 0;
  public static final int FORM = 1;
  public static final int LEAVE = 2;
  public static final int ACCEPT = 3;
  public static final int ACK = 4;
  public static final int DECLINE = 5;
  
  private int sn;
  private byte[] ip;
  private int type;
  
  public PacketHeader(int newSn, byte[] newIp, int newType) {
    this.sn = newSn;
    this.ip = Arrays.copyOf(newIp, newIp.length);
    this.type = newType;
  } 
  
  public int getSn() {
    return this.sn;
  }
  
  public byte[] getIp() {
    return this.ip;
  } 
  
  public int getType() {
    return this.type;
  }
  
}
