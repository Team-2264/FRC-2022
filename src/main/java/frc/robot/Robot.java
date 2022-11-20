// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
// import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
// import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

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

  public Joystick weeb;

  PS4Controller dualsense;

  NetworkTable table;

  NetworkTableEntry heading;

  Pose2d temp, tempAlt;

  boolean turbo;

  String kDefaultAuto = "Two";
  String kCustomAuto = "Four";
  String kCustomAutoTwo = "Zero";
  String m_autoSelected;
  SendableChooser<String> m_chooser = new SendableChooser<>();

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
    pf = new PathFinder();

    weeb = new Joystick(1);

    dualsense = new PS4Controller(3);

    se.smartdashboardSensorsInit();

    sh.smartdashboardShooterInit();

    NetworkTableInstance inst = NetworkTableInstance.getDefault();

    table = inst.getTable("Vision");
    heading = table.getEntry("heading");

    SmartDashboard.putData("Field", od.field);

    turbo = false;

    m_chooser.setDefaultOption("Two", kDefaultAuto);
    m_chooser.addOption("Four", kCustomAuto);
    m_chooser.addOption("Zero", kCustomAutoTwo);
    SmartDashboard.putData("Auto choices", m_chooser);
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

    // Shuffleboard.getTab("SmartDashboard")
    // .add("Gyro", od.getGyroAngle())
    // .withWidget(BuiltInWidgets.kGyro)
    // .getEntry();

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

    m_autoSelected = m_chooser.getSelected();

    heading.setDouble(2.0);

    od.gyro.reset();

    pf.stage = 0;

    od.m_odometry.resetPosition(new Pose2d(0.0, 0.0, new Rotation2d()), new Rotation2d());

  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {

    se.limeOn();

    od.updateOdometry(dt);

    if (m_autoSelected == kDefaultAuto) {
      pf.runTwoBall(od, dt, sh, se, in, au, dualsense);
    } else if (m_autoSelected == kCustomAuto) {
      pf.runFull(od, dt, sh, se, in, au, dualsense);
    } else {
      // pf.runOneBall(od, dt, sh, se, in, au, dualsense);
    }

    SmartDashboard.putNumber("Gyro", od.getGyroAngle());
    SmartDashboard.putNumber("X", od.getX());
    SmartDashboard.putNumber("Y", od.getY());

  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    cl.resetEncoders();

    od.gyro.reset();

    au.reverseTime = 0;

    in.resetLock();
    in.lockMotor();

  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    au.index(in, sh, se, dualsense);

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

    // ---------------- Shooting ----------------

    if (dualsense.getR2Button()) {
      se.limeOn();
      sh.smartShoot(se.calcDistance(), se.getTX(), dt, in, dualsense, false);
    } else if (dualsense.getR1Button()) {
      sh.manualShoot();
      in.reverseIndex();
    } else {
      sh.lastLimed = 0.0;
      sh.stopShoot();
    }

    cl.checkClimb(weeb, dualsense);
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
