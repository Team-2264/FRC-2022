package frc.robot;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import me.wobblyyyy.pathfinder2.Pathfinder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import me.wobblyyyy.pathfinder2.control.Controller;
import me.wobblyyyy.pathfinder2.control.GenericTurnController;
import me.wobblyyyy.pathfinder2.drive.MecanumDrive;
import me.wobblyyyy.pathfinder2.follower.FollowerGenerator;
import me.wobblyyyy.pathfinder2.follower.generators.GenericFollowerGenerator;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
import me.wobblyyyy.pathfinder2.robot.Drive;
import me.wobblyyyy.pathfinder2.robot.Odometry;
import me.wobblyyyy.pathfinder2.robot.Robot;
import me.wobblyyyy.pathfinder2.robot.simulated.SimulatedOdometry;
import me.wobblyyyy.pathfinder2.kinematics.WPIMecanumChassis;

import me.wobblyyyy.pathfinder2.trajectory.Trajectory;
import me.wobblyyyy.pathfinder2.trajectory.spline.SplineBuilderFactory;
import me.wobblyyyy.pathfinder2.trajectory.LinearTrajectory;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import me.wobblyyyy.pathfinder2.ctre.TalonFXMotor;
import me.wobblyyyy.pathfinder2.geometry.Translation;

public class PathFinder {

    public final Drive drive;
    private final Odometry odometry;
    private final Robot robot;
    private final Controller turnController;
    private final FollowerGenerator followerGenerator;
    public final Pathfinder pathfinder;

    WPI_TalonFXMotor frontRight;
    WPI_TalonFXMotor frontLeft;
    WPI_TalonFXMotor backRight;
    WPI_TalonFXMotor backLeft;

    double stage;

    public PathFinder(frc.robot.Odometry od, DriveTrain dt) {

        frontRight = new WPI_TalonFXMotor(dt.frontRight);
        frontLeft = new WPI_TalonFXMotor(dt.frontLeft);
        backRight = new WPI_TalonFXMotor(dt.backRight);
        backLeft = new WPI_TalonFXMotor(dt.backLeft);

        stage = 0;

        drive = new WPIMecanumChassis(
                Variables.frontBackDistance,
                Variables.rightLeftDistance,
                frontRight,
                frontLeft,
                backRight,
                backLeft);

        odometry = od;

        robot = new Robot(drive, odometry);

        turnController = new GenericTurnController(-0.0001);

        followerGenerator = new GenericFollowerGenerator(
                turnController);

        pathfinder = new Pathfinder(
                robot,
                followerGenerator);

        pathfinder.setSpeed(.0001);
        pathfinder.setTolerance(10);
        pathfinder.setAngleTolerance(Angle.fromDeg(10));

    }

    public void runTest(frc.robot.Odometry od, DriveTrain dt, Shooter sh, Sensors se, Intake in) {

        sh.runIntake();

        if (od.getX() < .5 && stage == 0) {
            dt.drive(0.4, 0, 0);
            SmartDashboard.putBoolean("RUNNING", true);
        } else if (stage == 0) {
            dt.fullStop();
            stage = 1;
            SmartDashboard.putBoolean("RUNNING", false);
        }

        if (od.getX() > .3 && stage == 1) {
            dt.drive(-0.2, 0, 0);
        } else if (stage == 1) {
            dt.fullStop();
            stage = 2;
        }

        if (od.getX() < 2 && stage == 2) {
            sh.runIntake();
            in.reverseIndex();
            dt.drive(0.2, 0, 0);
        } else if (stage == 2) {
            sh.stopIntake();
            dt.fullStop();
            in.stopIndex();
            stage = 3;
        }

        if (od.getGyroAngle() < 180 && stage == 3) {
            dt.drive(0, 0, 0.2);
        } else if (stage == 3) {
            dt.fullStop();
            stage = 4;
        }

        if (od.getGyroAngle() > 26 && stage == 4) {
            dt.drive(0, 0, -0.2);
        } else if (stage == 4) {
            dt.fullStop();
            stage = 5;
        }

        if (od.getX() < 5.2 && stage == 5) {
            if (od.getY() > -1.5) {
                dt.drive(0.3, 0.1, 0);
            } else {
                dt.drive(0.3, 0, 0);
            }
        } else if (stage == 5) {
            dt.fullStop();
            stage = 6;
        }

        if (od.getX() > 0 && stage == 6) {
            if (od.getY() < 0) {
                dt.drive(-0.2, -0.1, 0);
            } else if (od.getGyroAngle() < 180) {
                dt.drive(-0.2, 0, -0.2);
            } else {
                dt.drive(-0.2, 0, 0);
            }
        } else if (stage == 6) {
            dt.fullStop();
            stage = 7;
        }

    }

    /**
     * Drive in autonomous mode! Very cool.
     * <p>
     * You don't have to do any looping - this method will execute until
     * Pathfinder has finished following its path. There's a variety of
     * ways to improve upon this that we can explore later.
     */
    @SuppressWarnings("DuplicatedCode")
    public void autonomousDrive(frc.robot.Odometry od, DriveTrain dt) {
        // go in a big rectangle
        List<PointXYZ> path = new ArrayList<PointXYZ>() {

            {
                add(new PointXYZ(12, 0, 0));
            }
        };

        for (PointXYZ point : path) {

            pathfinder.goTo(point);

            while (pathfinder.isActive()) {

                SmartDashboard.putNumber("Gyro", od.getGyroAngle());
                SmartDashboard.putNumber("X", od.getX());
                SmartDashboard.putNumber("Y", od.getY());

                pathfinder.tick();
            }
        }
    }

    public void loopDrive(DriveTrain dt, Joystick j) {
        if (!pathfinder.isActive()) {
            dt.mecDrive(j);
        } else {
            if (j.getRawButton(10)) {
                pathfinder.clear();
            }
        }

        pathfinder.tick();
    }
}