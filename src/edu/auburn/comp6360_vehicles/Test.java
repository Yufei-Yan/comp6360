package edu.auburn.comp6360_vehicles;

import edu.auburn.com6360_utility.VehicleParaHandler;
import edu.auburn.com6360_utility.RoadTrainHandler;

/**
 *
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class Test {
  public static void main(String[] args) throws Exception {
    if (3 != args.length) {
      System.err.println("There must be 3 arguments.");
      return;
    }
    
    if (!args[0].equals("lead") && !args[0].equals("follow")) {
      System.err.println("Specify \"lead\" or \"follow\".");
      return;
    }
    
    if (args[0].equals("lead")) {
      System.out.println("Running " + args[0] + " vechicle");
      
      LeadVehicle lead = (LeadVehicle)new VehicleParaHandler().vehicleGenrator(args[0], args[2]);
      lead.start();
      
//      while (true) {
//        System.out.println("Test");
//        Thread.sleep(1000);
//      }
      
    } else {
      System.out.println("Running following vechicle");
      FollowingVehicle follow = (FollowingVehicle)new VehicleParaHandler().vehicleGenrator(args[0], args[2]);
      follow.start();
      
      RoadTrainHandler rt = new RoadTrainHandler();
      rt.start();
    }
  }
}
