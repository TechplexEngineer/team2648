package edu.team2648;

//import edu.adk.core.Autonomous;
import edu.adk.mechanism.*;
//import edu.adk.maneuver.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Servo;

/**
 * What goes here?
 * @author Techplex Engineer
 */
public class MyRobot extends SimpleRobot
{
	//define mechanisms here:
	//2 cims per wheel

	Drive drive = new Drive();
	//Servo Y, 2 Window motors
	Arm arm = new Arm();
	//Vic top, Vic Bot
	Claw claw = new Claw();
	MiniBot MBDS = new MiniBot();
	DriverStation ds = DriverStation.getInstance();
	DriverStationLCD dslcd = DriverStationLCD.getInstance();
	//define maneuvers here
//        Maneuver driveF;
//        Maneuver driveB;
//        Maneuver cap;
//        Maneuver spitt;
//        Maneuver finish;
	//define joysticks here:
	Joystick js1 = new Joystick(1);
	Joystick js2 = new Joystick(2);
	//define timers here:
	Timer timer = new Timer();
	DigitalInput haveTube = new DigitalInput(10);
	DigitalInput p_mid = new DigitalInput(1);
	DigitalInput p_lft = new DigitalInput(2);
	DigitalInput p_rt = new DigitalInput(3);
	DigitalInput autoSw = new DigitalInput(4);
	double armGoTo, clawGoTo;
	boolean mid, lft, rt;
	int blink_count = 0;
	boolean blink = false;

	/**
	 * RobotInit
	 *
	 * this is only called one time when the robot initializes
	 * it should contain things that only need to be initialized once
	 */
	protected void robotInit()
	{
		initialize();
	}

	/**
	 * Initialize
	 *
	 * a general use initialize function
	 * good for reseting the robot prior to autonomous or operator control
	 */
	public void initialize()
	{
		getWatchdog().setEnabled(false);

		//initialize timer
		timer.reset();
		timer.start();

		//initialize mechanisms
		drive.initialize();
		arm.initialize();
		claw.initialize();
		MBDS.initialize();
	}
	int counter = 0;

	/**
	 * Autonomous
	 *
	 * This function is called when the robot enters autonomous
	 */
	public void autonomous()
	{
		initialize();
		System.out.println("Entering Auto: ");

		if (autoSw.get() == false)
		{
			// T Code
			while (isAutonomous() && isEnabled() && timer.get() < 15)
			{
				Timer.delay(.05);
				mid = p_mid.get();
				lft = !p_lft.get();
				rt = !p_rt.get();

				System.out.println("Mid: " + mid + " | Lft: " + lft + " | Rt: " + rt);


				if (!lft && !rt)
				{
					drive.setLeftRightMotorSpeeds(-.375, -.375);
					System.out.println("straight");
				}

				if (lft)
				{
					drive.setLeftRightMotorSpeeds(.25, -.375);
					System.out.println("lft");
				} else if (rt)
				{
					drive.setLeftRightMotorSpeeds(-.375, .25);
					System.out.println("rt");
				}
				if (lft && rt)
				{
					System.out.println("TTTTTTTTTTTTTT");
					claw.wristUp();
					Timer.delay(.25);
					claw.wristRun(0);

					//Backup from the T
					System.out.println("Back Slow");
					drive.goBackwardSlow();       //this is opposite of what you think because of the new robot
					// for some reason forward == backward now
					Timer.delay(1.5);  //change from 1.25 //change from 1.75 //change from 2.25
					drive.stop();

					System.out.println("Raise Arm");
					arm.elbowUp();
					Timer.delay(3.75);//3.5
					arm.elbowRun(0);

					System.out.println("Forward Slow");
					drive.goForwardSlow();
					Timer.delay(.85); //changed from .28 // .35
					drive.stop();

					System.out.println("Lower Wrist");
					claw.wristRun(-.25);
					Timer.delay(.5);
					claw.wristRun(0);

					System.out.println("Hang Tube");
					claw.spitterOut();
					Timer.delay(2);
					claw.spitterRun(0);

					if (timer.get() > 15)
					{
						System.out.println("Break 1");
						break;
					}

					System.out.println("Backup Slow");
					drive.goBackwardSlow();
					Timer.delay(2);
					drive.stop();

					if (timer.get() > 15)
					{
						System.out.println("Break 2");
						break;
					}

					System.out.println("Lower arm");
					arm.elbowDwn();
					Timer.delay(1.5); //11.03
					arm.elbowRun(0);

					if (timer.get() > 15)
					{
						System.out.println("Break 3");
						break;
					}

					System.out.println("Done");
					return;
				}
			}
		} else if (autoSw.get() == true) //Y Code
		{

			//Y code
			while ( isAutonomous() && isEnabled() && timer.get() < 15)
			{
				Timer.delay(.05);
				mid = p_mid.get();
				lft = !p_lft.get();
				rt = !p_rt.get();

				System.out.println("Mid: " + mid + " | Lft: " + lft + " | Rt: " + rt);


				if (timer.get() > 3 && timer.get() < 6.5)
					arm.elbowUp();
				else
					arm.stop();


				if (!lft && !rt)
				{
					drive.setLeftRightMotorSpeeds(-.25, -.25);
					System.out.println("straight");
				}
				if (lft)
				{
					drive.setLeftRightMotorSpeeds(0, -.5);
					System.out.println("lft");
				} else if (rt)
				{
					drive.setLeftRightMotorSpeeds(-.5, 0);
					System.out.println("rt");
				}
				if (lft && rt)
				{
					System.out.println("Entering Stream - Hit the Y");
					drive.stop();

					drive.goForwardSlow();
					Timer.delay(.0626);
					drive.stop();
					lft = !p_lft.get();
					rt = !p_rt.get();

					while (!(lft || rt))
					{
						System.out.println("Going left");
						drive.goLeft();
						Timer.delay(.005);
						lft = !p_lft.get();
						rt = !p_rt.get();
					}


					System.out.println("Lower Wrist");
					claw.wristRun(-.25);
					Timer.delay(.5);
					claw.wristRun(0);

					System.out.println("Hang Tube");
					claw.spitterOut();
					Timer.delay(2);
					claw.spitterRun(0);

					if (timer.get() > 15)
						break;

					System.out.println("Backup Slow");
					drive.goForwardSlow();
					Timer.delay(2);
					drive.stop();

					if (timer.get() > 15)
						break;

					System.out.println("Lower arm");
					arm.elbowDwn();
					Timer.delay(1.5); //11.03
					arm.elbowRun(0);

					if (timer.get() > 15)
						break;


					return;
				}
			}
		}

	}
	boolean tube_out, tube_in, tube_up, tube_dwn;
	boolean arm_up, arm_dwn;
	boolean claw_up, claw_dwn;
	boolean deploy_mini, override;
	/**
	 * OperatorControl
	 *
	 * This function is called when the robot enters operator control
	 */
	String c;

	public void operatorControl()
	{
		initialize();
		//run the operator control code

		timer.reset();
		timer.start();




		while (isOperatorControl() && isEnabled())
		{
			//System.out.println(autoSw.get());

			if (js1.getRawButton(7)) //up
				MBDS.pinPull(0);
			else if (js1.getRawButton(9))
				MBDS.pinPull(1);

			//Timer.delay(.25);

//		}
//
//
//
//		while (isOperatorControl() && isEnabled() && false)
//		{
			mid = p_mid.get();
			lft = !p_lft.get();
			rt = !p_rt.get();




			if (js1.getRawButton(8))
				System.out.println("Mid: " + mid + " | Lft: " + lft + " | Rt: " + rt);

			tube_out = js1.getRawButton(1);
			tube_in = js1.getRawButton(2);// && !haveTube.get();
			tube_up = js1.getRawAxis(6) > .8;
			tube_dwn = js1.getRawAxis(6) < -.8;

			arm_up = js1.getRawButton(5) || js2.getRawButton(3);
			arm_dwn = js1.getRawButton(3) || js2.getRawButton(2);

			claw_up = js1.getRawButton(6) || js2.getRawButton(4);
			claw_dwn = js1.getRawButton(4) || js2.getRawButton(5);

			deploy_mini = js1.getRawButton(11) || js2.getRawButton(11);

			override = js1.getRawButton(10);


			drive.run(js1);

			//Conditional Debug statments




			if (js1.getRawButton(10))
			{
				arm.printPos();
				claw.printPos();
			}
			// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
			// Control the elbow
			if (arm_up)
				arm.elbowUp();
			else if (arm_dwn)
				arm.elbowDwn();
			else
				arm.elbowRun(0);

			// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
			// Control the wrist




			if (claw_up)
				claw.wristUp();
			else if (claw_dwn)
				claw.wristDwn();
			else
				claw.wristRun(0);


			// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
			//Control for the spitter





			if (tube_out)
				claw.spitterOut();
			else if (tube_in)
				claw.spitterIn();
			else if (tube_up)
				claw.spitterRotateTubeUp();
			else if (tube_dwn)
				claw.spitterRotateTubeDwn();
			else
				claw.spitterRun(0);


			if (timer.get() > 90 && timer.get() < 100)
			{
				//Blink code
				blink_count++;

				if (blink_count % 50 == 0)
				{
					blink = (!blink);
					ds.setDigitalOut(2, blink);

				}

			} else if (timer.get() > 100 && timer.get() < 110)
			{
				ds.setDigitalOut(2, true);


			} else if (timer.get() > 110 && timer.get() < 119)
			{
				ds.setDigitalOut(4, true);
			} else
			{
				ds.setDigitalOut(2, false);
				ds.setDigitalOut(4, false);
			}

			if (js1.getRawButton(12))
			{
				c = "";
				for (int i = 1; i < 4; i++)
				{
					if (!ds.getDigitalIn(i))
						c += (" DSIn" + i + " ");
				}
				if (!c.equals(""))
					System.out.println(c);
			}




			if (timer.get() > 109 || override || ds.getDigitalIn(1)) //Pull the pin just before the end game
			{
				MBDS.pinPull();
			}

			if (timer.get() > 110 || override || ds.getDigitalIn(1))
			{

				if (deploy_mini)
				{
					MBDS.deploy();
					Timer.delay(.05);
				} else
					MBDS.stop();

			} //junk
			Timer.delay(0.005);
		}
	}

	/**
	 * Disabled
	 *
	 * this is called while the robot is disabled.
	 */
	public void disabled()
	{
		initialize();


		while (isDisabled() && !isEnabled()) // NEW
		{
			MBDS.pinPull(false);
			if (js1.getRawButton(8))
				System.out.println("Mid: " + mid + " | Lft: " + lft + " | Rt: " + rt);

			drive.stop();
		}
	}
}
