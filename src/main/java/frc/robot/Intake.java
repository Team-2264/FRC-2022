package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTableEntry;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

public class Intake {
    NetworkTable networkTable;
    NetworkTableEntry heading;
    NetworkTableEntry objectAtBottom;

    CANSparkMax indexMotor;

    int ballsIn;
    double temp;
    long indexTime;

    public SparkMaxPIDController m_pidController;
    // private RelativeEncoder m_encoder;
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM, lockPos;

    public Intake() {
        indexMotor = new CANSparkMax(Variables.indexMotorPort, MotorType.kBrushless);
        indexMotor.setIdleMode(IdleMode.kBrake);

        // ballsIn = 0;
        NetworkTableInstance instance = NetworkTableInstance.getDefault();

        networkTable = instance.getTable("Vision");

        // -1 if ball is to the left, 1 if to the right, 2 if centered, and 0 if not
        // detected
        heading = networkTable.getEntry("heading");
        objectAtBottom = networkTable.getEntry("bottom");

        lockPos = 0;

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
        indexMotor.set(.2);
    }

    public void runIndexSlow() {
        // indexMotor.set(.04);
        indexMotor.set(.04);
    }

    public void reverseIndex() {
        indexMotor.set(-0.1);
    }

    public void reverseIndexSpecial() {
        indexMotor.set(-0.1);
    }

    public void reverseIndexFast() {
        indexMotor.set(-0.15);
    }

    public void reverseIndexFaster() {
        indexMotor.set(-0.2);
    }

    public void stopIndex() {
        indexMotor.stopMotor();
    }

    public void lockMotor() {
        if (lockPos == 0) {
            lockPos = indexMotor.getEncoder().getPosition();
        } else {
            temp = indexMotor.getEncoder().getPosition() - lockPos;
            temp = temp + .2;
            if (Math.abs(temp) < .2) {
                indexMotor.set(0);
            } else {
                if (temp < -1) {
                    temp = -1;
                }
                if (temp > 1) {
                    temp = 1;
                }
                SmartDashboard.putNumber("ENCODERDIFF", temp);
                indexMotor.set(-0.3 * temp);
            }
        }
    }

    public void resetLock() {
        if (lockPos != 0)
            stopIndex();
        lockPos = 0;
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