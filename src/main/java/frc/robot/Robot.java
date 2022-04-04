// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;

/*
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  public DriveTrain dt;
  public Shooter sh;
  public Sensors se;
  public Intake in;
  public Climbing cl;
  public AutoController au;
  public PathFinder pf;
  public Odometry od;

  public Joystick j;
  public Joystick weeb;

  PS4Controller dualsense;

  NetworkTable table;

  NetworkTableEntry heading;

  Pose2d temp, tempAlt;

  boolean turbo;

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {

    dt = new DriveTrain();
    sh = new Shooter();
    se = new Sensors();
    in = new Intake();
    cl = new Climbing();
    au = new AutoController();
    od = new Odometry();
    pf = new PathFinder(od, dt);

    j = new Joystick(0);
    weeb = new Joystick(1);

    dualsense = new PS4Controller(3);

    se.smartdashboardSensorsInit();

    sh.smartdashboardShooterInit();

    NetworkTableInstance inst = NetworkTableInstance.getDefault();

    table = inst.getTable("Vision");
    heading = table.getEntry("heading");

    // Smart Dashboard Init

    SmartDashboard.putBoolean("Shooter", false);
    SmartDashboard.putBoolean("Intake", false);

    SmartDashboard.putData("Field", od.field);

    turbo = false;

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and
   * test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    se.updateSensorsPlaceNumbers();
    SmartDashboard.putNumber("Heading", heading.getDouble(0.0));

    SmartDashboard.putNumber("Gyro", od.getGyroAngle());
    SmartDashboard.putNumber("X", od.getX());
    SmartDashboard.putNumber("Y", od.getY());

    temp = od.m_odometry.getPoseMeters();

    tempAlt = new Pose2d(temp.getX() / 36, temp.getY() / 36, temp.getRotation());

    od.field.setRobotPose(tempAlt);

  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different
   * autonomous modes using the dashboard. The sendable chooser code works with
   * the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the
   * chooser code and
   * uncomment the getString line to get the auto name from the text box below the
   * Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure
   * below with additional strings. If using the SendableChooser make sure to add
   * them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {

    heading.setDouble(2.0);

    od.gyro.reset();

    od.currentPose = new Pose2d(0.0, 0.0, new Rotation2d());

  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {

    od.updateOdometry(dt);

    pf.pathfinder.tick();

    pf.runTest(od, dt, sh, se, in);

    SmartDashboard.putNumber("Gyro", od.getGyroAngle());
    SmartDashboard.putNumber("X", od.getX());
    SmartDashboard.putNumber("Y", od.getY());

  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    cl.resetEncoders();
    pf.pathfinder.clearTasks();
    od.gyro.reset();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    if (dualsense.getL3ButtonPressed()) {
      turbo = !turbo;
    }

    od.updateOdometry(dt);

    sh.updateShooterMotorSpeeds();

    if (dualsense.getL2Button()) {
      sh.runIntakeFast();
    } else if (dualsense.getL1Button()) {
      sh.reverseIntake();
    } else {
      sh.stopIntake();
    }

    if (turbo) {
      dt.mecDriveTurbo(dualsense);
    } else {
      dt.mecDrive(dualsense);
    }

    au.index(in, sh, se, dualsense);

    // ---------------- Shooting ----------------

    if (dualsense.getR2Button()) {
      sh.smartShoot(se.calcDistance(), se.getTX(), dt, in);
      SmartDashboard.putBoolean("Shooter", true);
    } else if (dualsense.getR1Button()) {
      sh.manualShoot();
      in.reverseIndex();
    } else {
      sh.lastLimed = 0.0;
      sh.stopShoot();
      SmartDashboard.putBoolean("Shooter", false);
    }

    // ---------------- AIMBOT INTAKE ----------------

    if (j.getRawButton(7)) {
      if (heading.getDouble(0.0) < 0) {
        dt.drive(0, 0, -.1);
      } else if (heading.getDouble(0.0) < 1.5 && heading.getDouble(0.0) > .5) {
        dt.drive(0, 0, .1);
      } else {
        dt.drive(.24, 0, 0);
        sh.runIntake();
      }
    }

    // ---------------- regular intake ----------------

    if (j.getRawButton(8)) {
      if (heading.getDouble(0.0) < 0) {
        dt.drive(0, 0, -.15);
      } else if (heading.getDouble(0.0) < 1.5 && heading.getDouble(0.0) > .5) {
        dt.drive(0, 0, .15);
      } else {
        dt.drive(.3, 0, 0);
        sh.runIntake();
      }
    }

    cl.checkClimb(weeb);

    if (sh.intakeMotor.getSelectedSensorVelocity() != 0) {
      SmartDashboard.putBoolean("Intake", true);
    } else {
      SmartDashboard.putBoolean("Intake", false);
    }

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {

  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {

  }

}
