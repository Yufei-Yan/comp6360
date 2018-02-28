package edu.auburn.com6360_utility;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class PacketHeader implements Serializable {
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
