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

        rightClimbing.setInverted(true);

        compressor.enableDigital();

        beginClimbingSeq = 0;

        buttonPressed = 0;


        encoderLeftInit = leftClimbing.getSelectedSensorPosition();
        encoderRightInit = rightClimbing.getSelectedSensorPosition();
    }
    

    public void runClimber() {
        rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(500));
        leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(567));
    }

    public void reverseClimber() {
        rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(500));
        leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(567));
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
        leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(rpm));
    }

    public void reverseRight(int rpm) {
        rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(rpm));
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

    public void extendArms(){
        arms.extendSolenoid();
    }
    public void retractArms(){
        arms.retractSolenoid();
    }

    public void disableArms() {
        arms.disableSolenoid();
    }

    public void extendRam(){
        ram.extendSolenoid();
    }
    public void retractRam(){
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

    public void checkClimb(Joystick weeb) {
      SmartDashboard.putNumber("Arm Left", leftEncoderDisplacement());
      SmartDashboard.putNumber("Arm Right", rightEncoderDisplacement());
        
      if(buttonPressed == 0) {
        if(weeb.getRawButton(3)) {
            extendArms();
        } else if(weeb.getRawButton(1)) {
            retractArms();
        } 
        
        if(weeb.getRawButton(4)) {
            extendRam();
        } else if(weeb.getRawButton(2)) {
          retractRam();
        }

        if(weeb.getRawAxis(1) > 0) {
          reverseClimber();
        } else if (weeb.getRawAxis(1) < 0) {
          runClimber();
        } else {
          if((weeb.getRawAxis(3) > 0)) {
            runRight(2205);
          } else if (weeb.getRawButton(6)) {
            reverseRight(2205);
          } else {
            stopRight();
          }
        
          if((weeb.getRawAxis(2) > 0)) {
            runLeft(2500);
          } else if (weeb.getRawButton(5)) {
            reverseLeft(2500);
          } else {
            stopLeft();
          }
        }
      }  

      if(weeb.getRawButton(7) && (buttonPressed == 0 || buttonPressed == 7)) {
        if(beginClimbingSeq == 0) {
          retractArms();
          retractRam();
          beginClimbingSeq = currentTime();
          buttonPressed = 7;
        } else if (currentTime() - beginClimbingSeq > 1000) {
          reverseLeft(2500);
          reverseRight(2205);
        }  
      } else if(buttonPressed == 7) {
        stopRight();
        stopLeft();
        buttonPressed = 0;
        beginClimbingSeq = 0;
      }

      if((weeb.getRawButton(8) && buttonPressed == 0) || buttonPressed == 8) {
        if(buttonPressed == 0) buttonPressed = 8;

        if(leftEncoderDisplacement() < 0 && rightEncoderDisplacement() < 0) {
          buttonPressed = 0;
        } else {
          extendArms();
          if(leftEncoderDisplacement() < 0) {
            runLeft(1000);
          } else {
            stopLeft();
          }

          if(rightEncoderDisplacement() < 0) {
            runRight(1000);
          } else {
            stopRight();
          }
        }

      }

      if((weeb.getRawButton(9) && buttonPressed == 0) || buttonPressed == 9) {
        if(buttonPressed == 0) buttonPressed = 9;

        if(leftEncoderDisplacement() < 0 && rightEncoderDisplacement() < 0) {
          buttonPressed = 0;
        } else {
          if(leftEncoderDisplacement() < 0) {
            runLeft(1500);
          } else {
            stopLeft();
          }

          if(rightEncoderDisplacement() < 0) {
            runRight(1500);
          } else {
            stopRight();
          }
        }

      }

      if((weeb.getRawButton(10) && buttonPressed == 0) || buttonPressed == 10) {
        if(beginClimbingSeq == 0) {
          beginClimbingSeq = currentTime();
          buttonPressed = 10;
        } else {
          extendRam();
          if(currentTime() - beginClimbingSeq > 1000 &&  System.currentTimeMillis() - beginClimbingSeq < 1200) {
            retractArms();
          } 
          if(currentTime() - beginClimbingSeq > 1200) {
            runLeft(2500);
            runRight(2205);
          } else if(currentTime() - beginClimbingSeq > 2800) {
            stopRight();
            stopLeft();
          }
          if(currentTime() - beginClimbingSeq > 1800 && System.currentTimeMillis() - beginClimbingSeq < 2000) {
            extendArms();
          }
          if(currentTime() - beginClimbingSeq > 3000) {
            retractArms();
            beginClimbingSeq = 0;
            buttonPressed = 0;
          }
        }

      }

    }

    public void leftMove() {
      if(leftClimbing.getSelectedSensorPosition() > -50000) {
        reverseLeft(2500);
      }
    }

    public void rightMove() {
      if(rightClimbing.getSelectedSensorPosition() < 64000) {
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