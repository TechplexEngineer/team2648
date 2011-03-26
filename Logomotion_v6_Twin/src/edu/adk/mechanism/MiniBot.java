/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.adk.mechanism;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author Techplex Engineer
 */
public class MiniBot implements Mechanism
{
	SpeedController bot	= new Victor(5);
	boolean pinPulled = false;
	Servo pinPuller = new Servo(6);

	/**
	 * Calls the MBDS stop method
	 */
	public void initialize()
	{
		stop();
		pinPull(1);
	}
	public void pinPull()
	{
		pinPulled = true;
		pinPuller.set(0);
	}

	public void pinPull(boolean pos)
	{
		if(!pos)
		{
			pinPulled = false;
			pinPuller.set(1);
		}
		else
		{
			pinPulled = true;
			pinPuller.set(0);
		}
	}

	public void pinPull(double pos)
	{
		//pinPulled = true;
		pinPuller.set(pos);
	}

	/**
	 * Deploy's the minibot, simply runs the motor for a soecified amount of time.
	 */
	public void deploy()
	{
		if(pinPuller.get() < .8)
			bot.set(1);
		else
			this.pinPull();
	}
	/**
	 * Stops all the MBDS's motors
	 */
	public void stop()
	{
		bot.set(0);
	}

}
