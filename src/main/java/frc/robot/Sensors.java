package frc.robot;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.DigitalInput;

public class Sensors {

    private NetworkTable limeTable;
    private AnalogInput us;
    private DigitalInput bbOne, bbTwo;

    double d, voltageScaleFactor, height, angle, offset, ty, tx;
    boolean[] beamsBroken;
    
    public Sensors() {
        d = 0;

        beamsBroken = new boolean[2];
        beamsBroken[0] = false;
        beamsBroken[1] = false;

        height = Variables.height;
        offset = Variables.offset;

        // Ultrasonic setup
        us = new AnalogInput(Variables.ultrasonicPort);

        // Beam Break setup
        bbOne = new DigitalInput(Variables.beamBreakOnePort);
        bbTwo = new DigitalInput(Variables.beamBreakTwoPort);

        // Limelight setup
        limeTable = NetworkTableInstance.getDefault().getTable("limelight");
        limeTable.getEntry("stream").setNumber(2.0);
    }

    public void updateSensorsPlaceNumbers() {
        updateUltrasonicVoltage();
        updateLimelight();
        updateBeamBreaks();

        updateSmartDashboardSensors();
    }

    //  LIMELIGHT CODE

    public void updateLimelight() {
        limeTable.getEntry("stream").setNumber(2.0);

        ty = limeTable.getEntry("ty").getDouble(0);
        tx = limeTable.getEntry("tx").getDouble(0);
        
        offset = SmartDashboard.getNumber("Angle", 44.5);
    }

    public double calcDistance() {
        angle = offset + ty;
        return (((104-SmartDashboard.getNumber("Height", 37))*((Math.sin(Math.toRadians(90-angle))/Math.sin(Math.toRadians(angle)))))/12);
    }

    public NetworkTable getLimelight() {
        return limeTable;
    }

    public double getTX() {
        return tx;
    }

    public double getTY() {
        return ty;
    }

    // ULRASONIC CODE

    public void updateUltrasonicVoltage() {
        voltageScaleFactor = 5/RobotController.getVoltage5V(); 
    }

    public double getUltrasonic() {
        return us.getValue() * voltageScaleFactor * 0.0492;
    }

    // BEAM BREAK CODE

    public boolean[] getBeamBreaks() {
        return beamsBroken;
    }

    public void updateBeamBreaks() {
        beamsBroken[0] = bbOne.get();
        beamsBroken[1] = bbTwo.get();
    }

    // SMARTDASHBOARD CODE

    public void smartdashboardSensorsInit() {
        SmartDashboard.putNumber("Angle", Variables.offset);
        SmartDashboard.putNumber("Height", Variables.height); 
        SmartDashboard.putBoolean("Beam One", false);
        SmartDashboard.putBoolean("Beam Two", false);
    }

    public void updateSmartDashboardSensors() {
        SmartDashboard.putNumber("Ultrasonic (Inches)", getUltrasonic());
        SmartDashboard.putNumber("Distance", calcDistance());
        SmartDashboard.putBoolean("Beam One", beamsBroken[0]);
        SmartDashboard.putBoolean("Beam Two", beamsBroken[1]);
    }

    public void updateBallCount(int balls){
        SmartDashboard.putNumber("Balls In System", balls);
    }

    
}
