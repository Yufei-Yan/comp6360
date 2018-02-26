package edu.auburn.com6360_utility;

/**
 *
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class NetworkHandler {
  public static final int allNode = 50;
  public static boolean[] node = new boolean[allNode];
  
  public int assignNodeNum() {
    int i = 0;
    
    while(i < node.length && true == node[i]) {
      ++i;
    }
    
    return i + 1;
  }
  
  public boolean updateNodeArr(int nodeNum) {
    boolean ret = false;
    
    if (nodeNum >= allNode) {
      System.out.println("Illegal node number!");
      ret = false;
      return ret;
    }
    
    node[nodeNum] = true;
    ret = true;
    return ret;
  }
  
}
