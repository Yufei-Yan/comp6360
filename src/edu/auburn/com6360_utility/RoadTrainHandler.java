package edu.auburn.com6360_utility;

import java.util.Scanner;

public class RoadTrainHandler implements Runnable {

  @Override
  public void run() {
    while (true) {
      Scanner sc = new Scanner(System.in);
      String key = sc.next();

      if (key.equals("form")) {
        System.out.println("Form road train.");
      } else if (key.equals("leave")) {
        System.out.println("Leave road train.");
      }
    }
  }

  public void start() {
    System.out.println("Roadtrain.");
    String thraedName = "Roadtrain";
    
    Thread t = new Thread(this, thraedName);
    t.start();
  }
}
