package SwerveDrive;

import com.kauailabs.navx.frc.AHRS;

public class SwerveSystem {
	public SwerveModule front_right;
	public SwerveModule front_left;
	public SwerveModule back_right;
	public SwerveModule back_left;
	
	public AHRS nav;
	
	public double length=0;
	public double width=0;
	public double R=Math.sqrt(Math.pow(length, 2)+Math.pow(width,2)); //diagonal
	
	public enum RotationMode { RobotCenter, AroundRadius };
	
	public double rotationRadius = 0;
	
	public RotationMode rotationMode = RotationMode.RobotCenter;;
	
	public double rawForward=0;
	public double rawStrafe=0;
	public double rotation=0;
	
	public double fcForward=0;
	public double fcStrafe=0;
	
	public double sfr=0;
	public double sfl=0;
	public double sbr=0;
	public double sbl=0;
	
	public double afr=0;
	public double afl=0;
	public double abr=0;
	public double abl=0;
	
	
	public SwerveSystem(SwerveModule front_right, SwerveModule front_left, SwerveModule back_right, SwerveModule back_left, AHRS nav) {
		this.front_right=front_right;
		this.front_left=front_left;
		this.back_right=back_right;
		this.back_left=back_left;
		this.nav=nav;
	}
	
	//<p> returns the current gyro angle
	double getAngle() {
		//get angle
		return nav.getAngle();//degrees 
	}
	
	
	/*
	 * <p> Set the movement speeds of the system.
	 * 
	 * @param fwd The forward magnitude.
	 * @param str The side to side magnitude.
	 * @param rcw The magnitude of rotation (in/sec).
	 */
	public void set(double fwd, double str, double rcw) {
		rawForward=fwd;
		rawStrafe=str;
		rotation=rcw;	
	}
	
	/*
	 * <p> Sets the mode of rotation.
	 * 
	 * @param mode Choice of robot center or around a set radius (set with setRotationRadius()).
	 */
	public void setRotationMode(RotationMode mode) {
		
		rotationMode=mode;
	}
	
	/*
	 * <p> Sets the rotational radius the robot will follow.
	 * 
	 * @param setPoint The radius of the rotation circle.
	 */
	public void setRotationRadius(double setPoint) {
		rotationRadius=setPoint;
	}
	
	//<p> Uses the current angle to adjust joystick inputs to field center.
	void inputsToFieldCenter() {
		double angle=getAngle()*Math.PI/180; //to Radians
		fcForward=rawForward*Math.cos(angle)+rawStrafe*Math.sin(angle); //These equations take controller values and convert
		fcStrafe=-rawForward*Math.sin(angle)+rawStrafe*Math.cos(angle);	//them into fieldcentric commands by finding how much	
	}																	//of the straight and strafe magnitudes should be used from each rotated vector
	
	//<p> All major calculations of wheel speed and angle for all modules.
	void calculateVectors() {
		inputsToFieldCenter();
		
		//ANGULAR (V2)
		double magnitude=rotation;
		
		//calculate angles for each rotational vector
		
		//rotation mode around center
		
		double am1=0;
		double am2=0;
		double am3=0;
		double am4=0;
		
		if(rotationMode==RotationMode.RobotCenter) {
		
			am1 = Math.atan(length/width)+Math.PI/2;
			am2=-am1;
			am3=am1-Math.PI;
			am4=Math.PI-am1;
		
		}
		
		//rotation mode around radius
		
		else if(rotationMode==RotationMode.AroundRadius) {
			
			 am1 =Math.PI-Math.atan(rotationRadius/(width/2))-(Math.PI/2);
			 am2=Math.PI-am1;
			 am4=Math.PI-Math.atan((rotationRadius+length)/(width/2))-(Math.PI/2);
			 am3=Math.PI-am3;
			 //^^^^ am3 and am4 out of order on purpose
			
		}
				
		//calculate X and Y values of each rotational vector
		double xam1=magnitude*Math.cos(am1);
		double yam1=magnitude*Math.sin(am1);
		
		double xam2=magnitude*Math.cos(am2);
		double yam2=magnitude*Math.sin(am2);
	
		double xam3=magnitude*Math.cos(am3);
		double yam3=magnitude*Math.sin(am3);
		
		double xam4=magnitude*Math.cos(am4);
		double yam4=magnitude*Math.sin(am4);
		
		//add together X and Y values of the rotational and linear vectors to calculate final vectors
		double xm1 = fcStrafe + xam1;
		double ym1 = fcForward +yam1;
		
		double xm2 = fcStrafe + xam2;
		double ym2 = fcForward +yam2;
	
		double xm3 = fcStrafe + xam3;
		double ym3 = fcForward +yam3;
		
		double xm4 = fcStrafe + xam4;
		double ym4 = fcForward +yam4;
		
		//calculate magnitude of final vectors
		sfr=Math.sqrt(Math.pow(xm1, 2)+Math.pow(ym1, 2)); 
		sfl=Math.sqrt(Math.pow(xm2, 2)+Math.pow(ym2, 2));
		sbl=Math.sqrt(Math.pow(xm3, 2)+Math.pow(ym3, 2));
		sbr=Math.sqrt(Math.pow(xm4, 2)+Math.pow(ym4, 2));
		
		//calculate angle of final vectors based off of the positive x axis in degrees
		afr=Math.atan2(xm1, ym1)*180/Math.PI; //to Degrees
		afl=Math.atan2(xm2, ym2)*180/Math.PI; //inverse tangent
		abl=Math.atan2(xm3, ym3)*180/Math.PI;
		abr=Math.atan2(xm4, ym4)*180/Math.PI;
		
		double max = sfr; //finds the max speed
		if(sfl>max)max=sfl;
		if(sbr>max)max=sbr;
		if(sbl>max)max=sbl;
		
		if(max>1) {  //if max is larger than 1, then it scales all speeds down 
			sfr/=max;
			sfl/=max;
			sbr/=max;
			sbl/=max;
		}
		
	}//end calculations
	
	
	//<p> Recalculates all settings and sets the speed and angle to all PID loops.
	public void update() {
		
		calculateVectors();
		
		front_right.setSpeed(sfr);
		front_right.setAngle(afr);
		front_right.update();
		
		front_left.setSpeed(sfl);
		front_left.setAngle(afl);
		front_left.update();

		back_right.setSpeed(sbr);
		back_right.setAngle(abr);
		back_right.update();
		
		back_left.setSpeed(sbl);
		back_left.setAngle(abl);
		back_left.update();
	}
	
	
	//<p> Enables the PID controls of each swerve module.
	public void enable() {
		front_right.enable();
		front_left.enable();
		back_right.enable();
		back_left.enable();
	}
	
	//<p> Enables the PID controls of each swerve module.
	public void disable() {
		front_right.disable();
		front_left.disable();
		back_right.disable();
		back_left.disable();
	}
	
	
	
	
	
}
