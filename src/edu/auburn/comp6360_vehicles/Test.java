package edu.auburn.comp6360_vehicles;

import edu.auburn.com6360_utility.VehicleParaHandler;
import edu.auburn.com6360_utility.ConfigFileHandler;

/**
 *
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class Test {
  public static void main(String[] args) {
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
      LeadVehicle lead = (LeadVehicle)new VehicleParaHandler().vehicleGenrator(args[0]);
      
      lead.runLead(args[2]);
      
    } else {
      System.out.println("Running following vechicle");
    }
  }
}
