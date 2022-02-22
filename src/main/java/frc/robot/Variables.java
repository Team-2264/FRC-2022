package frc.robot;

public class Variables {

    // Motor Ports
    // public static final int frontLeftMotorPort = 4;
    // public static final int frontRightMotorPort = 1;
    public static final int frontLeftMotorPort = 4;
    public static final int frontRightMotorPort = 1;
    public static final int backLeftMotorPort = 3;
    public static final int backRightMotorPort = 2;

    public static final int shooterMotorTopPort = 5;
    public static final int shooterMotorBottomPort = 6;

    public static final int intakeMotorPort = 9;
    public static final int indexMotorPort = 8;

    public static final int climbingMotorPortRight = 7;
    public static final int climbingMotorPortLeft = 10;

    // Shooter and Control Panel speeds
    public static final double shooterBottom_kP = 0.15;
    public static final double shooterBottom_kI = 0.0;
    public static final double shooterBottom_kD = 0;
    public static final double shooterBottom_kF = 0.0463;

    public static final double shooterTop_kP = 0.15;
    public static final double shooterTop_kI = 0.0;
    public static final double shooterTop_kD = 0;
    public static final double shooterTop_kF = 0.0463;
   
    // Sensors

    public static final int ultrasonicPort = 0;
    public static final int beamBreakOnePort = 1;
    public static final int beamBreakTwoPort = 2;
    
    // Height and Angle

    public static final double height = 37;
    public static final double offset = 44.5;

    // Threshold for alignment in degrees
    public static final double tXthreshold = 2.0;

}