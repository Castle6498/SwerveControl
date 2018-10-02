/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6498.robot;

import org.usfirst.frc.team6498.robot.Robot.PinType;

import com.kauailabs.navx.frc.AHRS;

import SwerveDrive.SwerveModule;
import SwerveDrive.SwerveSystem;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Spark;

public class Robot extends IterativeRobot {
	
	public SwerveSystem system;
	public Joystick joystick;
	
	@Override
	public void robotInit() {
		//JOYSICK
		joystick=new Joystick(0);
		
		//FRONT RIGHT
		Spark frSMotor = new Spark(0);
		Encoder frSEncoder = new Encoder(0,1);
		
		Spark frAMotor = new Spark(1);
		Encoder frAEncoder = new Encoder(2,3);

		SwerveModule front_right=new SwerveModule(frSMotor, frSEncoder, frAMotor, frAEncoder);
		
		//FRONT LEFT
		Spark flSMotor = new Spark(2);
		Encoder flSEncoder = new Encoder(4,5);
				
		Spark flAMotor = new Spark(3);
		Encoder flAEncoder = new Encoder(6,7);

		SwerveModule front_left=new SwerveModule(flSMotor, flSEncoder, flAMotor, flAEncoder);
		
		//BACK RIGHT
		Spark brSMotor = new Spark(4);
		Encoder brSEncoder = new Encoder(getChannelFromPin( PinType.DigitalIO, 4 ),getChannelFromPin( PinType.DigitalIO, 5 ));
				
		Spark brAMotor = new Spark(5);
		Encoder brAEncoder = new Encoder(getChannelFromPin( PinType.DigitalIO, 6 ),getChannelFromPin( PinType.DigitalIO, 7 ));

		SwerveModule back_right=new SwerveModule(brSMotor, brSEncoder, brAMotor, brAEncoder);
		
		//BACK LEFT
		Spark blSMotor = new Spark(6);
		Encoder blSEncoder = new Encoder(getChannelFromPin( PinType.DigitalIO, 0 ),getChannelFromPin( PinType.DigitalIO, 1 ));
				
		Spark blAMotor = new Spark(7);
		Encoder blAEncoder = new Encoder(getChannelFromPin( PinType.DigitalIO, 2 ),getChannelFromPin( PinType.DigitalIO, 3 ));

		SwerveModule back_left=new SwerveModule(blSMotor, blSEncoder, blAMotor, blAEncoder);
		
		//NAV
		AHRS nav = new AHRS(SPI.Port.kMXP);
		
		
		//SYSTEM
		system = new SwerveSystem(front_right, front_left, back_right, back_left, nav);
		
	}

	
	@Override
	public void autonomousInit() {
	
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
	
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		system.set(joystick.getY(),joystick.getX(),joystick.getTwist());
		system.update();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
	
	
	
	
	
	public enum PinType { DigitalIO, PWM, AnalogIn, AnalogOut };
    public final int MAX_NAVX_MXP_DIGIO_PIN_NUMBER      = 9;
    public final int MAX_NAVX_MXP_ANALOGIN_PIN_NUMBER   = 3;
    public final int MAX_NAVX_MXP_ANALOGOUT_PIN_NUMBER  = 1;
    public final int NUM_ROBORIO_ONBOARD_DIGIO_PINS     = 10;
    public final int NUM_ROBORIO_ONBOARD_PWM_PINS       = 10;
    public final int NUM_ROBORIO_ONBOARD_ANALOGIN_PINS  = 4;
    /* getChannelFromPin( PinType, int ) - converts from a navX-MXP */

    /* Pin type and number to the corresponding RoboRIO Channel     */

    /* Number, which is used by the WPI Library functions.          */
    public int getChannelFromPin( PinType type, int io_pin_number ) 

               throws IllegalArgumentException {

        int roborio_channel = 0;

        if ( io_pin_number < 0 ) {

            throw new IllegalArgumentException("Error:  navX-MXP I/O Pin #");

        }

        switch ( type ) {

        case DigitalIO:

            if ( io_pin_number > MAX_NAVX_MXP_DIGIO_PIN_NUMBER ) {

                throw new IllegalArgumentException("Error:  Invalid navX-MXP Digital I/O Pin #");

            }

            roborio_channel = io_pin_number + NUM_ROBORIO_ONBOARD_DIGIO_PINS + 

                              (io_pin_number > 3 ? 4 : 0);

            break;

        case PWM:

            if ( io_pin_number > MAX_NAVX_MXP_DIGIO_PIN_NUMBER ) {

                throw new IllegalArgumentException("Error:  Invalid navX-MXP Digital I/O Pin #");

            }

            roborio_channel = io_pin_number + NUM_ROBORIO_ONBOARD_PWM_PINS;

            break;

        case AnalogIn:

            if ( io_pin_number > MAX_NAVX_MXP_ANALOGIN_PIN_NUMBER ) {

                throw new IllegalArgumentException("Error:  Invalid navX-MXP Analog Input Pin #");

            }

            roborio_channel = io_pin_number + NUM_ROBORIO_ONBOARD_ANALOGIN_PINS;

            break;

        case AnalogOut:

            if ( io_pin_number > MAX_NAVX_MXP_ANALOGOUT_PIN_NUMBER ) {

                throw new IllegalArgumentException("Error:  Invalid navX-MXP Analog Output Pin #");

            }

            roborio_channel = io_pin_number;            

            break;

        }

        return roborio_channel;

    }
}
