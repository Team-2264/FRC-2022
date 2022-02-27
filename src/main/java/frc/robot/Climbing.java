package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climbing {

  // private WPI_TalonFX leftClimbing;
  private WPI_TalonFX rightClimbing, leftClimbing;

  Compressor compressor;

  DoubleSolenoid doubleSolenoid;

  ClimbingSolenoid arms, ram;
  Variables varLib;

  public Climbing() {
    compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);

    rightClimbing = new WPI_TalonFX(Variables.climbingMotorPortRight);
    leftClimbing = new WPI_TalonFX(Variables.climbingMotorPortLeft);

    arms = new ClimbingSolenoid(4, 5);
    ram = new ClimbingSolenoid(6, 7);

    leftClimbing.configFactoryDefault();
    leftClimbing.config_kP(0, Variables.hangar_kP);
    leftClimbing.config_kI(0, Variables.shooterBottom_kI);
    leftClimbing.config_kD(0, Variables.shooterBottom_kD);
    leftClimbing.config_kF(0, Variables.shooterBottom_kF);

    leftClimbing.setNeutralMode(NeutralMode.Brake);

    rightClimbing.configFactoryDefault();
    rightClimbing.config_kP(0, Variables.hangar_kP);
    rightClimbing.config_kI(0, Variables.shooterBottom_kI);
    rightClimbing.config_kD(0, Variables.shooterBottom_kD);
    rightClimbing.config_kF(0, Variables.shooterBottom_kF);

    rightClimbing.setNeutralMode(NeutralMode.Brake);

    compressor.enableDigital();
    ;
  }

  public void runClimber() {
    rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(750));
    leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(750));
  }

  public void reverseClimber() {
    rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(-750));
    leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(-750));
  }

  public void runLeft(int rpm) {
    leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(rpm));
  }

  public void runRight(int rpm) {
    rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(rpm));
  }

  public void reverseLeft(int rpm) {
    leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(-1 * rpm));
  }

  public void reverseRight(int rpm) {
    rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(-1 * rpm));
  }

  public void stopRight() {
    rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(0));
  }

  public void stopLeft() {
    leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(0));
  }

  public void stopClimber() {
    rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(0));
    leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(0));
  }

  public void extendArms() {
    arms.extendSolenoid();
  }

  public void retractArms() {
    arms.retractSolenoid();
  }

  public void disableArms() {
    arms.disableSolenoid();
  }

  public void extendRam() {
    ram.extendSolenoid();
  }

  public void retractRam() {
    ram.retractSolenoid();
  }

  public void checkClimb(Joystick weeb, boolean readyToClimb) {
    SmartDashboard.putNumber("Arm Left", leftClimbing.getSelectedSensorPosition());
    SmartDashboard.putNumber("Arm Right", rightClimbing.getSelectedSensorPosition());

    if (weeb.getRawButton(3)) {
      extendArms();
    } else if (weeb.getRawButton(1)) {
      retractArms();
    }

    if (weeb.getRawButton(4)) {
      extendRam();
    } else if (weeb.getRawButton(2)) {
      retractRam();
    }

    if ((weeb.getRawAxis(3) > 0)) {
      runRight(2205);
    } else if (weeb.getRawButton(6)) {
      reverseRight(2205);
    } else if (readyToClimb && !weeb.getRawButton(8) && !weeb.getRawButton(9)) {
      stopRight();
    }

    if ((weeb.getRawAxis(2) > 0)) {
      reverseLeft(2500);
    } else if (weeb.getRawButton(5)) {
      runLeft(2500);
    } else if (readyToClimb && !weeb.getRawButton(8) && !weeb.getRawButton(9)) {
      stopLeft();
    }

    if (weeb.getRawAxis(1) > 0) {
      reverseClimber();
    } else if (weeb.getRawAxis(1) < 0) {
      runClimber();
    }
  }

  public void leftMove() {
    if (leftClimbing.getSelectedSensorPosition() > -50000) {
      reverseLeft(2500);
    }
  }

  public void rightMove() {
    if (rightClimbing.getSelectedSensorPosition() < 64000) {
      runRight(2205);
    }
  }

  // suck my dick

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
// it felt great ;)