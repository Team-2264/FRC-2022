package frc.robot;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Ultrasonic;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Sensors {

    private NetworkTable limeTable;

    private DigitalInput bbFrontOpen, bbFrontClosed;

    Thread m_visionThread;

    double d, height, angle, offset, ty, tx, usDistance;
    boolean[] beamsBroken;

    public Sensors() {
        d = 0;

        beamsBroken = new boolean[2];
        beamsBroken[0] = false;
        beamsBroken[1] = false;

        height = Variables.height;
        offset = Variables.offset;

        usDistance = 0;

        // Beam Break setup
        bbFrontOpen = new DigitalInput(0);
        bbFrontClosed = new DigitalInput(1);
        // bbBackOpen = new DigitalInput(5);
        // bbBackClosed = new DigitalInput(6);

        // Limelight setup
        // limeTable = NetworkTableInstance.getDefault().getTable("limelight");
        // limeTable.getEntry("stream").setNumber(2.0);
    }

    public void updateSensorsPlaceNumbers() {
        // updateLimelight();
        updateBeamBreaks();

        updateSmartDashboardSensors();

    }

    // LIMELIGHT CODE

    private void updateLimelight() {
        limeTable.getEntry("stream").setNumber(2.0);

        ty = limeTable.getEntry("ty").getDouble(0);
        tx = limeTable.getEntry("tx").getDouble(0);

        offset = SmartDashboard.getNumber("Angle", 44.5);
    }

    public double calcDistance() {
        angle = offset + ty;
        return (((104 - SmartDashboard.getNumber("Height", 37))
                * ((Math.sin(Math.toRadians(90 - angle)) / Math.sin(Math.toRadians(angle))))) / 12);
    }

    public boolean isAligned() {
        if (Math.abs(tx) < Variables.tXthreshold) {
            return true;
        }
        return false;
    }

    public NetworkTable getLimelight() {
        return limeTable;
    }

    public double getTX() {
        return tx;
    }

    public double getTY() {
        return ty;
    }

    // ULRASONIC CODEs

    public double getUltrasonic() {
        return usDistance;
    }

    public void updateUltrasonic() {

    }

    public void closeUltrasonic() {

    }

    // BEAM BREAK CODE

    public boolean[] getBeamBreaks() {
        return beamsBroken;
    }

    public boolean getBeamBroken() {
        return beamsBroken[1];
    }

    private void updateBeamBreaks() {
        beamsBroken[0] = bbFrontOpen.get();
        beamsBroken[1] = bbFrontClosed.get();
    }

    // Camera

    public void cameraInit() {
        m_visionThread = new Thread(
                () -> {
                    // Get the UsbCamera from CameraServer
                    UsbCamera camera = CameraServer.startAutomaticCapture();
                    // Set the resolution
                    camera.setResolution(640, 480);

                    // Get a CvSink. This will capture Mats from the camera
                    CvSink cvSink = CameraServer.getVideo();
                    // Setup a CvSource. This will send images back to the Dashboard
                    CvSource outputStream = CameraServer.putVideo("Rectangle", 640, 480);

                    // Mats are very memory expensive. Lets reuse this Mat.
                    Mat mat = new Mat();

                    // This cannot be 'true'. The program will never exit if it is. This
                    // lets the robot stop this thread when restarting robot code or
                    // deploying.
                    while (!Thread.interrupted()) {
                        // Tell the CvSink to grab a frame from the camera and put it
                        // in the source mat. If there is an error notify the output.
                        if (cvSink.grabFrame(mat) == 0) {
                            // Send the output the error.
                            outputStream.notifyError(cvSink.getError());
                            // skip the rest of the current iteration
                            continue;
                        }
                        // Put a rectangle on the image
                        Imgproc.rectangle(
                                mat, new Point(100, 100), new Point(400, 400), new Scalar(255, 255, 255), 5);
                        // Give the output stream a new image to display
                        outputStream.putFrame(mat);
                    }
                });
        m_visionThread.setDaemon(true);
        m_visionThread.start();
    }

    // SMARTDASHBOARD CODE

    public void smartdashboardSensorsInit() {
        SmartDashboard.putNumber("Angle", Variables.offset);
        SmartDashboard.putNumber("Height", Variables.height);
        SmartDashboard.putBoolean("Beam One", false);
        SmartDashboard.putBoolean("Beam Two", false);
    }

    private void updateSmartDashboardSensors() {
        // SmartDashboard.putNumber("Distance", calcDistance());

        SmartDashboard.putBoolean("Beam Front Open", beamsBroken[0]);
        SmartDashboard.putBoolean("Beam Front Closed", beamsBroken[1]);

        // SmartDashboard.putNumber("UltraSonic Distance (in)", usDistance);
    }

    public void updateBallCount(int balls) {
        SmartDashboard.putNumber("Balls In System", balls);
    }

}
