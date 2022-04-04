package frc.robot;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
// import edu.wpi.first.wpilibj.Ultrasonic;

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

    private DigitalInput bbFrontClosed, bbBackClosed;

    Thread m_visionThread;

    double d, height, angle, offset, ty, tx;
    boolean[] beamsBroken;

    public Sensors() {
        d = 0;

        beamsBroken = new boolean[2];
        beamsBroken[0] = false;
        beamsBroken[1] = false;

        height = Variables.height;
        offset = Variables.offset;

        // Beam Break setup

        bbFrontClosed = new DigitalInput(0);
        bbBackClosed = new DigitalInput(1);

        // Limelight setup
        limeTable = NetworkTableInstance.getDefault().getTable("limelight");
        limeTable.getEntry("stream").setNumber(2.0);

        cameraInit();
    }

    public void updateSensorsPlaceNumbers() {
        updateLimelight();
        updateBeamBreaks();

        updateSmartDashboardSensors();

    }

    // LIMELIGHT CODE

    private void updateLimelight() {
        limeTable.getEntry("stream").setNumber(2.0);

        ty = limeTable.getEntry("ty").getDouble(0);
        tx = limeTable.getEntry("tx").getDouble(0);

        offset = SmartDashboard.getNumber("Angle", 22.5);
    }

    public double calcDistance() {
        angle = offset - ty;
        return (((103 - SmartDashboard.getNumber("Height", 28))
                * (Math.sin(Math.toRadians(90 - angle)))) / Math.sin(Math.toRadians(angle)) / 12);
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

    // BEAM BREAK CODE

    public boolean[] getBeamBreaks() {
        return beamsBroken;
    }

    private void updateBeamBreaks() {
        beamsBroken[1] = !bbFrontClosed.get();
        beamsBroken[0] = !bbBackClosed.get();
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

    public boolean frontStatus() {
        return beamsBroken[1];
    }

    public boolean backStatus() {
        return beamsBroken[0];
    }

    // SMARTDASHBOARD CODE

    public void smartdashboardSensorsInit() {

        SmartDashboard.putBoolean("Beam Front Closed", false);
        SmartDashboard.putBoolean("Beam Back Closed", false);
    }

    private void updateSmartDashboardSensors() {
        SmartDashboard.putNumber("Distance", calcDistance());

        SmartDashboard.putBoolean("Beam Front Closed", beamsBroken[0]);
        SmartDashboard.putBoolean("Beam Back Closed", beamsBroken[1]);
    }

}
// santa 🎅🏿 -> actually Dhariya
// System.out.println("Santa Claus");
/**
 * public static void main(String[] args){
 * Scanner rd = new Scanner(system.in);
 * int height_cm;
 * System.out.println("Dhariya's height: ")
 * height_cm = rd.nextInt();
 * System.out.println("Doesn't matter, Dhairya still short");
 * 
 * }
 * Program very good
 * 
 */