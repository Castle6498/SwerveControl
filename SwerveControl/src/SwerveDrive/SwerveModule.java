package SwerveDrive;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedController;

public class SwerveModule {
	public SpeedController speedMotor;
	public Encoder speedEncoder;
	public SpeedController turnMotor;
	public Encoder turnEncoder;
	
	public AngleControl angleControl;
	public SpeedControl speedControl;
	
	double angleMSpeed=0;
	double speedMSpeed=0;
	
	public SwerveModule(SpeedController speedMotor, Encoder speedEncoder, SpeedController turnMotor, Encoder turnEncoder){
		this.speedMotor=speedMotor;
		this.speedEncoder=speedEncoder;
		this.turnMotor=turnMotor;
		this.turnEncoder=turnEncoder;

		double speedPerPulse=5;
		speedEncoder.setDistancePerPulse(speedPerPulse);
		speedEncoder.setPIDSourceType(PIDSourceType.kRate);
		
		double degreesPerPuse=10;
		turnEncoder.setDistancePerPulse(degreesPerPuse);
		turnEncoder.setPIDSourceType(PIDSourceType.kDisplacement);
		
		angleControl=new AngleControl(turnEncoder);
		angleControl.set(0);
		angleControl.enable();
		
		speedControl=new SpeedControl(speedEncoder);
		speedControl.set(0);
		speedControl.enable();
	}
		
	public void setAngle(double set) {
		angleControl.set(set);
	}
	
	public void setSpeed(double set) {
		speedControl.set(set);
	}
	
	public double getAngleSpeed() {
		angleMSpeed=angleControl.get();
		return angleMSpeed;
	}
	
	public double getSpeed() {
		speedMSpeed=speedControl.get();
		return speedMSpeed;
	}
	
	public void update() {
		speedMotor.set(speedMSpeed);
		turnMotor.set(angleMSpeed);
	}
	
	public void enable() {
		angleControl.enable();
		speedControl.enable();
	}
	
	public void disable() {
		angleControl.disable();
		speedControl.disable();
	}
	
	
	
	
	
	
	
	
}
