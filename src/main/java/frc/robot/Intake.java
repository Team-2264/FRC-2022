package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;

public class Intake {
    NetworkTable networkTable;
    NetworkTableEntry heading;
    NetworkTableEntry objectAtBottom;
    NetworkTableEntry objectStatus;
    Sensors sensors;
    WPI_TalonFX intakeMotor;
    // WPI_TalonSRX indexMotor;
    int ballsIn;
    long indexTime;

    public Intake() {
        intakeMotor = new WPI_TalonFX(Variables.intakeMotorPort);
        // indexMotor = new WPI_TalonSRX(Variables.indexMotorPort);
        // ballsIn = 0;
        NetworkTableInstance instance = NetworkTableInstance.getDefault();

        networkTable = instance.getTable("Vision");
        heading = networkTable.getEntry("heading");
        objectAtBottom = networkTable.getEntry("bottom");
        objectStatus = networkTable.getEntry("detection");

    }

    public boolean alignIntake(DriveTrain dt) {
        Double headingValue = heading.getDouble(0.0);
        if (headingValue == 0) {
            dt.fullStop();
            return true;
        }

        if (headingValue < 0) {
            dt.turn(1);
            return false;
        }

        if (headingValue > 0) {
            dt.turn(-1);
            return false;
        }

        return false;
    }

    public boolean objectDetected() {
        if (objectStatus.getBoolean(false)) {
            return true;
        } else {
            return false;
        }
    }

    public void alignAndIntake(DriveTrain dt) {
        if (!objectDetected()) {
            runIntake();
        } else {
            double objectPosBottom = objectAtBottom.getDouble(1);
            if (alignIntake(dt) == true) {
                if (objectPosBottom != 1) {
                    dt.driveForward();
                } else {
                    dt.fullStop();
                    runIntake();
                }
            } else {
                alignIntake(dt);
            }
        }
    }

    public void intake() {
        // intakeMotor.set(ControlMode.Velocity, convertToUnitsPer100ms(3000));
        // Call indexing code if ball spotted
    }

    // Run motors
    public void runIntake() {
        intakeMotor.set(ControlMode.Velocity, convertToUnitsPer100ms(3000));
    }

    public void runIndex() {
        // indexMotor.set(ControlMode.Velocity, convertToUnitsPer100ms(-4000));
    }

    public void stopIntake() {
        intakeMotor.set(ControlMode.Velocity, 0);
    }

    public void stopIndex() {
        // indexMotor.set(ControlMode.Velocity, 0);
    }

    // Indexing (make sure to... )

    public void index(Sensors se) {

        // if(se.getBeamBroken()) {
        // indexMotor.set(convertToUnitsPer100ms(200));
        // indexTime = System.currentTimeMillis();
        // if(System.currentTimeMillis() - indexTime > 2000) {
        // indexMotor.set(convertToUnitsPer100ms(0));
        // }
        // } else{
        // indexMotor.set(convertToUnitsPer100ms(200));
        // }

    }

    // if both beams open, run motors till second beam is broken.
    // if beam two is occupied, run motors till beam one is broken
    // make sure to run a little more
    // pause intake when beam 1 broken

    // have a special "no-indexing" mode

    public void manualIndex() {
        // indexMotor.set(convertToUnitsPer100ms(200));
    }

    public void stopManualIndex() {
        // indexMotor.set(0);
    }

    // make a function that works with shooter. to move the index motor and clear
    // bb2 and move ball from bb1 to bb2

    // Put the number of currrent balls on the smartdashboard

    private double convertToUnitsPer100ms(double rpm) {
        // This function converts RPM to the unit, called "unit," that the motors use.
        double unitsPerMinute = (rpm * 2048);
        double unitsPer100 = unitsPerMinute / 600;
        return unitsPer100;
    }

    private double convertToRPM(double input) {
        // This function converts the unit, called "unit," that the motors use into RPM.
        return ((int) input * 600) / 2048;
    }

}
// code goblin greets you 👺