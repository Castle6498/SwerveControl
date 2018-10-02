package SwerveDrive;

import org.usfirst.frc.team6498.control.TimerHelper;

import edu.wpi.first.wpilibj.Encoder;

public class SwerveAutonomous {
	public SwerveSystem system;
	boolean busy = false;
	Encoder encoder;
	
	public SwerveAutonomous(SwerveSystem system) {
		this.system=system;
		encoder=system.front_right.speedEncoder;
	}
	
	String linearHelper = "start";
	double distance=0;
	double linearGive=1;
	public void linear(double X, double Y, double F) {
	switch(linearHelper) {
	case"start":
		distance=Math.sqrt(Math.pow(X,2)+Math.pow(Y,2))+encoder.getDistance();
		double greatest = X;
		if(Y>X) greatest=Y;
		X/=greatest;
		Y/=greatest;
		X*=F;
		Y*=F;
		system.set(Y, X, 0);		
		busy=true;
		linearHelper="move";
		break;
		
	case "move":
		//dont just set Y and X to system again
		if(Math.abs(distance - encoder.getDistance()) <= linearGive) {	
			linearHelper = "finish";
		}
		break;
		
	case "finish":
		system.set(0, 0, 0);
		busy=false;
		linearHelper="start";
		break;
	
	}
	}
	
	public void clockwise(double X, double Y, double I, double J, double F) {
		//Crap this is going to be hard
	}
	
	public void counterClockwise(double X, double Y, double I, double J, double F) {
		
	}
	
	TimerHelper timer = new TimerHelper();
	public void dwell(double P) {
		if(timer.stopWatch(P/1000)) {
			busy=false;
		}else {
			busy=true;
		}
	}
	
	public boolean isBusy() {
		return busy;
	}
}
