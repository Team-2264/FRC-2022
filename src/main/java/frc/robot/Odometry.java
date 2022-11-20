package frc.robot;

import me.wobblyyyy.pathfinder2.robot.AbstractOdometry;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import me.wobblyyyy.pathfinder2.geometry.Angle;
import me.wobblyyyy.pathfinder2.geometry.PointXYZ;
// import me.wobblyyyy.pathfinder2.geometry.Translation;

import edu.wpi.first.math.kinematics.MecanumDriveKinematics;
import edu.wpi.first.math.kinematics.MecanumDriveOdometry;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;

public class Odometry extends AbstractOdometry {

    ADXRS450_Gyro gyro;

    Translation2d m_frontLeftLocation;
    Translation2d m_frontRightLocation;
    Translation2d m_backLeftLocation;
    Translation2d m_backRightLocation;

    MecanumDriveOdometry m_odometry;
    MecanumDriveWheelSpeeds wheelSpeeds;

    MecanumDriveKinematics m_kinematics;

    Pose2d currentPose;

    public Field2d field;

    public Odometry() {

        m_frontLeftLocation = new Translation2d(Variables.frontBackDistance / 2, Variables.rightLeftDistance / 2);
        m_frontRightLocation = new Translation2d(Variables.frontBackDistance / 2, -Variables.rightLeftDistance / 2);
        m_backLeftLocation = new Translation2d(-Variables.frontBackDistance / 2, Variables.rightLeftDistance / 2);
        m_backRightLocation = new Translation2d(-Variables.frontBackDistance / 2, -Variables.rightLeftDistance / 2);

        gyro = new ADXRS450_Gyro();

        m_kinematics = new MecanumDriveKinematics(m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation,
                m_backRightLocation);

        m_odometry = new MecanumDriveOdometry(m_kinematics, getGyroHeading(), new Pose2d(0, 0, new Rotation2d()));

        currentPose = new Pose2d();

        field = new Field2d();

    }

    public PointXYZ getRawPosition() {

        return new PointXYZ(currentPose.getX(), currentPose.getY(), new Angle(getGyroAngle()));
    }

    public double getX() {
        return currentPose.getX() / 12;
    }

    public double getY() {
        return currentPose.getY() / 12;
    }

    public Rotation2d getGyroHeading() {
        return Rotation2d.fromDegrees(-gyro.getAngle());
    }

    public double getGyroAngle() {
        return gyro.getAngle();
    }

    public void updateOdometry(DriveTrain dt) {

        wheelSpeeds = new MecanumDriveWheelSpeeds(convertToRPM(dt.frontLeft.getSelectedSensorVelocity()),
                convertToRPM(dt.frontRight.getSelectedSensorVelocity()),
                convertToRPM(dt.backLeft.getSelectedSensorVelocity()),
                convertToRPM(dt.backRight.getSelectedSensorVelocity()));

        currentPose = m_odometry.update(getGyroHeading(), wheelSpeeds);
    }

    private double convertToRPM(double input) {
        // This function converts the unit, called "unit," that the motors use into RPM.
        return ((((int) input * 600) / 2048) * Math.PI * .2032) / 60;
    }

}
