// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;

import java.util.ResourceBundle.Control;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  // public DriveTrain dt;
  // public Shooter sh;
  // public Sensors se;
  // public Intake in;
  public Climbing cl;
  public Test ts;

  public Joystick j;

  NetworkTable table;

  NetworkTableEntry xEntry;
  NetworkTableEntry yEntry;


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {

    // dt = new DriveTrain();
    // sh = new Shooter();
    // se = new Sensors();
    // in = new Intake();
    cl = new Climbing();
    
    
    j = new Joystick(0);

    // se.smartdashboardSensorsInit();

    // sh.smartdashboardShooterInit();

    NetworkTableInstance inst = NetworkTableInstance.getDefault();

    table = inst.getTable("Vision");

    xEntry = table.getEntry("target_x");
    yEntry = table.getEntry("target_y");

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    // dt.backUp();
    
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    // sh.autokF();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    // SmartDashboard.putNumber("X", xEntry.getDouble(1.0));
    // SmartDashboard.putNumber("Y", yEntry.getDouble(1.0));


    // se.updateSensorsPlaceNumbers();

    // sh.updateShooterMotorSpeeds();


    // dt.mecDrive(j);

    if(j.getRawButton(9)) {
      cl.runLeft();
    }
    if(j.getRawButton(10)) {
      cl.runRight();
    }
    if(j.getRawButton(11)) {
      cl.reverseLeft();
    }
    if(j.getRawButton(12)) {
      cl.reverseRight();
    }
    

    // Shooting

    // Manual Shoot
    
    // if(j.getRawButton(1)) {
    //   if(j.getRawButton(1)) {
    //     sh.manualShoot();
    //   } else if (j.getRawButton(2)) {
    //     sh.manualShootReverse();
    //   }
    // } else {
    //   sh.stopShoot();
    // }

    // if(j.getRawButton(2)) {
    //   in.runIntake();
    //   SmartDashboard.putBoolean("Cal", true);
    // } else {
    //   in.stopIntake();
    //   SmartDashboard.putBoolean("Cal", false);

    // }
    
    // if(j.getRawButton(4)) {
    //   in.runIntake();
    // } else {
    //   in.stopIntake();
    // }

    // if(j.getRawButton(5)) {
    //   in.runIndex();
    // } else {
    //   in.stopIndex();
    // }


    // Smart Shoot
    // if(j.getRawButton(1)) {
    //   if(dt.alignSelf(se)) {
    //     sh.smartShoot(se.calcDistance());
    //   } 
    // } else {
    //   if(dt.aligning) {
    //     dt.fullStop();
    //   }
    //   if(sh.isShooting()) {
    //     sh.stopShoot();
    //   }
    // }

    // Intake

    // if(j.getRawButton(5)){
    //   in.runIntake();
    // }
    // else{
    //   in.stopIntake();
    // }

    // if(j.getRawButton(6)){
    //   in.manualIndex();
    // }
    // else{
    //   in.stopManualIndex();
    // }

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    // se.closeUltrasonic();
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
    ts = new Test();
    ts.smartdashboardInit();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
    ts.callPeriodic(j);
    ts.smartdashboardUpdate();
  }
}
