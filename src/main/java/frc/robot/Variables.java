package frc.robot;

public class Variables {

    // Motor Ports
    public static final int frontLeftMotorPort = 4;
    public static final int frontRightMotorPort = 3;
    public static final int backLeftMotorPort = 2;
    public static final int backRightMotorPort = 1;

    public static final int shooterMotorTopPort = 6;
    public static final int shooterMotorBottomPort = 5;

    public static final int intakeMotorPort = 7;
    public static final int indexMotorPort = 8;

    public static final int climbingMotorPort = 9;

    // Shooter and Control Panel speeds
    public static final double shooterBottom_kP = 0.15;
    public static final double shooterBottom_kI = 0;
    public static final double shooterBottom_kD = 0;
    public static final double shooterBottom_kF = 0.0479;

    public static final double shooterTop_kP = 0.15;
    public static final double shooterTop_kI = 0;
    public static final double shooterTop_kD = 0;
    public static final double shooterTop_kF = 0.0479;
   
    // Sensors

    public static final int ultrasonicPort = 0;
    public static final int beamBreakOnePort = 0;
    public static final int beamBreakTwoPort = 1;
    
    // Height and Angle

    public static final double height = 37;
    public static final double offset = 44.5;

}