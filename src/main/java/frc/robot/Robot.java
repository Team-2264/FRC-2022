// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.networktables.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.AnalogInput;

import edu.wpi.first.cscore.VideoSink;



/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  double ty, tx, tv, ta, ts, zAdjust, uAdjust, integralZ, priorI, derivZ, priorEZ, d, height, angle, offset, voltageScaleFactor;


  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public DriveTrain dt;
  public Shooter sh;

  public Joystick j;

  public NetworkTable limeTable;
  AnalogInput us;

  VideoSink server;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    SmartDashboard.putNumber("Auto dfasl", 0);

    dt = new DriveTrain();
    sh = new Shooter();
    j = new Joystick(0);

    d = 0;

    limeTable = NetworkTableInstance.getDefault().getTable("limelight");
    us = new AnalogInput(0);

    limeTable.getEntry("stream").setNumber(2.0);

    height = 37;

    

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
    limeTable.getEntry("stream").setNumber(2.0);
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
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);



    

    // dt.backUp();

    
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    // sh.autokF();
    SmartDashboard.putNumber("Angle", 44.5);
    SmartDashboard.putNumber("ShooterBottom", 1000);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    dt.mecDrive(j);
    SmartDashboard.putNumber("TY UPDATING: ", limeTable.getEntry("ty").getDouble(0));
    ty = limeTable.getEntry("ty").getDouble(0);
    offset = SmartDashboard.getNumber("Angle", 44.5);
    angle = offset + ty;
    
    
    SmartDashboard.putNumber("Peanuts", ((104-height)*((Math.sin(Math.toRadians(90-angle))/Math.sin(Math.toRadians(angle)))))/12);

    voltageScaleFactor = 5/RobotController.getVoltage5V(); 

    SmartDashboard.putNumber("Ultrasonic (Inches)", us.getValue() * voltageScaleFactor * 0.0492);
    // SHOOTER
    SmartDashboard.putNumber("ShooterBottomVel", ((int) sh.shooterBottom.getSelectedSensorVelocity() * 600)/2048);
    SmartDashboard.putNumber("ShooterBottom2", SmartDashboard.getNumber("ShooterBottom", -1));

    if(j.getRawButton(1)) {
      sh.shooterBottom.set(ControlMode.Velocity, dt.convertToUnitsPer100ms(SmartDashboard.getNumber("ShooterBottom", 0)));
    } 
    else {
      sh.shooterBottom.set(0);
    }

    // WINCH

    if(j.getRawButton(2)) {
      dt.winchMotor.set(ControlMode.Velocity, dt.convertToUnitsPer100ms(SmartDashboard.getNumber("ShooterBottom", 0)));
    } 
    else {
      dt.winchMotor.set(0);
    }
    

   

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
