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
        frontLeft = new WPI_TalonFX(1);
        frontRight = new WPI_TalonFX(0);
        backLeft = new WPI_TalonFX(2);
        backRight = new WPI_TalonFX(3);
        frontLeft.setInverted(true);
        backLeft.setInverted(true);
        mDrive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);
        mDrive.setSafetyEnabled(true);

        varLib = new Variables();

    }

    public void Drive(Joystick j){
        mDrive.driveCartesian(doCalculations(j.getY()) * varLib.speedY, doCalculations(j.getX()) * varLib.speedX, doCalculations(j.getZ()) * varLib.speedZ);
    }

    public void BroStop(){
        mDrive.driveCartesian(0, 0, 0);
    }

public double doCalculations(double inValue){
    if(inValue < 0){
        return -(Math.sqrt(inValue));
    }
    else{
        return (Math.sqrt(inValue));
    }
}
}
