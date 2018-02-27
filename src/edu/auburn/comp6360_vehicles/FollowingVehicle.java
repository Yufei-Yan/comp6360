package edu.auburn.comp6360_vehicles;

/**
 *
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class FollowingVehicle extends Vehicle {

  public FollowingVehicle() {
    super();
  }
  
  public FollowingVehicle(String addr,
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
  
}
