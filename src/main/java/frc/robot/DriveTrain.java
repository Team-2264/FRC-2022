package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class DriveTrain {

    public boolean crossedLine;
    public boolean aligning;

    final double kP_z = 0.009;
    final double kF_z = 0.1;
    final double kI_z = 0;
    final double kD_z = 0.001;

    final double kP_x = 0.0175;
    final double kF_x = 0.1;
    final double kI_x = 0.005;
    final double kD_x = 0.003;

    final double kP_y = 0.03125;
    final double kF_y = 0.1;
    final double kI_y = 0;
    final double kD_y = 0;

    PIDController pid_x = new PIDController(kP_x, kI_x, kD_x);
    PIDController pid_y = new PIDController(kP_y, kI_y, kD_y);
    PIDController pid_z = new PIDController(kP_z, kI_z, kD_z);

    final double speed = 0.7;

    public static long startBackUpTime;

    WPI_TalonSRX backLeft;
    WPI_TalonSRX backRight;
    WPI_TalonSRX frontLeft;
    WPI_TalonSRX frontRight;

    MecanumDrive mDrive;

    public DriveTrain() {
        aligning = false;
        crossedLine = false;

        frontLeft = new WPI_TalonSRX(Variables.frontLeftMotorPort);
        frontRight = new WPI_TalonSRX(Variables.frontRightMotorPort);
        backRight = new WPI_TalonSRX(Variables.backRightMotorPort);
        backLeft = new WPI_TalonSRX(Variables.backLeftMotorPort);

        frontRight.setInverted(true);
        backRight.setInverted(true);

        mDrive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);
        mDrive.setSafetyEnabled(false);

        // PID Setup
        pid_x.setTolerance(5);
        pid_x.setSetpoint(0);
        pid_y.setTolerance(1);
        pid_y.setSetpoint(0);
        pid_z.setTolerance(1);
        pid_z.setSetpoint(0);
    }

    public void mecDrive(Joystick j) {
        mDrive.driveCartesian(-0.4 * j.getY(), 0.25 * j.getX(), 0.35 * j.getZ());
    }

    public void drive(double x, double y, double z) {
        mDrive.driveCartesian(x, y, z);
    }

    public void turn(int direction) {
        if (direction == -1)
            drive(0, 0, -0.5);
        if (direction == 1)
            drive(0, 0, 0.5);
    }

    public void fullStop() {
        aligning = false;
        mDrive.driveCartesian(0, 0, 0);
    }

    public void backUp() {
        startBackUpTime = System.currentTimeMillis();

        if (System.currentTimeMillis() - startBackUpTime < 1000) {
            // Should this be in here??
            drive(0, -0.5, 0);
        }

        crossedLine = true;
        fullStop();
    }

    public void driveForward() {
        mDrive.driveCartesian(-.25, 0, 0);
    }

    public void driveBackward() {
        mDrive.driveCartesian(.25, 0, 0);
    }

    public boolean alignSelf(Sensors se) {
        if (Math.abs(se.getTX()) < Variables.tXthreshold) {
            aligning = false;
            return true;
        } else {
            aligning = true;
            if (se.getTX() > 0) {
                // Turn Left
            } else {
                // Turn Right
            }
            return false;
        }
    }

}