package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climbing {

  // private WPI_TalonFX leftClimbing;
  private WPI_TalonFX rightClimbing, leftClimbing;

  Compressor compressor;

  DoubleSolenoid doubleSolenoid;

  ClimbingSolenoid arms, ram;
  Variables varLib;

  double beginClimbingSeq;
  int buttonPressed;

  double encoderLeftInit, encoderRightInit;

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

    leftClimbing.setInverted(false);

    rightClimbing.configFactoryDefault();
    rightClimbing.config_kP(0, Variables.hangar_kP);
    rightClimbing.config_kI(0, Variables.shooterBottom_kI);
    rightClimbing.config_kD(0, Variables.shooterBottom_kD);
    rightClimbing.config_kF(0, Variables.shooterBottom_kF);

    rightClimbing.setNeutralMode(NeutralMode.Brake);

    compressor.enableDigital();

    beginClimbingSeq = 0;

    buttonPressed = 0;

    encoderLeftInit = leftClimbing.getSelectedSensorPosition();
    encoderRightInit = rightClimbing.getSelectedSensorPosition();
  }

  public void resetEncoders() {
    encoderLeftInit = leftClimbing.getSelectedSensorPosition();
    encoderRightInit = rightClimbing.getSelectedSensorPosition();
  }

  public void runClimber() {
    rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(500));
    leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(567));
  }

  public void reverseClimber() {
    rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(-500));
    leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(-567));
  }

  // Move winches out (extend rope)

  public void runLeft(int rpm) {
    leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(rpm));
  }

  public void runRight(int rpm) {
    rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(rpm));
  }

  // Move winches in (bring in rope)

  public void reverseLeft(int rpm) {
    leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(-1 * rpm));
  }

  public void reverseRight(int rpm) {
    rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(-1 * rpm));
  }

  // Stop motors

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

  public double leftEncoderDisplacement() {
    return leftClimbing.getSelectedSensorPosition() - encoderLeftInit;
  }

  public double rightEncoderDisplacement() {
    return rightClimbing.getSelectedSensorPosition() - encoderRightInit;
  }

  public double currentTime() {
    return System.currentTimeMillis();
  }

  public void checkClimb(Joystick weeb, PS4Controller j) {

    if (buttonPressed == 0) {
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

      if (weeb.getRawAxis(1) > 0) {
        reverseClimber();
      } else if (weeb.getRawAxis(1) < 0) {
        runClimber();
      } else {
        if ((weeb.getRawAxis(3) > 0)) {
          reverseRight(2205);
        } else if (weeb.getRawButton(6)) {
          runRight(2205);
        } else {
          stopRight();
        }

        if ((weeb.getRawAxis(2) > 0)) {
          reverseLeft(2500);
        } else if (weeb.getRawButton(5)) {
          runLeft(2500);
        } else {
          stopLeft();
        }
      }
    }

    if (weeb.getRawButton(7) && (buttonPressed == 0 || buttonPressed == 7)) {
      if (beginClimbingSeq == 0) {
        retractArms();
        retractRam();
        beginClimbingSeq = currentTime();
        buttonPressed = 7;
      } else if (currentTime() - beginClimbingSeq > 1000) {
        runLeft(2500);
        runRight(2205);
      }
    } else if (buttonPressed == 7) {
      stopRight();
      stopLeft();
      buttonPressed = 0;
      beginClimbingSeq = 0;
    }

    if ((weeb.getRawButton(8) && buttonPressed == 0) || buttonPressed == 8) {
      if (buttonPressed == 0) {
        beginClimbingSeq = currentTime();
        buttonPressed = 8;
      } else {
        if (System.currentTimeMillis() - beginClimbingSeq < 1000) {
          runLeft(500);
          runRight(500);
        } else {
          buttonPressed = 0;
          beginClimbingSeq = 0;
        }
      }

    }

    if ((weeb.getRawButton(9) && buttonPressed == 0) || buttonPressed == 9) {
      if (buttonPressed == 0)
        buttonPressed = 9;

      if (leftEncoderDisplacement() < -103000 && rightEncoderDisplacement() < -103000) {
        buttonPressed = 0;
      } else {
        extendArms();
        if (leftEncoderDisplacement() > -103000) {
          reverseLeft(2500);
        } else {
          stopLeft();
        }

        if (rightEncoderDisplacement() > -103000) {
          reverseRight(2500);
        } else {
          stopRight();
        }
      }

    }

    if ((weeb.getRawButton(10) && buttonPressed == 0) || buttonPressed == 10) {
      if (beginClimbingSeq == 0) {
        beginClimbingSeq = currentTime();
        buttonPressed = 10;
      } else {
        extendRam();
        if (currentTime() - beginClimbingSeq > 1000 && System.currentTimeMillis() - beginClimbingSeq < 1200) {
          retractArms();
        }

        if (currentTime() - beginClimbingSeq > 1200 && currentTime() - beginClimbingSeq < 2600) {
          reverseLeft(3360);
          reverseRight(3500);
        } else {
          stopRight();
          stopLeft();
        }

        if (currentTime() - beginClimbingSeq > 1500 && System.currentTimeMillis() - beginClimbingSeq < 2000) {
          extendArms();
        }
        if (currentTime() - beginClimbingSeq > 3200) {
          beginClimbingSeq = 0;
          buttonPressed = 0;
        }
      }

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

}
// it felt great ;)