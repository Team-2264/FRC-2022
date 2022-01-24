package frc.robot;

public class Variables {
    //start of drivetrain vars
    //public final double speedX = -0.7;
    public final double speedX = 0.7;
    public final double speedY = 0.7;
    //public final double speedZ = -0.7;
    public final double speedZ = 0.7;
    public final double threshold = 0.1;
    public final double speedCurve = 2;
    //end of drivetrain vars
    //start of CAN IDs
    public final int frontLeftWheel = 1;
    public final int backLeftWheel = 2;
    public final int frontRightWheel = 0;
    public final int backRightWheel = 3;

    public final int intakeWheels = -1;
    public final int intakeDeploy = -1;
    public final int shooterLeft = -1;
    public final int shooterRight = -1;
    public final int shooterLoad = -1;
    //end of CAN IDs
    //start of PCM IDs
    public final int backRightForward = 7;
    public final int backRightBackward = 4;

    public final int backLeftForward = -1;
    public final int backLeftBackward = -1;
    public final int frontMiddleForward = -1;
    public final int frontMiddleBackward = -1;
    //end of PCM IDs
}
