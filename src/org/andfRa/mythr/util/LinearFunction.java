package org.andfRa.mythr.util;

import java.util.Random;

public class LinearFunction {
	
	/**
	 * Random generator.
	 */
	private static Random RANDOM = new Random();

	
	/** Slope. */
	private double m;
	
	/** Intercept. */
	private Double b;
	
	
	// Initialisation:
	/**
	 * Sets constant value.
	 * 
	 * @param value value
	 */
	public LinearFunction(Double value)
	 {
		m = 0;
		b = value;
	 }
	
	/** Fixes all missing fields. */
	public void complete() {
		
	}

	
	// CALCULATION:
	/**
	 * Calculates the y(x).
	 * 
	 * @param x x value
	 */
	public double y(double x) 
	 {
		return m*x + b;
	 }

	/**
	 * Calculates the y(x).
	 * 
	 * @param x x value
	 */
	public double y(int x) 
	 {
		return m*x + b;
	 }

	/**
	 * Calculates the y(x).
	 * Rounds down.
	 * 
	 * @param x x value
	 */
	public int yIntFloor(double x) 
	 {
		return (int)(m*x + b);
	 }
	
	/**
	 * Calculates the y(x).
	 * Rounds down.
	 * 
	 * @param x x value
	 */
	public int yIntFloor(int x) 
	 {
		return (int)(m*x + b);
	 }

	/**
	 * Calculates the y(x).
	 * Rounds up.
	 * 
	 * @param x x value
	 */
	public int yIntCeil(double x) 
	 {
		return (int)Math.ceil(m*x + b);
	 }
	
	/**
	 * Calculates the y(x).
	 * Rounds up.
	 * 
	 * @param x x value
	 */
	public int yIntCeil(int x) 
	 {
		return (int)Math.ceil(m*x + b);
	 }

	/**
	 * Calculates the y(x).
	 * Randomly rounds up or down.
	 * 
	 * @param x x value
	 */
	public Integer yIntRand(int x)
	 {
		return roundRand(y(x));
	 }

	/**
	 * Calculates the random boolean value for the given x value.
	 * 
	 * @param x x value
	 */
	public boolean boolRand(double x)
	 {
		return y(x) > RANDOM.nextDouble();
	 }
	
	
	// UTILITY:
	/**
	 * Rounds either up or down.
	 * 
	 * @param value value
	 * @return rounded value
	 */
	public static int roundRand(double value)
	 {
		if (value >= 0){
			double decimal = value - Math.floor(value);
			if(RANDOM.nextDouble() < decimal) return (int)value + 1;
			else return (int)value;
		}else{
			double decimal = -value + Math.ceil(value);
			if(RANDOM.nextDouble() < decimal) return (int)value - 1;
			else return (int)value;
		}
	}
	

	// OTHER:
	/* 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "m=" + m + ", b=" + m;
	}
	
}
