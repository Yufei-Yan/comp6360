package edu.auburn.comp6360.network;

import java.io.Serializable;

import edu.auburn.comp6360.application.GPS;

public class VehicleInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2009521561501538620L; 	// Automatically generated by Eclipse 
	private double x, y;
	private double velocity;
	private double acceleration;
	@SuppressWarnings("unused")
	private int brake;
	@SuppressWarnings("unused")
	private double gas;
	
	public VehicleInfo(GPS gps, double velocity, double acceleration) {
		x = gps.getX();
		y = gps.getY();
		this.velocity = velocity;
		this.acceleration = acceleration;
		brake = 1;
		gas = 100;
	}
	
	public GPS getGPS() {
		return new GPS(x, y);
	}
	
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getVelocity() {
		return this.velocity;
	}
	
	public double getAcceleration() {
		return this.acceleration;
	}
	
	public String toString() {
		return String.format("GPS = (%.1f, %d), speed = %.2f, acceleration = %.3f.\n", getX(), getY(), getVelocity(), getAcceleration());
	}
}
