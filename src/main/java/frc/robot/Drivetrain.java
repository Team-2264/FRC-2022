package frc.robot;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class Drivetrain {
    WPI_TalonFX frontLeft;
    WPI_TalonFX frontRight;
    WPI_TalonFX backLeft;
    WPI_TalonFX backRight;
    MecanumDrive mDrive;
    Variables varLib;

    public Drivetrain(){
        frontLeft = new WPI_TalonFX(varLib.frontLeftWheel);
        backLeft = new WPI_TalonFX(varLib.backLeftWheel);
        frontRight = new WPI_TalonFX(varLib.frontRightWheel);
        backRight = new WPI_TalonFX(varLib.backRightWheel);
        frontLeft.setInverted(true);
        //backLeft.setInverted(true);
        frontRight.setInverted(true);
        mDrive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);
        mDrive.setSafetyEnabled(true);

        varLib = new Variables();
    }

    public void Drive(Joystick joy){
            //make the robot move with calculations performed to each of the joystick values
            //mDrive.driveCartesian(doCalculations(joy.getY()) * varLib.speedY, doCalculations(joy.getX()) * varLib.speedX, doCalculations(joy.getZ()) * varLib.speedZ);
            mDrive.driveCartesian(doCalculations(joy.getX()) * varLib.speedX, doCalculations(joy.getY()) * varLib.speedY, doCalculations(joy.getZ()) * varLib.speedZ);
    }

    public double doCalculations(double inValue){
        //return 0 if the input value does not exceed the threshold
        if(Math.abs(inValue) < varLib.threshold){
            return 0;
        }
        //add an exponential transformation to the value depending on the speed curve
        if(inValue < 0){
            return -(Math.pow(inValue * -1, varLib.speedCurve));
        } else {
            return (Math.pow(inValue, varLib.speedCurve));
        }
    }
}
