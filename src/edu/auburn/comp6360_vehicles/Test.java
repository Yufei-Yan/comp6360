package edu.auburn.comp6360_vehicles;

/**
 *
 * @author Yufei Yan (yzy0050@auburn.edu)
 */
public class Test {
  public static void main(String[] args) {
    if (2 != args.length) {
      System.err.println("There must be 2 arguments.");
      return;
    }
    
    if (!args[0].equals("lead") && !args[0].equals("follow")) {
      System.err.println("Specify \"lead\" or \"follow\".");
      return;
    }
    
    if (args[0].equals("lead")) {
      System.out.println("Running lead vechicle");
    } else {
      System.out.println("Running following vechicle");
    }
  }
}
