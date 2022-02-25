package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class Shooter {

    WPI_TalonFX shooterTop;
    WPI_TalonFX shooterBottom;
    WPI_TalonFX intakeMotor;

    boolean manualShooting;
    boolean smartShooting;

    public Shooter() {
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

    }

    // SMARTDASHBOARD SETUP

    public void smartdashboardShooterInit() {
        SmartDashboard.putNumber("ShooterBottom", 500);
        SmartDashboard.putNumber("ShooterBottomVel", 0);
        SmartDashboard.putNumber("ShooterTop", 500);
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

    public void manualShootReverse() {
        shooterBottom.set(ControlMode.Velocity,
                convertToUnitsPer100ms(SmartDashboard.getNumber("ShooterBottom", 0)));
        shooterTop.set(ControlMode.Velocity, -1 * convertToUnitsPer100ms(SmartDashboard.getNumber("ShooterTop", 0)));
        manualShooting = true;
    }

    public boolean smartShoot(double dist, double tx, DriveTrain dt) {
        if(Math.abs(tx) > 3) {
            if(tx > 0) {
                dt.drive(0, 0, -.125);
            } else {
                dt.drive(0, 0, .125);
            }
            return false;
        } else { 
            shooterBottom.set(ControlMode.Velocity, -1 * convertToUnitsPer100ms(getRPM(dist)));
            shooterTop.set(ControlMode.Velocity, convertToUnitsPer100ms(getRPM(dist)));

            return true;
        }
    }

    public boolean smartShootTwo(double dist, double tx, DriveTrain dt, Intake in) {
        if(Math.abs(tx) > 3) {
            if(tx > 0) {
                dt.drive(0, 0, -.125);
            } else {
                dt.drive(0, 0, .125);
            }
            return false;
        } else { 
            shooterBottom.set(ControlMode.Velocity, -1 * convertToUnitsPer100ms(getRPMTwo(dist)));
            shooterTop.set(ControlMode.Velocity, convertToUnitsPer100ms(getRPMTwo(dist)));
            in.runIndex();
            return true;
        }
    }

    public void runIntake() {
        intakeMotor.set(ControlMode.Velocity, convertToUnitsPer100ms(-1000));
    }

    public double getRPM(double dist) {
        SmartDashboard.putNumber("RPM", Math.sqrt(125*dist - 100));
        return 100 * Math.sqrt(125*dist - 100);
    }

    public double getRPMTwo(double dist) {
        SmartDashboard.putNumber("RPM", 1.15*(Math.sqrt((162*(dist + 1.1))) - 252) + 5);
        return 100 * Math.sqrt(125*dist - 100);
    }

    public void reverseIntake() {
        intakeMotor.set(ControlMode.Velocity, convertToUnitsPer100ms(1000));
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

    private void sliderUpdate(Joystick j) {
        SmartDashboard.putNumber("ShooterBottom", 1000);
        SmartDashboard.putNumber("ShooterTop", 1000);
    }

}
