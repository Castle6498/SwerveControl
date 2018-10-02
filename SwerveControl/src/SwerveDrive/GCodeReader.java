package SwerveDrive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class GCodeReader {
	public String line = null;
	public boolean done = false;
	
	public enum Command {Rapid, Linear, Clockwise, CounterClockwise, Dwell};
	
	Command command = null;
	BufferedReader reader = null;
	public GCodeReader() {
		try {
			File textFile = new File("C:\\Users\\lukeo\\OneDrive\\Documents\\Robotics Club\\2018 season\\Programming\\TextFiles\\src\\textFiles\\org\\file.gcode");
			FileReader fileReader=null;
			
			fileReader = new FileReader(textFile);
			reader = new BufferedReader(fileReader);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void readLine() {
	try {		
		line=reader.readLine();
		
		System.out.println(line);
		if(line==null) {
			reader.close();
			done=true;
			System.out.println("Reader Closed");
		}else {
			done=false;
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	
	public void findCommand() {
		if(line!=null) {
		if(line.charAt(0)=='G') {
			double type = Double.parseDouble(Character.toString(line.charAt(1)));
			//System.out.println("Type "+type);
			if(type==0) command=Command.Rapid;
			else if(type==1) command = Command.Linear;
			else if(type==2) command = Command.Clockwise;
			else if(type==3) command = Command.CounterClockwise;
			else if(type==4) command = Command.Dwell;
			//System.out.println("Command Found");
		}
		}
		else {
			command = null;
			//System.out.println("No Command Found");
		}
		
	}
	
	public void executeCommand() {
		if(command!=null) {
		switch(command) {
		case Rapid:
			System.out.println("Rapid");
			System.out.println(line.substring(line.indexOf('X')+1,line.indexOf('Y')-1));
			System.out.println(line.substring(line.indexOf('Y')+1,line.indexOf('F')-1));
			System.out.println(line.substring(line.indexOf('F')+1));
			break;
		case Linear:
			System.out.println("Linear");
			System.out.println(line.substring(line.indexOf('X')+1,line.indexOf('Y')-1));
			System.out.println(line.substring(line.indexOf('Y')+1,line.indexOf('F')-1));
			System.out.println(line.substring(line.indexOf('F')+1));
			break;
		case Clockwise:
			System.out.println("Clockwise");
			break;
		case CounterClockwise:
			System.out.println("CounterClockwise");
			break;
		case Dwell:
			System.out.println("Dwell");
			break;
		
		}
		}else {
			System.out.println("Command Null");
		}
	}
	
	
}
