package SwerveDrive;

import org.usfirst.frc.team6498.control.PIDControlHelper;

import edu.wpi.first.wpilibj.Encoder;

public class SpeedControl {
public PIDControlHelper controller;
	
	public double setPoint=0;
	public double result=0;
	
	Encoder encoder;
	
	public SpeedControl(Encoder encoder) {
		
		this.encoder=encoder;
		
		double kP = 0.001;

	    double kI = 0.00;

	    double kD = 0.00;

	    double kF = 0.00;
	     
	    double kToleranceDegrees = 2.0f;
	    
	    //get input min and max
	    
	    controller = new PIDControlHelper(kP, kI, kD, kF, kToleranceDegrees, setPoint, encoder, -50, 50);
	     
	}
	
	public double get() {
		result = controller.result;
		return result;
	}
	
	public void set(double set) {
		setPoint=set;
		controller.set(setPoint);
	}
	
	public void enable() {
		 controller.enable();
	 }
	 
	public void disable() {
		 controller.disable();
	 }
	
}
