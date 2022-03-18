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

    public PathFinder(frc.robot.Odometry od, DriveTrain dt) {

        frontRight = new WPI_TalonFXMotor(dt.frontRight);
        frontLeft = new WPI_TalonFXMotor(dt.frontLeft);
        backRight = new WPI_TalonFXMotor(dt.backRight);
        backLeft = new WPI_TalonFXMotor(dt.backLeft);

        drive = new WPIMecanumChassis(
                Variables.frontBackDistance,
                Variables.rightLeftDistance,
                frontRight,
                frontLeft,
                backRight,
                backLeft);

        odometry = od;

        robot = new Robot(drive, odometry);

        turnController = new GenericTurnController(0.01);

        followerGenerator = new GenericFollowerGenerator(
                turnController);

        pathfinder = new Pathfinder(
                robot,
                followerGenerator);

        pathfinder.setSpeed(.005);
        pathfinder.setTolerance(10);
        pathfinder.setAngleTolerance(Angle.fromDeg(20));

    }

    public void runTest(frc.robot.Odometry od, DriveTrain dt) {

        Trajectory trajectory = new LinearTrajectory(
                new PointXYZ(12, 0, 0), // the trajectory's destination
                0.1, // the speed (0-1) to move at
                2, // give the trajectory a tolerance of 2
                Angle.fromDeg(5) // and an angle tolerance of 5 degrees
        );

        pathfinder.followTrajectory(trajectory);

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