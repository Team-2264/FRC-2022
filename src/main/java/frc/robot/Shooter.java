package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class Shooter {

    WPI_TalonFX shooterTop;
    WPI_TalonFX shooterBottom;
    WPI_TalonFX intakeMotor;

    boolean manualShooting;
    boolean smartShooting;

    double lastLimed;

    public Shooter() {

        SmartDashboard.putNumber("ShooterBottom", 0);
        SmartDashboard.putNumber("ShooterTop", 0);

        shooterTop = new WPI_TalonFX(Variables.shooterMotorTopPort);
        shooterBottom = new WPI_TalonFX(Variables.shooterMotorBottomPort);
        intakeMotor = new WPI_TalonFX(Variables.intakeMotorPort);

        shooterBottom.configFactoryDefault();
        shooterBottom.config_kP(0, Variables.shooterBottom_kP);
        shooterBottom.config_kI(0, Variables.shooterBottom_kI);
        shooterBottom.config_kD(0, Variables.shooterBottom_kD);
        shooterBottom.config_kF(0, Variables.shooterBottom_kF);

        shooterTop.configFactoryDefault();
        shooterTop.config_kP(0, Variables.shooterTop_kP);
        shooterTop.config_kI(0, Variables.shooterTop_kI);
        shooterTop.config_kD(0, Variables.shooterTop_kD);
        shooterTop.config_kF(0, Variables.shooterTop_kF);

        intakeMotor.configFactoryDefault();
        intakeMotor.config_kP(0, Variables.shooterTop_kP);
        intakeMotor.config_kI(0, Variables.shooterTop_kI);
        intakeMotor.config_kD(0, Variables.shooterTop_kD);
        intakeMotor.config_kF(0, Variables.shooterTop_kF);

        manualShooting = false;
        smartShooting = false;

        shooterTop.setInverted(true);
        shooterBottom.setInverted(true);

        intakeMotor.setNeutralMode(NeutralMode.Coast);

        lastLimed = 0.0;

    }

    // SMARTDASHBOARD SETUP

    public void smartdashboardShooterInitTest() {
        SmartDashboard.putNumber("ShooterBottom", -750);
        SmartDashboard.putNumber("ShooterBottomVel", 0);
        SmartDashboard.putNumber("ShooterTop", -750);
        SmartDashboard.putNumber("ShooterTopVel", 0);
    }

    public void smartdashboardShooterInit() {
        SmartDashboard.putNumber("ShooterBottom", -750);
        SmartDashboard.putNumber("ShooterBottomVel", 0);
        SmartDashboard.putNumber("ShooterTop", -750);
        SmartDashboard.putNumber("ShooterTopVel", 0);
    }

    // SHOOTING CODE

    public void updateShooterMotorSpeeds() {
        SmartDashboard.putNumber("ShooterBottomVel", convertToRPM(shooterBottom.getSelectedSensorVelocity()));
        SmartDashboard.putNumber("ShooterTopVel", convertToRPM(shooterTop.getSelectedSensorVelocity()));
    }

    public void manualShoot() {
        shooterBottom.set(ControlMode.Velocity, convertToUnitsPer100ms(SmartDashboard.getNumber("ShooterBottom", 0)));
        shooterTop.set(ControlMode.Velocity, -1 * convertToUnitsPer100ms(SmartDashboard.getNumber("ShooterTop", 0)));
        manualShooting = true;
    }

    public void manualShootTwo() {
        shooterBottom.set(ControlMode.Velocity, convertToUnitsPer100ms(-1000));
        shooterTop.set(ControlMode.Velocity, -1 * convertToUnitsPer100ms(-1000));
        manualShooting = true;
    }

    public void manualShootReverse() {
        shooterBottom.set(ControlMode.Velocity,
                convertToUnitsPer100ms(SmartDashboard.getNumber("ShooterBottom", 0)));
        shooterTop.set(ControlMode.Velocity, -1 * convertToUnitsPer100ms(SmartDashboard.getNumber("ShooterTop", 0)));
        manualShooting = true;
    }

    public boolean smartShoot(double dist, double tx, DriveTrain dt, Intake in) {
        if (Math.abs(tx) - 1 > 3) {
            if (tx > 0) {
                dt.drive(0, 0, -.2);
            } else {
                dt.drive(0, 0, .2);
            }
            return false;
        } else if (Math.abs(tx) - 1 > 1.5) {
            if (tx > .5) {
                dt.drive(0, 0, -.1);
            } else {
                dt.drive(0, 0, .1);
            }
            return false;
        } else {
            if (lastLimed == 0.0) {
                lastLimed = System.currentTimeMillis();
            } else {

                if (getRPMThree(dist) != 0) {
                    shooterBottom.set(ControlMode.Velocity, -1 * convertToUnitsPer100ms(getRPMThree(dist)));
                    shooterTop.set(ControlMode.Velocity, convertToUnitsPer100ms(getRPMThree(dist)));
                }

                if (System.currentTimeMillis() - lastLimed > 1000) {
                    if (dist > 14) {
                        in.reverseIndexFast();
                    } else if (dist > 7) {
                        in.reverseIndexSpecial();
                    } else {
                        in.reverseIndexFast();
                    }
                }
            }
            return true;
        }
    }

    public void runIntake() {
        intakeMotor.set(ControlMode.Velocity, convertToUnitsPer100ms(-800));
    }

    public void runIntakeFast() {
        intakeMotor.set(ControlMode.Velocity, convertToUnitsPer100ms(-2000));
    }

    public double getRPM(double dist) {
        SmartDashboard.putNumber("RPM", Math.sqrt(125 * dist - 100));
        return 100 * Math.sqrt(125 * dist - 100);
    }

    public double getRPMTwo(double dist) {
        if (dist > 15.1 || dist < 15) {
            SmartDashboard.putNumber("RPM", 1.15 * (Math.sqrt((162 * (dist + 1.1))) - 252) + 5);
            return (100 * Math.sqrt(125 * dist - 100));
        } else {
            return 0;
        }
    }

    public double getRPMThree(double dist) {
        if (dist > 15.1 || dist < 15) {
            SmartDashboard.putNumber("RPM", (-4.6296 * (dist * dist) + 214.8148 * (dist) + 1350.5648));
            if (dist > 14) {
                return (-4.6296 * (dist * dist) + 214.8148 * (dist) + 1900.5648);
            } else if (dist > 9)
                return (-4.6296 * (dist * dist) + 214.8148 * (dist) + 1800.5648);
            else
                return (-4.6296 * (dist * dist) + 214.8148 * (dist) + 1500.5648);
        } else {
            return 0;
        }
    }

    public void reverseIntake() {
        intakeMotor.set(ControlMode.Velocity, convertToUnitsPer100ms(400));
    }

    public void reverseIntakeSlow() {
        intakeMotor.set(ControlMode.Velocity, convertToUnitsPer100ms(400));
    }

    public void stopIntake() {
        intakeMotor.set(ControlMode.Velocity, convertToUnitsPer100ms(0));
    }

    public void stopShoot() {
        shooterBottom.set(ControlMode.PercentOutput, 0);
        shooterTop.set(ControlMode.PercentOutput, 0);

        smartShooting = false;
        manualShooting = false;
    }

    public boolean isMShooting() {
        return manualShooting;
    }

    public boolean isSShooting() {
        return smartShooting;
    }

    // PID CODE

    public void autokF() {
        // Initiate all variables neccesary
        double currentTopKf = Variables.shooterTop_kF;
        double currentBottomKf = Variables.shooterBottom_kF;

        int[] benchmarks = { 1000, 1500, 2000 };
        double[] kFArr = new double[3];
        Boolean topMotorTuned = false;
        Boolean bottomMotorTuned = false;

        double kFLeft, kFRight;

        // AUTO-TUNE kF of Top Shooter
        for (int i = 0; i <= 2; i++) {
            while (!topMotorTuned) {
                shooterTop.config_kF(0, currentTopKf);
                shooterTop.set(ControlMode.Velocity, convertToUnitsPer100ms(benchmarks[i]));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                int rpm = ((int) shooterTop.getSelectedSensorVelocity() * 600) / 2048;
                if (Math.abs(rpm - benchmarks[i]) < 10) {
                    topMotorTuned = true;
                } else if (rpm > benchmarks[i]) {
                    currentTopKf -= .0003;
                } else {
                    currentTopKf += .0003;
                }
            }
            kFArr[i] = currentTopKf;
            topMotorTuned = false;
        }
        kFLeft = (kFArr[0] + kFArr[1] + kFArr[2]) / 3;
        System.out.println(kFLeft);

        // AUTO-TUNE kF of Bottom Shooter

        for (int i = 0; i <= 2; i++) {
            while (!bottomMotorTuned) {
                shooterBottom.config_kF(0, currentBottomKf);
                shooterBottom.set(ControlMode.Velocity, convertToUnitsPer100ms(benchmarks[i]));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                int rpm = ((int) shooterBottom.getSelectedSensorVelocity() * 600) / 2048;
                if (Math.abs(rpm - benchmarks[i]) < 2) {
                    bottomMotorTuned = true;
                } else if (rpm > benchmarks[i]) {
                    currentBottomKf -= .0003;
                } else {
                    currentBottomKf += .0003;
                }
            }
            kFArr[i] = currentBottomKf;
            bottomMotorTuned = false;
            SmartDashboard.putNumber("Trial", kFArr[i]);
        }
        kFRight = (kFArr[0] + kFArr[1] + kFArr[2]) / 3;
        SmartDashboard.putNumber("Johnny", kFRight);
    }

    // MISC

    public double convertToUnitsPer100ms(double rpm) {
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
