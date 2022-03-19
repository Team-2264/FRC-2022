package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.HashMap;

public class Auto {
    private WPI_TalonSRX backLeftDrive, backRightDrive, frontLeftDrive, frontRightDrive;

    double backLeftEncoderInit, backRightEncoderInit, frontLeftEncoderInit, frontRightEncoderInit;
    double backRightEncoder90Limit = 2000.0;
    double backLeftEncoder90Limit = 2000.0;
    double frontRightEncoder90Limit = 2000.0;
    double frontLeftEncoder90Limit = 2000.0;
    HashMap<String, Boolean> routineStatus = new HashMap<String, Boolean>();

    public Auto(DriveTrain dt) {
        backLeftDrive = dt.backLeft;
        backRightDrive = dt.backRight;
        frontLeftDrive = dt.frontLeft;
        frontRightDrive = dt.frontRight;

        backLeftEncoderInit = backLeftDrive.getSelectedSensorPosition();
        backRightEncoderInit = backRightDrive.getSelectedSensorPosition();
        frontLeftEncoderInit = frontLeftDrive.getSelectedSensorPosition();
        frontRightEncoderInit = frontRightDrive.getSelectedSensorPosition();

    }

    public void runRoutine(DriveTrain dt) {
        if (!routineStatus.get("forwardFive")) {

        }
    }

    public void turn180(DriveTrain dt) {
        if (backLeftDrive.getSelectedSensorPosition() < (backLeftEncoder90Limit * 2)) {
            turn(dt);
        }
    }

    public void moveDistanceInFeet(double dist) {

    }

    public void turn(DriveTrain dt) {
        dt.drive(0, 0, 0.8);
    }
}
