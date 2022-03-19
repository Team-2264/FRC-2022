package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Intake {
    NetworkTable networkTable;
    NetworkTableEntry heading;
    NetworkTableEntry objectAtBottom;

    CANSparkMax indexMotor;

    int ballsIn;
    long indexTime;

    public Intake() {
        indexMotor = new CANSparkMax(Variables.indexMotorPort, MotorType.kBrushless);
        // ballsIn = 0;
        NetworkTableInstance instance = NetworkTableInstance.getDefault();

        networkTable = instance.getTable("Vision");

        // -1 if ball is to the left, 1 if to the right, 2 if centered, and 0 if not
        // detected
        heading = networkTable.getEntry("heading");
        objectAtBottom = networkTable.getEntry("bottom");

    }

    public boolean alignIntake(DriveTrain dt) {
        Double headingValue = heading.getDouble(0.0);
        if (headingValue == 2) {
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
        if (heading.getDouble(2) != 0) {
            return true;
        } else {
            return false;
        }
    }

    // Run motors

    public void runIndex() {
        indexMotor.set(0.01);
    }

    public void reverseIndex() {
        indexMotor.set(-0.01);
    }

    public void stopIndex() {
        indexMotor.set(0);
    }

    // Put the number of currrent balls on the smartdashboard

    public double convertToUnitsPer100ms(double rpm) {
        // This function converts RPM to the unit, called "unit," that the motors use.
        double unitsPerMinute = (rpm * 2048);
        double unitsPer100 = unitsPerMinute / 600;
        return unitsPer100;
    }

}
// code goblin greets you 👺