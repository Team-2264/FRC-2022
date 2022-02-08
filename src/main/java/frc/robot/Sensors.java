package frc.robot;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.RobotController;

public class Sensors {

    private NetworkTable limeTable;
    private AnalogInput us;

    double d, voltageScaleFactor, height, angle, offset, ty, tx;
    
    public Sensors() {
        d = 0;

        height = Variables.height;
        offset = Variables.offset;

        // Ultrasonic setup
        us = new AnalogInput(Variables.ultrasonicPort);

        // Limelight setup
        limeTable = NetworkTableInstance.getDefault().getTable("limelight");
        limeTable.getEntry("stream").setNumber(2.0);
    }

    public void updateSensorsPlaceNumbers() {
        updateUltrasonicVoltage();
        updateLimelight();

        SmartDashboard.putNumber("Ultrasonic (Inches)", getUltrasonic());
        SmartDashboard.putNumber("Distance", calcDistance());
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


    // SMARTDASHBOARD CODE

    public void smartdashboardSensorsInit() {
        SmartDashboard.putNumber("Angle", Variables.offset);
        SmartDashboard.putNumber("Height", Variables.height); 
    }


    
}
