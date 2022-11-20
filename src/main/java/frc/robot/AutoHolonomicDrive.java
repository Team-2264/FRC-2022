package frc.robot;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
// import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.robot.Trajectories.SplineExample;
import edu.wpi.first.math.controller.PIDController;

public class AutoHolonomicDrive {
    HolonomicDriveController controller;
    Trajectory trajectory;

    public AutoHolonomicDrive() {
        controller = new HolonomicDriveController(new PIDController(1, 0, 0), new PIDController(1, 0, 0),
                new ProfiledPIDController(1, 0, 0,
                        new TrapezoidProfile.Constraints(6.28, 3.14)));

    }

    public ChassisSpeeds getAdjustedChassisSpeeds(Odometry od, SplineExample sp) {
        trajectory = sp.getTrajectory();
        Trajectory.State goal = trajectory.sample(3.4);
        ChassisSpeeds adjustedSpeeds = controller.calculate(
                new Pose2d(od.getX(), od.getY(), od.getGyroHeading()), goal, Rotation2d.fromDegrees(70.0));
        return adjustedSpeeds;
    }

}
